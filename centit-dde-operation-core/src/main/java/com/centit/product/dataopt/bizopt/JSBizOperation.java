package com.centit.product.dataopt.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.BizOperation;
import com.centit.product.dataopt.utils.BizOptUtils;
import com.centit.product.dataopt.utils.BuiltInOperation;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.extend.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

public class JSBizOperation implements BizOperation {
    private static final Logger logger = LoggerFactory.getLogger(JSBizOperation.class);

    private MetaObjectService metaObjectService;
    private DatabaseRunTime databaseRunTime;

    public JSBizOperation(MetaObjectService metaObjectService,
                                   DatabaseRunTime databaseRunTime) {
        this.metaObjectService = metaObjectService;
        this.databaseRunTime = databaseRunTime;
    }

    @Override
    public void doOpt(BizModel bizModel, JSONObject bizOptJson) {
        JSRuntimeContext /*JsMateObjectEventRuntime*/ jsRuntimeContext = new JSRuntimeContext();

        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", "js");
        String javaScript = BuiltInOperation.getJsonFieldString(bizOptJson, "value", "");
        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }
        try {
            Object object = jsRuntimeContext.callJsFunc("runOpt",
                bizModel, this);
            bizModel.putDataSet(targetDSName,
                BizOptUtils.castObjectToDataSet(object));
        } catch (ScriptException | NoSuchMethodException e) {
            logger.error(e.getLocalizedMessage());
        }

    }

    public MetaObjectService getMetaObjectService() {
        return metaObjectService;
    }

    public DatabaseRunTime getDatabaseRunTime() {
        return databaseRunTime;
    }
}
