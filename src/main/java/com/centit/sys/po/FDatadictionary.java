package com.centit.sys.po;

import java.util.Date;

/**
 * FDatadictionary entity.
 *
 * @author MyEclipse Persistence Tools
 * @author codefan
 */
//数据字典表
public class FDatadictionary implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -4063651885248484498L;
    private FDatadictionaryId id;    //主键id
    private String extracode;        //附加代码1
    private String extracode2;        //附加代码2
    private String datatag;            //标志符
    private String datavalue;        //数据值
    private String datastyle;        //属性
    private String datadesc;        //备注


    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public FDatadictionary() {
        this.id = new FDatadictionaryId();
    }

    /**
     * minimal constructor
     */
    public FDatadictionary(FDatadictionaryId id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public FDatadictionary(FDatadictionaryId id, String extracode,
                           String extracode2, String datatag, String datavalue,
                           String datastyle, String datadesc) {
        this.id = id;
        this.extracode = extracode;
        this.extracode2 = extracode2;
        this.datatag = datatag;
        this.datavalue = datavalue;
        this.datastyle = datastyle;
        this.datadesc = datadesc;
    }

    // Property accessors

    public FDatadictionaryId getId() {
        return this.id;
    }

    public void setId(FDatadictionaryId id) {
        this.id = id;
    }

    public String getDatacode() {
        if (id == null)
            return null;
        return this.id.getDatacode();
    }

    public void setDatacode(String datacode) {
        if (id == null)
            id = new FDatadictionaryId();
        this.id.setDatacode(datacode);
    }

    public String getCatalogcode() {
        if (id == null)
            return null;
        return this.id.getCatalogcode();
    }

    public void setCatalogcode(String catalogcode) {
        if (id == null)
            id = new FDatadictionaryId();
        this.id.setCatalogcode(catalogcode);
    }

    public String getExtracode() {
        return this.extracode;
    }

    public void setExtracode(String extracode) {
        this.extracode = extracode;
    }

    public String getExtracode2() {
        return this.extracode2;
    }

    public void setExtracode2(String extracode2) {
        this.extracode2 = extracode2;
    }

    public String getDatatag() {
        return this.datatag;
    }

    public String getState() {
        return this.datatag;
    }

    public void setDatatag(String datastate) {
        this.datatag = datastate;
    }

    public String getDatavalue() {
        return this.datavalue;
    }

    public String getKey() {
        return this.id.getDatacode();
    }


    public String getFullKey() {
        return this.id.getCatalogcode() + "." + this.id.getDatacode();
    }

    public String getValue() {
        return this.datavalue;
    }

    public String toString() {
        return this.datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getDatastyle() {
        return this.datastyle;
    }

    public void setDatastyle(String datastyle) {
        this.datastyle = datastyle;
    }

    public String getDatadesc() {
        return this.datadesc;
    }

    public void setDatadesc(String datadesc) {
        this.datadesc = datadesc;
    }

    public void copy(FDatadictionary other) {
        this.setCatalogcode(other.getCatalogcode());
        this.setDatacode(other.getDatacode());
        this.extracode = other.getExtracode();
        this.extracode2 = other.getExtracode2();
        this.datatag = other.getDatatag();
        this.datavalue = other.getDatavalue();
        this.datastyle = other.getDatastyle();
        this.datadesc = other.getDatadesc();
    }

    public void copyNotNullProperty(FDatadictionary other) {

        if (other.getExtracode() != null)
            this.extracode = other.getExtracode();
        if (other.getExtracode2() != null)
            this.extracode2 = other.getExtracode2();
        if (other.getDatatag() != null)
            this.datatag = other.getDatatag();
        if (other.getDatavalue() != null)
            this.datavalue = other.getDatavalue();
        if (other.getDatastyle() != null)
            this.datastyle = other.getDatastyle();
        if (other.getDatadesc() != null)
            this.datadesc = other.getDatadesc();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public String display() {
        return "字典明细信息 [" + "标记=" + datatag
                + ", 标志符=" + datavalue + ", 类型=" + datastyle
                + ", 数据描述=" + datadesc + "]";
    }
}