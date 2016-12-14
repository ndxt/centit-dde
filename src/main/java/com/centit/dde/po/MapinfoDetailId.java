package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
@Embeddable
public class MapinfoDetailId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    @Column(name="MAPINFOID")
    @NotBlank(message = "字段不能为空")
    private Long mapinfoId;

    @Column(name="COLUMNNO")
    @NotBlank(message = "字段不能为空")
    private Long columnNo;

    // Constructors

    /**
     * default constructor
     */
    public MapinfoDetailId() {
    }

    /**
     * full constructor
     */
    public MapinfoDetailId(Long mapinfoId, Long columnNo) {

        this.mapinfoId = mapinfoId;
        this.columnNo = columnNo;
    }


    public Long getMapinfoId() {
        return this.mapinfoId;
    }

    public void setMapinfoId(Long mapinfoId) {
        this.mapinfoId = mapinfoId;
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
        if (!(other instanceof MapinfoDetailId))
            return false;

        MapinfoDetailId castOther = (MapinfoDetailId) other;
        boolean ret = true;

        ret = ret && (this.getMapinfoId() == castOther.getMapinfoId() ||
                (this.getMapinfoId() != null && castOther.getMapinfoId() != null
                        && this.getMapinfoId().equals(castOther.getMapinfoId())));

        ret = ret && (this.getColumnNo() == castOther.getColumnNo() ||
                (this.getColumnNo() != null && castOther.getColumnNo() != null
                        && this.getColumnNo().equals(castOther.getColumnNo())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getMapinfoId() == null ? 0 : this.getMapinfoId().hashCode());

        result = 37 * result +
                (this.getColumnNo() == null ? 0 : this.getColumnNo().hashCode());

        return result;
    }
}
