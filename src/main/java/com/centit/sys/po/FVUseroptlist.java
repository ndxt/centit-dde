package com.centit.sys.po;

/**
 * FVUseroptlist entity.
 *
 * @author MyEclipse Persistence Tools
 */
//用户业务操作 view
public class FVUseroptlist implements java.io.Serializable {

    // Fields
    private static final long serialVersionUID = 1L;
    private FVUseroptlistId id;    //主键
    private String optname;        //业务名字
    private String optid;        //业务编号
    private String optmethod;    //业务方法？？

    // Constructors

    /**
     * default constructor
     */
    public FVUseroptlist() {
    }

    /**
     * full constructor
     */
    public FVUseroptlist(FVUseroptlistId id) {
        this.id = id;
    }

    /**
     * full constructor
     */
    public FVUseroptlist(FVUseroptlistId id, String optname,
                         String optid, String optmethod) {
        this.id = id;
        this.optname = optname;
        this.optid = optid;
        this.optmethod = optmethod;
    }
    // Property accessors

    public FVUseroptlistId getId() {
        return this.id;
    }

    public void setId(FVUseroptlistId id) {
        this.id = id;
    }

    public String getOptname() {
        return this.optname;
    }

    public void setOptname(String optname) {
        this.optname = optname;
    }

    public String getOptid() {
        return this.optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOptmethod() {
        return this.optmethod;
    }

    public void setOptmethod(String optmethod) {
        this.optmethod = optmethod;
    }

    public void copy(FVUseroptlist other) {
        this.id = other.getId();
        this.optname = other.getOptname();
        this.optid = other.getOptid();
        this.optmethod = other.getOptmethod();
    }

    public void copyNotNullProperty(FVUseroptlist other) {

        if (other.getOptname() != null)
            this.optname = other.getOptname();
        if (other.getOptid() != null)
            this.optid = other.getOptid();
        if (other.getOptmethod() != null)
            this.optmethod = other.getOptmethod();
    }
}
