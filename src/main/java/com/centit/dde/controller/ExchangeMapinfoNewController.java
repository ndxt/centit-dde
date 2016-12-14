package com.centit.dde.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.sun.istack.Nullable;

@Controller
@RequestMapping("/exchangemapinfonew")
public class ExchangeMapinfoNewController extends BaseController {
    private static final Log log = LogFactory.getLog(ExchangeMapinfoNewController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    @Resource
    @Nullable
    private ExchangeMapinfoManager exchangeMapinfoMag;

    @Resource
    @Nullable
    private ExchangeTaskdetailManager exchangeTaskdetailManager;

    @Resource
    @Nullable
    private DatabaseInfoManager databaseInfoManager;


    @SuppressWarnings("unchecked")
    @RequestMapping(value="/list" ,method = {RequestMethod.GET})
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        try {
//            Map<Object, Object> paramMap = request.getParameterMap();
////            resetPageParam(paramMap);
//
//            Map<String, Object> filterMap = convertSearchColumn(paramMap);
//            pageDesc = DwzTableUtils.makePageDesc(request);
            Map<String, Object> searchColumn = convertSearchColumn(request);
            List<ExchangeMapinfo> objList = exchangeMapinfoMag.listObjects(searchColumn, pageDesc);
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

    @RequestMapping(value="/showMapinfoDetail/{{mapinfoId}}" ,method = {RequestMethod.GET})
    public void showMapinfoDetail(@PathVariable Long mapinfoId ,ExchangeMapinfo object, HttpServletRequest request,HttpServletResponse response) {
        object = exchangeMapinfoMag.getObjectById(mapinfoId);
        JsonResultUtils.writeSingleDataJson(object, response);
        /*return "showMapinfoDetail";*/
    }
    
    @RequestMapping(value="/add" ,method = {RequestMethod.GET})
    public void add(ExchangeMapinfo object,HttpServletRequest request,HttpServletResponse response) {
        Long mapinfoId = object.getMapinfoId();
        try {
            if (object == null) {
//              object = getEntityClass().newInstance();
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
    public void copy(ExchangeMapinfo object,HttpServletResponse response) {
        object = exchangeMapinfoMag.getObjectById(object.getMapinfoId());
        object.setMapinfoId(null);
        JsonResultUtils.writeSingleDataJson(object, response);
        /*return "showMapinfoDetail";*/
    }

    @RequestMapping(value="/edit/{{mapinfoId}}",method = {RequestMethod.GET})
    public void edit(@PathVariable Long mapinfoId,ExchangeMapinfo object,HttpServletRequest request,HttpServletResponse response) {
        try {
            if (object == null) {
//              object = getEntityClass().newInstance();
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

    @RequestMapping(value="/save/{{mapinfoId}}",method = {RequestMethod.PUT})
    public void save(@PathVariable Long mapinfoId,ExchangeMapinfo object, HttpServletRequest request,HttpServletResponse response) {
//        dwzResultParam = new DwzResultParam();
        // dwzResultParam.setNavTabId(tabid);
        if (object.getMapinfoName().equals(null)) {
//            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
//            dwzResultParam.setMessage("交换名称未填写");

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

        object.setMapinfoName(object.getMapinfoName().trim());

        //判断交换名称的唯一性
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mapinfoNameEq", object.getMapinfoName());

        List<ExchangeMapinfo> listObjects = exchangeMapinfoMag.listObjects(filterMap);


        if (!CollectionUtils.isEmpty(listObjects)) {
            ExchangeMapinfo exchangeMapinfoDb = exchangeMapinfoMag.getObjectById(mapinfoId);
            String message = "交换名称已存在";
            if (null == exchangeMapinfoDb) {
//                dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                return SUCCESS;
                JsonResultUtils.writeSuccessJson(response);
                return;
            } else {
                if (1 < listObjects.size() || !exchangeMapinfoDb.getMapinfoId().equals(listObjects.get(0)
                        .getMapinfoId())) {
//                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, message);

//                    return SUCCESS;
                    JsonResultUtils.writeSuccessJson(response);
                    return;
                }
            }
        }


        try {
            exchangeMapinfoMag.validator(object);

            exchangeMapinfoMag.saveObject(object, getLoginUser(request));
        } catch (SqlResolveException e) {
//            dwzResultParam.setStatusCode(DwzResultParam.STATUS_CODE_300);
            String message = null;
            if (0 == e.getErrorcode()) {
                message = e.getMessage();
            } else {
                message = SysParametersUtils.getStringValue(String.valueOf(e.getErrorcode()));
            }

//            dwzResultParam.setMessage(message);

//            return SUCCESS;
            JsonResultUtils.writeSuccessJson(response);
            return;
        }

//        dwzResultParam.setForwardUrl("/dde/exchangeMapinfoNew!list.do");
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/delete/{{mapinfoId}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long mapinfoId, HttpServletRequest request,HttpServletResponse response) {

        exchangeMapinfoMag.deleteObjectById(mapinfoId);
        exchangeTaskdetailManager.deleteDetailsByMapinfoId(mapinfoId);
//        deletedMessage();

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
    public void exportMapinfoDetail(ExchangeMapinfo object) throws IOException {
        HttpServletResponse response = ServletActionContext.getResponse();
        ServletOutputStream output = response.getOutputStream();
        ExchangeMapinfo exportSql = exchangeMapinfoMag.getObjectById(object.getMapinfoId());

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
