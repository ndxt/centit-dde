package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


/**
 * 交换任务明细
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXCHANGE_TASKDETAIL")
public class ExchangeTaskDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="MAPINFO_ID")
    @NotBlank(message = "字段不能为空")
    private Long mapInfoId;

    @Id
    @Column(name="TASK_ID")
    @NotBlank(message = "字段不能为空")
    private Long taskId;

    @Column(name="MAPINFO_ORDER")
    private Long mapInfoOrder;

    // Constructors

    /**
     * default constructor
     */
    public ExchangeTaskDetail() {
    }

    public ExchangeTaskDetail(@NotBlank(message = "字段不能为空") Long mapInfoId, @NotBlank(message = "字段不能为空") Long taskId) {
        this.mapInfoId = mapInfoId;
        this.taskId = taskId;
    }

    public ExchangeTaskDetail(@NotBlank(message = "字段不能为空") Long mapInfoId, @NotBlank(message = "字段不能为空") Long taskId, Long mapInfoOrder) {
        this.mapInfoId = mapInfoId;
        this.taskId = taskId;
        this.mapInfoOrder = mapInfoOrder;
    }

    // Property accessors

    public Long getMapInfoOrder() {
        return this.mapInfoOrder;
    }

    public void setMapInfoOrder(Long mapInfoOrder) {
        this.mapInfoOrder = mapInfoOrder;
    }


    public void copy(ExchangeTaskDetail other) {

        this.setMapInfoId(other.getMapInfoId());
        this.setTaskId(other.getTaskId());

        this.mapInfoOrder = other.getMapInfoOrder();

    }

    public void copyNotNullProperty(ExchangeTaskDetail other) {

        if (other.getMapInfoId() != null)
            this.setMapInfoId(other.getMapInfoId());
        if (other.getTaskId() != null)
            this.setTaskId(other.getTaskId());

        if (other.getMapInfoOrder() != null)
            this.mapInfoOrder = other.getMapInfoOrder();

    }

    public Long getMapInfoId() {
        return mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public void clearProperties() {

        this.mapInfoOrder = null;

    }
}
