package com.centit.dde.controller;

import com.centit.dde.po.DataOptInfo;
import com.centit.dde.service.DataOptInfoManager;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dataoptinfo")
public class DataOptInfoController extends BaseController {
    private static final Log log = LogFactory.getLog(DataOptInfoController.class);

    @Resource
    private DataOptInfoManager dataOptInfoManager;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<DataOptInfo> objList = dataOptInfoManager.listObjects(searchColumn, pageDesc);
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(DataOptInfo object, HttpServletRequest request,HttpServletResponse response) {
        dataOptInfoManager.saveObject(object, getLoginUser(request));

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/edit/{dataOptId}",method = {RequestMethod.GET})
    public void edit(@PathVariable String dataOptId,HttpServletResponse response) {
        DataOptInfo dataOptInfo = dataOptInfoManager.getObjectById(dataOptId);

        JsonResultUtils.writeSingleDataJson(dataOptInfo, response);
    }

    @RequestMapping(value="/delete/{dataOptId}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable String dataOptId, HttpServletResponse response) {
        dataOptInfoManager.deleteObjectById(dataOptId);

        JsonResultUtils.writeSuccessJson(response);
    }
}
