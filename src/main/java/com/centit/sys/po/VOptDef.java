package com.centit.sys.po;

public class VOptDef implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String optcode;
    private String optid;
    private String optname;    //操作名称
    private String optmethod;//操作方法
    private String methodname;

    private String optdesc;    //操作说明
    private String preoptid;            //上级业务模块编号
    private String opturl;                //业务url（b/s）
    private String formcode;            //界面代码(C/S)
    private String opttype;                //业务类别
    private Long msgno;                    //消息编号
    private String msgprm;                //业务参数
    private String isintoolbar;            //是否放入工具栏
    private Long imgindex;                //图标编号
    private String topoptid;            //顶层业务编号
    private Long orderind;                //业务顺序
    private String wfcode;                //流程代码


    public VOptDef() {

    }

    public VOptDef(String optcode) {
        this.optcode = optcode;
    }

    public VOptDef(String optcode, String optid, String optname, String preoptid, String optmethod, String methodname, String optdesc, String opturl, String formcode, String opttype, Long msgno, String msgprm
            , String isintoolbar, Long imgindex, String topoptid, Long orderind) {
        this.optcode = optcode;
        this.optid = optid;
        this.formcode = formcode;
        this.imgindex = imgindex;
        this.methodname = methodname;
        this.isintoolbar = isintoolbar;
        this.msgno = msgno;
        this.msgprm = msgprm;
        this.preoptid = preoptid;
        this.optdesc = optdesc;
        this.optmethod = optmethod;
        this.optname = optname;
        this.opttype = opttype;
        this.opturl = opturl;
        this.orderind = orderind;
        this.topoptid = topoptid;
    }

    public String getMethodname() {
        return methodname;
    }

    public void setMethodname(String methodname) {
        this.methodname = methodname;
    }

    public String getPreoptid() {
        return preoptid;
    }

    public void setPreoptid(String preoptid) {
        this.preoptid = preoptid;
    }

    public String getOptname() {
        return optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getOptmethod() {
        return optmethod;
    }

    public void setOptmethod(String optmethod) {
        this.optmethod = optmethod;
    }

    public String getOptdesc() {
        return optdesc;
    }

    public void setOptdesc(String optdesc) {
        this.optdesc = optdesc;
    }

    public String getOpturl() {
        return opturl;
    }

    public void setOpturl(String opturl) {
        this.opturl = opturl;
    }

    public String getFormcode() {
        return formcode;
    }

    public void setFormcode(String formcode) {
        this.formcode = formcode;
    }

    public String getOpttype() {
        return opttype;
    }

    public void setOpttype(String opttype) {
        this.opttype = opttype;
    }

    public Long getMsgno() {
        return msgno;
    }

    public void setMsgno(Long msgno) {
        this.msgno = msgno;
    }

    public String getMsgprm() {
        return msgprm;
    }

    public void setMsgprm(String msgprm) {
        this.msgprm = msgprm;
    }

    public String getIsintoolbar() {
        return isintoolbar;
    }

    public void setIsintoolbar(String isintoolbar) {
        this.isintoolbar = isintoolbar;
    }

    public Long getImgindex() {
        return imgindex;
    }

    public void setImgindex(Long imgindex) {
        this.imgindex = imgindex;
    }

    public String getTopoptid() {
        return topoptid;
    }

    public void setTopoptid(String topoptid) {
        this.topoptid = topoptid;
    }

    public Long getOrderind() {
        return orderind;
    }

    public void setOrderind(Long orderind) {
        this.orderind = orderind;
    }

    public String getWfcode() {
        return wfcode;
    }

    public void setWfcode(String wfcode) {
        this.wfcode = wfcode;
    }

    public String getOptcode() {
        return optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public String getOptid() {
        return optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }
}
