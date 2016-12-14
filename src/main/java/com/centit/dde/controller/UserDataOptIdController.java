package com.centit.dde.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.sun.istack.Nullable;
@Controller
@RequestMapping(name="userdataoptid")
public class UserDataOptIdController extends BaseController {
    private static final Log log = LogFactory.getLog(UserDataOptIdController.class);

    private static final long serialVersionUID = 1L;

    @Resource
    @Nullable
    private UserDataOptIdManager userDataOptIdManager;

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(UserDataOptId object,HttpServletRequest request,HttpServletResponse response) {
        if (null == object.getUdId()) {
            object.setCreateDate(new Date());
        } else {
            object.setLastModifyDate(new Date());
        }

        userDataOptIdManager.saveObject(object);
//        return super.save();
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(UserDataOptId object,HttpServletRequest request,HttpServletResponse response) {
//        super.edit();
        if (object == null) {
//          object = getEntityClass().newInstance();
          JsonResultUtils.writeBlankJson(response);
          return;
      } else {
          UserDataOptId o = userDataOptIdManager.getObjectById(object.getUdId());
          if (o != null)
              // 将对象o copy给object，object自己的属性会保留
              userDataOptIdManager.copyObject(object, o);
          else
              userDataOptIdManager.clearObjectProperties(object);
      }
//        return super.edit();

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete",method = {RequestMethod.PUT})
    public void delete(UserDataOptId object,HttpServletRequest request,HttpServletResponse response) {
//        return "delete";
        userDataOptIdManager.deleteObjectById(object.getUdId());
        JsonResultUtils.writeSuccessJson(response);
    }
}
