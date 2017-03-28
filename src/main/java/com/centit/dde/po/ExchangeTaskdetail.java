package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXCHANGE_TASKDETAIL")
public class ExchangeTaskdetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    @Column(name="CID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private ExchangeTaskdetailId cid;

    @Column(name="MAPINFO_ORDER")
    private Long mapinfoOrder;

    // Constructors

    /**
     * default constructor
     */
    public ExchangeTaskdetail() {
    }

    /**
     * minimal constructor
     */
    public ExchangeTaskdetail(ExchangeTaskdetailId id

    ) {
        this.cid = id;


    }

    /**
     * full constructor
     */
    public ExchangeTaskdetail(ExchangeTaskdetailId id

            , Long mapinfoOrder) {
        this.cid = id;


        this.mapinfoOrder = mapinfoOrder;
    }

    public ExchangeTaskdetailId getCid() {
        return this.cid;
    }

    public void setCid(ExchangeTaskdetailId id) {
        this.cid = id;
    }

    public Long getMapinfoId() {
        if (this.cid == null)
            this.cid = new ExchangeTaskdetailId();
        return this.cid.getMapinfoId();
    }

    public void setMapinfoId(Long mapinfoId) {
        if (this.cid == null)
            this.cid = new ExchangeTaskdetailId();
        this.cid.setMapinfoId(mapinfoId);
    }

    public Long getTaskId() {
        if (this.cid == null)
            this.cid = new ExchangeTaskdetailId();
        return this.cid.getTaskId();
    }

    public void setTaskId(Long taskId) {
        if (this.cid == null)
            this.cid = new ExchangeTaskdetailId();
        this.cid.setTaskId(taskId);
    }


    // Property accessors

    public Long getMapinfoOrder() {
        return this.mapinfoOrder;
    }

    public void setMapinfoOrder(Long mapinfoOrder) {
        this.mapinfoOrder = mapinfoOrder;
    }


    public void copy(ExchangeTaskdetail other) {

        this.setMapinfoId(other.getMapinfoId());
        this.setTaskId(other.getTaskId());

        this.mapinfoOrder = other.getMapinfoOrder();

    }

    public void copyNotNullProperty(ExchangeTaskdetail other) {

        if (other.getMapinfoId() != null)
            this.setMapinfoId(other.getMapinfoId());
        if (other.getTaskId() != null)
            this.setTaskId(other.getTaskId());

        if (other.getMapinfoOrder() != null)
            this.mapinfoOrder = other.getMapinfoOrder();

    }

    public void clearProperties() {

        this.mapinfoOrder = null;

    }
}
