package com.centit.sys.po;

import java.util.Date;

import com.centit.sys.service.CodeRepositoryUtil;

/**
 * FRoleinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */
//角色信息表
public class FRoleinfo implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private String rolecode;     //角色代码
    private String rolename;     //角色名称
    private String isvalid;        //是否生效
    private String roledesc;    //角色描述
    private Date createDate = new Date();
    private Date lastModifyDate;
    /**
     * 表单操作数据
     */
    private String optcodelist;

    // Constructors

    public String getOptcodelist() {
        return optcodelist;
    }

    public void setOptcodelist(String optcodelist) {
        this.optcodelist = optcodelist;
    }

    /**
     * default constructor
     */
    public FRoleinfo() {
    }

    /**
     * minimal constructor
     */
    public FRoleinfo(String rolecode, String isvalid) {
        this.rolecode = rolecode;
        this.isvalid = isvalid;
    }

    /**
     * full constructor
     */
    public FRoleinfo(String rolecode, String rolename, String isvalid,
                     String roledesc) {
        this.rolecode = rolecode;
        this.rolename = rolename;
        this.isvalid = isvalid;
        this.roledesc = roledesc;
    }

    // Property accessors
    public String getRolecode() {
        return this.rolecode;
    }

    public void setRolecode(String rolecode) {
        this.rolecode = rolecode;
    }

    public String getRolename() {
        return this.rolename;
    }

    public String toString() {
        return this.rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public String getIsvalid() {
        return this.isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getRoledesc() {
        return this.roledesc;
    }

    public void setRoledesc(String roledesc) {
        this.roledesc = roledesc;
    }

    public void copyNotNullProperty(FRoleinfo other) {
        //this.rolecode = other.getRolecode();
        if (other.getRolename() != null)
            this.rolename = other.getRolename();
        if (other.getIsvalid() != null)
            this.isvalid = other.getIsvalid();
        if (other.getRoledesc() != null)
            this.roledesc = other.getRoledesc();
    }

    public void copy(FRoleinfo other) {
        this.rolecode = other.getRolecode();
        this.rolename = other.getRolename();
        this.isvalid = other.getIsvalid();
        this.roledesc = other.getRoledesc();
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
        return "角色信息 [角色名=" + (null == rolename ? CodeRepositoryUtil.getValue("rolecode", rolecode) : rolename)
                + ", 是否生效=" + ("T".equals(isvalid) ? "启用" : "禁用")
                + ", 角色描述=" + roledesc + "]";
    }
}