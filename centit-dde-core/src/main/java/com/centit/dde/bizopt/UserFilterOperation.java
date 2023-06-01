package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.SysUserFilterEngine;
import com.centit.framework.components.impl.UserUnitMapTranslate;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author zhf
 */
public class UserFilterOperation implements BizOperation {
    static HashMap<String, Set<String>> createFilterParam(String key, String value){
        HashMap<String, Set<String>> param = new HashMap<>();
        param.put(key, CollectionsOpt.createHashSet(value));
        return param;
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String topUnit = bizModel.fetchTopUnit();
        Object userObj = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        String userFilter = Pretreatment.mapTemplateStringAsFormula(bizOptJson.getString("userFilter"),
            new BizModelJSONTransform(bizModel));

        Set<String> users ;
        if (userObj instanceof CentitUserDetails) {
            CentitUserDetails centitUserDetails = (CentitUserDetails) userObj;
            users = SysUserFilterEngine.calcSystemOperators(
                StringEscapeUtils.unescapeHtml4(userFilter), topUnit,
                createFilterParam("C", centitUserDetails.getCurrentUnitCode()),
                createFilterParam("C", centitUserDetails.getUserCode()), null,
                new UserUnitMapTranslate(BuiltInOperation.makeCalcParam(centitUserDetails)));
        } else {
            users = SysUserFilterEngine.calcSystemOperators(
                StringEscapeUtils.unescapeHtml4(userFilter), topUnit,
                null, null, null,
                new UserUnitMapTranslate() );
        }

        List<IUserInfo> retUsers = CodeRepositoryUtil.getUserInfosByCodes(bizModel.fetchTopUnit(), users);
        List<IUserInfo> lsUserInfo = new ArrayList<>(retUsers.size() + 1);
        for (IUserInfo ui : retUsers) {
            if ("T".equals(ui.getIsValid())) {
                lsUserInfo.add(ui);
            }
        }
        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(lsUserInfo));
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
