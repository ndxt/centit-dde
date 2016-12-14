package com.centit.dde.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.dde.po.ExchangeTask;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.ExchangeTaskManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.dde.service.ImportOptManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.framework.common.SysParametersUtils;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.core.dao.PageDesc;
import com.sun.istack.Nullable;

@Controller
@RequestMapping(name = "tasklog")
public class TaskLogController extends BaseController {
    private static final Log log = LogFactory.getLog(TaskLogController.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    @Resource
    @Nullable
    private TaskLogManager taskLogMag;
    @Resource
    @Nullable
    private ExchangeTaskManager exchangeTaskManager;
    @Resource
    @Nullable
    private ExportSqlManager exportSqlManager;
    @Resource
    @Nullable
    private ImportOptManager importOptManager;

    private String s_taskType;

    private InputStream inputSteam;
    private List<TaskDetailLog> taskDetailLogs;
    private List<ExchangeTask> exchangeTasklist;
    private List<String[]> taskLogStat;

    public List<TaskDetailLog> getTaskDetailLogs() {
        return taskDetailLogs;
    }

    public void setTaskDetailLogs(List<TaskDetailLog> taskDetailLogs) {
        this.taskDetailLogs = taskDetailLogs;
    }

    private String fileName;

    @RequestMapping(value = "/downFile", method = { RequestMethod.GET })
    public String downFile(HttpServletRequest request,
            HttpServletResponse response) {
        String export = SysParametersUtils.getStringValue("export");
        String logname = "";
        try {
            logname = new String(request.getParameter("logname").getBytes(
                    "iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String path = export + "/" + logname + "/" + logname
                + request.getParameter("logid") + ".zip";
        File file = new File(path);

        try {
            inputSteam = FileUtils.openInputStream(file);
            if (request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0) {
                logname = new String(logname.getBytes("gb2312"), "iso-8859-1");
            } else {
                logname = new String(logname.getBytes("UTF-8"), "iso-8859-1");
            }
            fileName = logname + request.getParameter("logid") + ".zip";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "download";
    }

    @RequestMapping(value = "/edit", method = { RequestMethod.GET })
    public void edit(TaskLog object,HttpServletRequest request, HttpServletResponse response) {
        try {
            if (object == null) {
//                object = getEntityClass().newInstance();
            } else {
                TaskLog o = taskLogMag.getObjectById(object.getLogId());
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    taskLogMag.copyObject(object, o);
                else
                    taskLogMag.clearObjectProperties(object);
            }
            for (TaskDetailLog taskDetailLog : object.getTaskDetailLogs()) {
                if (object.getTaskType().equals("1")) {
                    taskDetailLog.setMapinfoName(exchangeTaskManager
                            .getMapinfoName(taskDetailLog.getMapinfoId()));
                }
                if (object.getTaskType().equals("2")) {
                    taskDetailLog.setMapinfoName(exportSqlManager
                            .getMapinfoName(taskDetailLog.getMapinfoId()));
                }
                if (object.getTaskType().equals("3")) {
                    taskDetailLog.setMapinfoName(importOptManager
                            .getMapinfoName(taskDetailLog.getMapinfoId()));
                }
            }
            if (object.getTaskId() != null) {
                ServletActionContext.getContext().put("exchangeTask",
                        exchangeTaskManager.getObjectById(object.getTaskId()));
            }
            // return EDIT;
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }
    @RequestMapping(value = "/list", method = { RequestMethod.GET })
    public void list(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response) {
        // super.list();
        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);

        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        if (!orderField.equals(null)
                && !orderDirection.equals(null)) {

            searchColumn.put(CodeBook.SELF_ORDER_BY, orderField + " "
                    + orderDirection);

            // request.setAttribute("orderDirection", orderDirection);
            // request.setAttribute("orderField", orderField);
        }

        searchColumn.put("taskId",
                Long.parseLong(String.valueOf(searchColumn.get("taskId"))));

        List<TaskLog> objList = taskLogMag.listObjects(searchColumn, pageDesc);
//        totalRows = pageDesc.getTotalRows();
//
//        this.pageDesc = pageDesc;

        ExchangeTask exchangeTask = exchangeTaskManager
                .getObjectById((Long) searchColumn.get("taskId"));
        // request.setAttribute("exchangeTask", exchangeTask);
        // return LIST;
        ResponseData resData = new ResponseData();
        resData.addResponseData("OBJLIST", objList);
        resData.addResponseData("PAGE_DESC", pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value = "/listall", method = { RequestMethod.PUT })
    public void listall(PageDesc pageDesc,HttpServletRequest request, HttpServletResponse response)
            throws ParseException {

        // super.list();
//        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);
//
//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.remove("isvalid");
        if ("0".equals(searchColumn.get("isError"))) {
            searchColumn.put("isError", new Long(0));
        }
        searchColumn.put(CodeBook.SELF_ORDER_BY, "run_begin_time desc");
        String s = "";
        if (searchColumn.containsKey("runBeginTime")) {
            s = (String) searchColumn.get("runBeginTime");
            if (!"".equals(s)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date t = sdf.parse(s);
                searchColumn.put("runBeginTime", t);
            } else {
                searchColumn.remove("runBeginTime");
            }
        }
        String s2 = "";
        if (searchColumn.containsKey("runBeginTime2")) {
            s2 = (String) searchColumn.get("runBeginTime2");
            if (!"".equals(s2)) {
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Date t = sdf.parse(s2 + " 23:59:59");
                searchColumn.put("runBeginTime2", t);
            } else {
                searchColumn.remove("runBeginTime2");
            }
        }
//        PageDesc pageDesc = makePageDesc();
        List<TaskLog> objList = taskLogMag.listObjects(searchColumn, pageDesc);
//        totalRows = pageDesc.getTotalRows();
        exchangeTasklist = exchangeTaskManager.listObjects();
        if (!"".equals(s)) {
            searchColumn.put("runBeginTime", s);
        }
        if (!"".equals(s2)) {
            searchColumn.put("runBeginTime2", s2);
        }
//        setbackSearchColumn(searchColumn);

//        this.pageDesc = pageDesc;
        // return "listall";
        ResponseData resData = new ResponseData();
        resData.addResponseData("OBJLIST", objList);
        resData.addResponseData("PAGE_DESC", pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/listStat", method = { RequestMethod.GET })
    public void listStat(HttpServletRequest request,
            HttpServletResponse response) {
//        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);
//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.remove("isvalid");
        if (searchColumn.containsKey("isMonth")) {
            String s = String.format("%02d",
                    Integer.parseInt((String) searchColumn.get("month")));
            s = (String) searchColumn.get("year") + s;
            taskLogStat = taskLogMag.taskLogStat("month", s);
        } else {
            taskLogStat = taskLogMag.taskLogStat("year", searchColumn.get("year"));
        }
//        setbackSearchColumn(searchColumn);
        // return "listStat";
        JsonResultUtils.writeSingleDataJson(taskLogStat, response);
    }

    @RequestMapping(value = "/save", method = { RequestMethod.PUT })
    public void save(TaskLog object,HttpServletRequest request, HttpServletResponse response) {
        object.replaceTaskDetailLogs(taskDetailLogs);
        taskLogMag.saveObject(object);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value = "/delete", method = { RequestMethod.DELETE })
    public void delete(TaskLog object,HttpServletRequest request,
            HttpServletResponse response) {
        taskLogMag.deleteObjectById(object.getLogId());

        // return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }

    public List<ExchangeTask> getExchangeTasklist() {
        return exchangeTasklist;
    }

    public void setExchangeTasklist(List<ExchangeTask> exchangeTasklist) {
        this.exchangeTasklist = exchangeTasklist;
    }

    public ImportOptManager getImportOptManager() {
        return importOptManager;
    }

    public void setImportOptManager(ImportOptManager importOptManager) {
        this.importOptManager = importOptManager;
    }

    public List<String[]> getTaskLogStat() {
        return taskLogStat;
    }

    public void setTaskLogStat(List<String[]> taskLogStat) {
        this.taskLogStat = taskLogStat;
    }

    public InputStream getInputSteam() {
        return inputSteam;
    }

    public void setInputSteam(InputStream inputSteam) {
        this.inputSteam = inputSteam;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
