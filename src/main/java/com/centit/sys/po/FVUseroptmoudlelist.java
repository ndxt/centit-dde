package com.centit.sys.po;

/**
 * FVUseroptmoudlelistId entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class FVUseroptmoudlelist implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    // Fields
    private String usercode;        //用户代码
    private String optid;            //业务模块类
    private String optname;
    private String preoptid;
    private String formcode;
    private String opturl;
    private String opttype;
    private Long msgno;
    private String msgprm;
    private String isintoolbar;
    private Long imgindex;
    private String topoptid;
    private Long orderind;
    private String pageType;  //页面打开方式 D: DIV I： iFrame

    // Constructors

    /**
     * default constructor
     */
    public FVUseroptmoudlelist() {
    }

    /**
     * minimal constructor
     */
    public FVUseroptmoudlelist(String usercode, String optid, String optname) {
        this.usercode = usercode;
        this.optid = optid;
        this.optname = optname;
    }

    /**
     * full constructor
     */
    public FVUseroptmoudlelist(String usercode, String optid, String optname,
                               String preoptid, String formcode, String opturl, Long msgno,
                               String msgprm, String isintoolbar, Long imgindex, String topoptid,
                               String opttype,
                               Long orderind, String pageType) {
        this.usercode = usercode;
        this.optid = optid;
        this.optname = optname;
        this.preoptid = preoptid;
        this.formcode = formcode;
        this.opturl = opturl;
        this.msgno = msgno;
        this.msgprm = msgprm;
        this.isintoolbar = isintoolbar;
        this.imgindex = imgindex;
        this.topoptid = topoptid;
        this.orderind = orderind;
        this.pageType = pageType;
    }

    // Property accessors

    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOptname() {
        return this.optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getPreoptid() {
        return this.preoptid;
    }

    public void setPreoptid(String preoptid) {
        this.preoptid = preoptid;
    }

    public String getFormcode() {
        return this.formcode;
    }

    public void setFormcode(String formcode) {
        this.formcode = formcode;
    }

    public String getOpturl() {
        return this.opturl;
    }

    public void setOpturl(String opturl) {
        this.opturl = opturl;
    }

    public Long getMsgno() {
        return this.msgno;
    }

    public void setMsgno(Long msgno) {
        this.msgno = msgno;
    }

    public String getMsgprm() {
        return this.msgprm;
    }

    public void setMsgprm(String msgprm) {
        this.msgprm = msgprm;
    }

    public String getIsintoolbar() {
        return this.isintoolbar;
    }

    public void setIsintoolbar(String isintoolbar) {
        this.isintoolbar = isintoolbar;
    }

    public Long getImgindex() {
        return this.imgindex;
    }

    public void setImgindex(Long imgindex) {
        this.imgindex = imgindex;
    }

    public String getTopoptid() {
        return this.topoptid;
    }

    public void setTopoptid(String topoptid) {
        this.topoptid = topoptid;
    }

    public Long getOrderind() {
        return this.orderind;
    }

    public void setOrderind(Long orderind) {
        this.orderind = orderind;
    }

    /**
     * 页面打开方式 D: DIV I： iFrame
     *
     * @return
     */
    public String getPageType() {
        return pageType;
    }

    /**
     * 页面打开方式 D: DIV I： iFrame
     *
     * @param pageType
     */
    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FVUseroptmoudlelist))
            return false;
        FVUseroptmoudlelist castOther = (FVUseroptmoudlelist) other;

        return ((this.getUsercode() == castOther.getUsercode()) || (this
                .getUsercode() != null
                && castOther.getUsercode() != null && this.getUsercode()
                .equals(castOther.getUsercode())))
                && ((this.getOptid() == castOther.getOptid()) || (this
                .getOptid() != null
                && castOther.getOptid() != null && this.getOptid()
                .equals(castOther.getOptid())))
                && ((this.getOptname() == castOther.getOptname()) || (this
                .getOptname() != null
                && castOther.getOptname() != null && this.getOptname()
                .equals(castOther.getOptname())))
                && ((this.getPreoptid() == castOther.getPreoptid()) || (this
                .getPreoptid() != null
                && castOther.getPreoptid() != null && this
                .getPreoptid().equals(castOther.getPreoptid())))
                && ((this.getFormcode() == castOther.getFormcode()) || (this
                .getFormcode() != null
                && castOther.getFormcode() != null && this
                .getFormcode().equals(castOther.getFormcode())))
                && ((this.getOpturl() == castOther.getOpturl()) || (this
                .getOpturl() != null
                && castOther.getOpturl() != null && this.getOpturl()
                .equals(castOther.getOpturl())))
                && ((this.getMsgno() == castOther.getMsgno()) || (this
                .getMsgno() != null
                && castOther.getMsgno() != null && this.getMsgno()
                .equals(castOther.getMsgno())))
                && ((this.getMsgprm() == castOther.getMsgprm()) || (this
                .getMsgprm() != null
                && castOther.getMsgprm() != null && this.getMsgprm()
                .equals(castOther.getMsgprm())))
                && ((this.getIsintoolbar() == castOther.getIsintoolbar()) || (this
                .getIsintoolbar() != null
                && castOther.getIsintoolbar() != null && this
                .getIsintoolbar().equals(castOther.getIsintoolbar())))
                && ((this.getImgindex() == castOther.getImgindex()) || (this
                .getImgindex() != null
                && castOther.getImgindex() != null && this
                .getImgindex().equals(castOther.getImgindex())))
                && ((this.getTopoptid() == castOther.getTopoptid()) || (this
                .getTopoptid() != null
                && castOther.getTopoptid() != null && this
                .getTopoptid().equals(castOther.getTopoptid())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result
                + (getUsercode() == null ? 0 : this.getUsercode().hashCode());
        result = 37 * result
                + (getOptid() == null ? 0 : this.getOptid().hashCode());
        result = 37 * result
                + (getOptname() == null ? 0 : this.getOptname().hashCode());
        result = 37 * result
                + (getPreoptid() == null ? 0 : this.getPreoptid().hashCode());
        result = 37 * result
                + (getFormcode() == null ? 0 : this.getFormcode().hashCode());
        result = 37 * result
                + (getOpturl() == null ? 0 : this.getOpturl().hashCode());
        result = 37 * result
                + (getMsgno() == null ? 0 : this.getMsgno().hashCode());
        result = 37 * result
                + (getMsgprm() == null ? 0 : this.getMsgprm().hashCode());
        result = 37
                * result
                + (getIsintoolbar() == null ? 0 : this.getIsintoolbar()
                .hashCode());
        result = 37 * result
                + (getImgindex() == null ? 0 : this.getImgindex().hashCode());
        result = 37 * result
                + (getTopoptid() == null ? 0 : this.getTopoptid().hashCode());
        return result;
    }


    public void copy(FVUseroptmoudlelist other) {

        this.usercode = other.getUsercode();
        this.optname = other.getOptname();
        this.preoptid = other.getPreoptid();
        this.formcode = other.getFormcode();
        this.opturl = other.getOpturl();
        this.msgno = other.getMsgno();
        this.msgprm = other.getMsgprm();
        this.isintoolbar = other.getIsintoolbar();
        this.imgindex = other.getImgindex();
        this.topoptid = other.getTopoptid();
        this.orderind = other.getOrderind();
        this.pageType = other.getPageType();
    }

    public void copyNotNullProperty(FVUseroptmoudlelist other) {

        if (other.getUsercode() != null)
            this.usercode = other.getUsercode();
        if (other.getOptname() != null)
            this.optname = other.getOptname();
        if (other.getPreoptid() != null)
            this.preoptid = other.getPreoptid();
        if (other.getFormcode() != null)
            this.formcode = other.getFormcode();
        if (other.getOpturl() != null)
            this.opturl = other.getOpturl();
        if (other.getMsgno() != null)
            this.msgno = other.getMsgno();
        if (other.getMsgprm() != null)
            this.msgprm = other.getMsgprm();
        if (other.getIsintoolbar() != null)
            this.isintoolbar = other.getIsintoolbar();
        if (other.getImgindex() != null)
            this.imgindex = other.getImgindex();
        if (other.getTopoptid() != null)
            this.topoptid = other.getTopoptid();
        if (other.getOrderind() != null)
            this.orderind = other.getOrderind();
        if (other.getPageType() != null)
            this.pageType = other.getPageType();
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
    }
}
