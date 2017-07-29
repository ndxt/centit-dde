package com.centit.dde.po;

import com.centit.framework.core.dao.DictionaryMap;

import javax.persistence.*;

@Entity
@Table(name="D_MAPINFO_DETAIL")
public class MapInfoDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    @Column(name="CID")
    private MapInfoDetailId cid;

    @Column(name="SOURCE_FIELD_NAME")
    private String sourceFieldName;
    
    @Column(name="DEST_FIELD_NAME")
    private String destFieldName;
    
    @Column(name="SOURCE_FIELD_SENTENCE")
    private String sourceFieldSentence;
    
    @Column(name="SOURCE_FIELD_TYPE")
    private String sourceFieldType;
    
    @Column(name="DEST_FIELD_TYPE")
    private String destFieldType;
    
    @Column(name="IS_PK")
    @DictionaryMap(value="YesOrNo",fieldName = "IS_PK")
    private String isPk;
    
    @Column(name="DEST_FIELD_DEFAULT")
    private String destFieldDefault;
    
    @Column(name="IS_NULL")
    @DictionaryMap(value="YesOrNo", fieldName = "IS_NULL")
    private String isNull;

    @Transient
    private String sourceTableName;
    
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

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
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
    public MapInfoDetail() {
    }

    /**
     * minimal constructor
     */
    public MapInfoDetail(MapInfoDetailId id

            , String sourceFieldName, String sourceFieldSentence, String destFieldName) {
        this.cid = id;


        this.sourceFieldName = sourceFieldName;
        this.sourceFieldSentence = sourceFieldSentence;
        this.destFieldName = destFieldName;
    }

    /**
     * full constructor
     */
    public MapInfoDetail(MapInfoDetailId id, Long orderNo, String sourceFieldName,
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

    public MapInfoDetailId getCid() {
        return this.cid;
    }

    public void setCid(MapInfoDetailId id) {
        this.cid = id;
    }

    public Long getMapInfoId() {
        if (this.cid == null)
            this.cid = new MapInfoDetailId();
        return this.cid.getMapInfoId();
    }

    public void setMapinfoId(Long mapinfoId) {
        if (this.cid == null)
            this.cid = new MapInfoDetailId();
        this.cid.setMapInfoId(mapinfoId);
    }

    public Long getColumnNo(){
        if(this.cid == null)
            this.cid = new MapInfoDetailId();
        return this.cid.getColumnNo();
    }

    public void setColumnNo(Long columnNo) {
        if(this.cid == null){
            this.cid = new MapInfoDetailId();
        }
        this.cid.setColumnNo(columnNo);
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


    public void copy(MapInfoDetail other) {

        this.setMapinfoId(other.getMapInfoId());

        this.sourceFieldName = other.getSourceFieldName();
        this.sourceFieldSentence = other.getSourceFieldSentence();
        this.sourceFieldType = other.getSourceFieldType();
        this.destFieldName = other.getDestFieldName();
        this.destFieldType = other.getDestFieldType();
        this.isPk = other.getIsPk();
        this.destFieldDefault = other.getDestFieldDefault();
        this.isNull = other.getIsNull();

    }

    public void copyNotNullProperty(MapInfoDetail other) {

//        if (other.getMapInfoId() != null)
//            this.setMapinfoId(other.getMapInfoId());
        if(other.getCid() != null)
            this.setCid(other.getCid());
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
