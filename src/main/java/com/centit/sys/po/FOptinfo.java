package com.centit.sys.po;

import java.util.Date;
import java.util.List;

import com.centit.core.utils.LabelValueBean;
import com.centit.sys.service.CodeRepositoryUtil;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
//业务模块表	
public class FOptinfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String optid;                //业务编号
    private String preoptid;            //上级业务模块编号
    private String optname;                //业务名称
    private String opttype;                //业务类别
    private String formcode;            //界面代码(C/S)
    private String opturl;                //业务url（b/s）
    private Long msgno;                    //消息编号
    private String msgprm;                //业务参数
    private String isintoolbar;            //是否放入工具栏
    private Long imgindex;                //图标编号
    private String topoptid;            //顶层业务编号
    private String wfcode;                //流程代码
    private String pageType;  //页面打开方式 D: DIV I： iFrame

    private Long orderind;                //业务顺序
    private Date createDate = new Date();
    private Date lastModifyDate;
    // Constructors

    /**
     * default constructor
     */
    public FOptinfo() {
    }

    /**
     * minimal constructor
     */
    public FOptinfo(
            String optid
            , String optname) {


        this.optid = optid;

        this.optname = optname;
    }

    /**
     * full constructor
     */
    public FOptinfo(
            String optid
            , String preoptid, String optname, String formcode,
            String opturl, Long msgno, String msgprm, String isintoolbar,
            Long imgindex, String topoptid, String opttype,
            String wfcode, Long orderind, String pageType) {


        this.optid = optid;

        this.preoptid = preoptid;
        this.optname = optname;
        this.formcode = formcode;
        this.opturl = opturl;
        this.msgno = msgno;
        this.msgprm = msgprm;
        this.isintoolbar = isintoolbar;
        this.imgindex = imgindex;
        this.topoptid = topoptid;
        this.opttype = opttype;
        this.wfcode = wfcode;
        this.orderind = orderind;
        this.pageType = pageType;
    }


    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }
    // Property accessors

    public String getPreoptid() {
        return this.preoptid;
    }

    public void setPreoptid(String preoptid) {
        this.preoptid = preoptid;
    }

    public String toString() {
        return this.optname;
    }

    public String getOptname() {
        return this.optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getFormcode() {
        return this.formcode;
    }

    public void setFormcode(String formcode) {
        this.formcode = formcode;
    }

    public String getOpturl() {
        if (this.opturl == null)
            return "...";
        return this.opturl;
    }

    public void setOpturl(String opturl) {
        this.opturl = opturl;
    }

    public String getWfcode() {
        return wfcode;
    }

    public void setWfcode(String wfcode) {
        this.wfcode = wfcode;
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

    public String getOpttype() {
        return this.opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
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

    public void copy(FOptinfo other) {

        this.preoptid = other.getPreoptid();
        this.optname = other.getOptname();
        this.formcode = other.getFormcode();
        this.opturl = other.getOpturl();
        this.msgno = other.getMsgno();
        this.msgprm = other.getMsgprm();
        this.isintoolbar = other.getIsintoolbar();
        this.imgindex = other.getImgindex();
        this.topoptid = other.getTopoptid();
        this.opttype = other.getOpttype();
        this.wfcode = other.getWfcode();
        this.orderind = other.getOrderind();
        this.pageType = other.getPageType();
    }

    public void copyNotNullProperty(FOptinfo other) {

        if (other.getPreoptid() != null)
            this.preoptid = other.getPreoptid();
        if (other.getOptname() != null)
            this.optname = other.getOptname();
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
        if (other.getOpttype() != null)
            this.opttype = other.getOpttype();
        if (other.getWfcode() != null)
            this.wfcode = other.getWfcode();
        if (other.getOrderind() != null)
            this.orderind = other.getOrderind();
        if (other.getPageType() != null)
            this.pageType = other.getPageType();
    }

    public void clearProperties() {
        this.preoptid = null;
        this.optname = null;
        this.formcode = null;
        this.opturl = null;
        this.msgno = null;
        this.msgprm = null;
        this.isintoolbar = null;
        this.imgindex = null;
        this.topoptid = null;
        this.opttype = null;
        this.wfcode = null;
        this.orderind = null;
        this.pageType = "I";
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
        String opttype = this.opttype;
        List<LabelValueBean> lbls = CodeRepositoryUtil.getLabelValueBeans("OPTTYPE");
        for (LabelValueBean lbl : lbls) {
            if (opttype.equals(lbl.getValue())) {
                opttype = lbl.getLabel();

                break;
            }
        }


        return "业务模块信息 [业务名称=" + CodeRepositoryUtil.getValue("optid", optid)
                + ", 上级业务名称=" + CodeRepositoryUtil.getValue("optid", preoptid)
                + ", 业务类型=" + opttype + "]";
    }

}
