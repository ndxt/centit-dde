package com.centit.dde.transfer.Impl;

import com.centit.dde.dao.ExchangeMapinfoDao;
import com.centit.dde.dao.ExchangeTaskDao;
import com.centit.dde.dao.ExchangeTaskdetailDao;
import com.centit.dde.dao.MapinfoTriggerDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.*;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.dde.transfer.MapInfoDBConn;
import com.centit.dde.transfer.TableMapInfo;
import com.centit.dde.transfer.TransferManager;
import com.centit.dde.transfer.TransferResult;
import com.centit.dde.util.TaskConsoleWriteUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferManagerImpl implements TransferManager {
    private static final Log logger = LogFactory.getLog(TransferManager.class);

    private static boolean debugEnabled = logger.isDebugEnabled();

    private ExchangeMapinfoDao exchangeMapinfoDao;

     private ExchangeTaskDao exchangeTaskDao;

    private ExchangeTaskdetailDao exchangeTaskdetailDao;

    private MapinfoTriggerDao mapinfoTriggerDao;

    private TaskLogManager taskLogManager;

    private TaskErrorDataManager taskErrorDataManager;

    private TaskDetailLogManager taskDetailLogManager;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;

    public void setTaskDetailLogManager(TaskDetailLogManager taskDetailLogManager) {
        this.taskDetailLogManager = taskDetailLogManager;
    }

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    public void setTaskErrorDataManager(TaskErrorDataManager taskErrorDataManager) {
        this.taskErrorDataManager = taskErrorDataManager;
    }

    public void setMapinfoTriggerDao(MapinfoTriggerDao mapinfoTriggerDao) {
        this.mapinfoTriggerDao = mapinfoTriggerDao;
    }

    public void setExchangeTaskDao(ExchangeTaskDao exchangeTaskDao) {
        this.exchangeTaskDao = exchangeTaskDao;
    }

    public void setExchangeTaskdetailDao(ExchangeTaskdetailDao exchangeTaskdetailDao) {
        this.exchangeTaskdetailDao = exchangeTaskdetailDao;
    }

    public void setExchangeMapinfoDao(ExchangeMapinfoDao exchangeMapinfoDao) {
        this.exchangeMapinfoDao = exchangeMapinfoDao;
    }

    private static void setAdoParameter(PreparedStatement souce, int pn, int sn, ResultSet rs) throws SQLException {
        int rsc = rs.getMetaData().getColumnCount();// rs.getMetaData().getColumnType(sn);
        souce.setObject(pn, ((sn > rsc) ? null : rs.getObject(sn)));
    }

    private static void setAdoParameter(PreparedStatement souce, int pn, int sn, ResultSet rs, TableMapInfo mapinfo)
            throws SQLException {
        
        int rsc = rs.getMetaData().getColumnCount();
        if (sn > rsc) {
            souce.setObject(pn, null);
            return;
        }

        if (rs.getObject(sn) == null) {
            souce.setObject(pn, null);
            return;
        }
        // TableMapInfo.getType(String.valueOf(rs.getMetaData().getColumnTypeName(sn)));
        int sourceFieldType = mapinfo.getFieldsMap().get(sn - 1).getLeftColType();
        int destFieldType = mapinfo.getFieldsMap().get(sn - 1).getRightColType();

        if (sourceFieldType != destFieldType) {
            if (destFieldType == 4 && sourceFieldType == 5) {
                InputStream inputStream = rs.getBinaryStream(sn);
                if (inputStream != null)
                    souce.setAsciiStream(pn, inputStream);
                else
                    souce.setObject(pn, null);
            } else if (destFieldType == 5 && sourceFieldType == 4) {
                InputStream inputStream = rs.getAsciiStream(pn);
                if (inputStream != null)
                    souce.setBinaryStream(pn, inputStream);
                else
                    souce.setObject(pn, null);
            } else if (destFieldType == 4 && sourceFieldType == 2) {
                souce.setObject(pn, rs.getObject(sn));
            } else if (destFieldType == 2 && sourceFieldType == 1) {
                String rsString = String.valueOf(rs.getObject(sn));
                souce.setObject(pn, rsString);
            } else
                souce.setObject(pn, null);
        } else if (destFieldType == 5) {
            Blob b = rs.getBlob(sn);
            InputStream inputStream = b.getBinaryStream();
            if(null == inputStream) {
                souce.setObject(pn, null);
            }
        
            if(null != inputStream) {
                BufferedInputStream input = new BufferedInputStream(inputStream);               
                ByteArrayOutputStream   io = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];  
                int len;  
                try {
                    while ((len = input.read(buffer,0,buffer.length)) > -1 ) {  
                        io.write(buffer, 0, len);  
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }  
                try {
                    io.flush();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                souce.setBytes(pn, io.toByteArray());
                try {
                    io.close();
                    input.close();
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        } else if (destFieldType == 4) {
            Clob c = rs.getClob(sn);
            if(null == c) {
                logger.info("交换Clob字段，Clob字段为空");
            }
            if (null != c && debugEnabled) {
                logger.info("交换Clob字段，字段内容大小为" + (new BigDecimal(c.length()).divide(new BigDecimal(1024 * 1024), 2, BigDecimal.ROUND_HALF_EVEN)) + "M");
            }
            if(null != c) {
                souce.setClob(pn, c.getCharacterStream());
            }
            if (null != c && debugEnabled) {
                logger.info("Clob字段交换结束");
            }
        } else if (destFieldType == 3) {
            souce.setObject(pn, rs.getTimestamp(sn));
        } else {
            souce.setObject(pn, rs.getObject(sn));
        }

    }

    public static Timestamp currentSqlTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }

    /**
     * @param souce
     * @param pn
     * @param sPN
     * @param nSqlType      1:行级触发器 2：表级触发器
     * @param rs
     * @param nMoved
     * @param nError
     * @param sLastErrorMsg
     * @param dtBeginMove
     * @param dtEndMove
     * @throws SQLException
     */
    private static void setAdoParameter2(PreparedStatement souce, int pn, String sPN, int nSqlType, ResultSet rs,
                                         long nMoved, long nError, String sLastErrorMsg, Date dtBeginMove, Date dtEndMove) throws SQLException {
        String sPrmName = sPN.toUpperCase();
        if (sPrmName.equals("TODAY")) {
            souce.setTimestamp(pn, currentSqlTimestamp());
        } else if (nSqlType != 2) {
            if ((nSqlType == 1) && sPrmName.equals("SQL_ERROR_MSG")) {
                souce.setString(pn, sLastErrorMsg);
            } else if (rs != null) {
                setAdoParameter(souce, pn, rs.findColumn(sPN), rs);
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

    /**
     * @param souce
     * @param sql
     * @param sqlType       1:行级触发器 2：表级触发器
     * @param isProcedure
     * @param rs
     * @param nMoved
     * @param nError
     * @param sLastErrorMsg
     * @param dtBeginMove
     * @param dtEndMove
     * @throws SQLException
     */
    public static void execTriggerSql(Connection souce, String sql, int sqlType, String isProcedure, ResultSet rs,
                                      long nMoved, long nError, String sLastErrorMsg, Date dtBeginMove, Date dtEndMove) throws SQLException {
        // 存储过程其实也可以用下面的语句执行，这段代码应该不会执行到。
        if ("1".equals(isProcedure)) {
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
                List<String> sqlParameters = QueryUtils.getSqlNamedParameters(sql);
                pSouce = souce.prepareStatement(sqlParameters.get(sqlParameters.size() - 1));
                for (int i = 1; i <= pSouce.getParameterMetaData().getParameterCount(); i++) {
                    String sPN = sqlParameters.get(i - 1);
                    setAdoParameter2(pSouce, i, sPN, sqlType, rs, nMoved, nError, sLastErrorMsg, dtBeginMove, dtEndMove);
                }
                pSouce.execute();
                pSouce.close();
            } catch (SQLException e) {
                logger.error("执行触发器异常");
                logger.error(e.getMessage(), e);
            } finally {
                if (null != pSouce) {
                    try {
                        pSouce.close();
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

    private static void exeInsert(PreparedStatement pStmtSql, ResultSet rs, TableMapInfo mapinfo) throws SQLException {
        // int nC = pStmtSql.getParameterMetaData().getParameterCount();

        int nC = mapinfo.getPrameterSum();
        for (int i = 0; i < nC; i++)
            setAdoParameter(pStmtSql, i + 1, mapinfo.getInsertFieldMap().get(i) + 1, rs, mapinfo);
        pStmtSql.execute();

        // dealBlob(pStmtSql, rs, mapinfo);
        // dealClob(pStmtSql, rs, mapinfo);

    }

    private static void exeUpdate(PreparedStatement pStmtSql, ResultSet rs, TableMapInfo mapinfo) throws SQLException {
        // int nC = pStmtSql.getParameterMetaData().getParameterCount();

        int nC = mapinfo.getPrameterSum();

        for (int i = 0; i < nC; i++) {
            setAdoParameter(pStmtSql, i + 1, mapinfo.getUpdateFieldMap().get(i) + 1, rs, mapinfo);
        }
        pStmtSql.execute();

        // dealBlob(pStmtSql, rs, mapinfo);
        // dealClob(pStmtSql, rs, mapinfo);
    }

    private static void exeMoveData(PreparedStatement isExistStmt, PreparedStatement updateStmt,
                                    PreparedStatement insertStmt, ResultSet rs, TableMapInfo mapinfo) throws SQLException {
        if ("insert".equals(mapinfo.getRowOptType())) {
            exeInsert(insertStmt, rs, mapinfo);
        } else if ("update".equals(mapinfo.getRowOptType())) {
            exeUpdate(updateStmt, rs, mapinfo);
        } else {
            int nPC = isExistStmt.getParameterMetaData().getParameterCount();
            // logger.info("语句 ExistSql 参数个数")

            if (debugEnabled) {
                logger.debug("语句ExistSql参数个数 = " + nPC + " 主键字段 = " + mapinfo.getKeyFieldMap());
            }

            for (int i = 0; i < nPC; i++) {
                int nRN = mapinfo.getKeyFieldMap().get(i);
                setAdoParameter(isExistStmt, i + 1, nRN + 1, rs);
            }
            int nC = 0;
            ResultSet rsTmp = isExistStmt.executeQuery();
            if (rsTmp.next())
                nC = rsTmp.getInt(1);
            if (nC > 0) {
                exeUpdate(updateStmt, rs, mapinfo);
            } else {
                exeInsert(insertStmt, rs, mapinfo);
            }
        }
    }

    static private MapInfoDBConn loadMapInfoDBConfig(DatabaseInfo sourceDatabaseInfo, DatabaseInfo desDatabaseInfo) {
        MapInfoDBConn pInfo = new MapInfoDBConn();
        pInfo.loadDBConfig(sourceDatabaseInfo, desDatabaseInfo);
        return pInfo;
    }

    private TransferResult runDataMap(ExchangeMapInfo exchangMapinfo, Long taskLogId) {
        String msg = null;
        TaskLog taskLog = taskLogManager.getObjectById(taskLogId);
        Long taskId = taskLog.getTaskId();

        TransferResult transferResult = new TransferResult();
        transferResult.setRes(0);

        if (debugEnabled) {
            msg = "数据交换对应关系 Name = " + exchangMapinfo.getMapInfoName() + " querysql = "
                    + exchangMapinfo.getQuerySql() + " 源数据库 = " + exchangMapinfo.getSourceDatabaseName() + " 源表名 = "
                    + exchangMapinfo.getSourceTableName() + " 目标数据库 = " + exchangMapinfo.getDestDatabaseName()
                    + " 目标表名 = " + exchangMapinfo.getDestTableName() + " 记录操作[1、插入（insert）2、更新（update）3、合并（merge）] = "
                    + exchangMapinfo.getRecordOperate();
            logger.debug(msg);

            TaskConsoleWriteUtils.write(taskId, msg);
        }


        // 准备环境
        DatabaseInfo sourceDatabaseInfo = platformEnvironment.getDatabaseInfo(exchangMapinfo.getSourceDatabaseName());
        if (null == sourceDatabaseInfo) {
            msg = "源数据库 " + exchangMapinfo.getSourceDatabaseName() + " 不存在";
            logger.error(msg);

            TaskConsoleWriteUtils.writeError(taskId, msg);

            transferResult.setRes(-2);
            return transferResult;
        }

        DatabaseInfo desDatabaseInfo = platformEnvironment.getDatabaseInfo(exchangMapinfo.getDestDatabaseName());
        if (null == desDatabaseInfo) {
            msg = "目标数据库 " + exchangMapinfo.getDestDatabaseName() + " 不存在";

            TaskConsoleWriteUtils.writeError(taskId, msg);

            logger.error(msg);
            transferResult.setRes(-2);
            return transferResult;
        }

        List<MapInfoTrigger> mapInfoTriggers = mapinfoTriggerDao.listTriggerByMapinfoId(exchangMapinfo.getMapInfoId());

        TableMapInfo mapinfo = new TableMapInfo();
        try {
            mapinfo.loadMapFromData(exchangMapinfo);
        } catch (SqlResolveException e) {
            logger.error(e.getMessage(), e);
            msg = e.getMessage();

            TaskConsoleWriteUtils.writeError(taskId, msg);

            transferResult.setRes(-1);
            return transferResult;
        }
        MapInfoDBConn pInfo = loadMapInfoDBConfig(sourceDatabaseInfo, desDatabaseInfo);

        msg = "InsertSql = " + mapinfo.getInsertSql();
        logger.info(msg);

        TaskConsoleWriteUtils.write(taskId, msg);


        msg = "UpdateSql = " + mapinfo.getUpdateSql();
        logger.info(msg);

        TaskConsoleWriteUtils.write(taskId, msg);

        msg = "IsExistSql = " + mapinfo.getIsExistSql();
        logger.info(msg);

        TaskConsoleWriteUtils.write(taskId, msg);

        Date beginTime = DatetimeOpt.currentSqlDate();

        long nError = 0;
        long nSucceed = 0;
        long curMoved = 0, preMoved = 0, preSucceed = 0;

        int columnCount = 0;

        TaskDetailLog taskDetailLog = new TaskDetailLog();
        Long taskDetailLogId = taskDetailLogManager.getTaskDetailLogId();
        taskDetailLog.setLogDetailId(taskDetailLogId);
        taskDetailLog.setLogId(taskLogId);
        taskDetailLog.setMapinfoId(exchangMapinfo.getMapInfoId());
        taskDetailLog.setRunBeginTime(beginTime);

        // 开始运行
        boolean runBreak = false;
        String sLastErrorMsg = "";
        try {
            pInfo.connectDB();
            
            Connection lConn = pInfo.getLeftDBConn();
            Connection rConn = pInfo.getRightDBConn();
           // 向MySql 写数据时，需要关闭自动提交
            if ("MySQL" == rConn.getMetaData().getDatabaseProductName()) {
                rConn.setAutoCommit(false);
            }
            // 交换前事件
            if (mapInfoTriggers != null) {
                for (MapInfoTrigger mapInfoTrigger : mapInfoTriggers) {
                    if (!runBreak && mapInfoTrigger.isBeforeTransferAtSource()) {
                        if (debugEnabled) {
                            msg = "执行交换前事件 触发器语句 = " + mapInfoTrigger.getTriggerSql() + " 触发器类型 = 源表级触发器";
                            logger.debug(msg);

                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(lConn, mapInfoTrigger.getTriggerSql(), 2, mapInfoTrigger.getIsprocedure(),
                                    null, nSucceed, nError, sLastErrorMsg, beginTime, beginTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "交换前源数据库触发器执行错误：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                    if (!runBreak && mapInfoTrigger.isBeforeTransferAtDest()) {
                        if (debugEnabled) {
                            msg = "执行交换前事件 触发器语句 = " + mapInfoTrigger.getTriggerSql() + " 触发器类型 = 目标表级触发器";
                            logger.debug(msg);
                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(rConn, mapInfoTrigger.getTriggerSql(), 2, mapInfoTrigger.getIsprocedure(),
                                    null, nSucceed, nError, sLastErrorMsg, beginTime, beginTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "交换前目标数据库触发器执行错误：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
                lConn.commit();
                rConn.commit();
            }
            // 交换前触发器如果有必须执行完全成功
            if (!runBreak) {

                // 这个只是在 用来优化性能
                PreparedStatement insertStmt = null;
                PreparedStatement updateStmt = null;
                PreparedStatement isExistStmt = null;
                if (!mapinfo.getRowOptType().equals("update")) {
                    insertStmt = rConn.prepareStatement(mapinfo.getInsertSql());
                }
                if (!mapinfo.getRowOptType().equals("insert")) {
                    updateStmt = rConn.prepareStatement(mapinfo.getUpdateSql());
                }
                if (mapinfo.getRowOptType().equals("merge")) {
                    isExistStmt = rConn.prepareStatement(mapinfo.getIsExistSql());
                }

                // 内循环
                // 循环次数
                int runnum = 0;

                do {
                    preMoved = curMoved;
                    curMoved = 0;
                    preSucceed = nSucceed;// 记录上次成功的条数，如果一个循环没有一条成功的则终止循环

                    String sourceSql = mapinfo.getSourceSql();
                    // sql 中含有< > 等时会转码，此处需要再转换回来
                    sourceSql = HtmlUtils.htmlUnescape(sourceSql);

                    msg = "执行查询语句 " + sourceSql;

                    logger.info(msg);

                    TaskConsoleWriteUtils.write(taskId, msg);
                    TaskConsoleWriteUtils.write(taskId, "当前交换任务" + (mapinfo.isRepeatRun() ? "" : "没有") + "开启重复执行");

                    PreparedStatement pStmt = lConn.prepareStatement(sourceSql);
                    ResultSet rs = pStmt.executeQuery();
                    ResultSetMetaData rsmd = pStmt.getMetaData();
                    columnCount = rsmd.getColumnCount();

                    while (rs.next()) {
                        //源已查询到数据
                        transferResult.setSourceHasData(true);


                        curMoved++;
                        if (debugEnabled) {
                            msg = "正在执行第 " + curMoved + " 行数据交换";
                            logger.debug(msg);
                            TaskConsoleWriteUtils.write(taskId, msg);
                        }


                        try {
                            if (mapInfoTriggers != null) {
                                for (MapInfoTrigger mapInfoTrigger : mapInfoTriggers) {
                                    if (mapInfoTrigger.isBeforeWriteferAtSource()) {
                                        if (debugEnabled) {
                                            msg = "执行交换前事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                    + " 触发器类型 = 源表行级触发器";
                                            logger.debug(msg);
                                            TaskConsoleWriteUtils.write(taskId, msg);
                                        }

                                        execTriggerSql(lConn, mapInfoTrigger.getTriggerSql(), 1,
                                                mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError, sLastErrorMsg,
                                                beginTime, beginTime);
                                    }
                                    if (mapInfoTrigger.isBeforeWriteferAtDest()) {
                                        if (debugEnabled) {
                                            msg = "执行交换前事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                    + " 触发器类型 = 目标表行级触发器";
                                            logger.debug(msg);
                                            TaskConsoleWriteUtils.write(taskId, msg);
                                        }

                                        execTriggerSql(rConn, mapInfoTrigger.getTriggerSql(), 1,
                                                mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError, sLastErrorMsg,
                                                beginTime, beginTime);
                                    }
                                }
                            }

                            //执行交换
                            exeMoveData(isExistStmt, updateStmt, insertStmt, rs, mapinfo);


                            if (mapInfoTriggers != null) {
                                for (MapInfoTrigger mapInfoTrigger : mapInfoTriggers) {
                                    if (mapInfoTrigger.isAfterWriteAtSource()) {
                                        if (debugEnabled) {
                                            msg = "执行交换后事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                    + " 触发器类型 = 源表行级触发器";
                                            logger.debug(msg);
                                            TaskConsoleWriteUtils.write(taskId, msg);
                                        }

                                        execTriggerSql(lConn, mapInfoTrigger.getTriggerSql(), 1,
                                                mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError, sLastErrorMsg,
                                                beginTime, beginTime);
                                    }
                                    if (mapInfoTrigger.isAfterWriteAtDest()) {
                                        if (debugEnabled) {
                                            msg = "执行交换后事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                    + " 触发器类型 = 目标表表级触发器";
                                            logger.debug(msg);
                                            TaskConsoleWriteUtils.write(taskId, msg);
                                        }

                                        execTriggerSql(rConn, mapInfoTrigger.getTriggerSql(), 1,
                                                mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError, sLastErrorMsg,
                                                beginTime, beginTime);
                                    }
                                }
                            }
                            lConn.commit();
                            rConn.commit();
                            nSucceed++;

                            //输出交换成功失败数
                            TaskConsoleWriteUtils.writeProcess(taskId, nSucceed, nError, exchangMapinfo.getMapInfoName());


                            taskDetailLog.setSuccessPieces(nSucceed);
                            taskDetailLog.setErrorPieces(nError);
                            taskDetailLogManager.saveObject(taskDetailLog);

                        } catch (SQLException e) {
                            nError++;
                            sLastErrorMsg = e.getMessage();
                            logger.error(e.getMessage(), e);

                            TaskConsoleWriteUtils.writeError(taskId, e.getMessage());

                            try {
                                lConn.rollback();
                                rConn.rollback();
                            } catch (Exception e2) {
                                msg = "数据交换回滚异常 " + e2.getMessage();
                                logger.error(msg);
                                TaskConsoleWriteUtils.writeError(taskId, msg);
                            }

                            if (mapInfoTriggers != null) {
                                try {
                                    for (MapInfoTrigger mapInfoTrigger : mapInfoTriggers) {
                                        if (mapInfoTrigger.isWriteErrorAtSource()) {
                                            if (debugEnabled) {
                                                msg = "执行交换后错误事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                        + " 触发器类型 = 源表行级触发器";
                                                logger.debug(msg);
                                                TaskConsoleWriteUtils.writeError(taskId, msg);
                                            }
                                            execTriggerSql(lConn, mapInfoTrigger.getTriggerSql(), 1,
                                                    mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError,
                                                    sLastErrorMsg, beginTime, beginTime);
                                        }
                                        if (mapInfoTrigger.isWriteErrorAtDest()) {
                                            if (debugEnabled) {
                                                msg = "执行交换后错误事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                                        + " 触发器类型 = 目标表行级触发器";
                                                logger.debug(msg);
                                                TaskConsoleWriteUtils.writeError(taskId, msg);
                                            }
                                            execTriggerSql(rConn, mapInfoTrigger.getTriggerSql(), 1,
                                                    mapInfoTrigger.getIsprocedure(), rs, nSucceed, nError,
                                                    sLastErrorMsg, beginTime, beginTime);
                                        }
                                    }
                                    lConn.commit();
                                    rConn.commit();
                                } catch (Exception e2) {
                                    logger.error(e2);
                                    sLastErrorMsg = sLastErrorMsg + " || " + e.getMessage();
                                }
                            }
                            // 记录错误日志
                            TaskErrorData taskErrorData = new TaskErrorData();
                            taskErrorData.setDataId(taskErrorDataManager.getTaskErrorId());
                            taskErrorData.setLogDetailId(taskDetailLogId);
                            taskErrorData.setErrorMessage(sLastErrorMsg);
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < columnCount; i++) {
                                int feildType = mapinfo.getFieldsMap().get(i).getRightColType();
                                if (feildType == 4 || feildType == 5) {
                                    sb.append("LOB,");
                                } else {
                                    if (rs.getObject(i + 1) != null)
                                        sb.append(rs.getObject(i + 1).toString());
                                    sb.append(",");
                                }
                            }
                            taskErrorData.setDataContent(sb.toString());
                            // taskErrorDataManager.saveTaskErrorData(taskErrorData);
                            taskErrorDataManager.saveObject(taskErrorData);
                            taskErrorDataManager.flush();
                        }
                    } // while rs.next();
                    rs.close();
                    pStmt.close();

                    //每次循环执行记录一次循环执行的成功失败数据
                    taskDetailLog.setSuccessPieces(nSucceed);
                    taskDetailLog.setErrorPieces(nError);
                    taskDetailLogManager.saveObject(taskDetailLog);


                    String message = "当前循环执行 " + (++runnum) + " 次，已交换完 " + (preSucceed + curMoved) + " 条数据，本次交换 " + curMoved + " 条数据。";
                    logger.info(message);
                    TaskConsoleWriteUtils.write(taskId, message);

                } while (mapinfo.isRepeatRun() && (curMoved >= preMoved) && (preSucceed < nSucceed));

            }
            Date endTime = DatetimeOpt.currentSqlDate();
            if (!runBreak && mapInfoTriggers != null) {
                for (MapInfoTrigger mapInfoTrigger : mapInfoTriggers) {
                    if (mapInfoTrigger.isAfterTransferAtSource()) {
                        if (debugEnabled) {
                            msg = "执行交换后事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                    + " 触发器类型 = 数据源表级触发器";
                            logger.debug(msg);
                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(lConn, mapInfoTrigger.getTriggerSql(), 2, mapInfoTrigger.getIsprocedure(),
                                    null, nSucceed, nError, sLastErrorMsg, beginTime, endTime);

                        } catch (Exception e) {
                            runBreak = true;
                            msg = "执行交换后源数据库触发器异常：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                    if (mapInfoTrigger.isAfterTransferAtDest()) {
                        if (debugEnabled) {
                            msg = "执行交换后事件 触发器语句 = " + mapInfoTrigger.getTriggerSql()
                                    + " 触发器类型 = 数据目标表级触发器";
                            logger.debug(msg);
                            TaskConsoleWriteUtils.write(taskId, msg);
                        }
                        try {
                            execTriggerSql(rConn, mapInfoTrigger.getTriggerSql(), 2, mapInfoTrigger.getIsprocedure(),
                                    null, nSucceed, nError, sLastErrorMsg, beginTime, endTime);
                        } catch (Exception e) {
                            runBreak = true;
                            msg = "执行交换后目标数据库触发器异常：" + e.getMessage();
                            logger.error(msg);
                            taskDetailLog.appendOtherMessage(msg);

                            TaskConsoleWriteUtils.writeError(taskId, msg);
                        }
                    }
                }
                lConn.commit();
                rConn.commit();
            }
            pInfo.disConnectDB(lConn);
            pInfo.disConnectDB(rConn);
        } catch (Exception e) {
            runBreak = true;
            msg = "执行交换异常：" + e.getMessage();
            logger.error(msg);
            taskDetailLog.appendOtherMessage(msg);

            TaskConsoleWriteUtils.writeError(taskId, msg);
        }
        msg = "执行交换" + exchangMapinfo.getMapInfoName() + "完成；共交换" + String.valueOf(nSucceed + nError) + "条,其中成功"
                + String.valueOf(nSucceed) + "条,失败" + String.valueOf(nError) + "条。";
        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskId, msg);

        Date endTime = DatetimeOpt.currentSqlDate();
        taskDetailLog.setRunEndTime(endTime);
        taskDetailLog.setSuccessPieces(nSucceed);
        taskDetailLog.setErrorPieces(nError);
        taskDetailLogManager.saveObject(taskDetailLog);
        if (runBreak) {
            transferResult.setRes(-1);
        }

        transferResult.setSucc(nSucceed);
        transferResult.setError(nError);
        return transferResult;

    }

    public int doTransfer(Long mapinfoID, String usercode) {
        ExchangeMapInfo exchangMapinfo = exchangeMapinfoDao.getObjectById(mapinfoID);
        if (exchangMapinfo == null)
            return -1;
        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(0l);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType("1");
        taskLog.setRunner(usercode);
        taskLogManager.saveObject(taskLog);
        TransferResult transferResult = runDataMap(exchangMapinfo, taskLogId);
        int nRes = transferResult.getRes();
        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLogManager.saveObject(taskLog);
        return nRes;
    }

    public String runTransferTask(Long taskID, String usercode, String runType,String taskType) {

        String msg = null;

        ExchangeTask exchangeTask = exchangeTaskDao.getObjectById(taskID);
        if (exchangeTask == null) {
            msg = "系统中没有标号为 " + taskID + " 的交换任务。";
            logger.info(msg);

            TaskConsoleWriteUtils.writeError(taskID, msg);

            return msg;
        }
        exchangeTask.setLastRunTime(DatetimeOpt.currentSqlDate());
        exchangeTaskDao.saveObject(exchangeTask);
        msg = "开始执行交换：" + exchangeTask.getTaskName() + "........";
        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskID, msg);

        List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailDao.getTaskDetails(taskID);

        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(taskID);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType(runType);
        taskLog.setRunner(usercode);
        taskLog.setTaskType(taskType);
        taskLogManager.saveObject(taskLog);
        //taskLogManager.flush();
//        TaskLogQueue.put(taskLog);

        int nError = 0;
        int nSucceed = 0;

        List<String> errorMapinfoNameList = new ArrayList<String>();
        int succ = 0, error = 0;
        boolean sourceHasData = false;
        /**
         * 已执行完交换对应关系名称集合
         */
        List<String> exchangeMapinfoName = new ArrayList<String>();


        for (ExchangeTaskDetail taskDetail : exchangeTaskDetails) {
            ExchangeMapInfo exchangMapinfo = exchangeMapinfoDao.getObjectById(taskDetail.getMapinfoId());

            if (exchangMapinfo == null) {
                if (debugEnabled) {
                    msg = "交换任务中交换编号 = " + taskDetail.getMapinfoId() + " 不存在";
                    logger.debug(msg);

                    TaskConsoleWriteUtils.writeError(taskID, msg);
                }
                continue;
            }
            TransferResult transferResult = runDataMap(exchangMapinfo, taskLogId);
            int nRes = transferResult.getRes();

            //向页面推送本次交换中已执行的数据交换对应关系以及交换成功条数和失败条数
            exchangeMapinfoName.add("数据交换对应关系名称 " + exchangMapinfo.getMapInfoName() + " 交换成功 " + transferResult.getSucc() + "条数，交换失败 " + transferResult.getError() + " 条数");
            TaskConsoleWriteUtils.writeAlreadyProcess(taskID, StringUtils.collectionToDelimitedString
                    (exchangeMapinfoName, "_split_"));
            logger.info(exchangeMapinfoName);


            //整个交换任务中总的成功条数，失败条数
            succ += transferResult.getSucc();
            error += transferResult.getError();
            if (transferResult.isSourceHasData()) {
                sourceHasData = transferResult.isSourceHasData();
            }

            if (nRes < 0) {
                nError++;

                errorMapinfoNameList.add(exchangMapinfo.getMapInfoName());
            } else {
                nSucceed++;
            }
        }
        String message = "完成" + String.valueOf(nError + nSucceed) + "组交换,成功" + String.valueOf(nSucceed) + "组，失败"
                + String.valueOf(nError) + "组。";
        if (!CollectionUtils.isEmpty(errorMapinfoNameList)) {
            String errorMapinfoName = "失败交换名称为[";
            for (String s : errorMapinfoNameList) {
                errorMapinfoName += s + ',';
            }

            errorMapinfoName += "]。";

            message += errorMapinfoName;
        }

        message += "总成功 " + succ + " 条数，总失败 " + error + " 条数。";
        if (!sourceHasData) {
            message += "源数据未查询到任何数据。";
        }

        if (0 != nError || 0 != error) {
            message = "*" + message;
        }

        msg = "交换任务" + exchangeTask.getTaskName() + message;

        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskID, msg);
        TaskConsoleWriteUtils.stop(taskID);

        TaskLog taskLogTemp = taskLogManager.getObjectById(taskLogId);
        taskLogTemp.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLogTemp.setOtherMessage(message);
        taskLogManager.saveObject(taskLogTemp);
        return message;
    }

}
