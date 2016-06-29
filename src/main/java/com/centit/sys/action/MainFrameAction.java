package com.centit.sys.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.util.StringUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.FunctionManager;
import com.opensymphony.xwork2.ActionContext;

@SuppressWarnings("rawtypes")
public class MainFrameAction extends BaseEntityDwzAction {
    private static final long serialVersionUID = 1L;
    private FunctionManager functionMgr;
    private String userFirstPage;

    public String getUserFirstPage() {
        return userFirstPage;
    }

    public void setUserFirstPage(String userFirstPage) {
        this.userFirstPage = userFirstPage;
    }

    public void setFunctionMgr(FunctionManager functionMgr) {
        this.functionMgr = functionMgr;
    }

    public String showMain() throws Exception {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());
        if (uinfo == null) {
            return "login";
        }
        // Usersetting us = uinfo.getUserSetting();

        // try {
        // if (!StringUtils.hasText(us.getMainpage())) {
        // FOptinfo f = (FOptinfo) functionMgr.getFunctionsByUser(uinfo).get(0);
        // us.setMainpage(f.getOpturl());
        // }
        // } catch (Exception e) {
        // }

        // Map<String, Object> session = ActionContext.getContext().getSession();

        // String stylePath = request.getContextPath() + "/styles/" + us.getPagestyle();
        // session.put("STYLE_PATH", stylePath);
        // userFirstPage = uinfo.getUserSetting().getMainpage();
        // userFirstPage = "/sys/mainFrame!dashboard.do";
        userFirstPage = "/app/dashboard!show.do";
        request.setAttribute("firstPage", userFirstPage);
        // session.put("LAYOUT", us.getFramelayout());

        // 当前用户所能获取菜单
        ActionContext.getContext().put("OA_MENUS",
                this.getFunctionsByUserCode(((FUserinfo) super.getLoginUser()).getUsercode()));

        return "MainPage";
    }

    /**
     * 通过Ajax请求获取当前菜单下所有子菜单
     *
     * @param superFunctionId
     * @return
     */
    public String getSuperFunc() {
        String superFunctionId = this.request.getParameter("superFunctionId");
        if (!StringUtils.hasText(superFunctionId)) {
            return getMenuFunc(new ArrayList<FOptinfo>());
        }

        FUserinfo user = new FUserinfo();
        user.setUsercode(((FUserinfo) this.getLoginUser()).getUsercode());
        List<FOptinfo> menuFunsByUser = this.functionMgr.getMenuFuncByUserIDAndSuperFunctionId(user, superFunctionId);

        return getMenuFunc(menuFunsByUser);
    }

    private String getFunctionsByUserCode(String usercode) {
        FUserinfo user = new FUserinfo();
        user.setUsercode(((FUserinfo) this.getLoginUser()).getUsercode());
        List<FOptinfo> menuFunsByUser = this.functionMgr.getMenuFuncByUser(user);

        return getMenuFunc(menuFunsByUser);
    }

    private static String getMenuFunc(List<FOptinfo> menuFunsByUser) {
        Map<String, List<Map<String, String>>> result = new HashMap<String, List<Map<String, String>>>();

        List<Map<String, String>> menuHeader = new ArrayList<Map<String, String>>();
        List<Map<String, String>> menuList = new ArrayList<Map<String, String>>();


        for (FOptinfo fOptinfo : menuFunsByUser) {
            Map<String, String> menu = new HashMap<String, String>();
            if ("0".equals(fOptinfo.getPreoptid())) {
                // {"HID":"a","HText":"菜单一"}
                menu.put("HID", fOptinfo.getOptid());
                menu.put("HText", fOptinfo.getOptname());
                menu.put("IsInToolbar", fOptinfo.getIsintoolbar());

                //设置是否为默认显示菜单
//                if("1".equals(CodeRepositoryUtil.getValue("DEFAULT_MENU", fOptinfo.getOptid()))) {
                menu.put("DefaultMenu", "1");
//                }else{
//                    menu.put("DefaultMenu", "0");
//                }

                menuHeader.add(menu);
            } else {
                // {"MID":"aa","MText":"一级子菜单1","ParentID":"a","MUrl":"","MType":"A"}
                menu.put("MID", fOptinfo.getOptid());
                menu.put("MText", fOptinfo.getOptname());
                menu.put("ParentID", fOptinfo.getPreoptid());
                menu.put("MUrl", fOptinfo.getOpturl());
                menu.put("MType", fOptinfo.getPageType());
                menu.put("IsInToolbar", fOptinfo.getIsintoolbar());

                menuList.add(menu);
            }

        }
        result.put("menuHeader", menuHeader);
        result.put("menuList", menuList);

        return JSONObject.fromObject(result).toString();
    }

    public String otherPage() {
        return "otherPage";
    }

    public String login() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        if (request.getParameter("inDialog") != null) {
            session.put("loginInDialog", "true");
            return "loginInDialog";
        } else {
            session.put("loginInDialog", "false");
            return "login";
        }
    }

    public String loginError() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        String inDialog = (String) session.get("loginInDialog");
        if (inDialog != null && "true".equals(inDialog)) {
            saveError("用户名或密码错误！");
            return "loginResInDialog";
        } else {
            return "loginError";
        }
    }

    public String loginSuccess() {
        Map<String, Object> session = ActionContext.getContext().getSession();
        String inDialog = (String) session.get("loginInDialog");
        if (inDialog != null)
            session.remove("loginInDialog");
        if (inDialog != null && "true".equals(inDialog)) {
            saveMessage("登录成功");
            return "loginResInDialog";
        } else {
            return "loginSuccess";
        }
    }

    private String captchaKey;

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String loginWithRanKey() {
        return "randomkey";
    }

    public String logincas() throws Exception {
        return "lgoinSuccess";
    }

    public String dashboard() throws Exception {
        return "dashboard";
    }

}
