package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_TASK_ERROR_DATA")
public class TaskErrorData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    @Column(name="DATAID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long dataId;

    @Column(name="LOGDETAILID")
    private Long logDetailId;

    
    public Long getLogDetailId() {
        return logDetailId;
    }

    public void setLogDetailId(Long logDetailId) {
        this.logDetailId = logDetailId;
    }

    private String dataContent;
    private String errorMessage;

    // Constructors

    /**
     * default constructor
     */
    public TaskErrorData() {
    }

    /**
     * minimal constructor
     */
    public TaskErrorData(
            Long dataId
    ) {


        this.dataId = dataId;

    }

    /**
     * full constructor
     */
    public TaskErrorData(
            Long dataId
            , Long logDetailId, String dataContent, String errorMessage) {


        this.dataId = dataId;

        this.logDetailId = logDetailId;
        this.dataContent = dataContent;
        this.errorMessage = errorMessage;
    }


    public Long getDataId() {
        return this.dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }
    // Property accessors

    public String getDataContent() {
        return this.dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public void copy(TaskErrorData other) {

        this.setDataId(other.getDataId());

        this.logDetailId = other.getLogDetailId();
        this.dataContent = other.getDataContent();
        this.errorMessage = other.getErrorMessage();

    }

    public void copyNotNullProperty(TaskErrorData other) {

        if (other.getDataId() != null)
            this.setDataId(other.getDataId());

        if (other.getLogDetailId() != null)
            this.logDetailId = other.getLogDetailId();
        if (other.getDataContent() != null)
            this.dataContent = other.getDataContent();
        if (other.getErrorMessage() != null)
            this.errorMessage = other.getErrorMessage();

    }

    public void clearProperties() {

        this.logDetailId = null;
        this.dataContent = null;
        this.errorMessage = null;

    }
}
