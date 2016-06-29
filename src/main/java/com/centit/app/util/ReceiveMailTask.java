package com.centit.app.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextImpl;

import com.centit.app.po.Innermsg;
import com.centit.app.po.UserMailConfig;
import com.centit.app.service.InnermsgManager;
import com.centit.app.service.UserMailConfigManager;
import com.centit.core.action.BaseAction;
import com.centit.core.task.CentitCronTask;
import com.centit.sys.po.FUserinfo;

/**
 * 根据当前用户配置定时收取邮件
 *
 * @author sx
 * @create 2013-1-9
 */
public class ReceiveMailTask extends CentitCronTask {

    private InnermsgManager innermsgManager;
    private UserMailConfigManager userMailConfigManager;

    @Override
    public void runEntity(Date runTime) {
        HttpSession session = ServletActionContext.getRequest().getSession();
        SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (null == securityContext) {
            return;
        }

        String usercode = ((FUserinfo) BaseAction.getLoginUser(securityContext, session)).getUsercode();
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("usercode", usercode);
        List<UserMailConfig> configs = this.userMailConfigManager.listObjects(filterMap);

        for (UserMailConfig c : configs) {
            Innermsg imsg = new Innermsg();
            imsg.setC(c);
            imsg.setReceiveUserCode(usercode);

            this.innermsgManager.saveReceiveMail(imsg);

            super.setCronExpression(c.getIntervaltime() + " * * * * ?");
        }

    }

    public void setInnermsgManager(InnermsgManager innermsgManager) {
        this.innermsgManager = innermsgManager;
    }

    public void setUserMailConfigManager(UserMailConfigManager userMailConfigManager) {
        this.userMailConfigManager = userMailConfigManager;
    }

}
