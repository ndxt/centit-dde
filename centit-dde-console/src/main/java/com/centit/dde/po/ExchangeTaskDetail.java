package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


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
    @EmbeddedId
    @Column(name="CID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private ExchangeTaskDetailId cid;

    @Column(name="MAPINFO_ORDER")
    private Long mapInfoOrder;

    // Constructors

    /**
     * default constructor
     */
    public ExchangeTaskDetail() {
    }

    /**
     * minimal constructor
     */
    public ExchangeTaskDetail(ExchangeTaskDetailId id) {
        this.cid = id;
    }

    /**
     * full constructor
     */
    public ExchangeTaskDetail(ExchangeTaskDetailId id, Long mapInfoOrder) {
        this.cid = id;

        this.mapInfoOrder = mapInfoOrder;
    }

    public ExchangeTaskDetailId getCid() {
        return this.cid;
    }

    public void setCid(ExchangeTaskDetailId id) {
        this.cid = id;
    }

    public Long getMapInfoId() {
        if (this.cid == null)
            this.cid = new ExchangeTaskDetailId();
        return this.cid.getMapInfoId();
    }

    public void setMapInfoId(Long mapInfoId) {
        if (this.cid == null)
            this.cid = new ExchangeTaskDetailId();
        this.cid.setMapInfoId(mapInfoId);
    }

    public Long getTaskId() {
        if (this.cid == null)
            this.cid = new ExchangeTaskDetailId();
        return this.cid.getTaskId();
    }

    public void setTaskId(Long taskId) {
        if (this.cid == null)
            this.cid = new ExchangeTaskDetailId();
        this.cid.setTaskId(taskId);
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

    public void clearProperties() {

        this.mapInfoOrder = null;

    }
}
