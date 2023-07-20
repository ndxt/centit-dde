package com.centit.dde.utils;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class LoginUserPermissionCheck {

    public static void loginUserPermissionCheck(PlatformEnvironment platformEnvironment, String osId, HttpServletRequest request){
        if (StringUtils.isBlank(osId)){
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR, "osId不能为空！");
        }
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(request, "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY, "您未登录，请先登录！");
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(osId,loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限，请联系管理员！");
        }
    }
}
