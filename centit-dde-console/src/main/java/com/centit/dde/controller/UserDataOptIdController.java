package com.centit.dde.controller;

import com.centit.dde.po.UserDataOptId;
import com.centit.dde.service.UserDataOptIdManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.support.database.utils.PageDesc;
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
        List<UserDataOptId> objList = userDataOptIdManager.listObjects(searchColumn);
        ResponseMapData resData = new ResponseMapData();
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
        userDataOptIdManager.saveNewObject(object);
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{udId}",method = {RequestMethod.GET})
    public void edit(@PathVariable Long udId,HttpServletResponse response) {
        UserDataOptId userDataOptId = userDataOptIdManager.getObjectById(udId);
        JsonResultUtils.writeSingleDataJson(userDataOptId, response);
    }

    @RequestMapping(value="/delete/{udId}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long udId,HttpServletResponse response) {
        userDataOptIdManager.deleteObjectById(udId);
        JsonResultUtils.writeSuccessJson(response);
    }
}
