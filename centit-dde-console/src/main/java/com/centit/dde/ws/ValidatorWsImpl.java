package com.centit.dde.ws;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.security.DESSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sx on 2014/12/10.
 */
public class ValidatorWsImpl implements ValidatorWs {

    protected static String wsUnitCode = SysParametersUtils.getStringValue("ws_unit_code");

    private Md5PasswordEncoder passwordEncoder;
    public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    
    private UserDataOptIdManager userDataOptIdManager;

    public void setUserDataOptIdManager(UserDataOptIdManager userDataOptIdManager) {
        this.userDataOptIdManager = userDataOptIdManager;
    }
    
    @Override
    public String validatorUserinfo(String userName, String userPin) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPin)) {
            return "参数为空";
        }
        IUserInfo userInfo = CodeRepositoryUtil.getUserInfoByLoginName(userName);
        //FUserinfo userInfo = sysUserManager.getUserByLoginname(userName);
        if (null == userInfo) {
            return "1：用户名密码不匹配";
        }

        String password = DESSecurityUtils.decryptBase64String(userPin, DatabaseInfo.DESKEY);

        if (!userInfo.getUserPin().equals(passwordEncoder.encodePassword(password, userInfo.getUserCode()))) {
            return "1：用户名密码不匹配";
        }

        if (!"T".equals(userInfo.getIsValid())) {
            return "1：用户已被禁用";
        }
        List<? extends IUserUnit> listObjects =  CodeRepositoryUtil.listUserUnits(userInfo.getUserCode());
        boolean flag = false;
        for (IUserUnit userunit : listObjects) {
            if (wsUnitCode.equals(userunit.getUnitCode())) {
                flag = true;
            }
        }

        if (!flag) {
            return "1：用户不属性WS接口交换组成员";
        }


        return null;
    }

    @Override
    public String validatorDataOptId(String userName, String dataOptId, String dataOptType) {
        IUserInfo userInfo = CodeRepositoryUtil.getUserInfoByLoginName(userName);
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("usercode", userInfo.getUserCode());
        filterMap.put("dataOptId", dataOptId);
        filterMap.put("dataoptType", dataOptType);
        List<UserDataOptId> listObjects = userDataOptIdManager.listObjects(filterMap);
        if (CollectionUtils.isEmpty(listObjects) || StringUtils.isBlank(dataOptId)) {
            return "2：用户+" + userName + "+没有权限操作" + dataOptId;
        }
        return null;
    }
}
