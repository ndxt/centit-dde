package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * create by scaffold
 * <p/>
 * TaskLog,TaskDetailLog,TaskErrorData 的关系。
 * tasklog：任务日志
 * 每个交换任务的一次运行 会在tasklog中插入一条记录
 * 每个导入任务的会在tasklog中插入一条记录
 * 每个webService接收一条数据会插入一条记录，由于WebService不是定时任务，所以taskID为固定的值 2
 * TaskDetailLog ： 任务日志明细
 * 每个交换任务的一个对应关系会在     TaskDetailLog 插入一条记录
 * 每个导入任务的一个表的操作 会在 TaskDetailLog 插入一条记录
 * 每个webService接收数据的一个操作 会在 TaskDetailLog 插入一条记录
 * <p/>
 * 人工运行或者测试对用关系，或则导出操作是 也会插入一条记录，这是因为没有对应的taskID 所已经taskID的默认值社会自为1
 * TaskErrorData ：操作错误是数据信息
 * 每个交换操作错误时的数据记录
 * 导入数据操作时的数据记录
 * webService接收数据操作时的数据记录
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_TASK_LOG")
public class TaskLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="LOG_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long logId;

    @Column(name="TASK_ID")
    private Long taskId;
    @Column(name="RUN_BEGIN_TIME")
    private Date runBeginTime;
    @Column(name="RUN_END_TIME")
    private Date runEndTime;
    @Column(name="RUN_TYPE")
    private String runType;
    @Column(name="RUNNER")
    private String runner;
    @Column(name="OTHER_MESSAGE")
    private String otherMessage;
    @Column(name="TASK_TYPE")
    private String taskType;
    @Transient
    private Set<TaskDetailLog> taskDetailLogs = null;// new ArrayList<TaskDetailLog>();

    // Constructors

    /**
     * default constructor
     */
    public TaskLog() {
    }

    /**
     * minimal constructor
     */
    public TaskLog(
            Long logId
    ) {


        this.logId = logId;

    }

    /**
     * full constructor
     */
    public TaskLog(
            Long logId
            , Long taskId, Date runBeginTime, 
            Date runEndTime, String runType, 
            String runner, String otherMessage,String taskType) {


        this.logId = logId;

        this.taskId = taskId;
        this.runBeginTime = runBeginTime;
        this.runEndTime = runEndTime;
        this.runType = runType;
        this.runner = runner;
        this.otherMessage = otherMessage;
        this.taskType=taskType;
    }


    public Long getLogId() {
        return this.logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }
    // Property accessors

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getRunBeginTime() {
        return this.runBeginTime;
    }

    public void setRunBeginTime(Date runBeginTime) {
        this.runBeginTime = runBeginTime;
    }

    public Date getRunEndTime() {
        return this.runEndTime;
    }

    public void setRunEndTime(Date runEndTime) {
        this.runEndTime = runEndTime;
    }

    /**
     * 1:手动 0：系统自动 2:WebService接口
     *
     * @return
     */
    public String getRunType() {
        return this.runType;
    }

    /**
     * @param runType 1:手动 0：系统自动 2:WebService接口
     */
    public void setRunType(String runType) {
        this.runType = runType;
    }

    public String getRunner() {
        return this.runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public String getOtherMessage() {
        return this.otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }


    public Set<TaskDetailLog> getTaskDetailLogs() {
        if (this.taskDetailLogs == null)
            this.taskDetailLogs = new HashSet<TaskDetailLog>();
        return this.taskDetailLogs;
    }

    public void setTaskDetailLogs(Set<TaskDetailLog> taskDetailLogs) {
        this.taskDetailLogs = taskDetailLogs;
    }

    public void addTaskDetailLog(TaskDetailLog taskDetailLog) {
        if (this.taskDetailLogs == null)
            this.taskDetailLogs = new HashSet<TaskDetailLog>();
        this.taskDetailLogs.add(taskDetailLog);
    }

    public void removeTaskDetailLog(TaskDetailLog taskDetailLog) {
        if (this.taskDetailLogs == null)
            return;
        this.taskDetailLogs.remove(taskDetailLog);
    }

    public TaskDetailLog newTaskDetailLog() {
        TaskDetailLog res = new TaskDetailLog();

        res.setLogId(this.getLogId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceTaskDetailLogs(List<TaskDetailLog> taskDetailLogs) {
        List<TaskDetailLog> newObjs = new ArrayList<TaskDetailLog>();
        for (TaskDetailLog p : taskDetailLogs) {
            if (p == null)
                continue;
            TaskDetailLog newdt = newTaskDetailLog();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        //delete
        boolean found = false;
        Set<TaskDetailLog> oldObjs = new HashSet<TaskDetailLog>();
        oldObjs.addAll(getTaskDetailLogs());

        for (Iterator<TaskDetailLog> it = oldObjs.iterator(); it.hasNext(); ) {
            TaskDetailLog odt = it.next();
            found = false;
            for (TaskDetailLog newdt : newObjs) {
                if (odt.getLogDetailId().equals(newdt.getLogDetailId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeTaskDetailLog(odt);
        }
        oldObjs.clear();
        //insert or update
        for (TaskDetailLog newdt : newObjs) {
            found = false;
            for (Iterator<TaskDetailLog> it = getTaskDetailLogs().iterator();
                 it.hasNext(); ) {
                TaskDetailLog odt = it.next();
                if (odt.getLogDetailId().equals(newdt.getLogDetailId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addTaskDetailLog(newdt);
        }
    }


    public void copy(TaskLog other) {

        this.setLogId(other.getLogId());

        this.taskId = other.getTaskId();
        this.runBeginTime = other.getRunBeginTime();
        this.runEndTime = other.getRunEndTime();
        this.runType = other.getRunType();
        this.runner = other.getRunner();
        this.otherMessage = other.getOtherMessage();
        this.taskType= other.getTaskType();
        this.taskDetailLogs = other.getTaskDetailLogs();
    }

    public void copyNotNullProperty(TaskLog other) {

        if (other.getLogId() != null)
            this.setLogId(other.getLogId());

        if (other.getTaskId() != null)
            this.taskId = other.getTaskId();
        if (other.getRunBeginTime() != null)
            this.runBeginTime = other.getRunBeginTime();
        if (other.getRunEndTime() != null)
            this.runEndTime = other.getRunEndTime();
        if (other.getRunType() != null)
            this.runType = other.getRunType();
        if (other.getRunner() != null)
            this.runner = other.getRunner();
        if (other.getOtherMessage() != null)
            this.otherMessage = other.getOtherMessage();
        if (other.getTaskType() != null)
            this.taskType = other.getTaskType();
        this.taskDetailLogs = other.getTaskDetailLogs();
    }

    public void clearProperties() {

        this.taskId = null;
        this.runBeginTime = null;
        this.runEndTime = null;
        this.runType = null;
        this.runner = null;
        this.otherMessage = null;
        this.taskType = null;
        this.taskDetailLogs = new HashSet<TaskDetailLog>();
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
}
