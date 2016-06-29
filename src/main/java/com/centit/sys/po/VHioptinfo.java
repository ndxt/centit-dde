package com.centit.sys.po;

/**
 * VHioptinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */
// 用户业务操作 view
public class VHioptinfo implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4764189819261358422L;
    // Fields
    private String topoptid;
    private String optname;
    private String optid;
    private String hiLevel;

    // Constructors

    /**
     * default constructor
     */
    public VHioptinfo() {
    }

    public String getTopoptid() {
        return topoptid;
    }

    public void setTopoptid(String topoptid) {
        this.topoptid = topoptid;
    }

    public String getOptname() {
        return optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getOptid() {
        return optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getHiLevel() {
        return hiLevel;
    }

    public void setHiLevel(String hiLevel) {
        this.hiLevel = hiLevel;
    }

}
