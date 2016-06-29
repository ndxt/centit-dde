package com.centit.dde.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.web.util.WebUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.ExchangeTask;
import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.po.ExportSql;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.ExchangeTaskManager;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.dde.service.ExportSqlManager;
import com.centit.dde.service.MapinfoDetailManager;
import com.centit.dde.service.MapinfoTriggerManager;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.dde.service.TaskLogManager;
import com.centit.support.utils.HtmlFormUtils;
import com.centit.support.utils.StringRegularOpt;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.SysUserManager;
import com.centit.sys.util.SysParametersUtils;

public class ExchangeTaskAction extends BaseEntityDwzAction<ExchangeTask> {
    private static final Log log = LogFactory.getLog(ExchangeTaskAction.class);

    // private static final ISysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;

    private ExchangeTaskManager exchangeTaskMag;

    private SysUserManager sysUserMgr;

    private ExchangeMapinfoManager exchangeMapinfoManager;

    private ExchangeTaskdetailManager exchangeTaskdetailManager;

    private MapinfoDetailManager mapinfoDetailManager;

    private MapinfoTriggerManager mapinfoTriggerManager;

    private DatabaseInfoManager databaseInfoManager;

    private TaskErrorDataManager taskErrorDataManager;

    private TaskLogManager taskLogManager;

    private ExportSqlManager exportSqlManager;

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

    public TaskErrorDataManager getTaskErrorDataManager() {
        return taskErrorDataManager;
    }

    public void setTaskErrorDataManager(TaskErrorDataManager taskErrorDataManager) {
        this.taskErrorDataManager = taskErrorDataManager;
    }

    public TaskLogManager getTaskLogManager() {
        return taskLogManager;
    }

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    public DatabaseInfoManager getDatabaseInfoManager() {
        return databaseInfoManager;
    }

    public void setDatabaseInfoManager(DatabaseInfoManager databaseInfoManager) {
        this.databaseInfoManager = databaseInfoManager;
    }

    public MapinfoTriggerManager getMapinfoTriggerManager() {
        return mapinfoTriggerManager;
    }

    public void setMapinfoTriggerManager(MapinfoTriggerManager mapinfoTriggerManager) {
        this.mapinfoTriggerManager = mapinfoTriggerManager;
    }

    public MapinfoDetailManager getMapinfoDetailManager() {
        return mapinfoDetailManager;
    }

    public void setMapinfoDetailManager(MapinfoDetailManager mapinfoDetailManager) {
        this.mapinfoDetailManager = mapinfoDetailManager;
    }

    public ExchangeTaskdetailManager getExchangeTaskdetailManager() {
        return exchangeTaskdetailManager;
    }

    public void setExchangeTaskdetailManager(ExchangeTaskdetailManager exchangeTaskdetailManager) {
        this.exchangeTaskdetailManager = exchangeTaskdetailManager;
    }

    public ExchangeMapinfoManager getExchangeMapinfoManager() {
        return exchangeMapinfoManager;
    }

    public void setExchangeMapinfoManager(ExchangeMapinfoManager exchangeMapinfoManager) {
        this.exchangeMapinfoManager = exchangeMapinfoManager;
    }

    public void setExchangeTaskManager(ExchangeTaskManager basemgr) {
        exchangeTaskMag = basemgr;
        this.setBaseEntityManager(exchangeTaskMag);
    }

    public SysUserManager getSysUserMgr() {
        return sysUserMgr;
    }

    public void setSysUserMgr(SysUserManager sysUserMgr) {
        this.sysUserMgr = sysUserMgr;
    }

    private List<ExchangeTaskdetail> exchangeTaskdetails;

    public List<ExchangeTaskdetail> getNewExchangeTaskdetails() {
        return this.exchangeTaskdetails;
    }

    public void setNewExchangeTaskdetails(List<ExchangeTaskdetail> exchangeTaskdetails) {
        this.exchangeTaskdetails = exchangeTaskdetails;
    }

    private List<TaskLog> taskLogs;

    public List<TaskLog> getNewTaskLogs() {
        return this.taskLogs;
    }

    public void setNewTaskLogs(List<TaskLog> taskLogs) {
        this.taskLogs = taskLogs;
    }
    private String s_isvalid;
    @SuppressWarnings("unchecked")
    public String list() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            String orderField = request.getParameter("orderField");
            String orderDirection = request.getParameter("orderDirection");

            Map<String, Object> filterMap = convertSearchColumn(paramMap);

            if (!filterMap.containsKey("isvalid")) {
                filterMap.put("isvalid", "1");
            }
            //包含禁用
            if ("T".equals(filterMap.get("isvalid"))) {
                filterMap.remove("isvalid");
                filterMap.put("isvalid","1");
            }
            if ("0".equals(filterMap.get("isvalid"))) {
                filterMap.remove("isvalid");
            }
            s_isvalid =(String)filterMap.get("isvalid");
            if (null==s_isvalid)
                s_isvalid="0";
            if (!StringUtils.isBlank(orderField) && !StringUtils.isBlank(orderDirection)) {

                filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);

                // request.setAttribute("orderDirection", orderDirection);
                // request.setAttribute("orderField", orderField);
            }
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);

            for (int i = 0; i < objList.size(); i++) {
                ExchangeTask exchangeTask = objList.get(i);

                FUserinfo usesInfo = CodeRepositoryUtil.getUserInfoByCode(exchangeTask.getCreated());
                if (usesInfo != null)
                    exchangeTask.setCreatedName(usesInfo.getUsername());

            }

            setTaskTypeToPage();

            this.pageDesc = pageDesc;
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String save() {
        object.setTaskName(StringUtils.trim(object.getTaskName()));
        try {
            ExchangeTask dbObject = exchangeTaskMag.getObject(object);
            if (dbObject != null) {
                dbObject.copyNotNullProperty(object);
                // baseEntityManager.copyObjectNotNullProperty(dbObject,
                // object);
                object = dbObject;
            }

            // 判断是否为新增
            if (null == object.getTaskId() || 0 == object.getTaskId()) {
                Map<String, Object> filterMap = new HashMap<String, Object>();
                filterMap.put("taskNameEq", object.getTaskName());
                List<ExchangeTask> listObjects = exchangeTaskMag.listObjects(filterMap);
                if (!org.springframework.util.CollectionUtils.isEmpty(listObjects)) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, "任务名称已存在");
                    return SUCCESS;
                }


                object.setTaskId(exchangeTaskMag.getNewTaskId()); // Long.toString(System.currentTimeMillis()));
                object.setCreateTime(new Date());
                object.setIsvalid("1");
                // 获取登录用户
                FUserDetail uinfo = ((FUserDetail) getLoginUser());
                if (uinfo != null) {
                    object.setCreated(uinfo.getUsercode());
                }
                saveMessage("添加交换任务成功！");
            } else {
                saveMessage("编辑交换任务成功！");
            }
            if (StringUtils.isNotBlank(object.getTaskCron())) {
                exchangeTaskMag.saveNewTimerTask(object);
            }

            //更新下次执行时间
            if (StringUtils.isNotBlank(object.getTaskCron())) {
                try {
                    CronSequenceGenerator generator = new CronSequenceGenerator(object.getTaskCron(), TimeZone.getDefault());
                    object.setNextRunTime(generator.next(new Date()));
                } catch (Exception e) {
                    saveError("定时任务表达式不正确");

                    return ERROR;
                }
            }

            baseEntityManager.saveObject(object);

            // forward至列表页面
            dwzResultParam = new DwzResultParam("/dde/exchangeTask!list.do?s_taskType=" + object.getTaskType());

            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存交换对应关系："+ e.getMessage(), e);
            saveError("保存交换对应关系："+ e.getMessage());
            return ERROR;
        }
    }

    public String editAndsave() {
        try {
            object.setTaskName(StringUtils.trim(object.getTaskName()));

            Map<String, Object> filterMap = new HashMap<String, Object>();
            filterMap.put("taskNameEq", object.getTaskName());
            List<ExchangeTask> listObjects = exchangeTaskMag.listObjects(filterMap);
            if (!org.springframework.util.CollectionUtils.isEmpty(listObjects)) {
                if (1 < listObjects.size() || !listObjects.get(0).getTaskId().equals(object.getTaskId())) {
                    dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_300, "任务名称已存在");
                    return SUCCESS;
                }
            }


            // 判断taskCron字段是否修改，修改了为true
            boolean taskCron = false;
            ExchangeTask dbObject = baseEntityManager.getObject(object);
            if (dbObject.getTaskCron() != null && object.getTaskCron() != null) {
                if (!dbObject.getTaskCron().equals(object.getTaskCron())) {
                    taskCron = true;
                }
            } else if (dbObject.getTaskCron() == null && object.getTaskCron() != null) {
                taskCron = true;
            }
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }
            //更新下次执行时间
            if (StringUtils.isNotBlank(object.getTaskCron())) {
                try {
                    CronSequenceGenerator generator = new CronSequenceGenerator(object.getTaskCron(), TimeZone.getDefault());
                    object.setNextRunTime(generator.next(new Date()));
                } catch (Exception e) {
                    saveError("定时任务表达式不正确");

                    return "editAndsave";
                }
            } else {
                object.setNextRunTime(null);
            }

            saveMessage("编辑交换任务成功！");

            if (taskCron) {
                if (object.getTaskCron().equals("")) {
                    exchangeTaskMag.delTimerTask(object);
                } else {
                    exchangeTaskMag.updateTimerTask(object);
                }
            }


            baseEntityManager.saveObject(object);

            if ("3".equals(object.getTaskType())) {
                return "editAndsaveDialog";
            }
            dwzResultParam = new DwzResultParam("/dde/exchangeTask!list.do?s_taskType=" + object.getTaskType() + "&s_isvalid=1");
            dwzResultParam.setCallbackType("closeCurrent");
            return "editAndsave";
        } catch (Exception e) {
            log.error("编辑交换对应关系："+ e.getMessage(), e);
            saveError("编辑交换对应关系："+ e.getMessage());
            return ERROR;
        }
    }

    public String edit() {
        String view = EDIT;
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                ExchangeTask o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);

                // 是否为任务编辑，如果为任务编辑则添加创建人姓名
                if (null != object.getTaskId() && !"".equals(object.getTaskId())) {
                    // 调用sysconfig下的manager 通过id查询创建人姓名
                    FUserinfo usesInfo = CodeRepositoryUtil.getUserInfoByCode(o.getCreated());
                    if (usesInfo != null) {
                        object.setCreatedName(usesInfo.getUsername());
                    }

                    String taskType = WebUtils.findParameterValue(request, "s_taskType");

                    Map<String, Object> filterMap = new HashMap<String, Object>();
                    filterMap.put("task_Id", object.getTaskId());
                    // filterMap.put("order by", "mapinfo_order");
                    List<ExchangeTaskdetail> exchangeTaskdetails = exchangeTaskdetailManager.listObjects(filterMap);

                    if ("1".equals(taskType)) {
                        getExchangeMapinfo(exchangeTaskdetails);
                    } else if ("2".equals(taskType) || "4".equals(taskType)) {
                        getExportSql(exchangeTaskdetails);
                    } else if ("3".equals(taskType)) {
                        view = "editMonitor";
                    }

                }

            }

            setTaskTypeToPage();

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private void getExchangeMapinfo(List<ExchangeTaskdetail> exchangeTaskdetails) {
        List<ExchangeMapinfo> echangeMapInfoList = new ArrayList<ExchangeMapinfo>();

        for (ExchangeTaskdetail etd : exchangeTaskdetails) {
            ExchangeMapinfo exchangeMapinfo = exchangeMapinfoManager.getObjectById(etd.getMapinfoId());
            if (null != exchangeMapinfo) {
                exchangeMapinfo.setMapinfoOrder(etd.getMapinfoOrder());
                echangeMapInfoList.add(exchangeMapinfo);
            }
        }

        request.setAttribute("echangeMapInfoList", echangeMapInfoList);
    }

    private void getExportSql(List<ExchangeTaskdetail> exchangeTaskdetails) {
        List<ExportSql> exportSqlList = new ArrayList<ExportSql>();

        for (ExchangeTaskdetail etd : exchangeTaskdetails) {
            ExportSql exportSql = exportSqlManager.getObjectById(etd.getMapinfoId());
            if (null != exportSql) {
                exportSql.setExportsqlOrder(etd.getMapinfoOrder());
                exportSqlList.add(exportSql);
            }
        }

        request.setAttribute("exportSqlList", exportSqlList);
    }

    public String add() {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                ExchangeTask o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
                object.setTaskType(s_taskType);
            }
            return "add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 查看明细
     */
    public String view() {
        try {
            ExchangeTask o = baseEntityManager.getObject(object);
            if (object == null) {

                return LIST;
            }
            if (o != null)
                baseEntityManager.copyObject(object, o);

            // 是否为任务编辑，如果为任务编辑则添加创建人姓名
            if (null != object.getTaskId() && !"".equals(object.getTaskId())) {
                // 调用sysconfig下的manager 通过id查询创建人姓名
                FUserinfo usesInfo = CodeRepositoryUtil.getUserInfoByCode(o.getCreated());
                if (usesInfo != null) {
                    object.setCreatedName(usesInfo.getUsername());
                }
                // 查询数据交换关系列表
                String taskType = WebUtils.findParameterValue(request, "s_taskType");

                Map<String, Object> filterMap = new HashMap<String, Object>();
                filterMap.put("task_Id", object.getTaskId());
                // filterMap.put("order by", "mapinfo_order");
                List<ExchangeTaskdetail> exchangeTaskdetails = exchangeTaskdetailManager.listObjects(filterMap);

                if ("1".equals(taskType)) {
                    getExchangeMapinfo(exchangeTaskdetails);
                } else if ("2".equals(taskType)) {
                    getExportSql(exchangeTaskdetails);
                }
            }

            setTaskTypeToPage();

            return VIEW;
        } catch (Exception e) {
            log.error("查看交换对应关系："+ e.getMessage());
            return ERROR;
        }
    }

    public String delete() {
        try {
            exchangeTaskMag.delTimerTask(object);
            baseEntityManager.deleteObject(object);
            deletedMessage();
            return "delete";
        } catch (Exception e) {
            log.error("删除交换对应关系："+ e.getMessage(), e);
            saveError(e.getMessage());
            return ERROR;
        }

    }

    public String saveSequence() {
        // 返回页面
        String returnPage = request.getParameter("inputPage");

        String[] mapinfoIdAry = request.getParameterValues("mapinfoId");
        // String[] mapinfoOrderAry =
        // request.getParameterValues("mapinfoOrder");

        ExchangeTaskdetail exchangeTaskdetail;

        for (int i = 0; i < mapinfoIdAry.length; i++) {
            if (StringRegularOpt.isNumber(mapinfoIdAry[i])) {
                Long mapInfoId = Long.valueOf(mapinfoIdAry[i]);
                exchangeTaskdetail = new ExchangeTaskdetail();
                exchangeTaskdetail.setTaskId(object.getTaskId());
                exchangeTaskdetail.setMapinfoId(mapInfoId);
                exchangeTaskdetail.setMapinfoOrder(Long.valueOf(i + 1));

                exchangeTaskdetailManager.saveObject(exchangeTaskdetail);
            }

        }
        saveMessage("顺序保存成功！");

        if ("view".equals(returnPage)) {
            return view();
        } else if ("edit".equals(returnPage)) {
            return edit();
        } else {
            return ERROR;
        }

        // return returnPage;
    }

    @SuppressWarnings("unchecked")
    public String listExchangeMapInfo() {
        // List<String> mapinfoIds = new ArrayList<String>();
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);

        // filterMap.put("mapinfoIdNot", (String)filterMap.get("taskId"));
        filterMap.put("taskId", Long.valueOf(((String) filterMap.get("taskId"))));
        request.setAttribute("taskId", filterMap.get("taskId"));

        setTaskTypeToPage();

        String taskType = (String) filterMap.get("taskType");
        request.setAttribute("taskType", taskType);

        if ("1".equals(taskType)) {
            return listExchangeMapinfo(filterMap, pageDesc);
        }

        if ("2".equals(taskType) || "4".equals(taskType)) {
            return listExportSql(filterMap, pageDesc);
        }

        return null;
    }

    private String listExchangeMapinfo(Map<String, Object> filterMap, PageDesc pageDesc) {

        List<ExchangeMapinfo> exchangeMapinfos = exchangeMapinfoManager.listObjectExcludeUsed(filterMap, pageDesc);


        request.setAttribute("exchangeMapinfos", exchangeMapinfos);
        // ServletActionContext.getContext().put("taskId",
        // filterMap.get("taskId"));

        //List<Long> used = exchangeTaskdetailManager.getMapinfoIdUsed((Long) filterMap.get("taskId"));

        // 已存在的导出任务ID
        //request.setAttribute("used", used);


        this.pageDesc = pageDesc;

        return "listExchangeMapInfo";
    }

    /**
     * 导出离线文件
     *
     * @return
     */
    private String listExportSql(Map<String, Object> filterMap, PageDesc pageDesc) {
        List<ExportSql> exportSqls = exportSqlManager.listObjects(filterMap, pageDesc);

        List<Long> used = exchangeTaskdetailManager.getMapinfoIdUsed((Long) filterMap.get("taskId"));

        // 已存在的导出任务ID
        request.setAttribute("used", used);
        request.setAttribute("exportSqls", exportSqls);
        this.pageDesc = pageDesc;

        return "listExportSql";
    }

    @SuppressWarnings("unchecked")
    public String importExchangeMapinfo() {
        Map<Object, Object> paramMap = request.getParameterMap();

        String taskParamId = HtmlFormUtils.getParameterString(paramMap.get("taskId"));
        Long taskId = Long.valueOf(taskParamId);
        String[] str = (String[]) paramMap.get("ids");
        Set<Long> ids = new HashSet<Long>();
        for (String s : str) {
            if (NumberUtils.isNumber(s)) {
                ids.add(Long.parseLong(s));
            }
        }

        exchangeTaskdetailManager.saveObject(taskId, new ArrayList<Long>(ids));

        return "ImportExchangeMapinfo";
    }

    public String executeTask() {
        String sTaskId = request.getParameter("s_taskId");
        Long taskId = Long.valueOf(sTaskId);
        List<Long> mapinfoAndOrders = exchangeTaskdetailManager.getMapinfoIdUsed(taskId);
        for (int i = 0; i < mapinfoAndOrders.size(); i++) {
            executeMapinfo(mapinfoAndOrders.get(i), taskId);
        }
        return "executeTask";
    }

    private void executeMapinfo(Long mapinfoId, Long taskId) {
        ExchangeMapinfo exchangeMapinfo = this.exchangeMapinfoManager.getObjectById(Long.valueOf(mapinfoId));
        String sql = exchangeMapinfo.getQuerySql();
        DatabaseInfo databaseInfoSource = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo databaseInfoGoal = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());

		/*
         * String tableOperateSql =
		 * this.generateTableOperateSql(databaseInfoSource, exchangeMapinfo);
		 */

        List<List<Object>> datas = this.exchangeTaskMag.getSqlValues(databaseInfoSource, sql);
        String insertSql = generateInsertSql(mapinfoId);
        FUserinfo userDetails = (FUserinfo) this.getLoginUser();
        List<Object> logs = exchangeTaskMag.insertDatas(databaseInfoGoal, insertSql, datas);

        TaskLog TaskLog = this.generateTaskLog(logs, userDetails, taskId);
        taskLogManager.saveObject(TaskLog);

        this.saveTaskErrorData(logs, TaskLog.getLogId());

        if (Integer.valueOf((logs.get(2)).toString()) < datas.size()) {
            this.saveError("任务执行有误，请查看任务日志");
        } else {
            this.saveMessage("任务已经执行");
        }

    }

    /* 生成插入语句 */
    private String generateInsertSql(Long mapinfoId) {
        MapinfoDetail mapinfoDetail = mapinfoDetailManager.getObjectById(Long.valueOf(mapinfoId));
        ExchangeMapinfo exchangeMapinfo = this.exchangeMapinfoManager.getObjectById(Long.valueOf(mapinfoId));
        List<String> GoalColumnStrut = this.mapinfoDetailManager.getGoalColumnStrut(mapinfoId);
        StringBuffer sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(exchangeMapinfo.getDestTablename());
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
    private TaskLog generateTaskLog(List<Object> logs, FUserinfo userDetails, Long taskId) {
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogManager.getTaskLogId());
        taskLog.setTaskId(taskId);
        taskLog.setRunBeginTime((Date) logs.get(0));
        taskLog.setRunEndTime((Date) logs.get(1));
        taskLog.setRunType("1");
        taskLog.setRunner(userDetails.getUsercode());
        /*
         * taskLog.setSuccessPieces(Long.valueOf((logs.get(2)).toString()));
		 * taskLog.setErrorPieces(Long.valueOf((logs.get(3)).toString()));
		 */
        return taskLog;
    }

    /* 添加错误日志记录 */
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
	 * databaseInfoSource,ExchangeMapinfo exchangeMapinfo){
	 * List<Map<String,String>> sourceTableStrutsDatas=
	 * mapinfoDetailManager.getSourceTableStruct(databaseInfoSource,
	 * exchangeMapinfo.getSourceTablename()); StringBuffer tableOperateSql = new
	 * StringBuffer(); if(exchangeMapinfo.getTableOperate().equals("2")){
	 * tableOperateSql
	 * .append("create table "+exchangeMapinfo.getSourceTablename(
	 * )+" if not exists;");
	 * tableOperateSql.append("create table "+exchangeMapinfo
	 * .getSourceTablename()+"("); }else{
	 * 
	 * } return tableOperateSql.toString(); }
	 */

    private String generateCreateTableSql(List<Map<String, String>> sourceTableStrutsDatas, String sourceTablename) {
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

    public String generateCronExpression() {

        return "cronExpression";
    }


    /**
     * 实时交换数据控制台信息
     *
     * @return
     */
    public String console() {

        return "console";
    }

    /**
     * 将任务类型设置至页面，多个页面均要使用
     */
    void setTaskTypeToPage() {
        Map<String, String> taskType = new HashMap<String, String>();
        taskType.put("1", "直接交换");
        taskType.put("2", "导出离线文件");
        taskType.put("3", "监控文件夹导入文件");
        taskType.put("4", "调用接口");
        taskType.put("5", "接口事件");

        request.setAttribute("taskTypeList", taskType);

        request.setAttribute("consoleOutPut", "1".equals(SysParametersUtils.getValue("task.console.output")));
    }

    public String getS_isvalid() {
        return s_isvalid;
    }

    public void setS_isvalid(String s_isvalid) {
        this.s_isvalid = s_isvalid;
    }

  

    
}
