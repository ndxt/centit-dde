package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.BizOperation;
import com.centit.product.dataopt.utils.BizOptUtils;
import com.centit.support.extend.JSRuntimeContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;

public class JSBizOperation implements BizOperation {
    private static final Logger logger = LoggerFactory.getLogger(JSBizOperation.class);
    protected String javaScript;
    protected String jsFuncName;
    protected JSRuntimeContext jsRuntimeContext;

    @Override
    public BizModel apply(BizModel bizModel) {
        if(jsRuntimeContext == null){
            jsRuntimeContext = new JSRuntimeContext();
        }
        if(StringUtils.isNotBlank(javaScript)){
            jsRuntimeContext.compileScript(javaScript);
        }
        try {
            Object object = jsRuntimeContext.callJsFunc(
                StringUtils.isBlank(jsFuncName)? "runOpt" : jsFuncName, bizModel);
            return BizOptUtils.castObjectToBizModel(object);
        } catch (ScriptException | NoSuchMethodException e) {
            logger.error(e.getLocalizedMessage());
        }
        return bizModel;
    }

    public void setJavaScript(String javaScript) {
        this.javaScript = javaScript;
    }

    public JSRuntimeContext getJsRuntimeContext() {
        return jsRuntimeContext;
    }

    public void setJsRuntimeContext(JSRuntimeContext jsRuntimeContext) {
        this.jsRuntimeContext = jsRuntimeContext;
    }

    public void setJsFuncName(String jsFuncName) {
        this.jsFuncName = jsFuncName;
    }
}
