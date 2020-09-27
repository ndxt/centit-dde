package com.centit.product.dataopt.bizopt;

import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.utils.BizOptUtils;
import com.centit.support.extend.JSRuntimeContext;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.common.ObjectException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.util.Map;

/**
 * @author zhf
 */
@Data
public class JsMateObjectEventRuntime {

    private static final Logger logger = LoggerFactory.getLogger(JsMateObjectEventRuntime.class);

    private MetaObjectService metaObjectService;
    private DatabaseRunTime databaseRunTime;
    private JSRuntimeContext jsRuntimeContext;
    private BizModel bizModel;
    private Map<String, Object> parms;
    private String javaScript;
    private String targetDSName;


    public JsMateObjectEventRuntime(MetaObjectService metaObjectService,
                                    DatabaseRunTime databaseRunTime) {
        this.metaObjectService = metaObjectService;
        this.databaseRunTime = databaseRunTime;
    }

    /**
     * 运行js事件
     */
    public BizModel runEvent() {
        if (jsRuntimeContext == null) {
            jsRuntimeContext = new JSRuntimeContext();
        }

        if (StringUtils.isNotBlank(javaScript)) {
            jsRuntimeContext.compileScript(javaScript);
        }
        try {
            Object retObj = jsRuntimeContext.callJsFunc("runOpt", this, parms);
            bizModel.putDataSet(targetDSName,BizOptUtils.castObjectToDataSet(retObj));
            return bizModel;
        } catch (ScriptException e) {
            throw new ObjectException(ObjectException.UNKNOWN_EXCEPTION,
                e.getMessage());
        } catch (NoSuchMethodException e) {
            logger.info(e.getMessage());
            return this.bizModel;
        }
    }


}
