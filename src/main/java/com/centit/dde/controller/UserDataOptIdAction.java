package com.centit.dde.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.core.common.JsonResultUtils;
@Controller
@RequestMapping(name="userdataoptid")
public class UserDataOptIdAction extends BaseEntityDwzAction<UserDataOptId> {
    private static final Log log = LogFactory.getLog(UserDataOptIdAction.class);

    private static final long serialVersionUID = 1L;

    private UserDataOptIdManager userDataOptIdManager;

    public void setUserDataOptIdManager(UserDataOptIdManager userDataOptIdManager) {
        this.userDataOptIdManager = userDataOptIdManager;
        setBaseEntityManager(userDataOptIdManager);
    }


    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(HttpServletRequest request,HttpServletResponse response) {
        if (null == object.getUdId()) {
            object.setCreateDate(new Date());
        } else {
            object.setLastModifyDate(new Date());
        }

        super.save();
//        return super.save();
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        super.edit();
//        return super.edit();

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete",method = {RequestMethod.PUT})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
