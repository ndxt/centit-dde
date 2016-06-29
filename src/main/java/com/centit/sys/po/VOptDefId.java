package com.centit.sys.po;

public class VOptDefId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String optid;    //业务编号
    private String optcode;//操作代码

    public VOptDefId() {

    }

    public VOptDefId(String optid, String optcode) {
        this.optcode = optcode;
        this.optid = optid;
    }

    public String getOptid() {
        return optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOptcode() {
        return optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }
}
