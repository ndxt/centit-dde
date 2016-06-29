package com.centit.dde.action;

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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.struts2.ServletActionContext;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.ExchangeTask;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.ExchangeTaskManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.dde.service.ImportOptManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.sys.util.SysParametersUtils;

public class TaskLogAction extends BaseEntityDwzAction<TaskLog> {
    private static final Log log = LogFactory.getLog(TaskLogAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private TaskLogManager taskLogMag;
    private ExchangeTaskManager exchangeTaskManager;
    private ExportSqlManager exportSqlManager;
    private ImportOptManager importOptManager;
    public ExportSqlManager getExportSqlManager() {
        return exportSqlManager;
    }

    public void setExportSqlManager(ExportSqlManager exportSqlManager) {
        this.exportSqlManager = exportSqlManager;
    }


    private String s_taskType;


    public String getS_taskType() {
        return s_taskType;
    }

    public void setS_taskType(String s_taskType) {
        this.s_taskType = s_taskType;
    }

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

    public ExchangeTaskManager getExchangeTaskManager() {
        return exchangeTaskManager;
    }

    public void setExchangeTaskManager(ExchangeTaskManager exchangeTaskManager) {
        this.exchangeTaskManager = exchangeTaskManager;
    }

    public void setTaskLogManager(TaskLogManager basemgr) {
        taskLogMag = basemgr;
        this.setBaseEntityManager(taskLogMag);
    }
 private String fileName;
public String downFile(){
    String export = SysParametersUtils.getValue("export");
    String logname ="";
    try {
         logname = new String(request.getParameter("logname").getBytes("iso-8859-1"), "utf-8");
    } catch (UnsupportedEncodingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    String path = export+"/"+ logname+"/"
    +logname+request.getParameter("logid")+".zip";
    File file = new File(path);

    try {
        inputSteam  = FileUtils.openInputStream(file);
        if(request.getHeader("USER-AGENT").toLowerCase().indexOf( "msie" ) > 0){
            logname = new String(logname.getBytes("gb2312"), "iso-8859-1");                
        }else{
            logname = new String(logname.getBytes("UTF-8"), "iso-8859-1");
        }
        fileName = logname+request.getParameter("logid")+".zip";
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return "download";
}
    @Override
    public String edit() {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                TaskLog o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }
            for (TaskDetailLog taskDetailLog : object.getTaskDetailLogs()) {
                if (object.getTaskType().equals("1")){
                taskDetailLog.setMapinfoName(exchangeTaskManager.getMapinfoName(taskDetailLog.getMapinfoId()));
                }
                if (object.getTaskType().equals("2")){
                    taskDetailLog.setMapinfoName(exportSqlManager.getMapinfoName(taskDetailLog.getMapinfoId()));
                    }
                if (object.getTaskType().equals("3")){
                    taskDetailLog.setMapinfoName(importOptManager.getMapinfoName(taskDetailLog.getMapinfoId()));
                    }
            }
            if (object.getTaskId() != null) {
                ServletActionContext.getContext().put("exchangeTask", exchangeTaskManager.getObjectById(object.getTaskId()));
            }
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }
    
    public String list() {

//	    super.list();
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        if (!StringUtils.isBlank(orderField) && !StringUtils.isBlank(orderDirection)) {

            filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);

//         request.setAttribute("orderDirection", orderDirection);
//         request.setAttribute("orderField", orderField);
        }
        PageDesc pageDesc = makePageDesc();

        filterMap.put("taskId", Long.parseLong(String.valueOf(filterMap.get("taskId"))));

        objList = baseEntityManager.listObjects(filterMap, pageDesc);
        totalRows = pageDesc.getTotalRows();

        this.pageDesc = pageDesc;


        ExchangeTask exchangeTask = exchangeTaskManager.getObjectById((Long) filterMap.get("taskId"));
        request.setAttribute("exchangeTask", exchangeTask);
        return LIST;
    }
    public String listall() throws ParseException {

//      super.list();
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        filterMap.remove("isvalid");
        if ("0".equals(filterMap.get("isError"))) {
            filterMap.put("isError",new Long(0));
        }
        filterMap.put(CodeBook.SELF_ORDER_BY,  "run_begin_time desc");
        String s="";
        if (filterMap.containsKey("runBeginTime")) {
            s=(String) filterMap.get("runBeginTime");
            if (!"".equals(s)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");            
            Date t=sdf.parse(s);
            filterMap.put("runBeginTime", t);
            } 
            else {filterMap.remove("runBeginTime");}
        }
        String s2="";
        if (filterMap.containsKey("runBeginTime2")) {
            s2=(String) filterMap.get("runBeginTime2");
            if (!"".equals(s2)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");            
            Date t=sdf.parse(s2+" 23:59:59");
            filterMap.put("runBeginTime2", t);
            } 
            else {filterMap.remove("runBeginTime2");}
        }
        PageDesc pageDesc = makePageDesc();

        

        objList = baseEntityManager.listObjects(filterMap, pageDesc);
        totalRows = pageDesc.getTotalRows();
        exchangeTasklist = exchangeTaskManager.listObjects();
        if (!"".equals(s)){
            filterMap.put("runBeginTime", s);
          }
        if (!"".equals(s2)){
            filterMap.put("runBeginTime2", s2);
          }
        setbackSearchColumn(filterMap);
        
        this.pageDesc = pageDesc;
        return "listall";
    }
    @SuppressWarnings("unchecked")
    public String listStat(){        
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);
        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        filterMap.remove("isvalid");
        if (filterMap.containsKey("isMonth")){
            String s=String.format("%02d", Integer.parseInt((String)filterMap.get("month")));
            s = (String)filterMap.get("year")+s;
            taskLogStat = taskLogMag.taskLogStat("month", s);  
        }else {
            taskLogStat = taskLogMag.taskLogStat("year", filterMap.get("year"));
        }
        setbackSearchColumn(filterMap);
        return "listStat";
    }
    public String save() {
        object.replaceTaskDetailLogs(taskDetailLogs);

        return super.save();
    }


    public String delete() {
        super.delete();

        return "delete";
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
