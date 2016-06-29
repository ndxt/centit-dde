package com.centit.dde.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;

public class ExchangeMapinfoAction extends BaseEntityDwzAction<ExchangeMapinfo> {
    private static final Log log = LogFactory
            .getLog(ExchangeMapinfoAction.class);

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
    @Override
    public String list() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);
            this.pageDesc = pageDesc;
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }


    public String add() {
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
            ServletActionContext.getContext().put("DatabaseNames", DatabaseNames);
            return "add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    @Override
    public String edit() {
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
            ServletActionContext.getContext().put("DatabaseNames", DatabaseNames);
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }


    public String save() {
        try {
            ExchangeMapinfo dbObject = baseEntityManager.getObject(object);
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            baseEntityManager.saveObject(object);
            savedMessage();
            return SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            return ERROR;
        }
    }

    public String delete() {

        baseEntityManager.deleteObject(object);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(object.getMapinfoId());
        deletedMessage();

        return "delete";
    }
}
