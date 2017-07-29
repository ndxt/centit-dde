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
@IdClass(MapInfoTrigger.class)
public class MapInfoTriggerId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @Column(name="TRIGGER_ID")
    @NotBlank(message = "字段不能为空")
    private Long triggerId;

    @Column(name="MAPINFO_ID")
    @NotBlank(message = "字段不能为空")
    private Long mapInfoId;

    // Constructors

    /**
     * default constructor
     */
    public MapInfoTriggerId() {
    }

    /**
     * full constructor
     */
    public MapInfoTriggerId(Long triggerId, Long mapInfoId) {

        this.triggerId = triggerId;
        this.mapInfoId = mapInfoId;
    }


    public Long getTriggerId() {
        return this.triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getMapInfoId() {
        return this.mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof MapInfoTriggerId))
            return false;

        MapInfoTriggerId castOther = (MapInfoTriggerId) other;
        boolean ret = true;

        ret = ret && (this.getTriggerId() == castOther.getTriggerId() ||
                (this.getTriggerId() != null && castOther.getTriggerId() != null
                        && this.getTriggerId().equals(castOther.getTriggerId())));

        ret = ret && (this.getMapInfoId() == castOther.getMapInfoId() ||
                (this.getMapInfoId() != null && castOther.getMapInfoId() != null
                        && this.getMapInfoId().equals(castOther.getMapInfoId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getTriggerId() == null ? 0 : this.getTriggerId().hashCode());

        result = 37 * result +
                (this.getMapInfoId() == null ? 0 : this.getMapInfoId().hashCode());

        return result;
    }
}
