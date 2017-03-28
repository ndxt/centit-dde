package com.centit.dde.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.IdClass;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
@Embeddable
@IdClass(ExchangeTaskdetail.class)
public class ExchangeTaskdetailId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    @Column(name="MAPINFO_ID")
    @NotBlank(message = "字段不能为空")
    private Long mapinfoId;

    @Column(name="TASK_ID")
    @NotBlank(message = "字段不能为空")
    private Long taskId;

    // Constructors

    /**
     * default constructor
     */
    public ExchangeTaskdetailId() {
    }

    /**
     * full constructor
     */
    public ExchangeTaskdetailId(Long mapinfoId, Long taskId) {

        this.mapinfoId = mapinfoId;
        this.taskId = taskId;
    }


    public Long getMapinfoId() {
        return this.mapinfoId;
    }

    public void setMapinfoId(Long mapinfoId) {
        this.mapinfoId = mapinfoId;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof ExchangeTaskdetailId))
            return false;

        ExchangeTaskdetailId castOther = (ExchangeTaskdetailId) other;
        boolean ret = true;

        ret = ret && (this.getMapinfoId() == castOther.getMapinfoId() ||
                (this.getMapinfoId() != null && castOther.getMapinfoId() != null
                        && this.getMapinfoId().equals(castOther.getMapinfoId())));

        ret = ret && (this.getTaskId() == castOther.getTaskId() ||
                (this.getTaskId() != null && castOther.getTaskId() != null
                        && this.getTaskId().equals(castOther.getTaskId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getMapinfoId() == null ? 0 : this.getMapinfoId().hashCode());

        result = 37 * result +
                (this.getTaskId() == null ? 0 : this.getTaskId().hashCode());

        return result;
    }
}
