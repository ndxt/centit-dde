package com.centit.dde.controller;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userdataoptid")
public class UserDataOptIdController extends BaseController {
    private static final Log log = LogFactory.getLog(UserDataOptIdController.class);

    private static final long serialVersionUID = 1L;

    @Resource

    private UserDataOptIdManager userDataOptIdManager;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<UserDataOptId> objList = userDataOptIdManager.listObjects(searchColumn, pageDesc);
//        Map<String, Object> filterMap = convertSearchColumn(request);
//        filterMap.put("roleCode", roleCode);
//        List<UserDataOptId> listObjects = userDataOptIdManager.listObjectBysql(filterMap, pageDesc);
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);

    }

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

    @RequestMapping(value="/edit/{udId}",method = {RequestMethod.POST})
    public void edit(@PathVariable Long udId ,UserDataOptId object,HttpServletResponse response) {
        UserDataOptId userDataOptId = userDataOptIdManager.getObjectById(udId);
//        ResponseData resData = new ResponseData();
//        resData.addResponseData("userDataOptId", userDataOptId);
        JsonResultUtils.writeSingleDataJson(userDataOptId, response);
    }

    @RequestMapping(value="/delete/{udId}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long udId,UserDataOptId object,HttpServletResponse response) {
        userDataOptIdManager.deleteObjectById(udId);
        JsonResultUtils.writeSuccessJson(response);
    }
}
