package com.centit.dde.controller;

import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.MapinfoDetailManager;
import com.centit.dde.service.MapinfoTriggerManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.service.StaticEnvironmentManager;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log log = LogFactory.getLog(ExchangeMapInfoController.class);

    private static final long serialVersionUID = 1L;

    @Resource
    private ExchangeMapinfoManager exchangeMapinfoManager;

    @Resource
    private MapinfoDetailManager mapinfoDetailManager;

    @Resource
    private MapinfoTriggerManager mapinfoTriggerManager;

    @Resource
    protected StaticEnvironmentManager platformEnvironment;
    
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<ExchangeMapinfo> objList = exchangeMapinfoManager.listObjects(searchColumn, pageDesc);
        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/edit/{mapInfoId}" ,method = {RequestMethod.GET})
    public void edit(@PathVariable Long mapInfoId, HttpServletResponse response) {
        ExchangeMapinfo object = exchangeMapinfoManager.getObjectById(mapInfoId);
        List<MapinfoDetail> mapinfoDetails = mapinfoDetailManager.listByMapinfoId(object.getMapinfoId());
        List<MapinfoTrigger> mapinfoTriggers = mapinfoTriggerManager.listTrigger(object.getMapinfoId());
        object.setMapinfoDetails(mapinfoDetails);
        object.setMapinfoTriggers(mapinfoTriggers);
        JsonResultUtils.writeSingleDataJson(object, response);
    }

    /**
     * 复制现有交换任务
     */
    @RequestMapping(value="/copy/{mapInfoId}" ,method = {RequestMethod.GET})
    public void copy(@PathVariable Long mapInfoId,HttpServletResponse response) {
        ExchangeMapinfo object = exchangeMapinfoManager.getObjectById(mapInfoId);
        object.setMapinfoId(null);
        JsonResultUtils.writeSingleDataJson(object, response);
    }

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(ExchangeMapinfo object, HttpServletResponse response) {

        exchangeMapinfoManager.save(object);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete/{mapInfoId}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long mapInfoId, HttpServletResponse response) {

        exchangeMapinfoManager.deleteObjectById(mapInfoId);
        JsonResultUtils.writeSuccessJson(response);
    }

    //这里 新框架 有下载的功能 调用就好  不需要单独写
    @RequestMapping(value="/exportMapinfoDetail")
    public void exportMapinfoDetail(ExchangeMapinfo object,HttpServletResponse response) throws IOException {
        ServletOutputStream output = response.getOutputStream();
        ExchangeMapinfo exportSql = exchangeMapinfoManager.getObjectById(object.getMapinfoId());

        List<MapinfoDetail> exportFields = exportSql.getMapinfoDetails();

        Map<String, MapinfoDetail> values = new HashMap<>();
        for (int i = 0; i < exportFields.size(); i++) {
            values.put(String.valueOf(i), exportFields.get(i));
        }
        String text = com.alibaba.fastjson.JSONObject.toJSONString(values);

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getMapinfoName() + ".txt");//高速浏览器已下载的形式
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output, "UTF-8");
    }

    @RequestMapping(value="/resolveSQL", method = RequestMethod.GET)
    public void resolveSQL(ExchangeMapinfo mapinfo, HttpServletResponse response) {
        DatabaseInfo databaseInfo = platformEnvironment.getDatabaseInfo(mapinfo.getSourceDatabaseName());

        List<MapinfoDetail> mapinfoDetails = exchangeMapinfoManager.resolveSQL(databaseInfo, mapinfo.getQuerySql());

        JsonResultUtils.writeSingleDataJson(mapinfoDetails, response);
    }
}
