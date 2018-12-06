package com.centit.dde.po;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXPORT_FIELD")
public class ExportField implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="EXPORT_ID")
    @NotBlank(message = "字段不能为空")
    private Long exportId;

    @Id
    @Column(name="COLUMN_NO")
    @NotBlank(message = "字段不能为空")
    private Long columnNo;

    /**
     * 字段名称
     */
    @Column(name="FIELD_NAME")
    private String fieldName;

    /**
     * 字段语句
     */
    @Column(name="FIELD_SENTENCE")
    private String fieldSentence;

    /**
     * 字段类型
     */
    @Column(name="FIELD_TYPE")
    private String fieldType;

    /**
     * 字段格式
     */
    @Column(name="FIELD_FORMAT")
    private String fieldFormat;

    /**
     * 存储类型
     */
    @Column(name="FIELD_STORE_TYPE")
    private String fieldStoreType;

    /**
     * 是否主键
     */
    @Column(name="IS_PK")
    private String isPk;

    @Transient
    private String isNull;

    // Constructors

    /**
     * default constructor
     */
    public ExportField() {
    }

    public ExportField(@NotBlank(message = "字段不能为空") Long exportId, @NotBlank(message = "字段不能为空") Long columnNo) {
        this.exportId = exportId;
        this.columnNo = columnNo;
    }

    public ExportField(@NotBlank(message = "字段不能为空") Long exportId,
                       @NotBlank(message = "字段不能为空") Long columnNo,
                        String fieldName, String fieldSentence) {
        this.exportId = exportId;
        this.columnNo = columnNo;
        this.fieldName = fieldName;
        this.fieldSentence = fieldSentence;
    }

    public ExportField(@NotBlank(message = "字段不能为空") Long exportId, @NotBlank(message = "字段不能为空") Long columnNo, String fieldName, String fieldSentence, String fieldType, String fieldFormat, String fieldStoreType, String isPk, String isNull) {
        this.exportId = exportId;
        this.columnNo = columnNo;
        this.fieldName = fieldName;
        this.fieldSentence = fieldSentence;
        this.fieldType = fieldType;
        this.fieldFormat = fieldFormat;
        this.fieldStoreType = fieldStoreType;
        this.isPk = isPk;
        this.isNull = isNull;
    }

    public Long getExportId() {
        return exportId;
    }

    public void setExportId(Long exportId) {
        this.exportId = exportId;
    }

    public Long getColumnNo() {
        return columnNo;
    }

    public void setColumnNo(Long columnNo) {
        this.columnNo = columnNo;
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
        return String.valueOf(getColumnNo()+getExportId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof ExportField)) {
            return false;
        }

        return this.getColumnNo().equals(((ExportField) obj).getColumnNo()) &&
            this.getExportId().equals(((ExportField) obj).getExportId());
    }

}
