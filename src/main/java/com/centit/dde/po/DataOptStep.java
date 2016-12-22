package com.centit.dde.po;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

@Entity
@Table(name="D_DATA_OPT_STEP")
public class DataOptStep implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "OPTSTEPID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long optStepId;
    
    @Column(name = "IMPORTID")
    private Long importId;

    @Column(name = "OPTTYPE")
    private String optType;

    @Column(name = "DATAOPTID")
    private String dataOptId;

    @Column(name = "OSID")
    private String osId;

    @Column(name = "MAPINFOORDER")
    private Long mapinfoOrder;

    @Column(name = "IMPORTNAME")
    private String importName;

    // Constructors

    /**
     * default constructor
     */
    public DataOptStep() {
    }

    /**
     * minimal constructor
     */
    public DataOptStep(Long optStepId, String optType) {

        this.optStepId = optStepId;

        this.optType = optType;
    }

    /**
     * full constructor
     */
    public DataOptStep(Long optStepId, Long importId, String optType, String dataOptId, String osId, Long mapinfoOrder) {

        this.optStepId = optStepId;

        this.importId = importId;
        this.optType = optType;
        this.dataOptId = dataOptId;
        this.osId = osId;
        this.mapinfoOrder = mapinfoOrder;
    }

    public DataOptStep(Long optStepId, Long importId, String optType, String dataOptId, String osId, Long mapinfoOrder,
                       String importName) {
        super();
        this.optStepId = optStepId;
        this.importId = importId;
        this.optType = optType;
        this.dataOptId = dataOptId;
        this.osId = osId;
        this.mapinfoOrder = mapinfoOrder;
        this.importName = importName;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public Long getOptStepId() {
        return this.optStepId;
    }

    public void setOptStepId(Long optStepId) {
        this.optStepId = optStepId;
    }

    // Property accessors

    public Long getImportId() {
        return this.importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    public String getOptType() {
        return this.optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getDataOptId() {
        return this.dataOptId;
    }

    public void setDataOptId(String dataOptId) {
        this.dataOptId = dataOptId;
    }

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public Long getMapinfoOrder() {
        return this.mapinfoOrder;
    }

    public void setMapinfoOrder(Long mapinfoOrder) {
        this.mapinfoOrder = mapinfoOrder;
    }

    public void copy(DataOptStep other) {

        this.setOptStepId(other.getOptStepId());

        this.importId = other.getImportId();
        this.optType = other.getOptType();
        this.dataOptId = other.getDataOptId();
        this.osId = other.getOsId();
        this.mapinfoOrder = other.getMapinfoOrder();

    }

    public void copyNotNullProperty(DataOptStep other) {

        if (other.getOptStepId() != null)
            this.setOptStepId(other.getOptStepId());

        if (other.getImportId() != null)
            this.importId = other.getImportId();
        if (other.getOptType() != null)
            this.optType = other.getOptType();
        if (other.getDataOptId() != null)
            this.dataOptId = other.getDataOptId();
        if (other.getOsId() != null)
            this.osId = other.getOsId();
        if (other.getMapinfoOrder() != null)
            this.mapinfoOrder = other.getMapinfoOrder();

    }

    public void clearProperties() {

        this.importId = null;
        this.optType = null;
        this.dataOptId = null;
        this.osId = null;
        this.mapinfoOrder = null;

    }
}
