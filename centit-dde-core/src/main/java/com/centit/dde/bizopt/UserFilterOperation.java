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
import com.centit.framework.components.SysUserFilterEngine;
import com.centit.framework.components.impl.UserUnitMapTranslate;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhf
 */
public class UserFilterOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        Object centitUserDetails = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        if (!(centitUserDetails instanceof CentitUserDetails)) {
            return ResponseData.makeErrorMessage("用户没有登录！");
        }
        String userFilter = StringBaseOpt.castObjectToString(
            Pretreatment.mapTemplateStringAsFormula(bizOptJson.getString("userFilter"), new BizModelJSONTransform(bizModel))
        );
        Set<String> users = SysUserFilterEngine.calcSystemOperators(
            StringEscapeUtils.unescapeHtml4(userFilter),
            null, null, null,
            new UserUnitMapTranslate(BuiltInOperation.makeCalcParam(centitUserDetails)));
        List<IUserInfo> retUsers = CodeRepositoryUtil.getUserInfosByCodes(((CentitUserDetails) centitUserDetails).getTopUnitCode(), users);
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
