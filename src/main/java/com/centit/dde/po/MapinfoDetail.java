package com.centit.dde.po;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="D_MAPINFO_DETAIL")
public class MapinfoDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    @Column(name="CID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private MapinfoDetailId cid;

    @Column(name="SOURCE_FIELD_NAME")
    private String sourceFieldName;
    
    @Column(name="SOURCE_FIELD_SENTENCE")
    private String destFieldName;
    
    @Column(name="SOURCE_FIELD_TYPE")
    private String sourceFieldSentence;
    
    @Column(name="DEST_FIELD_NAME")
    private String sourceFieldType;
    
    @Column(name="DEST_FIELD_TYPE")
    private String destFieldType;
    
    @Column(name="IS_PK")
    private String isPk;
    
    @Column(name="DEST_FIELD_DEFAULT")
    private String destFieldDefault;
    
    @Column(name="IS_NULL")
    private String isNull;

    @Transient
    private String soueceTableName;
    
    @Transient
    private String goalTableName;
    
    @Transient
    private String sourceUrl;
    
    @Transient
    private String goalUrl;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getGoalUrl() {
        return goalUrl;
    }

    public void setGoalUrl(String goalUrl) {
        this.goalUrl = goalUrl;
    }

    public String getSoueceTableName() {
        return soueceTableName;
    }

    public void setSoueceTableName(String soueceTableName) {
        this.soueceTableName = soueceTableName;
    }

    public String getGoalTableName() {
        return goalTableName;
    }

    public void setGoalTableName(String goalTableName) {
        this.goalTableName = goalTableName;
    }


    // Constructors

    /**
     * default constructor
     */
    public MapinfoDetail() {
    }

    /**
     * minimal constructor
     */
    public MapinfoDetail(MapinfoDetailId id

            , String sourceFieldName, String sourceFieldSentence, String destFieldName) {
        this.cid = id;


        this.sourceFieldName = sourceFieldName;
        this.sourceFieldSentence = sourceFieldSentence;
        this.destFieldName = destFieldName;
    }

    /**
     * full constructor
     */
    public MapinfoDetail(MapinfoDetailId id, Long orderNo, String sourceFieldName, 
            String sourceFieldSentence, String sourceFieldType, String destFieldName, 
            String destFieldType, String isPk, String destFieldDefault, String isNull, String querySql) {
        this.cid = id;

        this.sourceFieldName = sourceFieldName;
        this.sourceFieldSentence = sourceFieldSentence;
        this.sourceFieldType = sourceFieldType;
        this.destFieldName = destFieldName;
        this.destFieldType = destFieldType;
        this.isPk = isPk;
        this.destFieldDefault = destFieldDefault;
        this.isNull = isNull;
    }

    public MapinfoDetailId getCid() {
        return this.cid;
    }

    public void setCid(MapinfoDetailId id) {
        this.cid = id;
    }

    public Long getMapinfoId() {
        if (this.cid == null)
            this.cid = new MapinfoDetailId();
        return this.cid.getMapinfoId();
    }

    public void setMapinfoId(Long mapinfoId) {
        if (this.cid == null)
            this.cid = new MapinfoDetailId();
        this.cid.setMapinfoId(mapinfoId);
    }


    // Property accessors


    public String getSourceFieldName() {
        return this.sourceFieldName;
    }

    public void setSourceFieldName(String sourceFieldName) {
        this.sourceFieldName = sourceFieldName;
    }

    public String getSourceFieldSentence() {
        return this.sourceFieldSentence;
    }

    public void setSourceFieldSentence(String sourceFieldSentence) {
        this.sourceFieldSentence = sourceFieldSentence;
    }

    public String getSourceFieldType() {
        return this.sourceFieldType;
    }

    public void setSourceFieldType(String sourceFieldType) {
        this.sourceFieldType = sourceFieldType;
    }

    public String getDestFieldName() {
        return this.destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public String getDestFieldType() {
        return this.destFieldType;
    }

    public void setDestFieldType(String destFieldType) {
        this.destFieldType = destFieldType;
    }

    public String getIsPk() {
        return this.isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public String getDestFieldDefault() {
        return this.destFieldDefault;
    }

    public void setDestFieldDefault(String destFieldDefault) {
        this.destFieldDefault = destFieldDefault;
    }

    public String getIsNull() {
        return this.isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }


    public void copy(MapinfoDetail other) {

        this.setMapinfoId(other.getMapinfoId());

        this.sourceFieldName = other.getSourceFieldName();
        this.sourceFieldSentence = other.getSourceFieldSentence();
        this.sourceFieldType = other.getSourceFieldType();
        this.destFieldName = other.getDestFieldName();
        this.destFieldType = other.getDestFieldType();
        this.isPk = other.getIsPk();
        this.destFieldDefault = other.getDestFieldDefault();
        this.isNull = other.getIsNull();

    }

    public void copyNotNullProperty(MapinfoDetail other) {

        if (other.getMapinfoId() != null)
            this.setMapinfoId(other.getMapinfoId());

        if (other.getSourceFieldName() != null)
            this.sourceFieldName = other.getSourceFieldName();
        if (other.getSourceFieldSentence() != null)
            this.sourceFieldSentence = other.getSourceFieldSentence();
        if (other.getSourceFieldType() != null)
            this.sourceFieldType = other.getSourceFieldType();
        if (other.getDestFieldName() != null)
            this.destFieldName = other.getDestFieldName();
        if (other.getDestFieldType() != null)
            this.destFieldType = other.getDestFieldType();
        if (other.getIsPk() != null)
            this.isPk = other.getIsPk();
        if (other.getDestFieldDefault() != null)
            this.destFieldDefault = other.getDestFieldDefault();
        if (other.getIsNull() != null)
            this.isNull = other.getIsNull();

    }

    public void clearProperties() {

        this.sourceFieldName = null;
        this.sourceFieldSentence = null;
        this.sourceFieldType = null;
        this.destFieldName = null;
        this.destFieldType = null;
        this.isPk = null;
        this.destFieldDefault = null;
        this.isNull = null;

    }
}
