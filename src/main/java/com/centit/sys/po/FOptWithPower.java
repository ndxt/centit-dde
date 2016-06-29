package com.centit.sys.po;

import java.util.ArrayList;
import java.util.List;

/**
 * FOptinfo entity.
 *
 * @author MyEclipse Persistence Tools
 */

public class FOptWithPower implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = -2765902477933166366L;

    private String optid;    //业务编号
    private String preoptid;    //上级业务编号
    private String optname;        //业务名称

    private List<String> powerlist;  //操作代码集合

    // Constructors

    /**
     * default constructor
     */
    public FOptWithPower() {
    }

    /**
     * minimal constructor
     */
    public FOptWithPower(String optid, String optname) {
        this.optid = optid;
        this.optname = optname;
    }

    /**
     * full constructor
     */
    public FOptWithPower(String optid, String preoptid, String optname) {
        this.optid = optid;
        this.preoptid = preoptid;
        this.optname = optname;
    }

    // Property accessors
    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

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

    public List<String> getPowerlist() {
        return powerlist;
    }

    public void setPowerlist(List<String> powerlist) {
        this.powerlist = powerlist;
    }

    public void addPower(String optcode) {
        if (powerlist == null)
            powerlist = new ArrayList<String>();
        powerlist.add(optcode);
    }

}