package com.centit.dde.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.MapinfoTrigger;
import com.centit.dde.service.MapinfoTriggerManager;


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
    @Override
    public String list() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            filterMap.put("cid.mapinfoId", filterMap.get("mapinfoId"));
            objList = mapinfoTriggerMag.listTrigger(Long.valueOf((String) filterMap.get("mapinfoId")));

            pageDesc.setTotalRows(objList.size());
            this.pageDesc = pageDesc;
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String list_add() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            filterMap.put("cid.mapinfoId", filterMap.get("mapinfoId"));
            objList = mapinfoTriggerMag.listTrigger(Long.valueOf((String) filterMap.get("mapinfoId")));

            pageDesc.setTotalRows(objList.size());
            this.pageDesc = pageDesc;
            return "list_add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    @Override
    public String edit() {
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
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit_add() {
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
            return "edit_add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String saveTrigger() {
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
            return "saveTrigger";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            return ERROR;
        }
    }

    public String addAndsaveTrigger() {
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
            return "addAndsaveTrigger";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            return ERROR;
        }
    }

    public String add() {
        return "add";
    }

    public String add_add() {
        return "add_add";
    }


    public String delete() {
        super.delete();

        return "delete";
    }

    public String delete_add() {
        super.delete();

        return "delete_add";
    }
}
