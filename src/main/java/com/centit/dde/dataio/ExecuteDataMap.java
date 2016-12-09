package com.centit.dde.dataio;

import com.centit.dde.datafile.TableFileReader;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ImportOpt;
import com.centit.dde.ws.WsDataException;
import com.centit.framework.staticsystem.po.OsInfo;

public interface ExecuteDataMap {

    /**
     * 执行数据导入操作，可能是调用其他业务WebService 也可能是直接写入数据库
     *
     * @param xmlData
     * @param usercode
     * @param runType   1:手动 0：系统自动 2:WebService接口
     * @param taskLogId
     * @return
     * @throws Exception
     */
    public int doExecute(TableFileReader xmlData, String usercode, String runType, Long taskLogId)
            throws SqlResolveException;

    /**
     * @param xmlData
     * @param osInfo
     * @param taskLogId
     * @return
     */
    public int doCallWebService(TableFileReader xmlData, OsInfo osInfo, Long taskLogId);// throws
    // Exception;

    /**
     * @param xmlData
     * @param importOpt
     * @param taskLogId
     * @return
     */
    public int doMergeToDatabase(TableFileReader xmlData, ImportOpt importOpt, Long taskLogId)
            throws SqlResolveException;

    /**
     * @param xmlData
     * @param usercode
     * @param runType   1:手动 0：系统自动 2:WebService接口
     * @param taskLogId
     * @return
     */
    public int doExecute(String xmlData, String usercode, String runType, Long taskLogId);// throws
    // Exception;

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
    public int updateLobField(String database, String tableName, String columnName, String keyDesc, byte[] lobData)
            throws WsDataException;

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
    public int updateLobField(String database, String tableName, String columnName, String keyDesc, String lobData)
            throws WsDataException;

}
