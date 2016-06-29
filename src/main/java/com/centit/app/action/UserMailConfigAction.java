package com.centit.app.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.centit.app.po.UserMailConfig;
import com.centit.app.service.UserMailConfigManager;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FUserinfo;

public class UserMailConfigAction extends BaseEntityDwzAction<UserMailConfig> {
    private static final Log log = LogFactory.getLog(UserMailConfigAction.class);

    // private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private UserMailConfigManager userMailConfigMag;


    public void setUserMailConfigManager(UserMailConfigManager basemgr) {
        userMailConfigMag = basemgr;
        this.setBaseEntityManager(userMailConfigMag);
    }


    public String delete() {
        super.delete();

        return "delete";
    }

    @SuppressWarnings("unchecked")
    @Override
    public String list() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        filterMap.put("usercode", this.getLoginUserCode());

        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        objList = baseEntityManager.listObjects(filterMap, pageDesc);
        this.pageDesc = pageDesc;

        return LIST;
    }

    private String getLoginUserCode() {
        return ((FUserinfo) this.getLoginUser()).getUsercode();
    }

    @Override
    public String edit() {
        return super.edit();
    }

    @Override
    public String save() {
        this.object.setUsercode(this.getLoginUserCode());
        this.object.setMailaccount(StringUtils.trimWhitespace(this.object.getMailaccount()));
        // TODO 用户代码+邮件账户 唯一。

        UserMailConfig dbObject = baseEntityManager.getObject(object);
        if (dbObject != null) {
            baseEntityManager.copyObjectNotNullProperty(dbObject, object);
            object = dbObject;
        } else {
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("usercode", this.getLoginUserCode());
            filterMap.put("mailaccountEQ", object.getMailaccount());

            if (!CollectionUtils.isEmpty(this.baseEntityManager.listObjects(filterMap))) {

                super.dwzResultParam = DwzResultParam.getErrorDwzResultParam("用户邮箱账户已存在");

                return ERROR;
            }
        }

        if (!this.userMailConfigMag.isEffective(object)) {
            super.dwzResultParam = DwzResultParam.getErrorDwzResultParam("用户邮箱账户错误");

            return ERROR;
        }

        return "saveConfig";
    }

    /**
     * 验证邮箱账户有效性
     *
     * @return
     */
    public String isEffective() {

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mailaccount", this.object.getMailaccount());
        filterMap.put("usercode", this.getLoginUserCode());

        objList = baseEntityManager.listObjects(filterMap);

        if (!CollectionUtils.isEmpty(objList)) {
            JSONObject.fromObject(false).toString();
        }

        return JSONObject.fromObject(true).toString();// JSONObject.fromObject(this.userMailConfigMag.isEffective(object)).toString();
    }
}
