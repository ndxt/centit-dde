package com.centit.sys.po;

import java.util.Date;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OptVariable implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private OptVariableId cid;


    private String vardesc;
    private String vartype;
    private String defaultvalue;
    private String isvalid;

    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public OptVariable() {
        this.isvalid = "T";
    }

    /**
     * minimal constructor
     */
    public OptVariable(OptVariableId id

            , String isvalid) {
        this.cid = id;
        this.isvalid = isvalid;
    }

    /**
     * full constructor
     */
    public OptVariable(OptVariableId id

            , String vardesc, String vartype, String defaultvalue, String isvalid) {
        this.cid = id;
        this.vardesc = vardesc;
        this.vartype = vartype;
        this.defaultvalue = defaultvalue;
        this.isvalid = isvalid;
    }

    public OptVariableId getCid() {
        return this.cid;
    }

    public void setCid(OptVariableId id) {
        this.cid = id;
    }

    public String getOptid() {
        if (this.cid == null)
            this.cid = new OptVariableId();
        return this.cid.getOptid();
    }

    public void setOptid(String optid) {
        if (this.cid == null)
            this.cid = new OptVariableId();
        this.cid.setOptid(optid);
    }

    public String getVarname() {
        if (this.cid == null)
            this.cid = new OptVariableId();
        return this.cid.getVarname();
    }

    public void setVarname(String varname) {
        if (this.cid == null)
            this.cid = new OptVariableId();
        this.cid.setVarname(varname);
    }


    // Property accessors

    public String getVardesc() {
        return this.vardesc;
    }

    public void setVardesc(String vardesc) {
        this.vardesc = vardesc;
    }

    public String getVartype() {
        return this.vartype;
    }

    public void setVartype(String vartype) {
        this.vartype = vartype;
    }

    public String getDefaultvalue() {
        return this.defaultvalue;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public String getIsvalid() {
        return this.isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }


    public void copy(OptVariable other) {

        this.setOptid(other.getOptid());
        this.setVarname(other.getVarname());

        this.vardesc = other.getVardesc();
        this.vartype = other.getVartype();
        this.defaultvalue = other.getDefaultvalue();
        this.isvalid = other.getIsvalid();

    }

    public void copyNotNullProperty(OptVariable other) {

        if (other.getOptid() != null)
            this.setOptid(other.getOptid());
        if (other.getVarname() != null)
            this.setVarname(other.getVarname());

        if (other.getVardesc() != null)
            this.vardesc = other.getVardesc();
        if (other.getVartype() != null)
            this.vartype = other.getVartype();
        if (other.getDefaultvalue() != null)
            this.defaultvalue = other.getDefaultvalue();
        if (other.getIsvalid() != null)
            this.isvalid = other.getIsvalid();

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
}
