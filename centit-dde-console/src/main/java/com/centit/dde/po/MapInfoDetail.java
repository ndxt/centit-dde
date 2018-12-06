package com.centit.dde.po;

import com.centit.framework.core.dao.DictionaryMap;
import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.ValueGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="D_MAPINFO_DETAIL")
public class MapInfoDetail implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="MAPINFO_ID")
    @NotBlank(message = "字段不能为空")
    private Long mapInfoId;

    @Id
    @Column(name="COLUMN_NO")
    @NotBlank(message = "字段不能为空")
    private Long columnNo;

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

    public MapInfoDetail(@NotBlank(message = "字段不能为空") Long mapInfoId, @NotBlank(message = "字段不能为空") Long columnNo) {
        this.mapInfoId = mapInfoId;
        this.columnNo = columnNo;
    }

    public MapInfoDetail(@NotBlank(message = "字段不能为空") Long mapInfoId, @NotBlank(message = "字段不能为空") Long columnNo, String sourceFieldName, String destFieldName, String sourceFieldSentence) {
        this.mapInfoId = mapInfoId;
        this.columnNo = columnNo;
        this.sourceFieldName = sourceFieldName;
        this.destFieldName = destFieldName;
        this.sourceFieldSentence = sourceFieldSentence;
    }


    public MapInfoDetail(@NotBlank(message = "字段不能为空") Long mapInfoId, @NotBlank(message = "字段不能为空") Long columnNo, String sourceFieldName, String destFieldName, String sourceFieldSentence, String sourceFieldType, String destFieldType, String isPk, String destFieldDefault, String isNull, String sourceTableName, String goalTableName, String sourceUrl, String goalUrl) {
        this.mapInfoId = mapInfoId;
        this.columnNo = columnNo;
        this.sourceFieldName = sourceFieldName;
        this.destFieldName = destFieldName;
        this.sourceFieldSentence = sourceFieldSentence;
        this.sourceFieldType = sourceFieldType;
        this.destFieldType = destFieldType;
        this.isPk = isPk;
        this.destFieldDefault = destFieldDefault;
        this.isNull = isNull;
        this.sourceTableName = sourceTableName;
        this.goalTableName = goalTableName;
        this.sourceUrl = sourceUrl;
        this.goalUrl = goalUrl;
    }

    public Long getMapInfoId() {
        return mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

    public Long getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(Long columnNo) {
        this.columnNo = columnNo;
    }

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

        this.setColumnNo(other.getColumnNo());
        this.setMapInfoId(other.getMapInfoId());
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

        if(other.getColumnNo() != null)
            this.setColumnNo(other.getColumnNo());
        if(other.getMapInfoId() != null)
            this.setMapInfoId(other.getMapInfoId());
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
