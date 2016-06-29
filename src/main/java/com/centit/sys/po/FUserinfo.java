package com.centit.sys.po;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.centit.sys.service.CodeRepositoryUtil;

/**
 * FUserinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */
// 系统用户信息表
public class FUserinfo implements java.io.Serializable, Comparator<FUserinfo> {

    // Fields
    private static final long serialVersionUID = -1753127177790732963L;

    private String usercode; // 用户代码
    private String userpin; // 用户密码
    private String isvalid; // 状态
    private String loginname; // 用户登录名
    private String username; // 用户姓名
    private String userword; // 用户自定义编码 在同一个机构中不重复

    private String userdesc; // 用户描述
    private Long logintimes; // 登录次数
    private Date activetime; // 最后一次登录时间
    private String loginip; // 登录地址
    private String usernamepinyin; //	（外不）
    private String regemail; // 注册email
    private String regCellPhone; // 注册手机号
    private String userPwd;

    private String primaryUnit; // 用户的主机构，只有在数据字典中有效（外不）
    private List<FUserunit> userUnits;    //（外不)
    private Long userorder;     //用户排序
    private Long addrbookid; // 通讯id 	（外不）


    private String nameFisrtLetter;

    private Date createDate = new Date();
    private Date lastModifyDate;

    /**
     * default constructor
     */
    public FUserinfo() {
        userUnits = null;
        primaryUnit = null;
    }

    @Override
    public int hashCode() {
        return loginname.hashCode();
    }

    @Override
    public int compare(FUserinfo o1, FUserinfo o2) {
        return o1.getLoginname().compareTo(o2.getLoginname());

    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof FUserinfo)) {
            return false;
        }

        return loginname.equals(((FUserinfo) obj).getLoginname());
    }

    /**
     * minimal constructor
     */
    public FUserinfo(String usercode, String userstate, String loginname,
                     String username) {
        this.usercode = usercode;
        this.isvalid = userstate;
        this.username = username;
        this.loginname = loginname;
        userUnits = null;
        primaryUnit = null;
    }

    /**
     * full constructor
     */
    public FUserinfo(String usercode, String userpin, String userstate,
                     String loginname, String username, String userword, String userdesc,
                     Long logintimes, Date activeime, String loginip, Long addrbookid, Long userorder, String password) {
        this.usercode = usercode;
        this.userpin = userpin;
        this.isvalid = userstate;
        this.username = username;
        this.userword = userword;
        this.userorder = userorder;
        this.userdesc = userdesc;
        this.logintimes = logintimes;
        this.activetime = activeime;
        this.loginip = loginip;
        this.loginname = loginname;
        this.addrbookid = addrbookid;
        // userUnits=null;
        primaryUnit = null;
        this.userPwd = password;
    }

    // Property accessors
    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String password) {
        this.userPwd = password;
    }

    public String getUserword() {
        return userword;
    }

    public void setUserword(String userword) {
        this.userword = userword;
    }

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public Long getUserorder() {
        return userorder;
    }

    public void setUserorder(Long userorder) {
        this.userorder = userorder;
    }

    public String getUserpin() {
        return this.userpin;
    }

    public void setUserpin(String userpin) {
        this.userpin = userpin;
    }

    /**
     * T:生效 F:无效
     *
     * @return
     */
    public String getIsvalid() {
        if (this.isvalid == null)
            return "F";
        return this.isvalid;
    }

    /**
     * @param userstate T:生效 F:无效
     */
    public void setIsvalid(String userstate) {
        this.isvalid = userstate;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("FUserinfo");
        sb.append("{usercode='").append(usercode).append('\'');
        sb.append(", userpin='").append(userpin).append('\'');
        sb.append(", isvalid='").append(isvalid).append('\'');
        sb.append(", loginname='").append(loginname).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", userdesc='").append(userdesc).append('\'');
        sb.append(", logintimes=").append(logintimes);
        sb.append(", activetime=").append(activetime);
        sb.append(", loginip='").append(loginip).append('\'');
        sb.append(", usernamepinyin='").append(usernamepinyin).append('\'');
        sb.append(", addrbookid=").append(addrbookid);
        sb.append(", regemail='").append(regemail).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserdesc() {
        return this.userdesc;
    }

    public void setUserdesc(String userdesc) {
        this.userdesc = userdesc;
    }

    public Long getLogintimes() {
        return this.logintimes;
    }

    public void setLogintimes(Long logintimes) {
        this.logintimes = logintimes;
    }

    public String getLoginip() {
        return this.loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public boolean isEnabled() {
        return "T".equals(isvalid);
    }

    public String getLoginname() {
        if (loginname == null)
            return "";
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public Date getActivetime() {
        return activetime;
    }

    public void setActivetime(Date activetime) {
        this.activetime = activetime;
    }

    public String getUsernamepinyin() {
        return usernamepinyin;
    }

    public void setUsernamepinyin(String usernamepinyin) {
        this.usernamepinyin = usernamepinyin;
    }

    public Long getAddrbookid() {
        return addrbookid;
    }

    public void setAddrbookid(Long addrbookid) {
        this.addrbookid = addrbookid;
    }

    public void setRegemail(String regemail) {
        this.regemail = regemail;
    }

    public String getRegemail() {
        return regemail;
    }

    public String getRegCellPhone() {
        return regCellPhone;
    }

    public void setRegCellPhone(String regCellPhone) {
        this.regCellPhone = regCellPhone;
    }

    public String getPrimaryUnit() {
        return primaryUnit;
    }

    public void setPrimaryUnit(String primaryUnit) {
        this.primaryUnit = primaryUnit;
    }

    public List<FUserunit> getUserUnits() {
        if (userUnits == null)
            userUnits = new ArrayList<FUserunit>();
        return userUnits;
    }

    public void setUserUnits(List<FUserunit> userUnits) {
        this.userUnits = userUnits;
        if (userUnits != null) {
            for (FUserunit uu : userUnits) {
                if (getPrimaryUnit() == null || "T".equals(uu.getIsprimary())) {
                    setPrimaryUnit(uu.getUnitcode());
                }
            }
        }
    }

    public void copy(FUserinfo other) {
        this.usercode = other.getUsercode();
        this.userpin = other.getUserpin();
        this.isvalid = other.getIsvalid();
        this.loginname = other.getLoginname();
        this.username = other.getUsername();
        this.userword = other.getUserword();

        this.userdesc = other.getUserdesc();
        this.logintimes = other.getLogintimes();
        this.activetime = other.getActivetime();
        this.loginip = other.getLoginip();
        this.addrbookid = other.getAddrbookid();
        this.regemail = other.getRegemail();
        this.userPwd = other.getUserPwd();
        this.userorder = other.getUserorder();
    }

    public void copyNotNullProperty(FUserinfo other) {
        if (other.getUsercode() != null)
            this.usercode = other.getUsercode();
        if (other.getUserpin() != null)
            this.userpin = other.getUserpin();
        if (other.getIsvalid() != null)
            this.isvalid = other.getIsvalid();
        if (other.getLoginname() != null)
            this.loginname = other.getLoginname();
        if (other.getUsername() != null)
            this.username = other.getUsername();
        if (other.getUserword() != null)
            this.userword = other.getUserword();
        if (other.getUserdesc() != null)
            this.userdesc = other.getUserdesc();
        if (other.getLogintimes() != null)
            this.logintimes = other.getLogintimes();
        if (other.getActivetime() != null)
            this.activetime = other.getActivetime();
        if (other.getLoginip() != null)
            this.loginip = other.getLoginip();
        if (other.getAddrbookid() != null)
            this.addrbookid = other.getAddrbookid();
        if (other.getRegemail() != null)
            this.regemail = other.getRegemail();
        if (other.getUserPwd() != null)
            this.userPwd = other.getUserPwd();
        if (other.getUserorder() != null)
            this.userorder = other.getUserorder();

    }

    public void clearProperties() {
        this.usercode = null;
        this.userpin = null;
        this.isvalid = null;
        this.loginname = null;
        this.username = null;
        this.userdesc = null;
        this.logintimes = null;
        this.activetime = null;
        this.loginip = null;
        this.addrbookid = null;
        this.regemail = null;
        this.userPwd = null;
        this.userorder = null;
    }


    public String getNameFisrtLetter() {
        return nameFisrtLetter;
    }


    public void setNameFisrtLetter(String nameFisrtLetter) {
        this.nameFisrtLetter = nameFisrtLetter;
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
        StringBuilder sb = new StringBuilder();
        sb.append("用户信息:[");
        sb.append("登陆名=").append(loginname);
        sb.append(", 用户名=").append(CodeRepositoryUtil.getValue("usercode", this.usercode));
        sb.append(", 用户描述=").append(null == userdesc ? "" : userdesc);
        sb.append(", isvalid=").append("T".equals(isvalid) ? "启用" : "禁用");
        sb.append("]");
        return sb.toString();
    }

}
