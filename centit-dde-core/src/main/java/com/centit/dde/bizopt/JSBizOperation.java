package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.utils.BizOptUtils;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.extend.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

/**
 * @author zhf
 */
public class JSBizOperation implements BizOperation {
    private static final Logger logger = LoggerFactory.getLogger(JSBizOperation.class);

    private MetaObjectService metaObjectService;

    public JSBizOperation(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        JSRuntimeContext jsRuntimeContext = new JSRuntimeContext();

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", "js");
        String javaScript = BuiltInOperation.getJsonFieldString(bizOptJson, "valuejs", "");
        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }
        int count = 0;
        try {
            Object object = jsRuntimeContext.callJsFunc("runOpt",
                this, bizModel,bizModel.toJsonObject());
            bizModel.putDataSet(targetDsName,
                BizOptUtils.castObjectToDataSet(object));
            if (object != null) {
                count = bizModel.fetchDataSetByName(targetDsName).getSize();
            }
        } catch (ScriptException | NoSuchMethodException e) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_PROCESS_FAILED,e.getMessage());
        }
        return BuiltInOperation.getResponseSuccessData(count);
    }

}
