package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * 交换任务
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXCHANGE_TASK")
public class ExchangeTask implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="TASK_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long taskId;

    @Column(name="TASK_NAME")
    private String taskName;
    
    @Column(name="TASK_CRON")
    private String taskCron;

    @Column(name="TASK_DESC")
    private String taskDesc;

    @Column(name="LAST_RUN_TIME")
    private Date lastRunTime;
    
    @Column(name="NEXT_RUN_TIME")
    private Date nextRunTime;
    
    @Column(name="IS_VALID")
    private String isvalid;
    
    @Column(name="CREATE_TIME")
    private Date createTime;
    
    @Column(name="CREATED")
    private String created;
    
    @Transient
    private String createdName;
    /**
     * '1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5:接口事件';
     */
    @Column(name="TASK_TYPE")
    private String taskType;// char(1) default '1'  not null

    @Column(name="LAST_UPDATE_TIME")
    private Date lastUpdateTime;//     date

    @Column(name="STORE_ISOLATION")
    private String storeIsolation;//      char(1)

    @Column(name="MONITOR_FOLDER")
    private String monitorFolder;//       varchar2(200);

    @Transient
    private Set<ExchangeTaskDetail> exchangeTaskDetails = null;// new
    // ArrayList<ExchangeTaskDetail>();
    
    @Transient
    private List<ExportSql> exportSqlList = null;
    
    @Transient
    private List<ExchangeMapInfo> exchangeMapInfoList = null;
    
    public List<ExchangeMapInfo> getExchangeMapInfoList() {
        return exchangeMapInfoList;
    }

    public void setExchangeMapInfoList(List<ExchangeMapInfo> exchangeMapInfoList) {
        this.exchangeMapInfoList = exchangeMapInfoList;
    }

    public List<ExportSql> getExportSqlList() {
        return exportSqlList;
    }

    public void setExportSqlList(List<ExportSql> exportSqlList) {
        this.exportSqlList = exportSqlList;
    }

//    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY )
//    @JoinColumn(name="TASK_ID") //这里表示数据库的外键 在t_street里面创建
    @Transient
    private List<TaskLog> taskLogs ;// new ArrayList<TaskLog>();

//    fetch:表示抓取策略,默认为FetchType.LAZY,因为关联的多个对象通常不必从数据库预先读取到内存
//    cascade:表示级联操作策略,对于OneToMany类型的关联非常重要,通常该实体更新或删除时,其关联的实体也应当被更新删除 
    
    // Constructors

    /**
     * default constructor
     */
    public ExchangeTask() {
    }

    /**
     * minimal constructor
     */
    public ExchangeTask(Long taskId, String taskName, String taskCron) {

        this.taskId = taskId;

        this.taskName = taskName;
        this.taskCron = taskCron;
    }

    /**
     * full constructor
     */
    public ExchangeTask(Long taskId, String taskName, String taskCron,
                        String taskDesc, Date lastRunTime, Date nextRunTime,
                        String isValid, Date createTime, String created, String taskType,
                        Date lastUpdateTime, String storeIsolation, String monitorFolder) {

        this.taskId = taskId;

        this.taskType = taskType;
        this.taskName = taskName;
        this.taskCron = taskCron;
        this.taskDesc = taskDesc;
        this.lastRunTime = lastRunTime;
        this.nextRunTime = nextRunTime;
        this.isvalid = isValid;
        this.createTime = createTime;
        this.created = created;

        this.lastUpdateTime = lastUpdateTime;
        this.storeIsolation = storeIsolation;
        this.monitorFolder = monitorFolder;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    // Property accessors

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * '1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5:接口事件';
     *
     * @return
     */
    public String getTaskType() {
        return taskType;
    }

    /**
     * '1: 直接交换 2 :导出离线文件 3：监控文件夹导入文件 4：调用接口 5:接口事件';
     *
     * @param taskType
     */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskCron() {
        return this.taskCron;
    }

    public void setTaskCron(String taskCron) {
        this.taskCron = taskCron;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Date getLastRunTime() {
        return this.lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public Date getNextRunTime() {
        return this.nextRunTime;
    }

    public void setNextRunTime(Date nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public String getIsvalid() {
        return this.isvalid;
    }

    public void setIsvalid(String isValid) {
        this.isvalid = isValid;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lasUpdateTime) {
        this.lastUpdateTime = lasUpdateTime;
    }

    public String getStoreIsolation() {
        return storeIsolation;
    }

    public void setStoreIsolation(String storeIsolation) {
        this.storeIsolation = storeIsolation;
    }

    public String getMonitorFolder() {
        return monitorFolder;
    }

    public void setMonitorFolder(String monitorFolder) {
        this.monitorFolder = monitorFolder;
    }

    public Set<ExchangeTaskDetail> getExchangeTaskDetails() {
        if (this.exchangeTaskDetails == null)
            this.exchangeTaskDetails = new HashSet<ExchangeTaskDetail>();
        return this.exchangeTaskDetails;
    }

    public void setExchangeTaskDetails(
            Set<ExchangeTaskDetail> exchangeTaskDetails) {
        this.exchangeTaskDetails = exchangeTaskDetails;
    }

    public void addExchangeTaskdetail(ExchangeTaskDetail exchangeTaskDetail) {
        if (this.exchangeTaskDetails == null)
            this.exchangeTaskDetails = new HashSet<ExchangeTaskDetail>();
        this.exchangeTaskDetails.add(exchangeTaskDetail);
    }

    public void removeExchangeTaskdetail(ExchangeTaskDetail exchangeTaskDetail) {
        if (this.exchangeTaskDetails == null)
            return;
        this.exchangeTaskDetails.remove(exchangeTaskDetail);
    }

    public ExchangeTaskDetail newExchangeTaskdetail() {
        ExchangeTaskDetail res = new ExchangeTaskDetail();

        res.setTaskId(this.getTaskId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不�?��的问�?
     */
    public void replaceExchangeTaskdetails(
            List<ExchangeTaskDetail> exchangeTaskDetails) {
        List<ExchangeTaskDetail> newObjs = new ArrayList<ExchangeTaskDetail>();
        for (ExchangeTaskDetail p : exchangeTaskDetails) {
            if (p == null)
                continue;
            ExchangeTaskDetail newdt = newExchangeTaskdetail();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<ExchangeTaskDetail> oldObjs = new HashSet<ExchangeTaskDetail>();
        oldObjs.addAll(getExchangeTaskDetails());

        for (Iterator<ExchangeTaskDetail> it = oldObjs.iterator(); it.hasNext(); ) {
            ExchangeTaskDetail odt = it.next();
            found = false;
            for (ExchangeTaskDetail newdt : newObjs) {
                if (odt.getCid().equals(newdt.getCid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeExchangeTaskdetail(odt);
        }
        oldObjs.clear();
        // insert or update
        for (ExchangeTaskDetail newdt : newObjs) {
            found = false;
            for (Iterator<ExchangeTaskDetail> it = getExchangeTaskDetails()
                    .iterator(); it.hasNext(); ) {
                ExchangeTaskDetail odt = it.next();
                if (odt.getCid().equals(newdt.getCid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addExchangeTaskdetail(newdt);
        }
    }

    public List<TaskLog> getTaskLogs() {
        if (taskLogs == null)
            taskLogs = new ArrayList<TaskLog>();
        return taskLogs;
    }

    public void setTaskLogs(List<TaskLog> taskLogs) {
        this.taskLogs = taskLogs;
    }

    public void addTaskLog(TaskLog taskLog) {
        if (this.taskLogs == null)
            this.taskLogs = new ArrayList<TaskLog>();
        this.taskLogs.add(taskLog);
    }
    
    public void removeTaskLog(TaskLog taskLog) {
        if (this.taskLogs == null)
            return;
        this.taskLogs.remove(taskLog);
    }

    public TaskLog newTaskLog() {
        TaskLog res = new TaskLog();
        res.setTaskId(taskId);
        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不�?��的问�?
     */
    public void replaceTaskLogs(List<TaskLog> taskLogs) {
        List<TaskLog> newObjs = new ArrayList<TaskLog>();
        for (TaskLog p : taskLogs) {
            if (p == null)
                continue;
            TaskLog newdt = newTaskLog();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<TaskLog> oldObjs = new HashSet<TaskLog>();
        oldObjs.addAll(getTaskLogs());

        for (Iterator<TaskLog> it = oldObjs.iterator(); it.hasNext(); ) {
            TaskLog odt = it.next();
            found = false;
            for (TaskLog newdt : newObjs) {
                if (odt.getLogId().equals(newdt.getLogId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeTaskLog(odt);
        }
        oldObjs.clear();
        // insert or update
        for (TaskLog newdt : newObjs) {
            found = false;
            for (Iterator<TaskLog> it = getTaskLogs().iterator(); it.hasNext(); ) {
                TaskLog odt = it.next();
                if (odt.getLogId().equals(newdt.getLogId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addTaskLog(newdt);
        }
    }

    public void copy(ExchangeTask other) {

        this.setTaskId(other.getTaskId());

        this.taskName = other.getTaskName();
        this.taskCron = other.getTaskCron();
        this.taskDesc = other.getTaskDesc();
        this.lastRunTime = other.getLastRunTime();
        this.nextRunTime = other.getNextRunTime();
        this.isvalid = other.getIsvalid();
        this.createTime = other.getCreateTime();
        this.created = other.getCreated();

        this.exchangeTaskDetails = other.getExchangeTaskDetails();
        this.taskLogs = other.getTaskLogs();

        this.lastUpdateTime = other.getLastUpdateTime();
        this.storeIsolation = other.getStoreIsolation();
        this.monitorFolder = other.getMonitorFolder();

    }

    public void copyNotNullProperty(ExchangeTask other) {

        if (other.getTaskId() != null)
            this.setTaskId(other.getTaskId());

        if (other.getTaskName() != null)
            this.taskName = other.getTaskName();
        if (other.getTaskCron() != null)
            this.taskCron = other.getTaskCron();
        if (other.getTaskDesc() != null)
            this.taskDesc = other.getTaskDesc();
        if (other.getLastRunTime() != null)
            this.lastRunTime = other.getLastRunTime();
        if (other.getNextRunTime() != null)
            this.nextRunTime = other.getNextRunTime();
        if (other.getIsvalid() != null)
            this.isvalid = other.getIsvalid();
        if (other.getCreateTime() != null)
            this.createTime = other.getCreateTime();
        if (other.getCreated() != null)
            this.created = other.getCreated();

        if (other.getLastUpdateTime() != null)
            this.lastUpdateTime = other.getLastUpdateTime();
        if (other.getStoreIsolation() != null)
            this.storeIsolation = other.getStoreIsolation();
        if (other.getMonitorFolder() != null)
            this.monitorFolder = other.getMonitorFolder();

        this.taskLogs = other.getTaskLogs();
        this.exchangeTaskDetails = other.getExchangeTaskDetails();
    }

    public void clearProperties() {

        this.taskName = null;
        this.taskCron = null;
        this.taskDesc = null;
        this.lastRunTime = null;
        this.nextRunTime = null;
        this.isvalid = null;
        this.createTime = null;
        this.created = null;
        this.lastUpdateTime = null;
        this.storeIsolation = null;
        this.monitorFolder = null;

        this.exchangeTaskDetails = new HashSet<ExchangeTaskDetail>();
        this.taskLogs = new ArrayList<TaskLog>();
    }
    public void addAll(){
        
    }
    
//    public void addAll(List<TaskLog> taskLogs){
//        getTaskLogs().clear();
//        if(CollectionUtils.isEmpty(taskLogs)){
//            return;
//        }
//        for(TaskLog detail:taskLogs){
//            detail.setTask(this);
//        }
//        getTaskLogs().addAll(taskLogs);
//        
//    }
}
