package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.extend.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public class InnerJSOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        JSRuntimeContext jsRuntimeContext = new JSRuntimeContext();

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", "js");
        String javaScript = BuiltInOperation.getJsonFieldString(bizOptJson, "valuejs", "");
        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }
        int count = 0;
        Object object = jsRuntimeContext.callJsFunc("runOpt",bizModel);
        bizModel.putDataSet(targetDsName, DataSet.toDataSet(object));
        if (object != null) {
            count = bizModel.getDataSet(targetDsName).getSize();
        }
        return BuiltInOperation.createResponseSuccessData(count);
    }

}
