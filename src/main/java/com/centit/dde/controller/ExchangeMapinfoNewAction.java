package com.centit.dde.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.CollectionUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

public class ExchangeMapinfoNewAction extends BaseEntityDwzAction<ExchangeMapinfo> {
    private static final Log log = LogFactory.getLog(ExchangeMapinfoNewAction.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    private ExchangeMapinfoManager exchangeMapinfoMag;

    private ExchangeTaskdetailManager exchangeTaskdetailManager;

    private DatabaseInfoManager databaseInfoManager;

    public void setDatabaseInfoManager(DatabaseInfoManager databaseInfoManager) {
        this.databaseInfoManager = databaseInfoManager;
    }

    public ExchangeTaskdetailManager getExchangeTaskdetailManager() {
        return exchangeTaskdetailManager;
    }

    public void setExchangeTaskdetailManager(ExchangeTaskdetailManager exchangeTaskdetailManager) {
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

    public String showMapinfoDetail() {
        object = baseEntityManager.getObject(object);

        return "showMapinfoDetail";
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

    /**
     * 复制现有交换任务
     *
     * @return
     */
    public String copy() {
        object = baseEntityManager.getObject(object);

        object.setMapinfoId(null);
        return "showMapinfoDetail";

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
        dwzResultParam = new DwzResultParam();
        // dwzResultParam.setNavTabId(tabid);
        if (StringUtils.isBlank(object.getMapinfoName())) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            dwzResultParam.setMessage("交换名称未填写");

            return SUCCESS;
        }

        object.setMapinfoName(StringUtils.trim(object.getMapinfoName()));

        //判断交换名称的唯一性
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mapinfoNameEq", object.getMapinfoName());

        List<ExchangeMapinfo> listObjects = exchangeMapinfoMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
            ExchangeMapinfo exchangeMapinfoDb = exchangeMapinfoMag.getObject(object);
            String message = "交换名称已存在";
            if (null == exchangeMapinfoDb) {
                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

                return SUCCESS;
            } else {
                if (1 < listObjects.size() || !exchangeMapinfoDb.getMapinfoId().equals(listObjects.get(0)
                        .getMapinfoId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

                    return SUCCESS;
                }
            }
        }


        try {
            exchangeMapinfoMag.validator(object);

            exchangeMapinfoMag.saveObject(object, (FUserDetail) getLoginUser());
        } catch (SqlResolveException e) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            String message = null;
            if (0 == e.getErrorcode()) {
                message = e.getMessage();
            } else {
                message = SysParametersUtils.getValue(String.valueOf(e.getErrorcode()));
            }

            dwzResultParam.setMessage(message);

            return SUCCESS;
        }

        dwzResultParam.setForwardUrl("/dde/exchangeMapinfoNew!list.do");
        return SUCCESS;
    }

    public String delete() {

        baseEntityManager.deleteObject(object);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(object.getMapinfoId());
        deletedMessage();

        return "delete";
    }

    public String sourceDs() {
        // 数据库连接
        List<DatabaseInfo> databaseInfos = databaseInfoManager.listObjects();
        request.setAttribute("databaseInfos", databaseInfos);

        return "sourceDs";
    }

    public String destDs() {
        // 数据库连接
        List<DatabaseInfo> databaseInfos = databaseInfoManager.listObjects();
        request.setAttribute("databaseInfos", databaseInfos);

        return "destDs";
    }

    /**
     * 源字段
     *
     * @return
     */
    public String formField() {

        return "formField";
    }

    /**
     * 触发器
     *
     * @return
     */
    public String formTrigger() {
        return "formTrigger";
    }
    public void exportMapinfoDetail() throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream output = response.getOutputStream();
        ExchangeMapinfo exportSql = exchangeMapinfoMag.getObject(object);


        List<MapinfoDetail> exportFields = exportSql.getMapinfoDetails();

        Map<String, MapinfoDetail> values = new HashMap<String, MapinfoDetail>();
        for (int i = 0; i < exportFields.size(); i++) {
            values.put(String.valueOf(i), exportFields.get(i));
        }
        String text = com.alibaba.fastjson.JSONObject.toJSONString(values);

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getMapinfoName() + ".txt");
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output);
    }
}
