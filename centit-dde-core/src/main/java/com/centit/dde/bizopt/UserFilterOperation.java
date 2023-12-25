package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.components.SysUserFilterEngine;
import com.centit.framework.components.impl.UserUnitMapTranslate;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

/**
 * @author zhf
 */
public class UserFilterOperation implements BizOperation {

    private PlatformEnvironment platformEnvironment;

    public UserFilterOperation(PlatformEnvironment platformEnvironment){
        this.platformEnvironment = platformEnvironment;
    }

    static HashMap<String, Set<String>> createFilterParam(String key, String value){
        HashMap<String, Set<String>> param = new HashMap<>();
        param.put(key, CollectionsOpt.createHashSet(value));
        return param;
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String topUnit = bizModel.fetchTopUnit();
        //获取用户信息
        Object userObj = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        //两种类别 用户表达式， 根据属性查询
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

        List<UserInfo> retUsers = CodeRepositoryUtil.getUserInfosByCodes(bizModel.fetchTopUnit(), users);
        List<UserInfo> lsUserInfo = new ArrayList<>(retUsers.size() + 1);
        for (UserInfo ui : retUsers) {
            if ("T".equals(ui.getIsValid())) {
                lsUserInfo.add(ui);
            }
        }
        lsUserInfo.sort(Comparator.comparing(UserInfo::getUserOrder));
        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(lsUserInfo));
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
