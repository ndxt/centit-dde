package com.centit.sys.po;

import java.util.Date;

import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.service.CodeRepositoryUtil;

/**
 * FUserrole entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户角色设定

public class FUserrole implements java.io.Serializable {

    // Fields
    //public final  SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    private static final long serialVersionUID = 8079422314053320707L;

    private FUserroleId id;        //主键
    private Date secededate;    //收回角色日期
    private String changedesc;    //更改角色日期
    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public FUserrole() {
    }

    /**
     * minimal constructor
     */
    public FUserrole(FUserroleId id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public FUserrole(FUserroleId id, Date secededate, String changedesc) {
        this.id = id;
        this.secededate = secededate;
        this.changedesc = changedesc;
    }

    // Property accessors

    public FUserroleId getId() {
        return this.id;
    }

    public void setId(FUserroleId id) {
        this.id = id;
    }

    public String getUsercode() {
        if (this.id == null)
            this.id = new FUserroleId();
        return this.id.getUsercode();
    }

    public void setUsercode(String usercode) {
        if (this.id == null)
            this.id = new FUserroleId();
        this.id.setUsercode(usercode);
    }

    public String getRolecode() {
        if (this.id == null)
            this.id = new FUserroleId();
        return this.id.getRolecode();
    }

    public void setRolecode(String rolecode) {
        if (this.id == null)
            this.id = new FUserroleId();
        this.id.setRolecode(rolecode);
    }

    public Date getObtaindate() {
        if (this.id == null)
            this.id = new FUserroleId();
        return this.id.getObtaindate();
    }

    public void setObtaindate(Date obtaindate) {
        if (this.id == null)
            this.id = new FUserroleId();
        this.id.setObtaindate(obtaindate);
    }

    public void setObtaindateToToday() {
        if (this.id == null)
            this.id = new FUserroleId();
        this.id.setObtaindateToToday();//( java.util.Date.valueOf( (new SimpleDateFormat("yyyy-MM-dd")).format(new java.util.Date() ) ));
    }
    /*
	public void setObtaindate(String sobtaindate){
		
		try {
			this.id.setObtaindate(sdfDate.parse(sobtaindate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	*/

    public Date getSecededate() {
        return this.secededate;
    }

    public void setSecededate(Date secededate) {
        this.secededate = secededate;
    }

    public void setSecededateToToday() {
        this.secededate = new java.util.Date();
    }

	/*
	public void setSecededate(String ssecededate) {
		try {
			this.secededate = sdfDate.parse(ssecededate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	*/

    public String getChangedesc() {
        return this.changedesc;
    }

    public void setChangedesc(String changedesc) {
        this.changedesc = changedesc;
    }

    public void copy(FUserrole other) {

        this.secededate = other.getSecededate();
        this.changedesc = other.getChangedesc();
    }

    public void copyNotNullProperty(FUserrole other) {

        if (other.getSecededate() != null)
            this.secededate = other.getSecededate();
        if (other.getChangedesc() != null)
            this.changedesc = other.getChangedesc();
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
        String secededateStr = "";
        if (null != this.secededate) {
            secededateStr = ", 到期时间=" + DatetimeOpt.convertDateToString(this.secededate, "yyyy-MM-dd HH:mm:ss");
        }

        return "[用户信息 [用户名 = " + CodeRepositoryUtil.getValue("usercode", id.getUsercode())
                + ",角色名 = " + CodeRepositoryUtil.getValue("rolecode", id.getRolecode()) + ", 获得角色时间 = "
                + DatetimeOpt.convertDateToString(id.getObtaindate(), "yyyy-MM-dd HH:mm:ss") + secededateStr + "]]";
    }
}