package com.centit.dde.po;

/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
public class MapInfoDetailId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    private Long mapInfoId;

    private Long columnNo;

    // Constructors

    /**
     * default constructor
     */
    public MapInfoDetailId() {
    }

    /**
     * full constructor
     */
    public MapInfoDetailId(Long mapInfoId, Long columnNo) {

        this.mapInfoId = mapInfoId;
        this.columnNo = columnNo;
    }


    public Long getMapInfoId() {
        return this.mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

    public Long getColumnNo() {
        return this.columnNo;
    }

    public void setColumnNo(Long columnNo) {
        this.columnNo = columnNo;
    }


    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof MapInfoDetailId))
            return false;

        MapInfoDetailId castOther = (MapInfoDetailId) other;
        boolean ret = true;

        ret = ret && (this.getMapInfoId() == castOther.getMapInfoId() ||
                (this.getMapInfoId() != null && castOther.getMapInfoId() != null
                        && this.getMapInfoId().equals(castOther.getMapInfoId())));

        ret = ret && (this.getColumnNo() == castOther.getColumnNo() ||
                (this.getColumnNo() != null && castOther.getColumnNo() != null
                        && this.getColumnNo().equals(castOther.getColumnNo())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getMapInfoId() == null ? 0 : this.getMapInfoId().hashCode());

        result = 37 * result +
                (this.getColumnNo() == null ? 0 : this.getColumnNo().hashCode());

        return result;
    }
}
