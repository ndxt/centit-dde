package com.centit.dde.controller;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Controller
@RequestMapping("/userdataoptid")
public class UserDataOptIdController extends BaseController {
    private static final Log log = LogFactory.getLog(UserDataOptIdController.class);

    private static final long serialVersionUID = 1L;

    @Resource

    private UserDataOptIdManager userDataOptIdManager;

/*    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> filterMap = convertSearchColumn(request);
        filterMap.put("roleCode", roleCode);
        List<UserDataOptId> listObjects = userDataOptIdManager.listObjectBysql(filterMap, pageDesc);

        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response);

    }*/

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(UserDataOptId object,HttpServletResponse response) {
        if (null == object.getUdId()) {
            object.setCreateDate(new Date());
        } else {
            object.setLastModifyDate(new Date());
        }

        userDataOptIdManager.saveObject(object);
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(UserDataOptId object,HttpServletResponse response) {
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
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete",method = {RequestMethod.PUT})
    public void delete(UserDataOptId object,HttpServletResponse response) {
        userDataOptIdManager.deleteObjectById(object.getUdId());
        JsonResultUtils.writeSuccessJson(response);
    }
}
