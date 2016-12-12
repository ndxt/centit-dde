package com.centit.dde.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.sun.istack.Nullable;

@Controller
@RequestMapping("/exchangemapinfo")
public class ExchangeMapinfoController extends BaseController {
    private static final Log log = LogFactory
            .getLog(ExchangeMapinfoController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    
    @Resource
    @Nullable
    private ExchangeMapinfoManager exchangeMapinfoMag;
    @Resource
    @Nullable
    private ExchangeTaskdetailManager exchangeTaskdetailManager;


    @SuppressWarnings("unchecked")
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        try {
//            Map<Object, Object> paramMap = request.getParameterMap();
////            resetPageParam(paramMap);
//
//            Map<String, Object> filterMap = convertSearchColumn(paramMap);
//            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
//            List<ExchangeMapinfo> objList = exchangeMapinfoMag.listObjects(filterMap, pageDesc);
            Map<String, Object> searchColumn = convertSearchColumn(request);
            List<ExchangeMapinfo> objList = exchangeMapinfoMag.listObjects(searchColumn, pageDesc);
            ResponseData resData = new ResponseData();
            resData.addResponseData("OBJLIST", objList);
            resData.addResponseData("PAGE_DESC", pageDesc);
            JsonResultUtils.writeResponseDataAsJson(resData, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("XXXXX", response);
        }
    }


    @SuppressWarnings("unused")
    @RequestMapping(value="/add" ,method = {RequestMethod.PUT})
    public void add(ExchangeMapinfo object,HttpServletRequest request,HttpServletResponse response) {
        Long mapinfoId = object.getMapinfoId();
        try {
            if (object == null) {
//                object = getEntityClass().newInstance();
                JsonResultUtils.writeBlankJson(response);
                return;
            } else {
                ExchangeMapinfo o = exchangeMapinfoMag.getObjectById(mapinfoId);;
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    exchangeMapinfoMag.copyObject(object, o);
                else
                    exchangeMapinfoMag.clearObjectProperties(object);
            }
            List<String> DatabaseNames = exchangeMapinfoMag.listDatabaseName();
            /*ServletActionContext.getContext().put("DatabaseNames", DatabaseNames);
            return "add";*/
            JsonResultUtils.writeSingleDataJson(DatabaseNames, response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/edit/{{mapinfoId}}",method = {RequestMethod.PUT})
    public void edit(@PathVariable Long mapinfoId,ExchangeMapinfo object, HttpServletRequest request,HttpServletResponse response) {
        try {
            if (object == null) {
//                object = getEntityClass().newInstance();
                JsonResultUtils.writeBlankJson(response);
                return;
            } else {
                ExchangeMapinfo o = exchangeMapinfoMag.getObjectById(mapinfoId);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    exchangeMapinfoMag.copyObject(object, o);
                else
                    exchangeMapinfoMag.clearObjectProperties(object);
            }
            List<String> DatabaseNames = exchangeMapinfoMag.listDatabaseName();
            /*ServletActionContext.getContext().put("DatabaseNames", DatabaseNames);
            return EDIT;*/
            JsonResultUtils.writeSingleDataJson(DatabaseNames, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/save/{{mapinfoId}}" ,method = {RequestMethod.PUT})
    public void save(@PathVariable Long mapinfoId,ExchangeMapinfo object, HttpServletRequest request,HttpServletResponse response) {
        try {
            ExchangeMapinfo dbObject = exchangeMapinfoMag.getObjectById(mapinfoId);
            if (dbObject != null) {
                exchangeMapinfoMag.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            exchangeMapinfoMag.saveObject(object);
//            savedMessage();
//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            saveError(e.getMessage());
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }
    @RequestMapping(value="/delete/{{mapinfoId}}" ,method = {RequestMethod.PUT})
    public void delete(@PathVariable Long mapinfoId, HttpServletRequest request,HttpServletResponse response) {
        ExchangeMapinfo object = exchangeMapinfoMag.getObjectById(mapinfoId);
        exchangeMapinfoMag.deleteObject(object);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(mapinfoId);
//        deletedMessage();
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
