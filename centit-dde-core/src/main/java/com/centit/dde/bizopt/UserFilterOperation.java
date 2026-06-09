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
import com.centit.framework.components.SysUserFilterEngine;
import com.centit.framework.components.impl.UserUnitMapTranslate;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.UserInfo;
import com.centit.framework.model.basedata.UserUnit;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author zhf
 */
public class UserFilterOperation implements BizOperation {

    private final PlatformEnvironment platformEnvironment;

    public UserFilterOperation(PlatformEnvironment platformEnvironment){
        this.platformEnvironment = platformEnvironment;
    }

    static HashMap<String, Set<String>> createFilterParam(String key, String value){
        HashMap<String, Set<String>> param = new HashMap<>();
        param.put(key, CollectionsOpt.createHashSet(value));
        return param;
    }

    private JSONObject buildUserJson(CentitUserDetails ud ){
        if(ud.getUserInfo()==null) return null;
        JSONObject userJson = JSONObject.from(ud.getUserInfo());
        String topUnit = ud.getTopUnitCode();
        userJson.put("topUnit", topUnit);
        userJson.put("currentUnitCode",  ud.getCurrentUnitCode());
        String affiliatedUnit = ud.getUserInfo().getPrimaryUnit();
        List<UserUnit> userUnits = new ArrayList<>();
        if(ud.getUserUnits()!=null && !ud.getUserUnits().isEmpty()) {
            for(UserUnit uu: ud.getUserUnits()){
                if(StringUtils.equals(topUnit, uu.getTopUnit())){
                    userUnits.add(uu);
                    if("T".equals(uu.getRelType())){
                        affiliatedUnit = uu.getUnitCode();
                    }
                }
            }
        }
        userJson.put("affiliatedUnit", affiliatedUnit);
        userJson.put("userUnits", userUnits);
        return userJson;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String topUnit = StringUtils.isBlank(dataOptContext.getTopUnit())? bizModel.fetchTopUnit() : dataOptContext.getTopUnit();
        //获取用户信息
        Object userObj = bizModel.getStackData(ConstantValue.SESSION_DATA_TAG);
        //两种类别 用户表达式， 根据属性查询 filterType： express， properties，//exact
        String filterType = bizOptJson.getString("filterType");
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);

        if ("properties".equals(filterType)) {
            //returnAsObject
            String propName = bizOptJson.getString("propName");
            String propValue = bizOptJson.getString("propValue");
            String pv = Pretreatment.mapTemplateStringAsFormula(propValue, transform);
            if(StringUtils.isNotBlank(pv)){
                propValue = pv;
            }

            CentitUserDetails ud = null ;
            switch (propName){
                case "loginName":
                    ud =  this.platformEnvironment.loadUserDetailsByLoginName(propValue);
                    break;
                case "regEmail":
                    ud =  this.platformEnvironment.loadUserDetailsByRegEmail(propValue);
                    break;
                case "cellPhone":
                    ud =  this.platformEnvironment.loadUserDetailsByRegCellPhone(propValue);
                    break;
                case "userWord": {
                        UserInfo userInfo = this.platformEnvironment.getUserInfoByUserWord(propValue);
                        if(userInfo!=null) {
                            ud = new CentitUserDetails();
                            ud.setUserInfo(userInfo);
                        }
                    }
                    break;
                case "idCardNo": {
                        UserInfo userInfo = this.platformEnvironment.getUserInfoByIdCardNo(propValue);
                        if(userInfo!=null) {
                            ud = new CentitUserDetails();
                            ud.setUserInfo(userInfo);
                        }
                    }
                    break;
                default:
                    ud =  this.platformEnvironment.loadUserDetailsByUserCode(propValue);
                    break;
            }
            if(ud!=null) {
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(buildUserJson(ud)));
                return BuiltInOperation.createResponseSuccessData(1);
            } else {
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet());
                return BuiltInOperation.createResponseSuccessData(0);
            }
        } else {
            String userFilter = Pretreatment.mapTemplateStringAsFormula(bizOptJson.getString("userFilter"),
                transform);

            Set<String> users;
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
                    new UserUnitMapTranslate());
            }

            List<UserInfo> retUsers = CodeRepositoryUtil.getUserInfosByCodes(topUnit, users);
            List<UserInfo> lsUserInfo = new ArrayList<>(retUsers.size() + 1);
            for (UserInfo ui : retUsers) {
                if ("T".equals(ui.getIsValid())) {
                    lsUserInfo.add(ui);
                }
            }
            lsUserInfo.sort(Comparator.comparing(UserInfo::getUserOrder));
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(JSON.toJSON(lsUserInfo)));
            return BuiltInOperation.createResponseSuccessData(1);
        }
    }
}
