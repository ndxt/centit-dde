package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.SysUnitFilterEngine;
import com.centit.framework.components.impl.UserUnitMapTranslate;
import com.centit.framework.model.basedata.IUnitInfo;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.List;
import java.util.Set;

/**
 * @author zhf
 */
public class UnitFilterOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        Object centitUserDetails = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        if(!(centitUserDetails instanceof CentitUserDetails)){
            return ResponseData.makeErrorMessage("用户没有登录！");
        }
        String unitFilter= StringBaseOpt.castObjectToString(
            Pretreatment.mapTemplateStringAsFormula(bizOptJson.getString("unitFilter"),new BizModelJSONTransform(bizModel))
        );
        Set<String> units = SysUnitFilterEngine.calcSystemUnitsByExp(
            StringEscapeUtils.unescapeHtml4(unitFilter), null,
            new UserUnitMapTranslate(BuiltInOperation.makeCalcParam(centitUserDetails))
        );

        List<IUnitInfo> retUntis = CodeRepositoryUtil.getUnitInfosByCodes(((CentitUserDetails)centitUserDetails).getTopUnitCode(), units);
        CollectionsOpt.sortAsTree(retUntis,
            ( p,  c) -> StringUtils.equals(p.getUnitCode(),c.getParentUnit()) );
        bizModel.putDataSet(bizOptJson.getString("id"),new DataSet(retUntis));
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
