/**
 *
 */
package com.centit.app.action;

import com.centit.app.po.UserMailInfo;
import com.centit.app.service.UserMailManager;
import com.centit.core.action.BaseEntityDwzAction;

/**
 * emial管理ACTION
 *
 * @author ljy
 * @create 2012-4-27
 */
public class UserMailAction extends BaseEntityDwzAction<UserMailInfo> {

    private static final long serialVersionUID = 1L;

    private UserMailManager userMailManager;

    /**
     * 获取用户邮箱邮件列表
     *
     * @return
     */
    public String listEmail() {

        objList = userMailManager.listUserMailsByAccount(object.getUserCode(),
                object.getMailAccount());

        return LIST;
    }

    /**
     * 读单条邮件详细信息
     *
     * @return
     */
    public String viewMail() {

        object = userMailManager.getMailInfoByID(object.getMsgNumber(),
                object.getUserCode(), object.getMailAccount());

        return VIEW;
    }

    public String saveMailConfig() {

        return "";
    }

    public String sendMail() {
        userMailManager.sendUserMail(object, object.getUserCode(), object.getMailAccount());
        return "";
    }

    public UserMailManager getUserMailManager() {
        return userMailManager;
    }

    public void setEmailMessageReceive(UserMailManager userMailManager) {
        this.userMailManager = userMailManager;
    }
}
