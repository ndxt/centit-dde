package com.centit.dde.vo;

import com.centit.dde.core.SimpleBizModel;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import lombok.Data;

import java.util.Map;

@Data
public class DataOptVo {
    private String runType;
    private SimpleBizModel bizModel;
    private Object preResult;
    private Map<String, Object> queryParams;
    private String logId;
    private String needRollback;

    public DataOptVo(String needRollback, SimpleBizModel bizModel){
        this.bizModel = bizModel;
        this.logId = bizModel.getModelName();
        this.needRollback = needRollback;
        this.queryParams= bizModel.getModelTag();
        String runType = (String) queryParams.get("runType");
        if (ConstantValue.RUN_TYPE_COPY.equals(runType)) {
            runType = ConstantValue.RUN_TYPE_COPY;
        } else {
            runType = ConstantValue.RUN_TYPE_NORMAL;
        }
        this.runType = runType;
        this.preResult = ResponseData.successResponse;
    }
}
