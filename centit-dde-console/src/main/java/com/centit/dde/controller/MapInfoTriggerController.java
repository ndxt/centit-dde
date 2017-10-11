package com.centit.dde.controller;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.service.MapInfoTriggerManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mapinfotrigger")
public class MapInfoTriggerController extends BaseController {
    private static final Log log = LogFactory.getLog(MapInfoTriggerController.class);

    private static final long serialVersionUID = 1L;

    @Resource
    private MapInfoTriggerManager mapinfoTriggerMag;

    @RequestMapping(value="/list",method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);

        searchColumn.put("cid.mapInfoId", searchColumn.get("mapInfoId"));
        List<MapInfoTrigger> objList = mapinfoTriggerMag.listTrigger(Long.valueOf((String) searchColumn.get("mapinfoId")));

        pageDesc.setTotalRows(objList.size());
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("OBJLIST", objList);
        resData.addResponseData("PAGE_DESC", pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/edit",method = {RequestMethod.GET})
    public void edit(MapInfoTrigger object , HttpServletRequest request, HttpServletResponse response) {
        if (object == null) {
//                object = getEntityClass().newInstance();
        } else {
            MapInfoTrigger o = mapinfoTriggerMag.getObjectById(object.getCid());
            if (o != null)
                mapinfoTriggerMag.copyObject(object, o);
            else
                mapinfoTriggerMag.clearObjectProperties(object);
        }
//            return EDIT;
        JsonResultUtils.writeSingleDataJson(object,response);
    }

    @RequestMapping(value="/saveTrigger",method = {RequestMethod.PUT})
    public void saveTrigger(MapInfoTrigger object, HttpServletResponse response) {
        try {
            MapInfoTrigger dbObject = mapinfoTriggerMag.getObjectById(object.getCid());
            if (dbObject != null) {
                mapinfoTriggerMag.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            if (object.getCid().getTriggerId() == null) {
                object.getCid().setTriggerId(
                        this.mapinfoTriggerMag.getTriggerId());
            }
            mapinfoTriggerMag.saveObject(object);
//            savedMessage();
//            return "saveTrigger";
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            saveError(e.getMessage());
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/addAndsaveTrigger",method = {RequestMethod.PUT})
    public void addAndsaveTrigger(MapInfoTrigger object, HttpServletRequest request, HttpServletResponse response) {
        try {
            MapInfoTrigger dbObject = mapinfoTriggerMag.getObjectById(object.getCid());
            if (dbObject != null) {
                mapinfoTriggerMag.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            if (object.getCid().getTriggerId() == null) {
                object.getCid().setTriggerId(
                        this.mapinfoTriggerMag.getTriggerId());
            }
            mapinfoTriggerMag.saveObject(object);
//            savedMessage();
//            return "addAndsaveTrigger";
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
//            saveError(e.getMessage());
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/delete",method = {RequestMethod.DELETE})
    public void delete(MapInfoTrigger object, HttpServletRequest request, HttpServletResponse response) {
        mapinfoTriggerMag.deleteObjectById(object.getCid());

        JsonResultUtils.writeSuccessJson(response);
    }

}
