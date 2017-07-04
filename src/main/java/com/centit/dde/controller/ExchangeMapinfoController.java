package com.centit.dde.controller;

import com.centit.dde.po.ExchangeMapInfo;
import com.centit.dde.po.MapInfoDetail;
import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.service.ExchangeMapInfoManager;
import com.centit.dde.service.MapInfoDetailManager;
import com.centit.dde.service.MapInfoTriggerManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.IntegrationEnvironment;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/exchangemapinfo")
public class ExchangeMapInfoController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeMapInfoController.class);

    @Resource
    private ExchangeMapInfoManager exchangeMapInfoManager;

    @Resource
    private MapInfoDetailManager mapInfoDetailManager;

    @Resource
    private MapInfoTriggerManager mapInfoTriggerManager;

    @Resource
    protected IntegrationEnvironment integrationEnvironment;
    
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<ExchangeMapInfo> objList = exchangeMapInfoManager.listObjects(searchColumn, pageDesc);
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/edit/{mapInfoId}" ,method = {RequestMethod.GET})
    public void edit(@PathVariable Long mapInfoId, HttpServletResponse response) {
        ExchangeMapInfo object = exchangeMapInfoManager.getObjectById(mapInfoId);
        List<MapInfoDetail> mapInfoDetails = mapInfoDetailManager.listByMapinfoId(object.getMapInfoId());
        List<MapInfoTrigger> mapInfoTriggers = mapInfoTriggerManager.listTrigger(object.getMapInfoId());
        object.setMapInfoDetails(mapInfoDetails);
        object.setMapInfoTriggers(mapInfoTriggers);
        JsonResultUtils.writeSingleDataJson(object, response);
    }

    /**
     * 复制现有交换任务
     */
    @RequestMapping(value="/copy/{mapInfoId}" ,method = {RequestMethod.GET})
    public void copy(@PathVariable Long mapInfoId,HttpServletResponse response) {
        ExchangeMapInfo object = exchangeMapInfoManager.getObjectById(mapInfoId);
        List<MapInfoDetail> mapInfoDetails = mapInfoDetailManager.listByMapinfoId(object.getMapInfoId());
        List<MapInfoTrigger> mapInfoTriggers = mapInfoTriggerManager.listTrigger(object.getMapInfoId());
        object.setMapInfoDetails(mapInfoDetails);
        object.setMapInfoTriggers(mapInfoTriggers);
        object.setMapInfoId(null);
        JsonResultUtils.writeSingleDataJson(object, response);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(ExchangeMapInfo object, HttpServletResponse response) {

        exchangeMapInfoManager.save(object);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete/{mapInfoId}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long mapInfoId, HttpServletResponse response) {

        exchangeMapInfoManager.deleteObjectById(mapInfoId);
        JsonResultUtils.writeSuccessJson(response);
    }

    //这里 新框架 有下载的功能 调用就好  不需要单独写
    @RequestMapping(value="/exportMapinfoDetail")
    public void exportMapinfoDetail(ExchangeMapInfo object, HttpServletResponse response) throws IOException {
        ServletOutputStream output = response.getOutputStream();
        ExchangeMapInfo exportSql = exchangeMapInfoManager.getObjectById(object.getMapInfoId());

        List<MapInfoDetail> exportFields = exportSql.getMapInfoDetails();

        Map<String, MapInfoDetail> values = new HashMap<>();
        for (int i = 0; i < exportFields.size(); i++) {
            values.put(String.valueOf(i), exportFields.get(i));
        }
        String text = com.alibaba.fastjson.JSONObject.toJSONString(values);

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getMapInfoName() + ".txt");//高速浏览器已下载的形式
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output, "UTF-8");
    }

    @RequestMapping(value="/resolveSQL", method = RequestMethod.GET)
    public void resolveSQL(ExchangeMapInfo mapinfo, HttpServletResponse response) {
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(mapinfo.getSourceDatabaseName());

        List<MapInfoDetail> mapInfoDetails = exchangeMapInfoManager.resolveSQL(databaseInfo, mapinfo.getQuerySql());

        JsonResultUtils.writeSingleDataJson(mapInfoDetails, response);
    }
}
