package com.centit.sys.po;

import java.util.Date;
import java.util.List;

import com.centit.core.utils.LabelValueBean;
import com.centit.sys.service.CodeRepositoryUtil;

/**
 * FUserunit entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户所属机构表
public class FUserunit implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private FUserunitId id;    //主键
    private String rankmemo;    //备注
    private String isprimary;    //是否为主
    private String unitname;    //机构名称
    private Date createDate = new Date();
    private Date lastModifyDate;
    /**
     * 仅在系统缓存中使用
     */
    private int xzRank;
    // Constructors

    /**
     * default constructor
     */
    public FUserunit() {
    }

    /**
     * minimal constructor
     */
    public FUserunit(FUserunitId id, String isprimary) {
        this.id = id;
        //this.userstation = userstation;
        this.isprimary = isprimary;
    }

    /**
     * full constructor
     */
    public FUserunit(FUserunitId id, //String userstation, String userrank,
                     String rankmemo, String isprimary) {
        this.id = id;
        //this.userstation = userstation;
        //this.userrank = userrank;
        this.rankmemo = rankmemo;
        this.isprimary = isprimary;
    }

    // Property accessors

    public FUserunitId getId() {
        return this.id;
    }

    public void setId(FUserunitId id) {
        this.id = id;
    }

    public String getUnitcode() {
        if (this.id == null)
            this.id = new FUserunitId();
        return this.id.getUnitcode();
    }

    public void setUnitcode(String unitcode) {
        if (this.id == null)
            this.id = new FUserunitId();
        this.id.setUnitcode(unitcode);
    }

    public String getUsercode() {
        if (this.id == null)
            this.id = new FUserunitId();
        return this.id.getUsercode();
    }

    public void setUsercode(String usercode) {
        if (this.id == null)
            this.id = new FUserunitId();
        this.id.setUsercode(usercode);
    }

    public String getUserstation() {
        if (this.id == null)
            this.id = new FUserunitId();
        return this.id.getUserstation();
    }

    public void setUserstation(String userstation) {
        if (this.id == null)
            this.id = new FUserunitId();
        this.id.setUserstation(userstation);
    }

    public String getUserrank() {
        if (this.id == null)
            this.id = new FUserunitId();
        return this.id.getUserrank();
    }

    public void setUserrank(String userrank) {
        if (this.id == null)
            this.id = new FUserunitId();
        this.id.setUserrank(userrank);
    }

    public String getRankmemo() {
        return this.rankmemo;
    }

    public void setRankmemo(String rankmemo) {
        this.rankmemo = rankmemo;
    }

    public String getIsprimary() {
        return this.isprimary;
    }

    public void setIsprimary(String isprimary) {
        this.isprimary = isprimary;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public int getXzRank() {
        return xzRank;
    }

    public void setXzRank(int xzRank) {
        this.xzRank = xzRank;
    }

    public void copy(FUserunit other) {

        this.setUserstation(other.getUserstation());
        this.setUserrank(other.getUserrank());
        this.rankmemo = other.getRankmemo();
        this.isprimary = other.getIsprimary();
    }

    public void copyNotNullProperty(FUserunit other) {

        if (other.getUserstation() != null)
            this.setUserstation(other.getUserstation());
        if (other.getUserrank() != null)
            this.setUserrank(other.getUserrank());
        if (other.getRankmemo() != null)
            this.rankmemo = other.getRankmemo();
        if (other.getIsprimary() != null)
            this.isprimary = other.getIsprimary();
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


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof FUserunit)) {
            return false;
        }
        return id.equals(((FUserunit) obj).getId());
    }

    public String display() {
        List<LabelValueBean> lvb = CodeRepositoryUtil
                .getLabelValueBeans("RankType");

        String userRank = this.id.getUserrank();
        for (LabelValueBean labelValueBean : lvb) {
            if (labelValueBean.getValue().equals(userRank)) {
                userRank = labelValueBean.getLabel();

                break;
            }
        }

        return "[机构信息 ["
                + "机构名称="
                + CodeRepositoryUtil
                .getValue("unitcode", this.id.getUnitcode())
                + ", 岗位="
                + CodeRepositoryUtil
                .getValue("unitcode", this.id.getUnitcode()) + ",职务="
                + userRank + "] ]";
    }

}
