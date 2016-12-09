package com.centit.dde.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;

@Controller
@RequestMapping("/exchangemapinfo")
public class ExchangeMapinfoController extends BaseEntityDwzAction<ExchangeMapinfo> {
    private static final Log log = LogFactory
            .getLog(ExchangeMapinfoController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private ExchangeMapinfoManager exchangeMapinfoMag;
    private ExchangeTaskdetailManager exchangeTaskdetailManager;


    public ExchangeTaskdetailManager getExchangeTaskdetailManager() {
        return exchangeTaskdetailManager;
    }

    public void setExchangeTaskdetailManager(
            ExchangeTaskdetailManager exchangeTaskdetailManager) {
        this.exchangeTaskdetailManager = exchangeTaskdetailManager;
    }

    public void setExchangeMapinfoManager(ExchangeMapinfoManager basemgr) {
        exchangeMapinfoMag = basemgr;
        this.setBaseEntityManager(exchangeMapinfoMag);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list( HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);
            this.pageDesc = pageDesc;
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


    @RequestMapping(value="/add" ,method = {RequestMethod.PUT})
    public void add(HttpServletRequest request,HttpServletResponse response) {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                ExchangeMapinfo o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
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

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                ExchangeMapinfo o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
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

    @RequestMapping(value="/save" ,method = {RequestMethod.PUT})
    public void save(HttpServletRequest request,HttpServletResponse response) {
        try {
            ExchangeMapinfo dbObject = baseEntityManager.getObject(object);
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            baseEntityManager.saveObject(object);
            savedMessage();
//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }
    @RequestMapping(value="/delete" ,method = {RequestMethod.PUT})
    public void delete(HttpServletRequest request,HttpServletResponse response) {

        baseEntityManager.deleteObject(object);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(object.getMapinfoId());
        deletedMessage();
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
