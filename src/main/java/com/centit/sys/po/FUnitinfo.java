package com.centit.sys.po;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.centit.core.utils.LabelValueBean;
import com.centit.sys.service.CodeRepositoryUtil;


/**
 * FUnitinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */
// 机构信息表
public class FUnitinfo implements java.io.Serializable, Comparator<FUnitinfo> {

    // Fields

    private static final long serialVersionUID = -2538006375160615889L;

    private String unitcode; // 机构代码
    private String parentunit; // 上级机构代码
    private String unittype; // 机构类别
    private String isvalid; // 状态
    private String unitname;// 机构名称
    private String unitshortname;//部门简称
    private String unitdesc; // 机构描述
    private Long addrbookid; // 通讯主体id
    private String unitword;   //机构代码
    private Long unitgrade;    //机构等级   
    private Long unitorder;    //机构排序号
    private Long level0;

    private String unitalias;//同步LDAP数据时，机构代码较长，存入此字段，(外部)


    public Long getLevel0() {
        return level0;
    }

    public void setLevel0(Long level0) {
        this.level0 = level0;
    }

    public String getUnitalias() {
        return unitalias;
    }

    public void setUnitalias(String unitalias) {
        this.unitalias = unitalias;
    }

    private List<FUnitinfo> child = new ArrayList<FUnitinfo>();

    public List<FUnitinfo> getChild() {
        return child;
    }

    public void setChild(List<FUnitinfo> child) {
        this.child = child;
    }

    private Date createDate = new Date();
    private Date lastModifyDate;

    public String getUnitword() {
        return unitword;
    }

    public void setUnitword(String unitword) {
        this.unitword = unitword;
    }

    public Long getUnitgrade() {
        return unitgrade;
    }

    public void setUnitgrade(Long unitgrade) {
        this.unitgrade = unitgrade;
    }

    public Long getUnitorder() {
        return unitorder;
    }

    public void setUnitorder(Long unitorder) {
        this.unitorder = unitorder;
    }

    /**
     * 这个变量只在系统数据字典缓存中有用
     */
    private Set<String> subUnits; // 所有子机构代码集合
    // private Set<String> subUsers; //所有下属用户代码集合
    private List<FUserunit> userUnits;

    // Constructors

    /**
     * default constructor
     */
    public FUnitinfo() {
        subUnits = null;
        userUnits = null;
    }

    /**
     * minimal constructor
     */
    public FUnitinfo(String unitcode, String unitstate, String unitname, String prefix, String unitdazt) {
        this.unitcode = unitcode;
        this.isvalid = unitstate;
        this.unitname = unitname;
        subUnits = null;
        userUnits = null;
    }

    /**
     * full constructor
     */
    public FUnitinfo(String unitcode, String parentunit, String unittype,
                     String unitstate, String unitname, String unitdesc,
                     Long addrbookid, String unitshortname, String unitword, Long unitorder, Long unitgrade, String prefix, String unitdazt) {
        this.unitcode = unitcode;
        this.parentunit = parentunit;
        this.unittype = unittype;
        this.isvalid = unitstate;
        this.unitname = unitname;
        this.unitdesc = unitdesc;
        this.addrbookid = addrbookid;
        this.unitshortname = unitshortname;
        this.unitword = unitword;
        this.unitorder = unitorder;
        this.unitgrade = unitgrade;
        subUnits = null;
        userUnits = null;
    }

    // Property accessors
    public String getUnitcode() {
        return this.unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getUnitshortname() {
        return unitshortname;
    }

    public void setUnitshortname(String unitshortname) {
        this.unitshortname = unitshortname;
    }

    public String getParentunit() {
        return this.parentunit;
    }

    public void setParentunit(String parentunit) {
        this.parentunit = parentunit;
    }

    public String getUnittype() {
        return this.unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }

    public String getIsvalid() {
        return this.isvalid;
    }

    public void setIsvalid(String unitstate) {
        this.isvalid = unitstate;
    }

    public String getUnitname() {
        return this.unitname;
    }

    public String toString() {
        return this.unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitdesc() {
        return this.unitdesc;
    }

    public void setUnitdesc(String unitdesc) {
        this.unitdesc = unitdesc;
    }

    public Long getAddrbookid() {
        return addrbookid;
    }

    public void setAddrbookid(Long addrbookid) {
        this.addrbookid = addrbookid;
    }

    public Set<String> getSubUnits() {
        if (subUnits == null)
            subUnits = new HashSet<String>();
        return subUnits;
    }

    public void setSubUnits(Set<String> subUnits) {
        this.subUnits = subUnits;
    }

    public List<FUserunit> getSubUserUnits() {
        if (userUnits == null)
            userUnits = new ArrayList<FUserunit>();
        return userUnits;
    }

    public void setSubUserUnits(List<FUserunit> SUs) {
        this.userUnits = SUs;
    }

    public void copy(FUnitinfo other) {

        this.parentunit = other.getParentunit();
        this.unittype = other.getUnittype();
        this.isvalid = other.getIsvalid();
        this.unitname = other.getUnitname();
        this.unitshortname = other.getUnitshortname();
        this.unitdesc = other.getUnitdesc();
        this.addrbookid = other.getAddrbookid();
        this.unitword = other.getUnitword();
        this.unitgrade = other.getUnitgrade();
        this.unitorder = other.getUnitorder();

    }

    public void copyNotNullProperty(FUnitinfo other) {

        if (other.getUnitcode() != null)
            this.unitcode = other.getUnitcode();
        if (other.getParentunit() != null)
            this.parentunit = other.getParentunit();
        if (other.getUnittype() != null)
            this.unittype = other.getUnittype();
        if (other.getIsvalid() != null)
            this.isvalid = other.getIsvalid();
        if (other.getUnitname() != null)
            this.unitname = other.getUnitname();
        if (other.getUnitdesc() != null)
            this.unitdesc = other.getUnitdesc();
        if (other.getAddrbookid() != null)
            this.addrbookid = other.getAddrbookid();
        if (other.getUnitshortname() != null)
            this.unitshortname = other.getUnitshortname();
        if (other.getUnitword() != null)
            this.unitword = other.getUnitword();
        if (other.getUnitorder() != null)
            this.unitorder = other.getUnitorder();
        if (other.getUnitgrade() != null)
            this.unitgrade = other.getUnitgrade();
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
        if (null != unitalias) {
            return unitalias.hashCode();
        }
        return unitcode.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof FUnitinfo)) {
            return false;
        }
        if (null != unitalias) {
            return unitalias.equals(((FUnitinfo) obj).getUnitalias());
        }
        return unitcode.equals(((FUnitinfo) obj).getUnitcode());
    }

    @Override
    public int compare(FUnitinfo o1, FUnitinfo o2) {
        if (null != unitalias) {
            return o1.unitalias.compareTo(o2.getUnitalias());
        }
        return o1.unitcode.compareTo(o2.getUnitcode());
    }

    public String display() {
        String unittype = this.unittype;
        List<LabelValueBean> labelValueBeans = CodeRepositoryUtil.getLabelValueBeans("UnitType");
        for (LabelValueBean labelValueBean : labelValueBeans) {
            if (labelValueBean.getValue().equals(unittype)) {
                unittype = labelValueBean.getLabel();
            }

            break;
        }

        return "部门信息 [部门名称=" + CodeRepositoryUtil.getValue("unitcode", this.unitcode) + ", 上级部门="
                + CodeRepositoryUtil.getValue("unitcode", this.parentunit)
                + ", 部门类型=" + unittype + ", 部门状态=" + ("T".equals(isvalid) ? "启用" : "禁用")
                + ", 部门备注=" + (null == unitdesc ? "" : unitdesc) + "]";
    }


}
