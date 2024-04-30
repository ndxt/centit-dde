package com.centit.dde.utils;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class LoginUserPermissionCheck {

    public static void loginUserPermissionCheck(BaseController controller,
        PlatformEnvironment platformEnvironment, String osId, HttpServletRequest request){
        if (StringUtils.isBlank(osId)){
            throw new ObjectException(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                controller.getI18nMessage( "error.701.field_is_blank", request, osId));
        }
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(request, "userCode");
        }
        if (StringUtils.isBlank(loginUser)){
            throw new ObjectException(ResponseData.HTTP_MOVE_TEMPORARILY,
                controller.getI18nMessage( "error.302.user_not_login", request));
        }
        if (!platformEnvironment.loginUserIsExistWorkGroup(osId, loginUser)){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION,
                controller.getI18nMessage( "error.403.access_forbidden", request));
        }
    }
}
