package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
@Embeddable
public class MapinfoTriggerId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @Column(name="TRIGGERID")
    private Long triggerId;

    @Column(name="MAPINFOID")
    private Long mapinfoId;

    // Constructors

    /**
     * default constructor
     */
    public MapinfoTriggerId() {
    }

    /**
     * full constructor
     */
    public MapinfoTriggerId(Long triggerId, Long mapinfoId) {

        this.triggerId = triggerId;
        this.mapinfoId = mapinfoId;
    }


    public Long getTriggerId() {
        return this.triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getMapinfoId() {
        return this.mapinfoId;
    }

    public void setMapinfoId(Long mapinfoId) {
        this.mapinfoId = mapinfoId;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof MapinfoTriggerId))
            return false;

        MapinfoTriggerId castOther = (MapinfoTriggerId) other;
        boolean ret = true;

        ret = ret && (this.getTriggerId() == castOther.getTriggerId() ||
                (this.getTriggerId() != null && castOther.getTriggerId() != null
                        && this.getTriggerId().equals(castOther.getTriggerId())));

        ret = ret && (this.getMapinfoId() == castOther.getMapinfoId() ||
                (this.getMapinfoId() != null && castOther.getMapinfoId() != null
                        && this.getMapinfoId().equals(castOther.getMapinfoId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getTriggerId() == null ? 0 : this.getTriggerId().hashCode());

        result = 37 * result +
                (this.getMapinfoId() == null ? 0 : this.getMapinfoId().hashCode());

        return result;
    }
}
