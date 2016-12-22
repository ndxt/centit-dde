package com.centit.dde.po;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXPORT_FIELD")
public class ExportField implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @Column(name="CID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private ExportFieldId cid;

    @Column(name="fieldName")
    private String fieldName;

    @Column(name="FIELDSENTENCE")
    private String fieldSentence;

    @Column(name="FIELDTYPE")
    private String fieldType;

    @Column(name="FIELDFORMAT")
    private String fieldFormat;

    @Column(name="FIELDSTORETYPE")
    private String fieldStoreType;

    @Column(name="ISPK")
    private String isPk;

    @Column(name="ISNULL")
    private String isNull;

    // Constructors

    /**
     * default constructor
     */
    public ExportField() {
    }

    /**
     * minimal constructor
     */
    public ExportField(ExportFieldId id

            , String fieldName, String fieldSentence) {
        this.cid = id;

        this.fieldName = fieldName;
        this.fieldSentence = fieldSentence;
    }

    /**
     * full constructor
     */
    public ExportField(ExportFieldId id

            , String fieldName, String fieldSentence, String fieldType, String fieldFormat, String fieldStoreType, String isPk) {
        this.cid = id;

        this.fieldName = fieldName;
        this.fieldSentence = fieldSentence;
        this.fieldType = fieldType;
        this.fieldFormat = fieldFormat;
        this.fieldStoreType = fieldStoreType;
        this.isPk = isPk;
    }

    public ExportFieldId getCid() {
        return this.cid;
    }

    public void setCid(ExportFieldId id) {
        this.cid = id;
    }

    public Long getExportId() {
        if (this.cid == null)
            this.cid = new ExportFieldId();
        return this.cid.getExportId();
    }

    public void setExportId(Long exportId) {
        if (this.cid == null)
            this.cid = new ExportFieldId();
        this.cid.setExportId(exportId);
    }

    public Long getColumnNo() {
        if (this.cid == null)
            this.cid = new ExportFieldId();
        return this.cid.getColumnNo();
    }

    public void setColumnNo(Long columnNo) {
        if (this.cid == null)
            this.cid = new ExportFieldId();
        this.cid.setColumnNo(columnNo);
    }

    // Property accessors

    public String getFieldName() {
        return this.fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldSentence() {
        return this.fieldSentence;
    }

    public void setFieldSentence(String fieldSentence) {
        this.fieldSentence = fieldSentence;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldFormat() {
        return this.fieldFormat;
    }

    public void setFieldFormat(String fieldFormat) {
        this.fieldFormat = fieldFormat;
    }

    /**
     * 1: infile 0:embedded
     *
     * @return
     */
    public String getFieldStoreType() {
        return this.fieldStoreType;
    }

    /**
     * 1: infile 0:embedded
     *
     * @param fieldStoreType
     */
    public void setFieldStoreType(String fieldStoreType) {
        this.fieldStoreType = fieldStoreType;
    }

    /**
     * @return the isPk
     */
    public String getIsPk() {
        return isPk;
    }

    /**
     * @param isPk the isPk to set
     */
    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public void copy(ExportField other) {

        this.setExportId(other.getExportId());
        this.setColumnNo(other.getColumnNo());

        this.fieldName = other.getFieldName();
        this.fieldSentence = other.getFieldSentence();
        this.fieldType = other.getFieldType();
        this.fieldFormat = other.getFieldFormat();
        this.fieldStoreType = other.getFieldStoreType();
        this.isPk = other.getIsPk();
    }

    public void copyNotNullProperty(ExportField other) {

        if (other.getExportId() != null)
            this.setExportId(other.getExportId());
        if (other.getColumnNo() != null)
            this.setColumnNo(other.getColumnNo());

        if (other.getFieldName() != null)
            this.fieldName = other.getFieldName();
        if (other.getFieldSentence() != null)
            this.fieldSentence = other.getFieldSentence();
        if (other.getFieldType() != null)
            this.fieldType = other.getFieldType();
        if (other.getFieldFormat() != null)
            this.fieldFormat = other.getFieldFormat();
        if (other.getFieldStoreType() != null)
            this.fieldStoreType = other.getFieldStoreType();
        if (other.getIsPk() != null)
            this.isPk = other.getIsPk();
    }

    public void copyNotNullPropertyNonePrimaryKey(ExportField other) {

        if (other.getColumnNo() != null)
            this.setColumnNo(other.getColumnNo());

        if (other.getFieldName() != null)
            this.fieldName = other.getFieldName();
        if (other.getFieldSentence() != null)
            this.fieldSentence = other.getFieldSentence();
        if (other.getFieldType() != null)
            this.fieldType = other.getFieldType();
        if (other.getFieldFormat() != null)
            this.fieldFormat = other.getFieldFormat();
        if (other.getFieldStoreType() != null)
            this.fieldStoreType = other.getFieldStoreType();
        if (other.getIsPk() != null)
            this.isPk = other.getIsPk();
    }

    public void clearProperties() {

        this.fieldName = null;
        this.fieldSentence = null;
        this.fieldType = null;
        this.fieldFormat = null;
        this.fieldStoreType = null;
        this.isPk = null;
    }

    @Override
    public int hashCode() {
        return getCid().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof ExportField)) {
            return false;
        }

        return this.getCid().equals(((ExportField) obj).getCid());
    }

}
