package com.centit.dde.ws;

import java.io.ByteArrayInputStream;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.datafile.TableFileReader;
import com.centit.dde.dataio.ExecuteDataMap;
import com.centit.dde.dataio.ImportDataImpl;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.TaskLogManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.TxtLogFile;

@WebService(endpointInterface = "com.centit.dde.ws.UploadData")
public class UploadDataImpl implements UploadData {



    public static final Log LOGGER = LogFactory.getLog(ImportDataImpl.class);

    private ExecuteDataMap executeDataMap;

    private TaskLogManager taskLogManager;

    private ValidatorWs validatorWs;

    public void setValidatorWs(ValidatorWs validatorWs) {
        this.validatorWs = validatorWs;
    }

    public void setExecuteDataMap(ExecuteDataMap executeDataMap) {
        this.executeDataMap = executeDataMap;
    }

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    /**
     * 上传数据接口，写入数据库之前需要进行用户身份验证
     *
     * @param userName  用户名
     * @param userPin   密码，加密形式
     * @param database  目标数据库
     * @param tableDate 数据格式参见离线文件数据格式，为了控制大小可以将大字段单独上传，也可以一起上传
     * @return
     */
    @WebMethod
    @Override
    public String uploadTableAsXml(String userName, String userPin, String tableData) {
        if (StringUtils.isBlank(tableData)) {
            return "-3:传入的数据为null";
        }


        //TODO 验证用户名密码； 验证不通过 返回  1：用户名密码不匹配
        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }

        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(2l);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType("2");
        taskLog.setRunner("ws");
        taskLogManager.saveObject(taskLog);

        TxtLogFile.writeLog(SysParametersUtils.getValue("wsdata") + "/ws" + taskLogId + ".xml", tableData);
        TableFileReader tr = new TableFileReader();
        tr.readTableInfoFromXML(tableData);
        
        //TODO：tr.getDataOptId() 根据这个判断用户是否有这个权限 ；没有权限 返回  2：用户+用户名+没有权限操作+tr.getDataOptId()

        validator = validatorWs.validatorDataOptId(userName, tr.getDataOptId(), "I");
        if (null != validator) {
            return validator;
        }


        int nRes = -1;
        String message = null;
        SqlResolveException ex = null;
        try {
            nRes = executeDataMap.doExecute(tr, userName, "2", taskLogId);
        } catch (SqlResolveException e) {
            ex = e;
            LOGGER.error(e.getMessage(), e);
        }

        if (nRes == 0) {
            message = "0:ok";// +userName + userPin + tableData;
        } else {
            message = String.valueOf(nRes) + ":执行过程报错";
        }

        // TaskLog taskLogTemp = taskLogManager.getObjectById(taskLogId);
        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLog.setOtherMessage(message);
        taskLogManager.saveObject(taskLog);

        if (null != ex) {
            return ex.getMessage();
        }

        return message;
    }


    /**
     * 上传数据接口，写入数据库之前需要进行用户身份验证
     *
     * @param userName  用户名
     * @param userPin   密码，加密形式
     * @param database  目标数据库
     * @param tableDate 数据格式参见离线文件数据格式，为了控制大小可以将大字段单独上传，也可以一起上传
     * @return
     */
    @WebMethod
    @Override
    public String uploadTableAsByteArray(String userName, String userPin, byte[] tableData) {
        
        if (ArrayUtils.isEmpty(tableData)) {
            return "-3:传入的数据为null";
        }
        //TODO 验证用户名密码； 验证不通过 返回  1：用户名密码不匹配
        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }

        
        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(2l);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType("2");
        taskLog.setRunner("ws");
        taskLogManager.saveObject(taskLog);

        TableFileReader tr = new TableFileReader();
        try {
            tr.readTableInfo(new ByteArrayInputStream(tableData));
            TxtLogFile.writeLog(SysParametersUtils.getStringValue("wsdata") + "/ws" + taskLogId + ".xml", tr.getXML());
        } catch (Exception e) {
            taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
            taskLog.setOtherMessage("-4:" + e.getMessage());
            taskLogManager.saveObject(taskLog);
            return "-4:" + e.getMessage();
        }
        
        //TODO：tr.getDataOptId() 根据这个判断用户是否有这个权限 ；没有权限 返回  2：用户+用户名+没有权限操作+tr.getDataOptId()
        validator = validatorWs.validatorDataOptId(userName, tr.getDataOptId(), "I");
        if (null != validator) {
            return validator;
        }

        int nRes = -1;
        String message = null;
        try {
            nRes = executeDataMap.doExecute(tr, userName, "2", 0l);
        } catch (SqlResolveException e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (nRes == 0)
            message = "0:ok";// +userName + userPin + tableData;
        else
            message = String.valueOf(nRes) + ":执行过程报错";

        // TaskLog taskLogTemp = taskLogManager.getObjectById(taskLogId);
        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLog.setOtherMessage(message);
        taskLogManager.saveObject(taskLog);

        return message;
    }

    /**
     * 单独上传CLOB大字段接口
     *
     * @param userName
     * @param userPin
     * @param database   数据库名称 D_DataBase_Info 的databasename
     * @param tableName
     * @param columnName
     * @param keyDesc    key="3232" and key2="ewr"
     * @param isBase64
     * @param lobData
     * @return
     */
    @WebMethod
    @Override
    public String uploadLobAsString(String userName, String userPin, String database, String tableName,
                                    String columnName, String keyDesc, boolean isBase64, String lobData) {
        //TODO 验证用户名密码； 验证不通过 返回  1：用户名密码不匹配
        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("userName = " + userName + ' ' + "userPin = " + userPin + " database = " + database
                    + " tableName = " + tableName + " columnName = " + columnName + " keyDesc = " + keyDesc
                    + " isBase = " + isBase64);
        }
        if (LOGGER.isDebugEnabled()) {
            String text = null;
            if (lobData.length() < 200) {
                text = lobData;
            } else {
                text = "lobData字段值长度大于 200, 起始文字为 " + lobData.substring(0, 100) + " 结束文字为 "
                        + lobData.substring(lobData.length() - 100, lobData.length() - 1);
            }

            LOGGER.debug(text);
        }

        try {
            if (isBase64) 
                executeDataMap.updateLobField(database, tableName, columnName, keyDesc,
                        StringBaseOpt.decodeBase64(lobData));
            else
                executeDataMap.updateLobField(database, tableName, columnName, keyDesc, lobData);
        } catch (WsDataException e) {
            return SysParametersUtils.getStringValue(String.valueOf(e.getErrorcode()));            
        }
        return "0:ok";
    }

    /**
     * 单独上传BLOB大字段接口
     *
     * @param userName
     * @param userPin
     * @param database   数据库名称 D_DataBase_Info 的databasename
     * @param tableName
     * @param columnName
     * @param keyDesc
     * @param lobData
     * @return
     */
    @Override
    public String uploadLobAsByteArray(@WebParam(name = "userName") String userName, @WebParam(name = "userPin") String userPin,
                                       @WebParam(name = "database") String database, @WebParam(name = "tableName") String tableName,
                                       @WebParam(name = "columnName") String columnName, @WebParam(name = "keyDesc") String keyDesc,
                                       @WebParam(name = "lobData") byte[] lobData) {
        //TODO 验证用户名密码； 验证不通过 返回  1：用户名密码不匹配
        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("userName = " + userName + ' ' + "userPin = " + userPin + " database = " + database
                    + " tableName = " + tableName + " columnName = " + columnName + " keyDesc = " + keyDesc);
        }
        try {
            executeDataMap.updateLobField(database, tableName, columnName, keyDesc,
                    lobData);
        } catch (WsDataException e) {
            LOGGER.error(e.getMessage(), e);
            return SysParametersUtils.getStringValue(String.valueOf(e.getErrorcode()));
        }
        return "0:ok";
    }

}
