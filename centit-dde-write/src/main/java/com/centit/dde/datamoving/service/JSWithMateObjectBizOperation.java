package com.centit.dde.datamoving.service;

import com.centit.product.dataopt.bizopt.JSBizOperation;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.utils.BizOptUtils;
import com.centit.product.dataopt.utils.JSRuntimeContext;
import com.centit.product.metadata.service.MetaObjectService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

/**
 * @author zhf
 */
public class JSWithMateObjectBizOperation extends JSBizOperation {
    private static final Logger logger = LoggerFactory.getLogger(JSWithMateObjectBizOperation.class);

    private MetaObjectService metaObjectService;

    @Override
    public BizModel apply(BizModel bizModel) {
        if (jsRuntimeContext == null) {
            jsRuntimeContext = new JSRuntimeContext();
        }

        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }

        try {
            Object object = jsRuntimeContext.callJsFunc(
                StringUtils.isBlank(jsFuncName) ? "runOpt" : jsFuncName, metaObjectService, bizModel);
            return BizOptUtils.castObjectToBizModel(object);
        } catch (ScriptException | NoSuchMethodException e) {
            logger.error(e.getLocalizedMessage());
        }
        return bizModel;
    }

    public void setMetaObjectService(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }
}
