package com.centit.dde.core;

import com.centit.dde.po.TaskLog;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.common.ObjectException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataOptContext {
    private String runType;
    private String logId;
    private String needRollback;
    private TaskLog taskLog;
    private String optId;
    private String debugId;
    private String osId;
    private String packetId;
    private Integer logLevel;
    /**
     * 调用参数
     * RequestBody/File
     * Session
     */
    private Map<String, Object> callStackData;


    public DataOptContext() {
        this.callStackData = new HashMap<>(8);
    }

    //String runType = StringBaseOpt.castObjectToString(bizModel.getStackData(ConstantValue.RUN_TYPE_TAG));
    public String getRunType() {
        return ConstantValue.RUN_TYPE_COPY.equals(runType) ? runType : ConstantValue.RUN_TYPE_NORMAL;
    }

    public void setStackData(String key, Object value) {
        if (key == null || !key.startsWith(ConstantValue.DOUBLE_UNDERLINE)) {
            throw new ObjectException("内部堆栈数据必须以'__'开头");
        }
        callStackData.put(key, value);
    }
}
