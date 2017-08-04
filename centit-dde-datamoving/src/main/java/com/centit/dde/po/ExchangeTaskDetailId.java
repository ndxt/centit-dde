package com.centit.dde.po;

/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
public class ExchangeTaskDetailId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    private Long mapInfoId;

    private Long taskId;

    // Constructors

    /**
     * default constructor
     */
    public ExchangeTaskDetailId() {
    }

    /**
     * full constructor
     */
    public ExchangeTaskDetailId(Long mapInfoId, Long taskId) {

        this.mapInfoId = mapInfoId;
        this.taskId = taskId;
    }


    public Long getMapInfoId() {
        return this.mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
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
        if (!(other instanceof ExchangeTaskDetailId))
            return false;

        ExchangeTaskDetailId castOther = (ExchangeTaskDetailId) other;
        boolean ret = true;

        ret = ret && (this.getMapInfoId() == castOther.getMapInfoId() ||
                (this.getMapInfoId() != null && castOther.getMapInfoId() != null
                        && this.getMapInfoId().equals(castOther.getMapInfoId())));

        ret = ret && (this.getTaskId() == castOther.getTaskId() ||
                (this.getTaskId() != null && castOther.getTaskId() != null
                        && this.getTaskId().equals(castOther.getTaskId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getMapInfoId() == null ? 0 : this.getMapInfoId().hashCode());

        result = 37 * result +
                (this.getTaskId() == null ? 0 : this.getTaskId().hashCode());

        return result;
    }
}
