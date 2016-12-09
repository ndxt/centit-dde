package com.centit.dde.dataio;

import com.centit.dde.dao.DataOptInfoDao;
import com.centit.dde.dao.ImportOptDao;
import com.centit.dde.datafile.TableFileReader;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.dde.util.ConnPool;
import com.centit.dde.util.ItemValue;
import com.centit.dde.util.TaskConsoleWriteUtils;
import com.centit.dde.ws.UploadData;
import com.centit.dde.ws.WebServiceTransferClient;
import com.centit.dde.ws.WsDataException;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OsInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class ExecuteDataMapImpl implements ExecuteDataMap {

    private static final Log logger = LogFactory.getLog(ExecuteDataMapImpl.class);

    @Resource
    protected StaticEnvironmentManager platformEnvironment;
    //private static boolean debugEnabled = logger.isDebugEnabled();

    private DataOptInfoDao dataOptInfoDao;

    private ImportOptDao importOptDao;

    private TaskErrorDataManager taskErrorDataManager;

    private TaskDetailLogManager taskDetailLogManager;

    private TaskLogManager taskLogManager;

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    public void setTaskDetailLogManager(TaskDetailLogManager taskDetailLogManager) {
        this.taskDetailLogManager = taskDetailLogManager;
    }

    public void setTaskErrorDataManager(TaskErrorDataManager taskErrorDataManager) {
        this.taskErrorDataManager = taskErrorDataManager;
    }

     public void setImportOptDao(ImportOptDao importOptDao) {
        this.importOptDao = importOptDao;
    }

    public void setDataOptInfoDao(DataOptInfoDao dataOptInfoDao) {
        this.dataOptInfoDao = dataOptInfoDao;
    }

     /**
     * @param xmlData
     * @param usercode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return
     * @throws SqlResolveException
     */
    public int doExecute(TableFileReader xmlData, String usercode, String runType, Long taskLogId)
            throws SqlResolveException {
        String msg = null;

        TaskLog taskLog = taskLogManager.getObjectById(taskLogId);
        Long taskId = taskLog.getTaskId();

        if (logger.isDebugEnabled()) {
            logger.debug("xmlData = " + xmlData);
            logger.debug("用户代码 = " + usercode + " 运行类型 = " + runType + " 任务日志id = " + taskLogId);
        }

        String mapinfoId = xmlData.getDataOptId();

        if (logger.isDebugEnabled()) {
            String runTypeText = "手动";
            if ("2".equals(runType)) {
                runTypeText = "WebService接口";
            } else if ("0".equals(runType)) {
                runTypeText = "系统自动";
            }
            logger.debug(runTypeText + " 执行的 业务操作ID = " + mapinfoId);
        }

        // 开始运行
        DataOptInfo optInfo = dataOptInfoDao.getObjectById(mapinfoId);
        if (optInfo == null) {
            String message = "交换平台中找不到对应：" + mapinfoId + " 业务的操作。";
            logger.error(message);
            // throw new Exception("交换平台中找不到对应："+mapinfoId+" 业务的操作。");

            TaskConsoleWriteUtils.writeError(taskId, message);

            if (logger.isDebugEnabled()) {
                logger.debug(message);
            }

            return -1;
        }
        int nRes = 0;
        for (DataOptStep optStep : optInfo.getDataOptSteps()) {
            String message = "无法获取业务系统信息，OSID：" + optStep.getOsId();
            if ("1".equals(optStep.getOptType())) {
                msg = "正在执行离线文件导入操作...";
                logger.info(msg);
                TaskConsoleWriteUtils.write(taskId, msg);
                ImportOpt importOpt = importOptDao.getObjectById(optStep.getImportId());
                if (importOpt != null) {
                    doMergeToDatabase(xmlData, importOpt, taskLogId);
                } else {
                    nRes++;
                    logger.error(message);

                    TaskConsoleWriteUtils.writeError(taskId, message);

                    if (logger.isDebugEnabled()) {
                        logger.debug(message + "计数变量 nRes = " + nRes);
                    }
                }
            } else if ("2".equals(optStep.getOptType())) {
                msg = "执行WebService接口导入操作...";
                logger.info(msg);
                TaskConsoleWriteUtils.write(taskId, msg);
                OsInfo osInfo = platformEnvironment.getOsInfo(optStep.getOsId());
                if (osInfo != null) {
                    doCallWebService(xmlData, osInfo, taskLogId);
                } else {
                    nRes++;
                    logger.error(message);
                    if (logger.isDebugEnabled()) {
                        logger.debug(message + "计数变量 nRes = " + nRes);
                    }
                }
            }
        }
        return nRes;
    }

    public int doCallWebService(TableFileReader xmlData, OsInfo osInfo, Long taskLogId) {

        TaskDetailLog taskDetailLog = new TaskDetailLog();
        Long taskDetailLogId = taskDetailLogManager.getTaskDetailLogId();
        taskDetailLog.setLogDetailId(taskDetailLogId);
        taskDetailLog.setLogId(taskLogId);
        taskDetailLog.setOsId(osInfo.getOsId());
        Date beginTime = DatetimeOpt.currentSqlDate();
        taskDetailLog.setRunBeginTime(beginTime);
        taskDetailLogManager.saveObject(taskDetailLog);


        TaskLog taskLog = taskLogManager.getObjectById(taskLogId);
        Long taskId = taskLog.getTaskId();
        String msg = null;

        UploadData dataIterface = WebServiceTransferClient.getWSDLInterface(
                osInfo.getDdeSyncUrl(), UploadData.class);

        msg = "WebService接口地址URL = " + osInfo.getDdeSyncUrl();
        logger.info(msg);
        TaskConsoleWriteUtils.write(taskId, msg);

        if (dataIterface == null) {
            msg = "WebService接口地址解析失败，返回 -2";
            logger.error(msg);
            TaskConsoleWriteUtils.writeError(taskId, msg);
            return -2;
        }
        String res = "res";// dataIterface.uploadTableAsXml(osInfo.
                //StringBaseOpt.decryptBase64Des(osInfo.getOsPassword()), xmlData.getXML());

        Date endTime = DatetimeOpt.currentSqlDate();
        taskDetailLog.setRunEndTime(endTime);
        taskDetailLog.appendOtherMessage(res);

        if ("0:ok".equals(res)) {
            taskDetailLog.setSuccessPieces(1l);
            taskDetailLog.setErrorPieces(0l);
            taskDetailLogManager.saveObject(taskDetailLog);
            return 0;
        } else {// write log
            taskDetailLog.setSuccessPieces(0l);
            taskDetailLog.setErrorPieces(1l);
            taskDetailLogManager.saveObject(taskDetailLog);
            return -3;
        }
    }

    private static void setAdoParameter(PreparedStatement souce, int pn, String fieldName,
                                        Map<String, ItemValue> rowData) throws SQLException {
        // int rsc =
        // rs.getMetaData().getColumnCount();//rs.getMetaData().getColumnType(sn);
        if (null == rowData.get(fieldName)) {
            souce.setObject(pn, null);
            return;
        }

        if (logger.isDebugEnabled()) {

            if (String.class.equals(rowData.get(fieldName).getValue().getClass())) {
                String text = null;
                String value = String.valueOf(rowData.get(fieldName).getValue());
                if (200 < value.length()) {
                    text = "字段值长度大于200， 起始值 = " + value.substring(0, 100) + " ... 结尾值 = "
                            + value.substring(value.length() - 100, value.length() - 1);
                } else {
                    text = value;
                }

                logger.debug("设置参数类型，fieldName = " + fieldName + " fieldValue = " + text + " 参数值类型 = "
                        + rowData.get(fieldName).getValue().getClass().getName());
            } else {
                logger.debug("设置参数类型，fieldName = " + fieldName + " fieldValue = " + rowData.get(fieldName).getValue()
                        + " 参数值类型 = " + rowData.get(fieldName).getValue().getClass().getName());
            }

        }
        if (rowData.get(fieldName).getValue() instanceof java.util.Date) {
            souce.setObject(pn, DatetimeOpt.convertSqlDate((java.util.Date) rowData.get(fieldName).getValue()));
        } else {
            souce.setObject(pn, rowData.get(fieldName).getValue());
        }

    }

    private static boolean exeInsert(PreparedStatement pStmtSql, Map<String, ItemValue> rowData, ImportSqlOpt mapinfo)
            throws SQLException {
        // int nC = pStmtSql.getParameterMetaData().getParameterCount();

        if (logger.isDebugEnabled()) {
            logger.debug("执行Insert操作开始");
        }

        int nC = mapinfo.getPrameterSum();
        for (int i = 0; i < nC; i++) {
            ImportField field = mapinfo.getImportField(mapinfo.getInsertFieldMap().get(i));

            if (logger.isDebugEnabled()) {
                logger.debug("字段信息 " + field);
            }

            setAdoParameter(pStmtSql, i + 1, field.getSourceFieldName(), rowData);
        }
        boolean result = pStmtSql.execute();

        if (logger.isDebugEnabled()) {
            logger.debug("执行Insert操作结束,执行结果 = " + result);
        }
        return result;
        // dealBlob(pStmtSql, rs, mapinfo);
        // dealClob(pStmtSql, rs, mapinfo);

    }

    private static boolean exeUpdate(PreparedStatement pStmtSql, Map<String, ItemValue> rowData, ImportSqlOpt mapinfo)
            throws SQLException {
        // int nC = pStmtSql.getParameterMetaData().getParameterCount();

        int nC = mapinfo.getPrameterSum();

        if (logger.isDebugEnabled()) {
            logger.debug("执行Update操作开始，参数prameterSum值 = " + nC);
        }

        for (int i = 0; i < nC; i++) {
            ImportField field = mapinfo.getImportField(mapinfo.getUpdateFieldMap().get(i));
            if (logger.isDebugEnabled()) {
                logger.debug("导入字段值 field =  " + field);
            }
            setAdoParameter(pStmtSql, i + 1, field.getSourceFieldName(), rowData);
        }
        boolean result = pStmtSql.execute();

        if (logger.isDebugEnabled()) {
            logger.debug("执行Update操作结束，执行结果 = " + result);
        }

        return result;
        // dealBlob(pStmtSql, rs, mapinfo);
        // dealClob(pStmtSql, rs, mapinfo);
    }

    private static void exeMoveData(PreparedStatement isExistStmt, PreparedStatement updateStmt,
                                    PreparedStatement insertStmt, Map<String, ItemValue> rowData, ImportSqlOpt mapinfo) throws SQLException {
        if (mapinfo.getRowOptType().equals("insert"))
            exeInsert(insertStmt, rowData, mapinfo);
        else if (mapinfo.getRowOptType().equals("update"))
            exeUpdate(updateStmt, rowData, mapinfo);
        else {
            int nPC = isExistStmt.getParameterMetaData().getParameterCount();
            if (logger.isDebugEnabled()) {
                logger.debug("执行Margen操作开始，参数getParameterCount()值 = " + nPC);
            }
            // logger.info("语句 ExistSql 参数个数")
            for (int i = 0; i < nPC; i++) {
                ImportField field = mapinfo.getImportField(mapinfo.getKeyFieldMap().get(i));
                setAdoParameter(isExistStmt, i + 1, field.getSourceFieldName(), rowData);
            }
            int nC = 0;
            boolean result = false;
            ResultSet rsTmp = isExistStmt.executeQuery();
            if (rsTmp.next()) {
                nC = rsTmp.getInt(1);
            }
            if (nC > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("执行Margen操作时调用Update操作");
                }

                result = exeUpdate(updateStmt, rowData, mapinfo);
            } else {
                result = exeInsert(insertStmt, rowData, mapinfo);

                if (logger.isDebugEnabled()) {
                    logger.debug("执行Margen操作时调用Insert操作");
                }
            }

            if (logger.isDebugEnabled()) {
                logger.debug("执行Margen操作结束，执行结果  = " + result);
            }
        }
    }

    private static void setAdoParameter2(PreparedStatement souce, int pn, String sPN, int nSqlType,
                                         Map<String, ItemValue> rowData, long nMoved, long nError, String sLastErrorMsg, Date dtBeginMove,
                                         Date dtEndMove) throws SQLException {
        String sPrmName = sPN.toUpperCase();

        if (logger.isDebugEnabled()) {
            logger.debug("参数 sPN.toUpperCase() = " + sPrmName);
        }

        if (sPrmName.equals("TODAY")) {
            souce.setTimestamp(pn, new java.sql.Timestamp(System.currentTimeMillis()));
        } else if (nSqlType != 2) {
            if ((nSqlType == 1) && sPrmName.equals("SQL_ERROR_MSG")) {
                souce.setString(pn, sLastErrorMsg);
            } else if (rowData != null) {
                setAdoParameter(souce, pn, sPN, rowData);
            }
        } else {
            if (sPrmName.equals("SYNC_DATA_PIECES"))
                souce.setLong(pn, nMoved + nError);
            else if (sPrmName.equals("SUCCEED_PIECES"))
                souce.setLong(pn, nMoved);
            else if (sPrmName.equals("FAULT_PIECES"))
                souce.setLong(pn, nError);
            else if (sPrmName.equals("SYNC_BEGIN_TIME"))
                souce.setDate(pn, dtBeginMove);
            else if (sPrmName.equals("SYNC_END_TIME"))
                souce.setDate(pn, dtEndMove);
        }
    }

    private static void execTriggerSql(Connection souce, String sql, int sqlType, String isProcedure,
                                       Map<String, ItemValue> rowData, long nMoved, long nError, String sLastErrorMsg, Date dtBeginMove,
                                       Date dtEndMove) throws SQLException {
        // 存储过程其实也可以用下面的语句执行，这段代码应该不会执行到。
        if ("1".equals(isProcedure)) {
            if (logger.isDebugEnabled()) {
                logger.debug("执行触发器时 执行存储过程...");
            }
            CallableStatement callStmt = null;
            try {
                callStmt = souce.prepareCall(sql);
                callStmt.execute();
            } finally {
                if (null != callStmt) {
                    try {
                        callStmt.close();
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        } else {
            PreparedStatement pSouce = null;
            try {
                List<String> sqlParameters = QueryUtils.getSqlNamedParameters(sql);//.getRight();

                if (logger.isDebugEnabled()) {
                    logger.debug("执行触发器的sql语句为 = " + sql + " 解析参数结果 = " + sqlParameters);
                }

                pSouce = souce.prepareStatement(sqlParameters.get(sqlParameters.size() - 1));
                for (int i = 1; i <= pSouce.getParameterMetaData().getParameterCount(); i++) {
                    String sPN = sqlParameters.get(i - 1);
                    setAdoParameter2(pSouce, i, sPN, sqlType, rowData, nMoved, nError, sLastErrorMsg, dtBeginMove,
                            dtEndMove);
                }
                pSouce.execute();
                pSouce.close();
            } finally {
                if (null != pSouce) {
                    try {
                        pSouce.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public int doMergeToDatabase(TableFileReader xmlData, ImportOpt importOpt, Long taskLogId)
            throws SqlResolveException {
        // String mapinfoId= xmlData.getMapinfoId();
        // importOpt.
        TaskDetailLog taskDetailLog = new TaskDetailLog();
        Long taskDetailLogId = taskDetailLogManager.getTaskDetailLogId();
        taskDetailLog.setLogDetailId(taskDetailLogId);
        taskDetailLog.setLogId(taskLogId);
        taskDetailLog.setMapinfoId(importOpt.getImportId());
        Date beginTime = DatetimeOpt.currentSqlDate();
        taskDetailLog.setRunBeginTime(beginTime);
        taskDetailLogManager.saveObject(taskDetailLog);

        String msg = null;
        TaskLog taskLog = taskLogManager.getObjectById(taskLogId);
        Long taskId = taskLog.getTaskId();


        // D_DataBase_info
        // importOpt.getDestDatabaseName();
        List<ImportTrigger> importTriggers = importOpt.getImportTriggers();
        // 开始运行
        long nErrors = 0;
        long nSucceed = 0;
        long nRows = xmlData.getRowSum();
        boolean runBreak = false;
        String sLastErrorMsg = "";
        Date endTime = null;

        DatabaseInfo dbInfo = platformEnvironment.getDatabaseInfo(importOpt.getDestDatabaseName());

        SQLException se = null;
        try {
            Connection conn = ConnPool.getConn(dbInfo);

            // 交换前事件
            if (importTriggers != null) {
                for (ImportTrigger importTrigger : importTriggers) {
                    if (!runBreak && importTrigger.isBeforeTransferAtDest()) {
                        if (logger.isDebugEnabled()) {
                            msg = "执行交换前事件触发器... 触发器语句 = " + importTrigger.getTriggerSql();
                            logger.debug(msg);

                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(conn, importTrigger.getTriggerSql(), 2, importTrigger.getIsprocedure(),
                                    null, nSucceed, nErrors, sLastErrorMsg, beginTime, beginTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "交换前目标数据库触发器执行错误：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("开始执行导入数据操作...");
            }
            ImportSqlOpt sqlOpt = new ImportSqlOpt();
            sqlOpt.loadMapFromData(importOpt);

            PreparedStatement insertStmt = null;
            PreparedStatement updateStmt = null;
            PreparedStatement isExistStmt = null;
            if (!sqlOpt.getRowOptType().equals("update")) {
                insertStmt = conn.prepareStatement(sqlOpt.getInsertSql());

                if (logger.isDebugEnabled()) {
                    logger.debug("导入数据操作 为" + sqlOpt.getRowOptType() + " 操作 sql = " + sqlOpt.getInsertSql());
                }
            }
            if (!sqlOpt.getRowOptType().equals("insert")) {
                updateStmt = conn.prepareStatement(sqlOpt.getUpdateSql());

                if (logger.isDebugEnabled()) {
                    logger.debug("导入数据操作 为" + sqlOpt.getRowOptType() + "操作 sql = " + sqlOpt.getInsertSql());
                }
            }
            if (sqlOpt.getRowOptType().equals("merge")) {
                isExistStmt = conn.prepareStatement(sqlOpt.getIsExistSql());

                if (logger.isDebugEnabled()) {
                    logger.debug("导入数据操作 为" + sqlOpt.getRowOptType() + "操作 sql = " + sqlOpt.getIsExistSql());
                }
            }

            // 交换前触发器如果有必须执行完全成功
            if (!runBreak) {
                if (logger.isDebugEnabled()) {
                    logger.debug("执行导入数据操作...");
                }

                for (int i = 0; i < nRows; i++) {
                    Map<String, ItemValue> rowData = xmlData.readRowData(xmlData.getRowElement(i));

                    try {
                        if (importTriggers != null) {
                            for (ImportTrigger importTrigger : importTriggers) {
                                if (importTrigger.isBeforeWriteAtDest()) {
                                    execTriggerSql(conn, importTrigger.getTriggerSql(), 1,
                                            importTrigger.getIsprocedure(), rowData, nSucceed, nErrors, sLastErrorMsg,
                                            beginTime, beginTime);
                                }
                            }
                        }

                        if (logger.isDebugEnabled()) {
                            logger.debug("执行导入，目标表 = " + sqlOpt.getDesTable() + " 开始");
                        }

                        exeMoveData(isExistStmt, updateStmt, insertStmt, rowData, sqlOpt);

                        if (logger.isDebugEnabled()) {
                            logger.debug("执行导入，目标表 = " + sqlOpt.getDesTable() + " 结束");
                        }
                        if (importTriggers != null) {
                            for (ImportTrigger importTrigger : importTriggers) {
                                if (importTrigger.isAfterWriteAtDest()) {
                                    execTriggerSql(conn, importTrigger.getTriggerSql(), 1,
                                            importTrigger.getIsprocedure(), rowData, nSucceed, nErrors, sLastErrorMsg,
                                            beginTime, beginTime);
                                }
                            }
                        }

                        nSucceed++;
                        conn.commit();

                        TaskConsoleWriteUtils.writeProcess(taskId, nSucceed, nErrors, null);
                    } catch (SQLException e) {
                        nErrors++;
                        sLastErrorMsg = e.getMessage();
                        try {
                            conn.rollback();

                        } catch (Exception e2) {
                            logger.error(e2.getMessage());
                            sLastErrorMsg = sLastErrorMsg + " || " + e2.getMessage();
                        }
                        if (importTriggers != null) {
                            try {
                                for (ImportTrigger importTrigger : importTriggers) {
                                    if (importTrigger.isWriteErrorAtDest()) {
                                        execTriggerSql(conn, importTrigger.getTriggerSql(), 1,
                                                importTrigger.getIsprocedure(), rowData, nSucceed, nErrors,
                                                sLastErrorMsg, beginTime, beginTime);
                                    }
                                }
                                conn.commit();
                            } catch (Exception e2) {
                                sLastErrorMsg = sLastErrorMsg + " || " + e.getMessage();
                                logger.error(sLastErrorMsg);
                                logger.error(e2.getMessage());

                                msg = sLastErrorMsg + " " + e2.getMessage();
                                TaskConsoleWriteUtils.writeError(taskId, msg);
                            }
                        }
                        // 记录错误日志
                        TaskErrorData taskErrorData = new TaskErrorData();
                        taskErrorData.setDataId(taskErrorDataManager.getTaskErrorId());
                        taskErrorData.setLogDetailId(taskDetailLogId);
                        taskErrorData.setErrorMessage(sLastErrorMsg);
                        taskErrorData.setDataContent(xmlData.getRowElement(i).asXML());
                        // taskErrorDataManager.saveTaskErrorData(taskErrorData);
                        taskErrorDataManager.saveObject(taskErrorData);
                        // e.printStackTrace();

                        se = e;
                    }
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("执行导入数据操作结束...");
                }
            }
            endTime = DatetimeOpt.currentSqlDate();
            if (!runBreak && importTriggers != null) {
                for (ImportTrigger importTrigger : importTriggers) {

                    if (importTrigger.isAfterTransferAtDest()) {
                        if (logger.isDebugEnabled()) {
                            msg = "执行交换后事件触发器... 触发器语句 = " + importTrigger.getTriggerSql();
                            logger.debug(msg);

                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(conn, importTrigger.getTriggerSql(), 2, importTrigger.getIsprocedure(),
                                    null, nSucceed, nErrors, sLastErrorMsg, beginTime, endTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "执行交换后目标数据库触发器异常：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);
                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
            }
            ConnPool.closeConn(conn);
        } catch (SQLException e) {
            se = e;
        }

        endTime = DatetimeOpt.currentSqlDate();
        taskDetailLog.setSuccessPieces(nRows - nErrors);
        taskDetailLog.setErrorPieces(nErrors);
        taskDetailLog.setRunEndTime(endTime);
        taskDetailLogManager.saveObject(taskDetailLog);

        if (null != se) {
            TaskConsoleWriteUtils.writeError(taskId, se.getMessage());
            throw new SqlResolveException(se.getMessage(), se);
        }

        return 0;
    }

    public int doExecute(String xmlData, String usercode, String runType, Long taskLogId) {
        //TODO 因此通常来说，0x00-0x20 都会引起一定的问题，又因为这些字符不可见，因此用通常的编辑器进行编辑的时候找不到问题所在。
        //文本过大的时候会不会影响效率???
        String xmlTableData = xmlData;//.replaceAll("[\\x00-\\x0c\\x0e-\\x1f]","");

        TableFileReader table = new TableFileReader();
        table.readTableInfoFromXML(xmlTableData);

        try {
            return doExecute(table, usercode, runType, taskLogId);
        } catch (SqlResolveException e) {
            logger.error(e.getMessage(), e);
            return -1;
        }
    }

    /**
     * 修改大字段数据内容
     *
     * @param database   数据库名称 D_DataBase_Info 的databasename
     * @param tableName
     * @param columnName
     * @param keyDesc
     * @param lobData
     * @return
     */
    public int updateLobField(String database, String tableName, String columnName, String keyDesc, final byte[] lobData)
            throws WsDataException {
        final LobHandler lobHandler = new DefaultLobHandler();
        return updateLobField(database, tableName, columnName, keyDesc,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
                            DataAccessException {
                        lobCreator.setBlobAsBytes(ps, 1, lobData);
                    }
                });
    }

    @Override
    public int updateLobField(String database, String tableName, String columnName, String keyDesc, final String lobData)
            throws WsDataException {
        final LobHandler lobHandler = new DefaultLobHandler();
        return updateLobField(database, tableName, columnName, keyDesc,
                new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
                    @Override
                    protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException,
                            DataAccessException {
                        lobCreator.setClobAsString(ps, 1, lobData);
                    }
                });
    }

    private int updateLobField(String database, String tableName, String columnName, String keyDesc,
                               AbstractLobCreatingPreparedStatementCallback callback) throws WsDataException {
        if (logger.isDebugEnabled()) {
            logger.debug("database = " + database + ' ' + "tableName = " + tableName + ' ' + "columnName = "
                    + columnName + " keyDesc = " + keyDesc);
        }

        DatabaseInfo dbInfo = platformEnvironment.getDatabaseInfo(database);
        if (null == dbInfo) {
            logger.error(SysParametersUtils.getStringValue("ERR-20002","ERR-20002"));
            throw new WsDataException(20002, null);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("DatabaseInfo 参数为 " + dbInfo);
        }

        DataSource ds = null;
        try {
            ds = ConnPool.getDataSource(dbInfo);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new WsDataException(-1, e.getMessage(), e);
        }

        String sql = "update " + tableName + " set " + columnName + "=? where " + keyDesc;

        if (logger.isDebugEnabled()) {
            logger.debug("sql拼装  = " + sql);
        }

        JdbcTemplate jt = new JdbcTemplate(ds);
        try {
            return jt.execute(sql, callback);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new WsDataException(-1, e.getMessage(), e);
        }
    }

}
