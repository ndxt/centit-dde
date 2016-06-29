package com.centit.sys.action;

import java.util.List;
import java.util.Map;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.Usersetting;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.FunctionManager;
import com.centit.sys.service.SysUserManager;
import com.centit.sys.service.UserSettingManager;
import com.opensymphony.xwork2.ActionContext;


public class UserSettingAction extends BaseEntityDwzAction<Usersetting> {
    private static final long serialVersionUID = 1L;
    private SysUserManager sysUserMgr;
    private UserSettingManager userSettingMgr;
    private FunctionManager functionMgr;
    private FUserinfo uinfo;
    private List<FOptinfo> functions;

    public FunctionManager getFunctionMgr() {
        return functionMgr;
    }

    public List<FOptinfo> getFunctions() {
        return functions;
    }

    public void setFunctions(List<FOptinfo> func) {
        this.functions = func;
    }

    public FUserinfo getUinfo() {
        return uinfo;
    }

    public void setUinfo(FUserinfo uinfo) {
        this.uinfo = uinfo;
    }

    public void setFunctionMgr(FunctionManager fMgr) {
        this.functionMgr = fMgr;
    }

    public void setUserSettingMgr(UserSettingManager basemgr) {
        userSettingMgr = basemgr;
        this.setBaseEntityManager(userSettingMgr);
    }

    public UserSettingManager getUserSettingMgr() {
        return userSettingMgr;
    }

    public void setSysUserMgr(SysUserManager sysuserMagr) {
        this.sysUserMgr = sysuserMagr;
    }

    public SysUserManager getSysUserMgr() {
        return sysUserMgr;
    }

    @Override
    public String edit() {

        try {
            uinfo = ((FUserDetail) getLoginUser());
            if (uinfo == null)
                return ERROR;
            object = sysUserMgr.getUserSetting(uinfo.getUsercode());
            object.setUsercode(uinfo.getUsercode());
            functions = functionMgr.getFunctionsByUser(uinfo);
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String save() {
        FUserDetail ud = ((FUserDetail) getLoginUser());
        object.setUsercode(ud.getUsercode());
        super.save();
        ud.getUserSetting().copyNotNullProperty(object);
        Map<String, Object> session = ActionContext.getContext().getSession();
        String stylePath = request.getContextPath() + "/styles/" + object.getPagestyle();
        session.put("STYLE_PATH", stylePath);
        session.put("LAYOUT", object.getFramelayout());

        return EDIT;
    }

}
