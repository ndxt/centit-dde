package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.session.CentitSessionRepo;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public class SessionDataOperation implements BizOperation {

    private final PlatformEnvironment platformEnvironment;
    private final CentitSessionRepo centitSessionRepo;
    public SessionDataOperation(PlatformEnvironment platformEnvironment, CentitSessionRepo centitSessionRepo){
        this.platformEnvironment = platformEnvironment;
        this.centitSessionRepo = centitSessionRepo;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        // session 数据类型 sessionData ， systemUserId
        String sessionDateType = bizOptJson.getString("dataType"); // logout  kickOthers

        if("logout".equals(sessionDateType)){ // 登出
            HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
            if(request == null){
                return BuiltInOperation.createResponseSuccessData(0);
            }
            SecurityContextHolder.getContext().setAuthentication(null);
            request.getSession().invalidate();
            return BuiltInOperation.createResponseSuccessData(1);
        }

        if("kickOthers".equals(sessionDateType)){ // 踢掉其他用户
            if(this.centitSessionRepo!=null) {
                String sessionId = dataOptContext.getSessionId();
                String loginName = dataOptContext.getCurrentUserDetail().getUserInfo().getLoginName();
                this.centitSessionRepo.kickSessionByName(loginName, sessionId);
                return BuiltInOperation.createResponseSuccessData(1);
            }else{
                return BuiltInOperation.createResponseSuccessData(0);
            }
        }

        CentitUserDetails ud;
        if("sessionData".equals(sessionDateType)){
            String jsonValue = bizOptJson.getString("sessionData");
            ud = dataOptContext.getCurrentUserDetail();
            if (StringUtils.isNotEmpty(jsonValue)){
                Object data = // 这儿的sesion不可能是数组
                    JSONTransformer.transformer(jsonValue.startsWith("{") /*|| jsonValue.startsWith("[")*/ ?
                        JSON.parse(jsonValue) : jsonValue, new BizModelJSONTransform(bizModel));
                JSONObject object = JSONObject.from(data);
                CentitUserDetails userDetails = object.toJavaObject(CentitUserDetails.class);
                if(ud==null){
                    ud = userDetails;
                } else { // 覆盖session中的数据
                    ud.setUserInfo(userDetails.getUserInfo());
                    ud.setUserRoles(userDetails.getUserRoles());
                }
            }
        } else { // login
            String userNameValue = bizOptJson.getString("userId");
            String userName = StringBaseOpt.castObjectToString(
                JSONTransformer.transformer(userNameValue, new BizModelJSONTransform(bizModel)));
            if (StringUtils.isBlank(userName)) {
                return BuiltInOperation.createResponseData(0, 1,
                    ObjectException.DATA_VALIDATE_ERROR, "用户名设置不正确！");
            }
            ud = platformEnvironment.loadUserDetailsByUserCode(userName);
            if (ud == null) {
                ud = platformEnvironment.loadUserDetailsByLoginName(userName);
            }
            if (ud == null) {
                return BuiltInOperation.createResponseData(0, 1,
                    ObjectException.DATA_VALIDATE_ERROR, "找不到对应的用户！");
            }
        }
        //设置用户session可以实现自定义登录
        SecurityContextUtils.setLastLoginInfo(ud,
            dataOptContext.getLogId(), dataOptContext.getSessionId(), platformEnvironment);
        SecurityContextHolder.getContext().setAuthentication(ud);
        dataOptContext.setStackData(ConstantValue.SESSION_DATA_TAG, ud);
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
