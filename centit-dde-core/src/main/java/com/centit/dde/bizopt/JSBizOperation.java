package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.BuiltInOperation;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.database.transaction.ConnectThreadHolder;
import com.centit.support.extend.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.sql.SQLException;

/**
 * @author zhf
 */
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
    public void runOpt(BizModel bizModel, JSONObject bizOptJson) {
        JSRuntimeContext /*JsMateObjectEventRuntime*/ jsRuntimeContext = new JSRuntimeContext();

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", "js");
        String javaScript = BuiltInOperation.getJsonFieldString(bizOptJson, "value", "");
        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }
        try {
            Object object = jsRuntimeContext.callJsFunc("runOpt",
                this, bizModel);
            ConnectThreadHolder.commitAndRelease();
            bizModel.putDataSet(targetDsName,
                BizOptUtils.castObjectToDataSet(object));
        } catch (ScriptException | NoSuchMethodException | SQLException e) {
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
