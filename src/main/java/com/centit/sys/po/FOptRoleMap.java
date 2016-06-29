package com.centit.sys.po;


public class FOptRoleMap implements java.io.Serializable {

    private static final long serialVersionUID = 396021378825483579L;

    private FRolepowerId id;

    private String opturl;
    private String optmethod;
    private String optid;


    public FOptRoleMap() {
    }

    /**
     * minimal constructor
     */
    public FOptRoleMap(FRolepowerId id) {
        this.id = id;
    }

    // Property accessors
    public FRolepowerId getId() {
        return this.id;
    }

    public void setId(FRolepowerId id) {
        this.id = id;
    }

    public FOptRoleMap(FRolepowerId id, String opturl, String optmethod) {
        this.opturl = opturl;
        this.optmethod = optmethod;
        this.id = id;
    }

    /**
     * full constructor
     */
    public FOptRoleMap(FRolepowerId id, String opturl, String optmethod, String rolecode,
                       String optid, String optcode) {
        this.opturl = opturl;
        this.optmethod = optmethod;
        this.id = id;
        this.optid = optid;
    }

    public String getOptid() {
        return optid;
    }

    public void setOptid(String optid) {
        this.optid = optid;
    }

    public String getOpturl() {
        return opturl;
    }

    public void setOpturl(String opturl) {
        this.opturl = opturl;
    }

    public String getRolecode() {
        return this.id.getRolecode();
    }

    public void setRolecode(String rolecode) {
        this.id.setRolecode(rolecode);
    }

    public String getOptcode() {
        return this.id.getOptcode();
    }

    public void setOptcode(String optcode) {
        this.id.setOptcode(optcode);
    }

    public String getOptmethod() {
        if (optmethod == null)
            return "";
        return optmethod;
    }

    public void setOptmethod(String optmethod) {
        this.optmethod = optmethod;
    }

    public void copy(FOptRoleMap other) {

        this.opturl = other.getOpturl();
        this.optmethod = other.getOptmethod();
        this.optid = other.getOptid();
    }

    public void copyNotNullProperty(FOptRoleMap other) {

        if (other.getOpturl() != null)
            this.opturl = other.getOpturl();
        if (other.getOptmethod() != null)
            this.optmethod = other.getOptmethod();
        if (other.getOptid() != null)
            this.optid = other.getOptid();
    }

}