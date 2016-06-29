package com.centit.app.util;

import java.util.Map.Entry;

import javax.servlet.http.HttpSession;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.impl.DefaultScriptSessionManager;
import org.springframework.security.core.context.SecurityContextImpl;

import com.centit.core.action.BaseAction;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryManager;

/**
 * ScriptSession与当前用户 usercode 关联管理
 *
 * @author sx
 * @create 2012-12-12
 */
public class DwrScriptSessionManager extends DefaultScriptSessionManager {
    /**
     * ScriptSession 与 HttpSession 创建时机及方法不一致，需注意
     */
    public DwrScriptSessionManager() {
        super.addScriptSessionListener(new ScriptSessionListener() {

            @Override
            public void sessionDestroyed(ScriptSessionEvent event) {
                ScriptSession scriptSession = event.getSession();

                // 有可能value值已被覆盖，不存在于关联Map
                if (!CodeRepositoryManager.USERCODE_SCRIPTSESSION.containsValue(scriptSession.getId())) {
                    return;
                }

                for (Entry<String, String> e : CodeRepositoryManager.USERCODE_SCRIPTSESSION.entrySet()) {
                    if (scriptSession.getId().equals(e.getValue())) {
                        CodeRepositoryManager.USERCODE_SCRIPTSESSION.remove(e.getKey());

                        break;
                    }
                }

            }

            @Override
            public void sessionCreated(ScriptSessionEvent event) {
                HttpSession session = WebContextFactory.get().getSession();
                SecurityContextImpl securityContext = (SecurityContextImpl) session
                        .getAttribute("SPRING_SECURITY_CONTEXT");
                if (null == securityContext) {
                    return;
                }

                // String usercode = ((FUserinfo) securityContext.getAuthentication().getPrincipal()).getUsercode();
                String usercode = ((FUserinfo) BaseAction.getLoginUser(securityContext, session)).getUsercode();
                ScriptSession scriptSession = event.getSession();

                CodeRepositoryManager.USERCODE_SCRIPTSESSION.put(usercode, scriptSession.getId());
            }
        });
    }

}
