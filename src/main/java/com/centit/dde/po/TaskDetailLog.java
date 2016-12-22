package com.centit.dde.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_TASK_DETAIL_LOG")
public class TaskDetailLog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="LOGDETAILID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long logDetailId;

    @Column(name="LOGID")
    private Long logId;
    
    @Column(name="LOGID")
    private Date runBeginTime;
    
    @Column(name="RUNENDTIME")
    private Date runEndTime;
    
    @Column(name="MAPINFOID")
    private Long mapinfoId;
    
    @Column(name="OPTTYPE")
    private String optType;
    
    @Column(name="OSID")
    private String osId;
    
    @Column(name="SUCCESSPIECES")
    private Long successPieces;
    
    @Column(name="ERRORPIECES")
    private Long errorPieces;
    
    @Column(name="OTHERMESSAGE")
    private String otherMessage;
    private Set<TaskErrorData> taskErrorDatas = null;// new ArrayList<TaskErrorData>();
    private String mapinfoName;

    public String getMapinfoName() {
        return mapinfoName;
    }

    public void setMapinfoName(String mapinfoName) {
        this.mapinfoName = mapinfoName;
    }
    // Constructors

    /**
     * default constructor
     */
    public TaskDetailLog() {
    }

    /**
     * minimal constructor
     */
    public TaskDetailLog(
            Long logDetailId
    ) {


        this.logDetailId = logDetailId;

    }

    /**
     * full constructor
     */
    public TaskDetailLog(
            Long logDetailId
            , Long logId, Date runBeginTime, Date runEndTime, Long mapinfoId,
            Long successPieces, Long errorPieces, String optType, String osId, String otherMessage) {
        this.logDetailId = logDetailId;
        this.otherMessage = otherMessage;
        this.logId = logId;
        this.runBeginTime = runBeginTime;
        this.runEndTime = runEndTime;
        this.mapinfoId = mapinfoId;
        this.successPieces = successPieces;
        this.errorPieces = errorPieces;
        this.optType = optType;
        this.osId = osId;
    }


    public Long getLogDetailId() {
        return this.logDetailId;
    }

    public void setLogDetailId(Long logDetailId) {
        this.logDetailId = logDetailId;
    }
    // Property accessors

    public Long getLogId() {
        return this.logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
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

    public Long getMapinfoId() {
        return this.mapinfoId;
    }

    public void setMapinfoId(Long mapinfoId) {
        optType = "1";
        this.mapinfoId = mapinfoId;
    }

    public Long getExportId() {

        return this.mapinfoId;
    }

    public void setExportId(Long exportId) {
        optType = "3";
        this.mapinfoId = exportId;
    }

    public Long getImportId() {
        return this.mapinfoId;
    }

    public void setImportId(Long importId) {
        optType = "4";
        this.mapinfoId = importId;
    }

    public Long getSuccessPieces() {
        return this.successPieces;
    }

    public void setSuccessPieces(Long successPieces) {
        this.successPieces = successPieces;
    }

    public Long getErrorPieces() {
        return this.errorPieces;
    }

    public void setErrorPieces(Long errorPieces) {
        this.errorPieces = errorPieces;
    }

    /**
     * '1, 交换 3 导出 4 导入 2 调用WS';
     *
     * @return
     */
    public String getOptType() {
        return optType;
    }

    /**
     * '1, 交换 3 导出 4 导入 2 调用WS';
     *
     * @param optType
     */
    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        optType = "2";
        this.osId = osId;
    }

    public String getOtherMessage() {
        return this.otherMessage;
    }

    public void setOtherMessage(String otherMessage) {
        this.otherMessage = otherMessage;
    }

    public void appendOtherMessage(String otherMessage) {
        if (this.otherMessage == null)
            this.otherMessage = otherMessage;
        else
            this.otherMessage = this.otherMessage + " || " + otherMessage;
    }

    public Set<TaskErrorData> getTaskErrorDatas() {
        if (this.taskErrorDatas == null)
            this.taskErrorDatas = new HashSet<TaskErrorData>();
        return this.taskErrorDatas;
    }

    public void setTaskErrorDatas(Set<TaskErrorData> taskErrorDatas) {
        this.taskErrorDatas = taskErrorDatas;
    }

    public void addTaskErrorData(TaskErrorData taskErrorData) {
        if (this.taskErrorDatas == null)
            this.taskErrorDatas = new HashSet<TaskErrorData>();
        this.taskErrorDatas.add(taskErrorData);
    }

    public void removeTaskErrorData(TaskErrorData taskErrorData) {
        if (this.taskErrorDatas == null)
            return;
        this.taskErrorDatas.remove(taskErrorData);
    }

    public TaskErrorData newTaskErrorData() {
        TaskErrorData res = new TaskErrorData();

        res.setLogDetailId(this.getLogDetailId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceTaskErrorDatas(List<TaskErrorData> taskErrorDatas) {
        List<TaskErrorData> newObjs = new ArrayList<TaskErrorData>();
        for (TaskErrorData p : taskErrorDatas) {
            if (p == null)
                continue;
            TaskErrorData newdt = newTaskErrorData();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        //delete
        boolean found = false;
        Set<TaskErrorData> oldObjs = new HashSet<TaskErrorData>();
        oldObjs.addAll(getTaskErrorDatas());

        for (Iterator<TaskErrorData> it = oldObjs.iterator(); it.hasNext(); ) {
            TaskErrorData odt = it.next();
            found = false;
            for (TaskErrorData newdt : newObjs) {
                if (odt.getDataId().equals(newdt.getDataId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeTaskErrorData(odt);
        }
        oldObjs.clear();
        //insert or update
        for (TaskErrorData newdt : newObjs) {
            found = false;
            for (Iterator<TaskErrorData> it = getTaskErrorDatas().iterator();
                 it.hasNext(); ) {
                TaskErrorData odt = it.next();
                if (odt.getDataId().equals(newdt.getDataId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addTaskErrorData(newdt);
        }
    }


    public void copy(TaskDetailLog other) {

        this.setLogDetailId(other.getLogDetailId());

        this.logId = other.getLogId();
        this.runBeginTime = other.getRunBeginTime();
        this.runEndTime = other.getRunEndTime();
        this.mapinfoId = other.getMapinfoId();
        this.successPieces = other.getSuccessPieces();
        this.errorPieces = other.getErrorPieces();
        this.otherMessage = other.getOtherMessage();
        this.optType = other.getOptType();
        this.osId = other.getOsId();
        this.taskErrorDatas = other.getTaskErrorDatas();
    }

    public void copyNotNullProperty(TaskDetailLog other) {

        if (other.getLogDetailId() != null)
            this.setLogDetailId(other.getLogDetailId());

        if (other.getLogId() != null)
            this.logId = other.getLogId();
        if (other.getRunBeginTime() != null)
            this.runBeginTime = other.getRunBeginTime();
        if (other.getRunEndTime() != null)
            this.runEndTime = other.getRunEndTime();
        if (other.getMapinfoId() != null)
            this.mapinfoId = other.getMapinfoId();
        if (other.getSuccessPieces() != null)
            this.successPieces = other.getSuccessPieces();
        if (other.getErrorPieces() != null)
            this.errorPieces = other.getErrorPieces();
        if (other.getOtherMessage() != null)
            this.otherMessage = other.getOtherMessage();

        if (other.getOptType() != null)
            this.optType = other.getOptType();

        if (other.getOsId() != null)
            this.osId = other.getOsId();

        this.taskErrorDatas = other.getTaskErrorDatas();
    }


    public void clearProperties() {

        this.logId = null;
        this.runBeginTime = null;
        this.runEndTime = null;
        this.mapinfoId = null;
        this.successPieces = null;
        this.errorPieces = null;
        this.optType = null;
        this.osId = null;

        this.taskErrorDatas = new HashSet<TaskErrorData>();
    }
}
