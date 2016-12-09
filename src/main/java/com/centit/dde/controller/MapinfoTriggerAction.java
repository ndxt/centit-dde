package com.centit.dde.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.service.MapinfoTriggerManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;


@Controller
@RequestMapping("/mapinfotrigger")
public class MapinfoTriggerAction extends BaseEntityDwzAction<MapinfoTrigger> {
    private static final Log log = LogFactory.getLog(MapinfoTriggerAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private MapinfoTriggerManager mapinfoTriggerMag;

    public void setMapinfoTriggerManager(MapinfoTriggerManager basemgr) {
        mapinfoTriggerMag = basemgr;
        this.setBaseEntityManager(mapinfoTriggerMag);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/list",method = {RequestMethod.GET})
    public void list(HttpServletRequest request,HttpServletResponse response) {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            filterMap.put("cid.mapinfoId", filterMap.get("mapinfoId"));
            objList = mapinfoTriggerMag.listTrigger(Long.valueOf((String) filterMap.get("mapinfoId")));

            pageDesc.setTotalRows(objList.size());
            this.pageDesc = pageDesc;
//            return LIST;
            ResponseData resData = new ResponseData();
            resData.addResponseData("OBJLIST", objList);
            resData.addResponseData("PAGE_DESC", pageDesc);
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/list_add",method = {RequestMethod.PUT})
    public void list_add(HttpServletRequest request,HttpServletResponse response) {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            filterMap.put("cid.mapinfoId", filterMap.get("mapinfoId"));
            objList = mapinfoTriggerMag.listTrigger(Long.valueOf((String) filterMap.get("mapinfoId")));

            pageDesc.setTotalRows(objList.size());
            this.pageDesc = pageDesc;
//            return "list_add"; /page/dde/mapinfoTriggerList_add.jsp
            ResponseData resData = new ResponseData();
            resData.addResponseData("OBJLIST", objList);
            resData.addResponseData("PAGE_DESC", pageDesc);
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/edit",method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        //MapinfoTriggerId cid = new MapinfoTriggerId();
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                MapinfoTrigger o = baseEntityManager.getObject(object);
                if (o != null)
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }
//            return EDIT;
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/edit_add",method = {RequestMethod.GET})
    public void edit_add(HttpServletRequest request,HttpServletResponse response) {
        //MapinfoTriggerId cid = new MapinfoTriggerId();
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                MapinfoTrigger o = baseEntityManager.getObject(object);
                if (o != null)
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }
//            return "edit_add"; /page/dde/editMapinfoDetailForm_add.jsp
            JsonResultUtils.writeSingleDataJson(object, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/saveTrigger",method = {RequestMethod.PUT})
    public void saveTrigger(HttpServletRequest request,HttpServletResponse response) {
        try {
            MapinfoTrigger dbObject = baseEntityManager.getObject(object);
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            if (object.getCid().getTriggerId() == null) {
                object.getCid().setTriggerId(
                        this.mapinfoTriggerMag.getTriggerId());
            }
            baseEntityManager.saveObject(object);
            savedMessage();
//            return "saveTrigger";
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/addAndsaveTrigger",method = {RequestMethod.PUT})
    public void addAndsaveTrigger(HttpServletRequest request,HttpServletResponse response) {
        try {
            MapinfoTrigger dbObject = baseEntityManager.getObject(object);
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            if (object.getCid().getTriggerId() == null) {
                object.getCid().setTriggerId(
                        this.mapinfoTriggerMag.getTriggerId());
            }
            baseEntityManager.saveObject(object);
            savedMessage();
//            return "addAndsaveTrigger";
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }
//  页面之间的跳转
//    @RequestMapping(value="/add")
//    public String add() {
//        return "add";
//    }

//    @RequestMapping(value="/add_add")
//    public String add_add() {
//        return "add_add";
//    }


    @RequestMapping(value="/delete",method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete_add",method = {RequestMethod.PUT})
    public void delete_add(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete_add";
        JsonResultUtils.writeSuccessJson(response);
    }
}
