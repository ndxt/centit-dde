package com.centit.dde.core;

import com.centit.dde.adapter.po.TaskLog;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.common.ObjectException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataOptContext {
    private String runType;

    private String needRollback;
    private TaskLog taskLog;
    private String optId;
    private String debugId;
    private String osId;
    private String packetId;
    private Integer logLevel;
    private String topUnit;
    /**
     * 调用参数
     * RequestBody/File
     * Session
     */
    private Map<String, Object> callStackData;

    public String getLogId() {
        if (taskLog==null){
            return null;
        }
        return this.taskLog.getLogId();
    }

    public int getStepNo() {
        if (taskLog==null){
            taskLog = new TaskLog();
        }
        return this.taskLog.getStepNo();
    }
    public int plusStepNo() {
        if (taskLog==null){
            taskLog = new TaskLog();
        }
        return this.taskLog.plusStepNo();
    }
    public DataOptContext() {
        this.callStackData = new HashMap<>(8);
    }

    //String runType = StringBaseOpt.castObjectToString(bizModel.getStackData(ConstantValue.RUN_TYPE_TAG));
    public String getRunType() {
        return ConstantValue.RUN_TYPE_DEBUG.equals(runType) ? runType : ConstantValue.RUN_TYPE_NORMAL;
    }

    public void setStackData(String key, Object value) {
        if (key == null || !key.startsWith(ConstantValue.DOUBLE_UNDERLINE)) {
            throw new ObjectException("内部堆栈数据必须以'__'开头");
        }
        callStackData.put(key, value);
    }

    public Object getStackData(String key) {
        if (callStackData==null) {
            return null;
        }
        return callStackData.get(key);
    }

    public CentitUserDetails getCurrentUserDetail(){
        return (CentitUserDetails)callStackData.get(ConstantValue.SESSION_DATA_TAG);
    }

    public String getCurrentUserCode(){
        CentitUserDetails currentUserDetails = getCurrentUserDetail();
        if(currentUserDetails!=null){
            return currentUserDetails.getUserCode();
        }
        return null;
    }

    public String getCurrentUnitCode(){
        CentitUserDetails currentUserDetails = getCurrentUserDetail();
        if(currentUserDetails!=null){
            return currentUserDetails.getCurrentUnitCode();
        }
        return null;
    }
}
