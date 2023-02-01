package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;

/**
 * 日志查询，这个按道理只能查当前租户的日志
 *
 */
public class OptLogQueryOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
