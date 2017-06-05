package com.centit.dde.dataio;

import com.centit.dde.dao.ExchangeTaskDao;
import com.centit.dde.dao.ExchangeTaskdetailDao;
import com.centit.dde.dao.ExportSqlDao;
import com.centit.dde.datafile.ExchangeFileWriter;
import com.centit.dde.datafile.TableFileWriter;
import com.centit.dde.po.*;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.dde.transfer.Impl.TransferManagerImpl;
import com.centit.dde.util.ConnPool;
import com.centit.dde.util.ItemValue;
import com.centit.dde.util.TaskConsoleWriteUtils;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.algorithm.DatetimeOpt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import java.sql.*;
import java.util.List;

public class ExportDataImpl implements ExportData, CallWebService {
    private static final Log logger = LogFactory.getLog(ExportDataImpl.class);

    private static boolean debugEnabled = logger.isDebugEnabled();

    @Resource
    protected StaticEnvironmentManager platformEnvironment;

    private ExportSqlDao exportSqlDao;

    private ExchangeTaskDao exchangeTaskDao;

    private ExchangeTaskdetailDao exchangeTaskdetailDao;

    private TaskLogManager taskLogManager;

    private TaskDetailLogManager taskDetailLogManager;

    private TaskErrorDataManager taskErrorDataManager;

    private ExecuteDataMap executeDataMap;

    public void setExportSqlDao(ExportSqlDao exportSqlDao) {
        this.exportSqlDao = exportSqlDao;
    }

    public void setExchangeTaskDao(ExchangeTaskDao exchangeTaskDao) {
        this.exchangeTaskDao = exchangeTaskDao;
    }

    public void setExchangeTaskdetailDao(ExchangeTaskdetailDao exchangeTaskdetailDao) {
        this.exchangeTaskdetailDao = exchangeTaskdetailDao;
    }

    public void setTaskErrorDataManager(TaskErrorDataManager taskErrorDataManager) {
        this.taskErrorDataManager = taskErrorDataManager;
    }

    public void setTaskDetailLogManager(TaskDetailLogManager taskDetailLogManager) {
        this.taskDetailLogManager = taskDetailLogManager;
    }

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    public void setExecuteDataMap(ExecuteDataMap executeDataMap) {
        this.executeDataMap = executeDataMap;
    }

    @Override
    public int doExportSql(ExportSql exportSql, TableFileWriter tableWriter, String usercode,
                           TaskDetailLog taskDetailLog) {
        String msg = null;

        Long taskId = 0L;
        if(null != taskDetailLog) {
            TaskLog taskLog = taskLogManager.getObjectById(taskDetailLog.getLogId());
            taskId = taskLog.getTaskId();
        }

        int nRows = 0;
        long nErrors = 0;
        long nSucceed = 0;
        boolean runBreak = false;
        String sLastErrorMsg = "";
        Date beginTime = DatetimeOpt.currentSqlDate();

        try {

            List<ExportTrigger> exportTriggers = exportSql.getExportTriggers();

            DatabaseInfo dbInfo = platformEnvironment.getDatabaseInfo(exportSql.getSourceDatabaseName());
            Connection conn = ConnPool.getConn(dbInfo);

            // 交换前事件
            if (exportTriggers != null) {
                for (ExportTrigger exportTrigger : exportTriggers) {
                    if (!runBreak && exportTrigger.isBeforeTransferAtSource()) {
                        if (debugEnabled) {
                            msg = "执行交换前事件 触发器语句 = " + exportTrigger.getTriggerSql() + " 触发器类型 = 源表级触发器";
                            logger.debug(msg);

                            TaskConsoleWriteUtils.write(taskId, msg);
                        }


                        try {
                            TransferManagerImpl.execTriggerSql(conn, exportTrigger.getTriggerSql(), 2,
                                    exportTrigger.getIsprocedure(), null, nSucceed, nErrors, sLastErrorMsg, beginTime,
                                    beginTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "导出前源数据库触发器执行错误：" + e.getMessage();
                            logger.error(msg);
                            if (taskDetailLog != null) {
                                taskDetailLog.appendOtherMessage(msg);
                            }
                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
            }

            msg = "执行导出Sql查询语句 = " + exportSql.getQuerySql();
            logger.info(msg);
            TaskConsoleWriteUtils.write(taskId, msg);


            PreparedStatement pStmt = conn.prepareStatement(exportSql.getQuerySql());
            ResultSet rs = pStmt.executeQuery();
            ResultSetMetaData rsmd = pStmt.getMetaData();
            int columnCount = rsmd.getColumnCount();
            int fieldCount = exportSql.getExportFieldCount();

            tableWriter.writeTableBegin();
            while (rs.next()) {
                try {
                    if (exportTriggers != null) {
                        for (ExportTrigger exportTrigger : exportTriggers) {
                            if (!runBreak && exportTrigger.isBeforeWriteAtSource()) {
                                if (debugEnabled) {
                                    msg = "执行导出前事件 触发器语句 = " + exportTrigger.getTriggerSql()
                                            + " 触发器类型 = 源表行级触发器";
                                    logger.debug(msg);
                                    TaskConsoleWriteUtils.write(taskId, msg);
                                }

                                TransferManagerImpl.execTriggerSql(conn, exportTrigger.getTriggerSql(), 1,
                                        exportTrigger.getIsprocedure(), rs, nSucceed, nErrors, sLastErrorMsg,
                                        beginTime, beginTime);
                            }
                        }
                    }

                    tableWriter.writeRowBegin();
                    for (int i = 0; i < columnCount && i < fieldCount; i++) {
                        if (rs.getObject(i + 1) == null) {
                            continue;
                        }
                        ExportField field = exportSql.getExportField(i);

                        int ft = ItemValue.mapDbType(field.getFieldType());
                        switch (ft) {
                            case ItemValue.datetime:
                                tableWriter.writeDatetimeField(field.getFieldName(), rs.getDate(i + 1));
                                break;
                            case ItemValue.blob:
                                tableWriter.writeBlobField(field.getFieldName(), rs.getBytes(i + 1),
                                        "1".equals(field.getFieldStoreType()), "lob" + nRows);
                                break;
                            case ItemValue.clob:
                                tableWriter.writeClobField(field.getFieldName(), rs.getString(i + 1),
                                        "1".equals(field.getFieldStoreType()), "lob" + nRows);
                                break;
                            case ItemValue.varchar:
                                String sv = rs.getString(i + 1);
                                if (sv.length() > 200)
                                    tableWriter.writeCDataField(field.getFieldName(), sv);
                                else
                                    tableWriter.writeField(field.getFieldName(), sv);
                                break;
                            default:
                                tableWriter.writeField(field.getFieldName(), rs.getString(i + 1));
                                break;
                        }
                        // tableWriter.writeField(field.getFieldName()
                        // ,rs.getObject(i+1).toString());
                    }
                    tableWriter.writeRowEnd();

                    nRows++;

                    if (exportTriggers != null) {
                        for (ExportTrigger exportTrigger : exportTriggers) {
                            if (!runBreak && exportTrigger.isAfterWriteAtSource()) {

                                if (debugEnabled) {
                                    msg = "执行导出后事件 触发器语句 = " + exportTrigger.getTriggerSql()
                                            + " 触发器类型 = 源表行级触发器";
                                    logger.debug(msg);
                                    TaskConsoleWriteUtils.write(taskId, msg);
                                }

                                TransferManagerImpl.execTriggerSql(conn, exportTrigger.getTriggerSql(), 1,
                                        exportTrigger.getIsprocedure(), rs, nSucceed, nErrors, sLastErrorMsg,
                                        beginTime, beginTime);
                            }
                        }
                    }
                    nSucceed++;

                    if (debugEnabled) {
                        msg = "已成功导出 " + nSucceed + " 行数据";
                        logger.debug(msg);

                        TaskConsoleWriteUtils.write(taskId, msg);
                    }

                    TaskConsoleWriteUtils.writeProcess(taskId, nSucceed, nErrors, null);

                } catch (SQLException e) {
                    nErrors++;
                    sLastErrorMsg = e.getMessage();
                    try {
                        conn.rollback();

                    } catch (Exception e2) {
                        logger.error(e2.getMessage());
                        sLastErrorMsg = sLastErrorMsg + " || " + e2.getMessage();

                        TaskConsoleWriteUtils.writeError(taskId, sLastErrorMsg);
                    }
                    if (exportTriggers != null) {
                        try {
                            for (ExportTrigger exportTrigger : exportTriggers) {
                                if (!runBreak && exportTrigger.isWriteErrorAtSource()) {

                                    if (debugEnabled) {
                                        msg = "执行交换后错误事件 触发器语句 = " + exportTrigger.getTriggerSql()
                                                + " 触发器类型 = 源表行级触发器";
                                        logger.debug(msg);
                                        TaskConsoleWriteUtils.writeError(taskId, msg);
                                    }

                                    TransferManagerImpl.execTriggerSql(conn, exportTrigger.getTriggerSql(), 1,
                                            exportTrigger.getIsprocedure(), rs, nSucceed, nErrors, sLastErrorMsg,
                                            beginTime, beginTime);
                                }
                            }
                            conn.commit();
                        } catch (Exception e2) {

                            sLastErrorMsg = sLastErrorMsg + " || " + e2.getMessage();

                            TaskConsoleWriteUtils.writeError(taskId, sLastErrorMsg);
                        }
                    }
                    // 记录错误日志
                    if (taskDetailLog != null) {
                        TaskErrorData taskErrorData = new TaskErrorData();
                        taskErrorData.setDataId(taskErrorDataManager.getTaskErrorId());
                        taskErrorData.setLogDetailId(taskDetailLog.getLogDetailId());
                        taskErrorData.setErrorMessage(sLastErrorMsg);
                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < columnCount; i++) {
                            int feildType = ItemValue.mapDbType(exportSql.getExportField(i).getFieldType());
                            // mapinfo.getFieldsMap().get(i).getRightColType();
                            if (feildType == 4 || feildType == 5) {
                                sb.append("LOB,");
                            } else {
                                if (rs.getObject(i + 1) != null) {
                                    sb.append(rs.getObject(i + 1).toString());
                                }
                                sb.append(",");
                            }
                        }
                        taskErrorData.setDataContent(sb.toString());
                        // taskErrorDataManager.saveTaskErrorData(taskErrorData);
                        taskErrorDataManager.saveObject(taskErrorData);
                    }
                    // e.printStackTrace();
                }
            }

            tableWriter.writeTableEnd();
            Date endTime = DatetimeOpt.currentSqlDate();
            if (exportTriggers != null) {
                for (ExportTrigger exportTrigger : exportTriggers) {
                    if (!runBreak && exportTrigger.isAfterTransferAtSource()) {
                        try {
                            TransferManagerImpl.execTriggerSql(conn, exportTrigger.getTriggerSql(), 2,
                                    exportTrigger.getIsprocedure(), null, nSucceed, nErrors, sLastErrorMsg, beginTime,
                                    endTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "导出后源数据库触发器异常：" + e.getMessage();
                            logger.error(msg);
                            if (taskDetailLog != null) {
                                taskDetailLog.appendOtherMessage(msg);
                            }

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
            }

            ConnPool.closeConn(conn);
        } catch (SQLException e) {
            nRows = -1;
            msg = e.getMessage() + " cause by :" + e.getCause().getMessage();
            logger.error(msg);
            // e.printStackTrace();

            TaskConsoleWriteUtils.writeError(taskId, msg);
        }
        return nRows;
    }

    @Override
    public int doExport(Long exportID, String usercode) {
        ExportSql exportSql = exportSqlDao.getObjectById(exportID);
        TableFileWriter tableWriter = new TableFileWriter();

        tableWriter.setFilePath("[export]/temp");

        tableWriter.setExportName("exp" + exportSql.getExportId());
        tableWriter.setDataOptId(exportSql.getDataOptId());
        tableWriter.setSourceOsId(exportSql.getSourceOsId());
        tableWriter.setSourceDBName(exportSql.getSourceDatabaseName());

        tableWriter.prepareWriter();
        // exportSql.getExportField()
        int nRes = doExportSql(exportSql, tableWriter, usercode, null);
        tableWriter.closeWriter();
        return nRes;
    }

    @Override
    public String runExportTask(Long taskID, String userCode, String runType,String taskType) {
        ExchangeTask exchangeTask = exchangeTaskDao.getObjectById(taskID);
        exchangeTask.setLastRunTime(DatetimeOpt.currentSqlDate());
        exchangeTaskDao.saveObject(exchangeTask);
        String msg = "开始执行任务编号 = " + taskID + " 导出任务名称 = " + exchangeTask.getTaskName() + " 的导出任务........";
        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskID, msg);

        List<ExchangeTaskdetail> exchangeTaskdetails = exchangeTaskdetailDao.getTaskDetails(taskID);

        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(taskID);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType(runType);
        taskLog.setRunner(userCode);
        taskLog.setTaskType(taskType);
        taskLogManager.saveObject(taskLog);

        ExchangeFileWriter ef = new ExchangeFileWriter();

        String export = SysParametersUtils.getStringValue("export");
        String ddeId = SysParametersUtils.getStringValue("dde_id");
        String filePath = SysParametersUtils.getStringValue("app.home") + "/export/temp";

        TaskConsoleWriteUtils.write(taskID, "导出文件目录 = " + export);

        ef.setFilePath(export);
        // FileSystemOpt.createDirect(appPath+"/temp");
        ef.setDdeID(ddeId);
        ef.setExchangeName(exchangeTask.getTaskName());
        ef.setTaskID(String.valueOf(taskLogId));
        ef.setOperator(userCode);
        ef.setExportTime(DatetimeOpt.currentUtilDate());

        ef.prepareWriter();
        ef.writeExchangeBegin();
        ef.writeDataBegin();

        int nError = 0;
        int nSucceed = 0;
        for (ExchangeTaskdetail taskDetail : exchangeTaskdetails) {
            ExportSql exportSql = exportSqlDao.getObjectById(taskDetail.getMapinfoId());
            if (exportSql == null) {
                msg = "主键 = " + taskDetail.getMapinfoId() + " 的导出数据不存在";
                logger.error(msg);

                TaskConsoleWriteUtils.writeError(taskID, msg);
                continue;
            }

            TaskDetailLog taskDetailLog = new TaskDetailLog();
            Long taskDetailLogId = taskDetailLogManager.getTaskDetailLogId();
            taskDetailLog.setLogDetailId(taskDetailLogId);
            taskDetailLog.setLogId(taskLogId);
            taskDetailLog.setExportId(exportSql.getExportId());
            Date beginTime = DatetimeOpt.currentSqlDate();
            taskDetailLog.setRunBeginTime(beginTime);
            taskDetailLogManager.saveObject(taskDetailLog);

            TableFileWriter tableWriter = new TableFileWriter();

            tableWriter.setFilePath(filePath);
            tableWriter.setExportName("exp" + exportSql.getExportId());
            tableWriter.setDataOptId(exportSql.getDataOptId());
            tableWriter.setSourceOsId(exportSql.getSourceOsId());
            tableWriter.setSourceDBName(exportSql.getSourceDatabaseName());
            tableWriter.setFilePath(ef.getExchangeFilePath());
            // 判断是否需要写入到单独的文件中
            if ("1".equals(exportSql.getTableStoreType())) {
                tableWriter.prepareWriter();
            } else {
                tableWriter.setTableWriter(ef.getExchangeWriter());
            }

            int nRes = doExportSql(exportSql, tableWriter, userCode, taskDetailLog);

            Date endTime = DatetimeOpt.currentSqlDate();
            taskDetailLog.setRunEndTime(endTime);
            if (nRes < 0) {
                nError++;
                taskDetailLog.setErrorPieces(Long.valueOf(0 - nRes));
                msg = "导出失败。";
                taskDetailLog.appendOtherMessage(msg);

                TaskConsoleWriteUtils.writeError(taskID, msg);
            } else {
                nSucceed++;
                msg = "导出完成，文件路径 = " + ef.getExchangeFilePath() + ".zip";
                taskDetailLog.appendOtherMessage(msg);
                taskDetailLog.setSuccessPieces(Long.valueOf(nRes));

                TaskConsoleWriteUtils.writeInfo(taskID, msg);
            }
            taskDetailLogManager.saveObject(taskDetailLog);

        }
        ef.writeDataEnd();
        ef.writeExchangeEnd();
        ef.closeWriter();
        // 打包成ZIP文件
        ef.compressExchangeFile();

        String message = "完成" + String.valueOf(nError + nSucceed) + "组交换,成功" + String.valueOf(nSucceed) + "组，失败"
                + String.valueOf(nError) + "组。";
        msg = "交换任务" + exchangeTask.getTaskName() + message;
        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskID, msg);
        TaskConsoleWriteUtils.writeInfo(taskID, "导出文件目录 = " + export);
        TaskConsoleWriteUtils.stop(taskID);

        // TaskLog taskLogTemp = taskLogManager.getObjectById(taskLogId);
        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLog.setOtherMessage(message);
        taskLogManager.saveObject(taskLog);

        return message;

    }

    @Override
    public int doCallService(Long exportID, String usercode) {
        ExportSql exportSql = exportSqlDao.getObjectById(exportID);
        TableFileWriter tableWriter = new TableFileWriter();

        tableWriter.setExportName("exp" + exportSql.getExportId());
        tableWriter.setDataOptId(exportSql.getDataOptId());
        tableWriter.setSourceOsId(exportSql.getSourceOsId());
        tableWriter.setSourceDBName(exportSql.getSourceDatabaseName());

        tableWriter.prepareMemoryWriter();
        // exportSql.getExportField()
        int nRes = doExportSql(exportSql, tableWriter, usercode, null);

        String xmlData = tableWriter.getMemoryDataXML();//.toString();

        tableWriter.closeWriter();

        if (nRes > 0) {
            executeDataMap.doExecute(xmlData, usercode, "1", 0l);
        }

        return nRes;
    }

    @Override
    public String runCallServiceTask(Long taskID, String usercode, String runType,String taskType) {
        ExchangeTask exchangeTask = exchangeTaskDao.getObjectById(taskID);
        exchangeTask.setLastRunTime(DatetimeOpt.currentSqlDate());
        exchangeTaskDao.saveObject(exchangeTask);
        logger.info("开始执行导出：" + exchangeTask.getTaskName() + "........");
        List<ExchangeTaskdetail> exchangeTaskdetails = exchangeTaskdetailDao.getTaskDetails(taskID);

        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(taskID);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType(runType);
        taskLog.setRunner(usercode);
        taskLog.setTaskType(taskType);
        taskLogManager.saveObject(taskLog);

        int nError = 0;
        int nSucceed = 0;
        for (ExchangeTaskdetail taskDetail : exchangeTaskdetails) {
            ExportSql exportSql = exportSqlDao.getObjectById(taskDetail.getMapinfoId());
            if (exportSql == null) {
                continue;
            }

            TaskDetailLog taskDetailLog = new TaskDetailLog();
            Long taskDetailLogId = taskDetailLogManager.getTaskDetailLogId();
            taskDetailLog.setLogDetailId(taskDetailLogId);
            taskDetailLog.setLogId(taskLogId);
            taskDetailLog.setExportId(exportSql.getExportId());
            Date beginTime = DatetimeOpt.currentSqlDate();
            taskDetailLog.setRunBeginTime(beginTime);
            taskDetailLogManager.saveObject(taskDetailLog);

            TableFileWriter tableWriter = new TableFileWriter();

            tableWriter.setExportName("exp" + exportSql.getExportId());
            tableWriter.setDataOptId(exportSql.getDataOptId());
            tableWriter.setSourceOsId(exportSql.getSourceOsId());
            tableWriter.setSourceDBName(exportSql.getSourceDatabaseName());
            tableWriter.prepareMemoryWriter();

            int nRes = doExportSql(exportSql, tableWriter, usercode, taskDetailLog);

            tableWriter.closeWriter();
            String xmlData = tableWriter.getMemoryDataXML();

            if (nRes > 0)
                nRes = executeDataMap.doExecute(xmlData, usercode, "0", taskLogId);

            Date endTime = DatetimeOpt.currentSqlDate();
            taskDetailLog.setRunEndTime(endTime);
            if (nRes < 0) {
                nError++;
                taskDetailLog.setErrorPieces(Long.valueOf(0 - nRes));
                taskDetailLog.appendOtherMessage("导出失败。");
            } else {
                nSucceed++;
                taskDetailLog.appendOtherMessage("导出完成。");
                taskDetailLog.setSuccessPieces(Long.valueOf(nRes));

            }
            taskDetailLogManager.saveObject(taskDetailLog);

        }

        String message = "完成" + String.valueOf(nError + nSucceed) + "组交换,成功" + String.valueOf(nSucceed) + "组，失败"
                + String.valueOf(nError) + "组。";
        logger.info("交换任务" + exchangeTask.getTaskName() + message);

        // TaskLog taskLogTemp = taskLogManager.getObjectById(taskLogId);
        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLog.setOtherMessage(message);
        taskLogManager.saveObject(taskLog);

        return message;

    }

}
