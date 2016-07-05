package com.centit.dde.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.support.algorithm.StringBaseOpt;

/**
 * Created by sx on 2014/12/10.
 */
public class ValidatorWsImpl implements ValidatorWs {

    protected static String wsUnitCode = SysParametersUtils.getStringValue("ws_unit_code");

    protected PlatformEnvironment platformEnvironment;

    private UserDataOptIdManager userDataOptIdManager;

    public void setUserDataOptIdManager(UserDataOptIdManager userDataOptIdManager) {
        this.userDataOptIdManager = userDataOptIdManager;
    }
    
    @Override
    public String validatorUserinfo(String userName, String userPin) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPin)) {
            return "参数为空";
        }
        IUserInfo userInfo = platformEnvironment.getLoginNameRepo().get(userName);
        //FUserinfo userInfo = sysUserManager.getUserByLoginname(userName);
        if (null == userInfo) {
            return "1：用户名密码不匹配";
        }

        String password = StringBaseOpt.decryptBase64Des(userPin);

        if (!userInfo.get.equals(sysUserManager.encodePassword(password, userInfo.getUsercode()))) {
            return "1：用户名密码不匹配";
        }

        if (!"T".equals(userInfo.getIsValid())) {
            return "1：用户已被禁用";
        }


        List<FUserunit> listObjects = sysUnitManager.getSysUnitsByUserId(userInfo.getUsercode());
        boolean flag = false;
        for (FUserunit userunit : listObjects) {
            if (wsUnitCode.equals(userunit.getUnitcode())) {
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
        IUserInfo userInfo = platformEnvironment.getLoginNameRepo().get(userName);
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
