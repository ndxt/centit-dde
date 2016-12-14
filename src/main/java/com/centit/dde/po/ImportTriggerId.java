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
public class ImportTriggerId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    
    @Column(name="TRIGGERID")
    @NotBlank(message = "字段不能为空")
    private Long triggerId;

    @Column(name="IMPORTID")
    @NotBlank(message = "字段不能为空")
    private Long importId;

    // Constructors

    /**
     * default constructor
     */
    public ImportTriggerId() {
    }

    /**
     * full constructor
     */
    public ImportTriggerId(Long triggerId, Long mapinfoId) {

        this.triggerId = triggerId;
        this.importId = mapinfoId;
    }


    public Long getTriggerId() {
        return this.triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getImportId() {
        return this.importId;
    }

    public void setImportId(Long mapinfoId) {
        this.importId = mapinfoId;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof ImportTriggerId))
            return false;

        ImportTriggerId castOther = (ImportTriggerId) other;
        boolean ret = true;

        ret = ret && (this.getTriggerId() == castOther.getTriggerId() ||
                (this.getTriggerId() != null && castOther.getTriggerId() != null
                        && this.getTriggerId().equals(castOther.getTriggerId())));

        ret = ret && (this.getImportId() == castOther.getImportId() ||
                (this.getImportId() != null && castOther.getImportId() != null
                        && this.getImportId().equals(castOther.getImportId())));

        return ret;
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result +
                (this.getTriggerId() == null ? 0 : this.getTriggerId().hashCode());

        result = 37 * result +
                (this.getImportId() == null ? 0 : this.getImportId().hashCode());

        return result;
    }
}
