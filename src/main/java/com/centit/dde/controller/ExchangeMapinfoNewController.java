package com.centit.dde.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.util.SysParametersUtils;

@Controller
@RequestMapping("/exchangemapinfonew")
public class ExchangeMapinfoNewController extends BaseEntityDwzAction<ExchangeMapinfo> {
    private static final Log log = LogFactory.getLog(ExchangeMapinfoNewController.class);

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
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("page/dde/exchangeMapinfoNewList");
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);
            ResponseData resData = new ResponseData();
            resData.addResponseData("OBJLIST", objList);
            resData.addResponseData("PAGE_DESC", pageDesc);
            JsonResultUtils.writeResponseDataAsJson(resData, response);
//            /page/dde/exchangeMapinfoNewList.jsp
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/showMapinfoDetail" ,method = {RequestMethod.GET})
    public void showMapinfoDetail(HttpServletRequest request,HttpServletResponse response) {
        object = baseEntityManager.getObject(object);
        JsonResultUtils.writeSingleDataJson(object, response);
        /*return "showMapinfoDetail";*/
    }
    
    @RequestMapping(value="/add" ,method = {RequestMethod.GET})
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
//            /page/dde/addExchangeMapinfoForm.jsp
            JsonResultUtils.writeSingleDataJson(DatabaseNames, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    /**
     * 复制现有交换任务
     *
     * @return
     */
    @RequestMapping(value="/copy" ,method = {RequestMethod.GET})
    public void copy(HttpServletRequest request,HttpServletResponse response) {
        object = baseEntityManager.getObject(object);
        object.setMapinfoId(null);
        JsonResultUtils.writeSingleDataJson(object, response);
        /*return "showMapinfoDetail";*/
    }

    @RequestMapping(value="/edit",method = {RequestMethod.GET})
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

    @RequestMapping(value="/save",method = {RequestMethod.PUT})
    public void save(HttpServletRequest request,HttpServletResponse response) {
        dwzResultParam = new DwzResultParam();
        // dwzResultParam.setNavTabId(tabid);
        if (StringUtils.isBlank(object.getMapinfoName())) {
            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            dwzResultParam.setMessage("交换名称未填写");

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
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

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !exchangeMapinfoDb.getMapinfoId().equals(listObjects.get(0)
                        .getMapinfoId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                    JsonResultUtils.writeSuccessJson(response);
                    return;
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

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

        dwzResultParam.setForwardUrl("/dde/exchangeMapinfoNew!list.do");
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete", method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {

        baseEntityManager.deleteObject(object);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(object.getMapinfoId());
        deletedMessage();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/sourceDs", method = {RequestMethod.GET})
    public void sourceDs(HttpServletRequest request,HttpServletResponse response) {
        // 数据库连接
        List<DatabaseInfo> databaseInfos = databaseInfoManager.listObjects();
        /*request.setAttribute("databaseInfos", databaseInfos);

        return "sourceDs";*/
        JsonResultUtils.writeSingleDataJson(databaseInfos, response);
    }
    
    @RequestMapping(value="/destDs" , method = {RequestMethod.GET})
    public void destDs(HttpServletResponse response) {
        // 数据库连接
        List<DatabaseInfo> databaseInfos = databaseInfoManager.listObjects();
        /*request.setAttribute("databaseInfos", databaseInfos);

        return "destDs";*/
        JsonResultUtils.writeSingleDataJson(databaseInfos, response);
        
    }

    /**
     * 源字段
     *
     * @return
     */
//    @RequestMapping(value="/formField")
//    public void formField() {
//
//        return "formField";
// //        /page/dde/exportFieldNewForm.jsp 只是 跳转 页面 以后直接在页面里面跳转
//    }

    /**
     * 触发器
     *
     * @return
     */
//    @RequestMapping(value="/formTrigger")
//    public void formTrigger() {
//        return "formTrigger";
////        /page/dde/exportTriggerNewForm.jsp
//    }
    
    
    //这里 新框架 有下载的功能 调用就好  不需要单独写
    @RequestMapping(value="/exportMapinfoDetail")
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

        response.setHeader("Content-disposition", "attachment;filename=" + exportSql.getMapinfoName() + ".txt");//高速浏览器已下载的形式
        response.setContentType("application/txt");
        response.setHeader("Content_Length", String.valueOf(text.length()));

        IOUtils.copy(new StringReader(text), output);
    }
}
