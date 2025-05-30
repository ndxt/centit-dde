package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
import com.centit.framework.model.basedata.UnitInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author zhf
 */
public class UnitFilterOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        String topUnit = StringUtils.isBlank(dataOptContext.getTopUnit())? bizModel.fetchTopUnit() : dataOptContext.getTopUnit();

        String unitFilter=
            Pretreatment.mapTemplateStringAsFormula(bizOptJson.getString("unitFilter"),
                new BizModelJSONTransform(bizModel));

        //两种类别 机构表达式， 根据属性查询 filterType： express， properties
        Set<String> units;
        Object userObj = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        if (userObj instanceof CentitUserDetails) {
            CentitUserDetails centitUserDetails = (CentitUserDetails) userObj;
            units = SysUnitFilterEngine.calcSystemUnitsByExp(
                StringEscapeUtils.unescapeHtml4(unitFilter), topUnit,
                UserFilterOperation.createFilterParam("C", centitUserDetails.getCurrentUnitCode()),
                new UserUnitMapTranslate(BuiltInOperation.makeCalcParam(centitUserDetails))
            );
        } else {
            units = SysUnitFilterEngine.calcSystemUnitsByExp(
                StringEscapeUtils.unescapeHtml4(unitFilter), topUnit,
                null,
                new UserUnitMapTranslate()
            );
        }
        List<UnitInfo> retUntis = CodeRepositoryUtil.getUnitInfosByCodes(topUnit, units);

        retUntis.sort(Comparator.comparing(UnitInfo::getUnitOrder));
        CollectionsOpt.sortAsTree(retUntis,
            ( p,  c) -> StringUtils.equals(p.getUnitCode(),c.getParentUnit()) );

        bizModel.putDataSet(bizOptJson.getString("id"),new DataSet(JSON.toJSON(retUntis)));
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
