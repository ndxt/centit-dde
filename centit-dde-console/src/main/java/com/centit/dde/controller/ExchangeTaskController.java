package com.centit.dde.controller;

import com.centit.dde.po.*;
import com.centit.dde.service.*;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.staticsystem.po.UserInfo;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/exchangetask")
public class ExchangeTaskController extends BaseController {
    private static final Log log = LogFactory.getLog(ExchangeTaskController.class);

    private static final long serialVersionUID = 1L;

    @Resource
    private ExchangeTaskManager exchangeTaskMag;

    @Resource
    private ExchangeMapInfoManager exchangeMapInfoManager;

    @Resource
    private ExchangeTaskdetailManager exchangeTaskdetailManager;

    @Resource
    private MapInfoDetailManager mapInfoDetailManager;

    @Resource
    private IntegrationEnvironment integrationEnvironment;

    @Resource
    private TaskErrorDataManager taskErrorDataManager;

    @Resource
    private TaskLogManager taskLogManager;

    @Resource
    private ExportSqlManager exportSqlManager;

    @RequestMapping(value="/list/{taskType}" , method = {RequestMethod.GET})
    public void list(@PathVariable String taskType ,PageDesc pageDesc, HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        searchColumn.put("taskType", taskType);
        if(searchColumn.get("isvalid")!=null){
            searchColumn.remove("isvalid");
        }else{
            searchColumn.put("isvalid", "1");
        }
        List<ExchangeTask>  objList = exchangeTaskMag.listObjects(searchColumn);//pageDesc

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/save", method = {RequestMethod.PUT})
    public void save(ExchangeTask object ,HttpServletRequest request ,HttpServletResponse response) {
        CentitUserDetails user = getLoginUser(request);
        exchangeTaskMag.save(object, user);

        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/editAndsave" , method = {RequestMethod.PUT})
    public void editAndsave(@Valid  ExchangeTask object,HttpServletRequest request,HttpServletResponse response) {
        try {
            object.setTaskName(object.getTaskName().trim());
            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("taskName", object.getTaskName());
            List<ExchangeTask> listObjects = exchangeTaskMag.listObjects(filterMap);
            if (!org.springframework.util.CollectionUtils.isEmpty(listObjects)) {
                if (1 < listObjects.size() || !listObjects.get(0).getTaskId().equals(object.getTaskId())) {
                    JsonResultUtils.writeSuccessJson(response);
                    return;
                }
            }

            // 判断taskCron字段是否修改，修改了为true
            boolean taskCron = false;
            ExchangeTask dbObject = exchangeTaskMag.getObjectById(object.getTaskId());
            if (dbObject.getTaskCron() != null && object.getTaskCron() != null) {
                if (!dbObject.getTaskCron().equals(object.getTaskCron())) {
                    taskCron = true;
                }
            } else if (dbObject.getTaskCron() == null && object.getTaskCron() != null) {
                taskCron = true;
            }
            if (dbObject != null) {
                dbObject.copyNotNullProperty(object);

//                BeanUtils.copyProperties(object, dbObject, new String[] { "taskLogs" });
//                dbObject.addAll(object.getTaskLogs());
            }
            //更新下次执行时间
            if (dbObject.getTaskCron() != null && !"".equals(object.getTaskCron())) {
                try {
                    CronSequenceGenerator generator = new CronSequenceGenerator(dbObject.getTaskCron(), TimeZone.getDefault());
                    dbObject.setNextRunTime(generator.next(new Date()));
                } catch (Exception e) {
//                    saveError("定时任务表达式不正确");
//                    return "editAndsave";
                    JsonResultUtils.writeBlankJson(response);
                    return;
                }
            } else {
                dbObject.setNextRunTime(null);
            }

//            saveMessage("编辑交换任务成功！");

            if (taskCron) {
                if (dbObject.getTaskCron().equals("")) {
                    exchangeTaskMag.delTimerTask(dbObject);
                } else {
                    exchangeTaskMag.updateTimerTask(dbObject);
                }
            }
//            object.getTaskLogs().clear();
//            object.getTaskLogs().add(e);
            //exchangeTaskMag.updateObject(dbObject);
            exchangeTaskMag.editAndsave(dbObject);


            if ("3".equals(object.getTaskType())) {
//                return "editAndsaveDialog";
                JsonResultUtils.writeBlankJson(response);
            }
//            dwzResultParam = new DwzResultParam("/dde/exchangeTask!list.do?s_taskType=" + object.getTaskType() + "&s_isvalid=1");
//            dwzResultParam.setCallbackType("closeCurrent");
//            return "editAndsave";
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error("编辑交换对应关系："+ e.getMessage(), e);
//            saveError("编辑交换对应关系："+ e.getMessage());
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("XXXX", response);
        }
    }

    @RequestMapping(value="/edit/{taskId}/{taskType}/{ExchangeIds}" , method = {RequestMethod.GET})
    public  void edit(@PathVariable Long taskId,@PathVariable String taskType,@PathVariable String ExchangeIds, HttpServletResponse response) {
        ExchangeTask exchangeTask = exchangeTaskMag.getObjectById(taskId);
        if(exchangeTask==null)
            exchangeTask =new ExchangeTask();
        //Map<String, Object> filterMap = new HashMap<>();
        //filterMap.put("task_Id", taskId);
        List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailManager.getTaskDetails(taskId);

        List<ExportSql> exportSqlList = new ArrayList<>();

        List<ExchangeMapInfo> exchangeMapInfoList = new ArrayList<>();

        if (taskType.equals("1")) {
            for (ExchangeTaskDetail etd : exchangeTaskDetails) {
                ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(etd.getMapInfoId());
                if (null != exchangeMapInfo) {
                    exchangeMapInfo.setMapInfoOrder(etd.getMapInfoOrder());
                    exchangeMapInfoList.add(exchangeMapInfo);
                }
            }
            String[] str = ExchangeIds.split(",");
            for (String s : str) {
                if (NumberUtils.isCreatable(s)) {
                    if (exchangeTaskDetails.size() > 0) {
                        for (int i = 0; i < exchangeTaskDetails.size(); i++) {
                            if (Long.valueOf(s) != exchangeTaskDetails.get(i).getMapInfoId()) {
                                ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.valueOf(s));
                                if (null != exchangeMapInfo) {
                                    exchangeMapInfoList.add(exchangeMapInfo);
                                }
                            }
                        }
                    } else {
                        ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.valueOf(s));
                        if (null != exchangeMapInfo) {
                            exchangeMapInfoList.add(exchangeMapInfo);
                        }
                    }
                }
            }
            exchangeTask.setExchangeMapInfoList(exchangeMapInfoList);
        } else {
            if (exchangeTaskDetails.size() > 0) {
                for (ExchangeTaskDetail etd : exchangeTaskDetails) {
                    ExportSql exportSql = exportSqlManager.getObjectById(etd.getMapInfoId());
                    if (null != exportSql) {
                        exportSql.setExportsqlOrder(etd.getMapInfoOrder());
                        exportSqlList.add(exportSql);
                    }
                }
            }
            String[] str = ExchangeIds.split(",");
            for (String s : str) {
                if (NumberUtils.isCreatable(s)) {
                    if (exchangeTaskDetails.size() > 0) {
                        for (int i = 0; i < exchangeTaskDetails.size(); i++) {
                            if (Long.valueOf(s) != exchangeTaskDetails.get(i).getMapInfoId()) {
                                ExportSql exportSql = exportSqlManager.getObjectById(Long.valueOf(s));
                                if (null != exportSql) {
                                    exportSqlList.add(exportSql);
                                }
                            }
                        }
                    } else {
                        ExportSql exportSql = exportSqlManager.getObjectById(Long.valueOf(s));
                        if (null != exportSql) {
                            exportSqlList.add(exportSql);
                        }
                    }
                }
            }
            exchangeTask.setExportSqlList(exportSqlList);
        }

        UserInfo usesInfo = (UserInfo)CodeRepositoryUtil.getUserInfoByCode(exchangeTask.getCreated());
        if (usesInfo != null){
            exchangeTask.setCreatedName(usesInfo.getUserName());
        }
        JsonResultUtils.writeSingleDataJson(exchangeTask, response);
    }

    @RequestMapping(value="/getExchangeMapinfo" , method = {RequestMethod.GET})
    private void getExchangeMapinfo(List<ExchangeTaskDetail> exchangeTaskDetails, HttpServletResponse response) {
        List<ExchangeMapInfo> echangeMapInfoList = new ArrayList<>();

        for (ExchangeTaskDetail etd : exchangeTaskDetails) {
            ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(etd.getMapInfoId());
            if (null != exchangeMapInfo) {
                exchangeMapInfo.setMapInfoOrder(etd.getMapInfoOrder());
                echangeMapInfoList.add(exchangeMapInfo);
            }
        }

        JsonResultUtils.writeSingleDataJson(echangeMapInfoList, response);
    }

    @RequestMapping(value="/getExportSql", method = {RequestMethod.GET})
    private void getExportSql(List<ExchangeTaskDetail> exchangeTaskDetails, HttpServletResponse response) {
        List<ExportSql> exportSqlList = new ArrayList<>();

        for (ExchangeTaskDetail etd : exchangeTaskDetails) {
            ExportSql exportSql = exportSqlManager.getObjectById(etd.getMapInfoId());
            if (null != exportSql) {
                exportSql.setExportsqlOrder(etd.getMapInfoOrder());
                exportSqlList.add(exportSql);
            }
        }

        JsonResultUtils.writeSingleDataJson(exportSqlList, response);
    }

    @RequestMapping(value="/add/{taskId}", method = {RequestMethod.PUT})
    public void add(@PathVariable Long taskId,ExchangeTask object, HttpServletRequest request,HttpServletResponse response) {
        try {
            String s_taskType =request.getParameter("s_taskType");
            if (object == null) {
//                object = getEntityClass().newInstance();
            } else {
                ExchangeTask o = exchangeTaskMag.getObjectById(taskId);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    object.copy(o);

                else
                    object.clearProperties();
                    object.setTaskType(s_taskType);
            }
//            return "add";
//            /page/dde/addExchangeTaskForm.jsp
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    /**
     * 查看明细
     */
    @RequestMapping(value="/view/{taskId}" , method = {RequestMethod.GET})
    public void view(PageDesc pageDesc,ExchangeTask object,@PathVariable Long taskId,HttpServletRequest request,HttpServletResponse response) {
        try {
            ExchangeTask o = exchangeTaskMag.getObjectById(taskId);
            Map<String, Object> searchColumn = convertSearchColumn(request);
            List<ExchangeTask> objList = exchangeTaskMag.listObjects(searchColumn);
            if (object == null) {
//                /page/dde/mapinfoTriggerList.jsp
//                return LIST;
                
                ResponseMapData resData = new ResponseMapData();
                resData.addResponseData("OBJLIST", objList);
                resData.addResponseData("PAGE_DESC", pageDesc);
                JsonResultUtils.writeResponseDataAsJson(resData, response);
            }
            if (o != null)
                object.copy(o);

            // 是否为任务编辑，如果为任务编辑则添加创建人姓名
            if (null != object.getTaskId() && !"".equals(object.getTaskId())) {
                // 调用sysconfig下的manager 通过id查询创建人姓名
                UserInfo usesInfo = (UserInfo)CodeRepositoryUtil.getUserInfoByCode(o.getCreated());
                if (usesInfo != null) {
                    object.setCreatedName(usesInfo.getUserName());
                }
                // 查询数据交换关系列表
                String taskType = WebUtils.findParameterValue(request, "s_taskType");

                Map<String, Object> filterMap = new HashMap<String, Object>();
                filterMap.put("task_Id", object.getTaskId());
                // filterMap.put("order by", "mapinfo_order");
                List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailManager.listObjects(filterMap);
                JsonResultUtils.writeSingleDataJson(exchangeTaskDetails, response);
                
                if ("1".equals(taskType)) {
                    getExchangeMapinfo(exchangeTaskDetails, response);
                } else if ("2".equals(taskType)) {
                    getExportSql(exchangeTaskDetails, response);
                }
            }

            setTaskTypeToPage(request);

            /*return VIEW;*/
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error("查看交换对应关系："+ e.getMessage());
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/delete/{taskId}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long taskId,ExchangeTask object,HttpServletRequest request,HttpServletResponse response) {
        //ExchangeTask exchangeTask = exchangeTaskMag.getObjectById(taskId);
        try {
            /*Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("taskId", taskId);
            List<TaskLog> TaskLogList = taskLogManager.listObjects(filterMap);
            if(TaskLogList!=null){
                for(TaskLog o : TaskLogList){
                    taskLogManager.deleteObject(o);
                }
            }
            List<ExchangeTaskDetail> exchangeTaskDetailList =exchangeTaskdetailManager.listObjects(filterMap);
            if(exchangeTaskDetailList !=null){
                for(ExchangeTaskDetail o : exchangeTaskDetailList){
                    exchangeTaskdetailManager.deleteObject(o);
                }
            }
            exchangeTaskMag.delTimerTask(exchangeTask);
            exchangeTaskMag.deleteObjectById(taskId);*/
//            deletedMessage();
//            return "delete";
            exchangeTaskMag.deleteObjectByIdInfo(taskId);
            JsonResultUtils.writeSuccessJson(response);
        } catch (Exception e) {
            log.error("删除交换对应关系："+ e.getMessage(), e);
//            saveError(e.getMessage());
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }

    }

    @RequestMapping(value="/saveSequence/{taskId}" , method = {RequestMethod.PUT})
    public void saveSequence(@PathVariable Long taskId, ExchangeTask object,HttpServletRequest request,HttpServletResponse response) {
        // 返回页面
        String returnPage = request.getParameter("inputPage");

        String[] mapinfoIdAry = request.getParameterValues("mapinfoId");
        // String[] mapinfoOrderAry =
        // request.getParameterValues("mapinfoOrder");

        ExchangeTaskDetail exchangeTaskDetail;

        for (int i = 0; i < mapinfoIdAry.length; i++) {
            if (StringRegularOpt.isNumber(mapinfoIdAry[i])) {
                Long mapInfoId = Long.valueOf(mapinfoIdAry[i]);
                exchangeTaskDetail = new ExchangeTaskDetail();
                exchangeTaskDetail.setTaskId(object.getTaskId());
                exchangeTaskDetail.setMapInfoId(mapInfoId);
                exchangeTaskDetail.setMapInfoOrder(Long.valueOf(i + 1));

                exchangeTaskdetailManager.saveNewObject(exchangeTaskDetail);
            }

        }
//        saveMessage("顺序保存成功！");
        JsonResultUtils.writeErrorMessageJson("error", response);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/listExchangeMapInfo/{taskType}/{taskId}/{exportId}", method = {RequestMethod.GET})
    public void listExchangeMapInfo(@PathVariable String taskType,@PathVariable Long taskId,@PathVariable String exportId,
                                    PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        
        //做一个判断为了 前面 有的list就不要了
        /*Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("task_Id", taskId);
        List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailManager.listObjects(filterMap);*/
        List<ExportSql> exportSqlList = new ArrayList<ExportSql>();
        /*for (ExchangeTaskDetail etd : exchangeTaskDetails) {
            ExportSql exportSql = exportSqlManager.getObjectById(etd.getMapInfoId());
            if (null != exportSql) {
                exportSql.setExportsqlOrder(etd.getMapInfoOrder());
                exportSqlList.add(exportSql);
            }
        }*/
        String[] str = exportId.split(",");
        for (String s : str) {
            if (NumberUtils.isCreatable(s)) {
                ExportSql exportSql = exportSqlManager.getObjectById(Long.valueOf(s));
                if (null != exportSql) {
                    exportSqlList.add(exportSql);
                }
            }
        }
        //为了 去除重复项
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<ExportSql> objList = exportSqlManager.listObjects(searchColumn);
        for(ExportSql sql : exportSqlList ){
            for(int i=0;i< objList.size();i++ ){
                if(sql.getExportId() == objList.get(i).getExportId()){
                    objList.remove(i);
                }
            }
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    
    @RequestMapping(value="/ExchangeMapInfolist/{taskType}/{taskId}/{ExchangeIds}", method = {RequestMethod.GET})
    public void ExchangeMapInfolist(@PathVariable String taskType,@PathVariable Long taskId,@PathVariable String ExchangeIds,
                                    PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        
        //做一个判断为了 前面 有的list就不要了
/*        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("taskId", taskId);
        List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailManager.listObjects(filterMap);*/
        List<ExchangeMapInfo> exchangeMapInfoList = new ArrayList<ExchangeMapInfo>();
        List<ExportSql> exportSqlList = new ArrayList<ExportSql>();
        /*for (ExchangeTaskDetail etd : exchangeTaskDetails) {
            ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(etd.getMapInfoId());
            if (null != exchangeMapInfo) {
                exchangeMapInfo.setMapInfoOrder(etd.getMapInfoOrder());
                exchangeMapInfoList.add(exchangeMapInfo);
            }
        }*/
        String[] str = ExchangeIds.split(",");
        for (String s : str) {
            if (NumberUtils.isCreatable(s)) {
                ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.valueOf(s));
                if (null != exchangeMapInfo) {
                    exchangeMapInfoList.add(exchangeMapInfo);
                }
            }
        }
        //为了 去除重复项
        Map<String, Object> searchColumn = convertSearchColumn(request);
        List<ExchangeMapInfo> objList = exchangeMapInfoManager.listObjects(searchColumn);
        for(ExchangeMapInfo map : exchangeMapInfoList){
            for(int i=0;i< objList.size();i++ ){
                if(map.getMapInfoId() == objList.get(i).getMapInfoId()){
                    objList.remove(i);
                }
            }
        }
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData(OBJLIST, objList);
        resData.addResponseData(PAGE_DESC, pageDesc);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/listExchangeMapinfo", method = {RequestMethod.GET})
    private void listExchangeMapinfo(Map<String, Object> filterMap, PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {

        List<ExchangeMapInfo> exchangeMapInfos = exchangeMapInfoManager.listObjectExcludeUsed(filterMap, pageDesc);


//        request.setAttribute("exchangeMapInfos", exchangeMapInfos);
        // ServletActionContext.getContext().put("taskId",
        // filterMap.get("taskId"));

        //List<Long> used = exchangeTaskdetailManager.getMapinfoIdUsed((Long) filterMap.get("taskId"));

        // 已存在的导出任务ID
        //request.setAttribute("used", used);


//        this.pageDesc = pageDesc;

//        return "listExchangeMapInfo";
        JsonResultUtils.writeSingleDataJson(exchangeMapInfos, response);
    }

    /**
     * 导出离线文件
     *
     * @return
     */
    @RequestMapping(value="/listExportSql", method = {RequestMethod.GET})
    private void listExportSql(Map<String, Object> filterMap, PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        List<ExportSql> exportSqls = exportSqlManager.listObjects(filterMap);

        List<Long> used = exchangeTaskdetailManager.getMapinfoIdUsed((Long) filterMap.get("taskId"));

        // 已存在的导出任务ID
       /* request.setAttribute("used", used);
        request.setAttribute("exportSqls", exportSqls);*/
//        this.pageDesc = pageDesc;

//        return "listExportSql";
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("exportSqls", exportSqls);
        resData.addResponseData("used", used);
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/importExchangeMapinfo/{ExchangeIds}/{taskId}", method = {RequestMethod.PUT})
    public void importExchangeMapinfo(@PathVariable String ExchangeIds,@PathVariable Long taskId,HttpServletRequest request,HttpServletResponse response) {
//        Map<String, String[]> paramMap = request.getParameterMap();
        
        //MapinfoOrder的判定
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("task_Id", taskId);
        Long mapinfoOrder = (long) 0;
        List<ExchangeTaskDetail> exchangeTaskDetails = exchangeTaskdetailManager.listObjects(filterMap);
        if(exchangeTaskDetails.size() == 0){
            mapinfoOrder = (long) 1;
        }else if(exchangeTaskDetails.size() == 1){
            mapinfoOrder = exchangeTaskDetails.get(0).getMapInfoOrder();
        }else{
            for(int i = 1; i< exchangeTaskDetails.size(); i++){
                 if(exchangeTaskDetails.get(i-1).getMapInfoOrder() <= exchangeTaskDetails.get(i).getMapInfoOrder()){
                     mapinfoOrder = exchangeTaskDetails.get(i).getMapInfoOrder() + 1;
                 }else{
                     mapinfoOrder = exchangeTaskDetails.get(i-1).getMapInfoOrder() + 1;
                 }
            }
        }
        

        String[] str = ExchangeIds.split(",");
        for (String s : str) {
            if (NumberUtils.isCreatable(s)) {
                ExchangeTaskDetail excTD = new ExchangeTaskDetail();
                excTD.setMapInfoId(Long.parseLong(s));
                excTD.setTaskId(taskId);
                excTD.setMapInfoOrder(mapinfoOrder);
                //exchangeTaskdetailManager.saveNewObject(excTD);
                mapinfoOrder =mapinfoOrder + 1;//mapinfoOrder自增长
            }
        }
        
//        exchangeTaskdetailManager.saveObject(taskId, new ArrayList<Long>(ids));

//        return "ImportExchangeMapinfo"; 
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/executeTask", method = {RequestMethod.GET})
    public void executeTask(HttpServletRequest request,HttpServletResponse response) {
        String sTaskId = request.getParameter("s_taskId");
        Long taskId = Long.valueOf(sTaskId);
        List<Long> mapinfoAndOrders = exchangeTaskdetailManager.getMapinfoIdUsed(taskId);
        for (int i = 0; i < mapinfoAndOrders.size(); i++) {
            executeMapinfo(mapinfoAndOrders.get(i), taskId, request, response);
        }
//        return "executeTask";
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/executeMapinfo", method = {RequestMethod.GET})
    private void executeMapinfo(Long mapinfoId, Long taskId,HttpServletRequest request,HttpServletResponse response) {
        ExchangeMapInfo exchangeMapInfo = this.exchangeMapInfoManager.getObjectById(Long.valueOf(mapinfoId));
        String sql = exchangeMapInfo.getQuerySql();
        DatabaseInfo databaseInfoSource = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        DatabaseInfo databaseInfoGoal = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());

		/*
         * String tableOperateSql =
		 * this.generateTableOperateSql(databaseInfoSource, exchangeMapInfo);
		 */

        List<List<Object>> datas = this.exchangeTaskMag.getSqlValues(databaseInfoSource, sql);
        String insertSql = generateInsertSql(mapinfoId);
        CentitUserDetails userDetails = getLoginUser(request);
        List<Object> logs = exchangeTaskMag.insertDatas(databaseInfoGoal, insertSql, datas);

        TaskLog TaskLog = this.generateTaskLog(logs, userDetails.getUserCode(), taskId);
        taskLogManager.saveNewObject(TaskLog);

        this.saveTaskErrorData(logs, TaskLog.getLogId());

        if (Integer.valueOf((logs.get(2)).toString()) < datas.size()) {
//            this.saveError("任务执行有误，请查看任务日志");
        } else {
//            this.saveMessage("任务已经执行");
        }

    }

    /* 生成插入语句 */
    @RequestMapping(value="/generateInsertSql", method = {RequestMethod.GET})
    private String generateInsertSql(Long mapinfoId) {
//        MapInfoDetail mapinfoDetail = mapInfoDetailManager.getObjectById(Long.valueOf(mapinfoId));方法需要重新写
        ExchangeMapInfo exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.valueOf(mapinfoId));
        List<String> GoalColumnStrut = mapInfoDetailManager.getGoalColumnStrut(mapinfoId);
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(exchangeMapInfo.getDestTableName());
        sql.append(" (");
        String GoalColumnStrutSql = "";
        String values = "";
        for (int i = 0; i < GoalColumnStrut.size(); i++) {
            if (i < GoalColumnStrut.size() - 1) {
                GoalColumnStrutSql = GoalColumnStrutSql + GoalColumnStrut.get(i) + ",";
                values = values + "?,";
            } else if (i == GoalColumnStrut.size() - 1) {
                GoalColumnStrutSql = GoalColumnStrutSql + GoalColumnStrut.get(i);
                values = values + "?";
            }
        }
        sql.append(GoalColumnStrutSql);
        sql.append(") values(");
        sql.append(values);
        sql.append(")");
        return sql.toString();
    }

    /* 生成日志记录 */
    private TaskLog generateTaskLog(List<Object> logs, String usercode, Long taskId) {
        ExchangeTask task = exchangeTaskMag.getObjectById(taskId);
        TaskLog taskLog = new TaskLog();
//        taskLog.setTask(task);
        taskLog.setTaskId(taskId);
        taskLog.setLogId(taskLogManager.getTaskLogId());
        //askLog.getTaskId().setTaskId(taskId);
        taskLog.setRunBeginTime((Date) logs.get(0));
        taskLog.setRunEndTime((Date) logs.get(1));
        taskLog.setRunType("1");
        taskLog.setRunner(usercode);
        /*
         * taskLog.setSuccessPieces(Long.valueOf((logs.get(2)).toString()));
		 * taskLog.setErrorPieces(Long.valueOf((logs.get(3)).toString()));
		 */
        return taskLog;
    }

    /* 添加错误日志记录 */
    @RequestMapping(value="/saveTaskErrorData")
    private void saveTaskErrorData(List<Object> logs, Long logId) {
        List<Object> alogs = (List<Object>) logs.get(4);
        for (int i = 0; i < alogs.size(); i++) {
            if (!((List) alogs.get(i)).isEmpty()) {
                TaskErrorData taskErrorData = new TaskErrorData();
                taskErrorData.setDataId(taskErrorDataManager.getTaskErrorId());
                taskErrorData.setErrorMessage((String) ((List) alogs.get(i)).get(0));
				/* taskErrorData.setLogId(logId); */
                taskErrorData.setDataContent(((List) alogs.get(i)).get(1).toString());
                taskErrorDataManager.saveTaskErrorData(taskErrorData);
            }
        }
    }

	/*
	 * 生成表操作记录语句 private String generateTableOperateSql(DatabaseInfo
	 * databaseInfoSource,ExchangeMapInfo exchangeMapinfo){
	 * List<Map<String,String>> sourceTableStrutsDatas=
	 * mapInfoDetailManager.getSourceTableStruct(databaseInfoSource,
	 * exchangeMapinfo.getSourceTableName()); StringBuffer tableOperateSql = new
	 * StringBuffer(); if(exchangeMapinfo.getTableOperate().equals("2")){
	 * tableOperateSql
	 * .append("create table "+exchangeMapinfo.getSourceTableName(
	 * )+" if not exists;");
	 * tableOperateSql.append("create table "+exchangeMapinfo
	 * .getSourceTableName()+"("); }else{
	 * 
	 * } return tableOperateSql.toString(); }
	 */

    @RequestMapping(value="/generateCreateTableSql")
    private String generateCreateTableSql(List<Map<String, String>> sourceTableStrutsDatas, String sourceTablename,HttpServletRequest request,HttpServletResponse response) {
        String generateCreateTableSql = "create table" + sourceTablename + "(";
        for (int i = 0; i < sourceTableStrutsDatas.size(); i++) {
            generateCreateTableSql = generateCreateTableSql + sourceTableStrutsDatas.get(i).get("COLUMNNAME") + " ";
            generateCreateTableSql = generateCreateTableSql + sourceTableStrutsDatas.get(i).get("COLUMNTYPE") + " ";
            if (sourceTableStrutsDatas.get(i).get("ISNULLABLE").equals("1")) {

            }
            if (sourceTableStrutsDatas.get(i).get("COLUMNTYPE").equals("1")) {
                generateCreateTableSql = generateCreateTableSql + " ";
            }
        }
       return null;
    }
//页面之间的跳转
//    @RequestMapping(value="/generateCronExpression")
//    public String generateCronExpression() {
//
//        return "cronExpression";
//    } 


    /**
     * 实时交换数据控制台信息
     *
     * @return
     */
//    @RequestMapping(value="/console",method = {RequestMethod.GET})
//    public String console() {
//
//        return "console";
////        /page/dde/exchangeTaskConsole.jsp 页面之间跳转
//    }

    /**
     * 将任务类型设置至页面，多个页面均要使用
     */
    @RequestMapping(value="/setTaskTypeToPage")
    private void setTaskTypeToPage(HttpServletRequest request) {
        Map<String, String> taskType = new HashMap<>();
        taskType.put("1", "直接交换");
        taskType.put("2", "导出离线文件");
        taskType.put("3", "监控文件夹导入文件");
        taskType.put("4", "调用接口");
        taskType.put("5", "接口事件");

        request.setAttribute("taskTypeList", taskType);

    //    request.setAttribute("consoleOutPut", "1".equals(SysParametersUtils.getValue("task.console.output")));
    }

    
}
