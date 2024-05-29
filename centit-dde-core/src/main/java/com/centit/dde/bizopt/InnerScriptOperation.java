package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.extend.AbstractRuntimeContext;
import com.centit.support.extend.JSRuntimeContext;
import com.centit.support.extend.PythonRuntimeContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhf
 */
public class InnerScriptOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", "js");
        String javaScript = BuiltInOperation.getJsonFieldString(bizOptJson, "valuejs", "");
        String scriptType = BuiltInOperation.getJsonFieldString(bizOptJson, "scriptType", "js");
        AbstractRuntimeContext runtimeContext;
        if("python".equals(scriptType)){
            // 需要添加 jython-standalone 依赖
            runtimeContext = new PythonRuntimeContext();
        } else {
            runtimeContext = new JSRuntimeContext();
        }
        if (StringUtils.isNotBlank(javaScript)) {
            runtimeContext.compileScript(javaScript);
        }
        int count = 0;
        Object object = runtimeContext.callFunc("runOpt", bizModel);
        bizModel.putDataSet(targetDsName, DataSet.toDataSet(object));
        if (object != null) {
            count = bizModel.getDataSet(targetDsName).getSize();
        }
        return BuiltInOperation.createResponseSuccessData(count);
    }

}
