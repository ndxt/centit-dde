package com.centit.sys.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class AddressBook implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String usercode;

    private String bodytype;
    private String bodycode;
    private String bodyname;
    private String unitname;
    private String unitcode;
    private String deptname;
    private String rankname;
    private String representation;
    private String email;
    private String email2;
    private String email3;
    private String homepage;
    private String qq;
    private String msn;
    private String wangwang;
    private String buzphone;
    private String buzphone2;
    private String buzfax;
    private String assiphone;
    private String callbackphone;
    private String carphone;
    private String unitphone;
    private String homephone;
    private String homephone2;
    private String homephone3;
    private String homefax;
    private String mobilephone;
    private String mobilephone2;
    private String mobilephone3;
    private String unitzip;
    private String unitprovince;
    private String unitcity;
    private String unitdistrict;
    private String unitstreet;
    private String unitaddress;
    private String homezip;
    private String homeprovince;
    private String homecity;
    private String homedistrict;
    private String homestreet;
    private String homeaddress;
    private String home2zip;
    private String home2province;
    private String home2city;
    private String home2district;
    private String home2street;
    private String home2address;
    private String inuseaddress;
    private String searchstring;
    private String isprivate;
    private String memo;
    private Date lastmodifydate;
    private Date createdate;
    private String groupid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
    // Constructors

    /**
     * default constructor
     */
    public AddressBook() {
    }

    /**
     * minimal constructor
     */
    public AddressBook(
            String usercode
            , String bodytype, String bodycode) {


        this.usercode = usercode;

        this.bodytype = bodytype;
        this.bodycode = bodycode;
    }

    /**
     * full constructor
     */
    public AddressBook(
            String usercode
            , String bodytype, String bodycode, String bodyname, String unitname, String unitcode, String deptname, String rankname, String representation, String email, String email2, String email3, String homepage, String qq, String msn, String wangwang, String buzphone, String buzphone2, String buzfax, String assiphone, String callbackphone, String carphone, String unitphone, String homephone, String homephone2, String homephone3, String homefax, String mobilephone, String mobilephone2, String mobilephone3, String unitzip, String unitprovince, String unitcity, String unitdistrict, String unitstreet, String unitaddress, String homezip, String homeprovince, String homecity, String homedistrict, String homestreet, String homeaddress, String home2zip, String home2province, String home2city, String home2district, String home2street, String home2address, String inuseaddress, String searchstring, String isprivate, String memo, Date lastmodifydate, Date createdate) {


        this.usercode = usercode;

        this.bodytype = bodytype;
        this.bodycode = bodycode;
        this.bodyname = bodyname;
        this.unitname = unitname;
        this.unitcode = unitcode;
        this.deptname = deptname;
        this.rankname = rankname;
        this.representation = representation;
        this.email = email;
        this.email2 = email2;
        this.email3 = email3;
        this.homepage = homepage;
        this.qq = qq;
        this.msn = msn;
        this.wangwang = wangwang;
        this.buzphone = buzphone;
        this.buzphone2 = buzphone2;
        this.buzfax = buzfax;
        this.assiphone = assiphone;
        this.callbackphone = callbackphone;
        this.carphone = carphone;
        this.unitphone = unitphone;
        this.homephone = homephone;
        this.homephone2 = homephone2;
        this.homephone3 = homephone3;
        this.homefax = homefax;
        this.mobilephone = mobilephone;
        this.mobilephone2 = mobilephone2;
        this.mobilephone3 = mobilephone3;
        this.unitzip = unitzip;
        this.unitprovince = unitprovince;
        this.unitcity = unitcity;
        this.unitdistrict = unitdistrict;
        this.unitstreet = unitstreet;
        this.unitaddress = unitaddress;
        this.homezip = homezip;
        this.homeprovince = homeprovince;
        this.homecity = homecity;
        this.homedistrict = homedistrict;
        this.homestreet = homestreet;
        this.homeaddress = homeaddress;
        this.home2zip = home2zip;
        this.home2province = home2province;
        this.home2city = home2city;
        this.home2district = home2district;
        this.home2street = home2street;
        this.home2address = home2address;
        this.inuseaddress = inuseaddress;
        this.searchstring = searchstring;
        this.isprivate = isprivate;
        this.memo = memo;
        this.lastmodifydate = lastmodifydate;
        this.createdate = createdate;
    }


    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
    // Property accessors

    public String getBodytype() {
        return this.bodytype;
    }

    public void setBodytype(String bodytype) {
        this.bodytype = bodytype;
    }

    public String getBodycode() {
        return this.bodycode;
    }

    public void setBodycode(String bodycode) {
        this.bodycode = bodycode;
    }

    public String getBodyname() {
        return this.bodyname;
    }

    public void setBodyname(String bodyname) {
        this.bodyname = bodyname;
    }

    public String getUnitname() {
        return this.unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitcode() {
        return this.unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getDeptname() {
        return this.deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getRankname() {
        return this.rankname;
    }

    public void setRankname(String rankname) {
        this.rankname = rankname;
    }

    public String getRepresentation() {
        return this.representation;
    }

    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return this.email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return this.email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getHomepage() {
        return this.homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMsn() {
        return this.msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getWangwang() {
        return this.wangwang;
    }

    public void setWangwang(String wangwang) {
        this.wangwang = wangwang;
    }

    public String getBuzphone() {
        return this.buzphone;
    }

    public void setBuzphone(String buzphone) {
        this.buzphone = buzphone;
    }

    public String getBuzphone2() {
        return this.buzphone2;
    }

    public void setBuzphone2(String buzphone2) {
        this.buzphone2 = buzphone2;
    }

    public String getBuzfax() {
        return this.buzfax;
    }

    public void setBuzfax(String buzfax) {
        this.buzfax = buzfax;
    }

    public String getAssiphone() {
        return this.assiphone;
    }

    public void setAssiphone(String assiphone) {
        this.assiphone = assiphone;
    }

    public String getCallbackphone() {
        return this.callbackphone;
    }

    public void setCallbackphone(String callbackphone) {
        this.callbackphone = callbackphone;
    }

    public String getCarphone() {
        return this.carphone;
    }

    public void setCarphone(String carphone) {
        this.carphone = carphone;
    }

    public String getUnitphone() {
        return this.unitphone;
    }

    public void setUnitphone(String unitphone) {
        this.unitphone = unitphone;
    }

    public String getHomephone() {
        return this.homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
    }

    public String getHomephone2() {
        return this.homephone2;
    }

    public void setHomephone2(String homephone2) {
        this.homephone2 = homephone2;
    }

    public String getHomephone3() {
        return this.homephone3;
    }

    public void setHomephone3(String homephone3) {
        this.homephone3 = homephone3;
    }

    public String getHomefax() {
        return this.homefax;
    }

    public void setHomefax(String homefax) {
        this.homefax = homefax;
    }

    public String getMobilephone() {
        return this.mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getMobilephone2() {
        return this.mobilephone2;
    }

    public void setMobilephone2(String mobilephone2) {
        this.mobilephone2 = mobilephone2;
    }

    public String getMobilephone3() {
        return this.mobilephone3;
    }

    public void setMobilephone3(String mobilephone3) {
        this.mobilephone3 = mobilephone3;
    }

    public String getUnitzip() {
        return this.unitzip;
    }

    public void setUnitzip(String unitzip) {
        this.unitzip = unitzip;
    }

    public String getUnitprovince() {
        return this.unitprovince;
    }

    public void setUnitprovince(String unitprovince) {
        this.unitprovince = unitprovince;
    }

    public String getUnitcity() {
        return this.unitcity;
    }

    public void setUnitcity(String unitcity) {
        this.unitcity = unitcity;
    }

    public String getUnitdistrict() {
        return this.unitdistrict;
    }

    public void setUnitdistrict(String unitdistrict) {
        this.unitdistrict = unitdistrict;
    }

    public String getUnitstreet() {
        return this.unitstreet;
    }

    public void setUnitstreet(String unitstreet) {
        this.unitstreet = unitstreet;
    }

    public String getUnitaddress() {
        return this.unitaddress;
    }

    public void setUnitaddress(String unitaddress) {
        this.unitaddress = unitaddress;
    }

    public String getHomezip() {
        return this.homezip;
    }

    public void setHomezip(String homezip) {
        this.homezip = homezip;
    }

    public String getHomeprovince() {
        return this.homeprovince;
    }

    public void setHomeprovince(String homeprovince) {
        this.homeprovince = homeprovince;
    }

    public String getHomecity() {
        return this.homecity;
    }

    public void setHomecity(String homecity) {
        this.homecity = homecity;
    }

    public String getHomedistrict() {
        return this.homedistrict;
    }

    public void setHomedistrict(String homedistrict) {
        this.homedistrict = homedistrict;
    }

    public String getHomestreet() {
        return this.homestreet;
    }

    public void setHomestreet(String homestreet) {
        this.homestreet = homestreet;
    }

    public String getHomeaddress() {
        return this.homeaddress;
    }

    public void setHomeaddress(String homeaddress) {
        this.homeaddress = homeaddress;
    }

    public String getHome2zip() {
        return this.home2zip;
    }

    public void setHome2zip(String home2zip) {
        this.home2zip = home2zip;
    }

    public String getHome2province() {
        return this.home2province;
    }

    public void setHome2province(String home2province) {
        this.home2province = home2province;
    }

    public String getHome2city() {
        return this.home2city;
    }

    public void setHome2city(String home2city) {
        this.home2city = home2city;
    }

    public String getHome2district() {
        return this.home2district;
    }

    public void setHome2district(String home2district) {
        this.home2district = home2district;
    }

    public String getHome2street() {
        return this.home2street;
    }

    public void setHome2street(String home2street) {
        this.home2street = home2street;
    }

    public String getHome2address() {
        return this.home2address;
    }

    public void setHome2address(String home2address) {
        this.home2address = home2address;
    }

    public String getInuseaddress() {
        return this.inuseaddress;
    }

    public void setInuseaddress(String inuseaddress) {
        this.inuseaddress = inuseaddress;
    }

    public String getSearchstring() {
        return this.searchstring;
    }

    public void setSearchstring(String searchstring) {
        this.searchstring = searchstring;
    }

    public String getIsprivate() {
        return this.isprivate;
    }

    public void setIsprivate(String isprivate) {
        this.isprivate = isprivate;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Date getLastmodifydate() {
        return this.lastmodifydate;
    }

    public void setLastmodifydate(Date lastmodifydate) {
        this.lastmodifydate = lastmodifydate;
    }

    public Date getCreatedate() {
        return this.createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }


    public void copy(AddressBook other) {

        this.setUsercode(other.getUsercode());

        this.bodytype = other.getBodytype();
        this.bodycode = other.getBodycode();
        this.bodyname = other.getBodyname();
        this.unitname = other.getUnitname();
        this.unitcode = other.getUnitcode();
        this.deptname = other.getDeptname();
        this.rankname = other.getRankname();
        this.representation = other.getRepresentation();
        this.email = other.getEmail();
        this.email2 = other.getEmail2();
        this.email3 = other.getEmail3();
        this.homepage = other.getHomepage();
        this.qq = other.getQq();
        this.msn = other.getMsn();
        this.wangwang = other.getWangwang();
        this.buzphone = other.getBuzphone();
        this.buzphone2 = other.getBuzphone2();
        this.buzfax = other.getBuzfax();
        this.assiphone = other.getAssiphone();
        this.callbackphone = other.getCallbackphone();
        this.carphone = other.getCarphone();
        this.unitphone = other.getUnitphone();
        this.homephone = other.getHomephone();
        this.homephone2 = other.getHomephone2();
        this.homephone3 = other.getHomephone3();
        this.homefax = other.getHomefax();
        this.mobilephone = other.getMobilephone();
        this.mobilephone2 = other.getMobilephone2();
        this.mobilephone3 = other.getMobilephone3();
        this.unitzip = other.getUnitzip();
        this.unitprovince = other.getUnitprovince();
        this.unitcity = other.getUnitcity();
        this.unitdistrict = other.getUnitdistrict();
        this.unitstreet = other.getUnitstreet();
        this.unitaddress = other.getUnitaddress();
        this.homezip = other.getHomezip();
        this.homeprovince = other.getHomeprovince();
        this.homecity = other.getHomecity();
        this.homedistrict = other.getHomedistrict();
        this.homestreet = other.getHomestreet();
        this.homeaddress = other.getHomeaddress();
        this.home2zip = other.getHome2zip();
        this.home2province = other.getHome2province();
        this.home2city = other.getHome2city();
        this.home2district = other.getHome2district();
        this.home2street = other.getHome2street();
        this.home2address = other.getHome2address();
        this.inuseaddress = other.getInuseaddress();
        this.searchstring = other.getSearchstring();
        this.isprivate = other.getIsprivate();
        this.memo = other.getMemo();
        this.lastmodifydate = other.getLastmodifydate();
        this.createdate = other.getCreatedate();

    }

    public void copyNotNullProperty(AddressBook other) {

        if (other.getUsercode() != null)
            this.setUsercode(other.getUsercode());

        if (other.getBodytype() != null)
            this.bodytype = other.getBodytype();
        if (other.getBodycode() != null)
            this.bodycode = other.getBodycode();
        if (other.getBodyname() != null)
            this.bodyname = other.getBodyname();
        if (other.getUnitname() != null)
            this.unitname = other.getUnitname();
        if (other.getUnitcode() != null)
            this.unitcode = other.getUnitcode();
        if (other.getDeptname() != null)
            this.deptname = other.getDeptname();
        if (other.getRankname() != null)
            this.rankname = other.getRankname();
        if (other.getRepresentation() != null)
            this.representation = other.getRepresentation();
        if (other.getEmail() != null)
            this.email = other.getEmail();
        if (other.getEmail2() != null)
            this.email2 = other.getEmail2();
        if (other.getEmail3() != null)
            this.email3 = other.getEmail3();
        if (other.getHomepage() != null)
            this.homepage = other.getHomepage();
        if (other.getQq() != null)
            this.qq = other.getQq();
        if (other.getMsn() != null)
            this.msn = other.getMsn();
        if (other.getWangwang() != null)
            this.wangwang = other.getWangwang();
        if (other.getBuzphone() != null)
            this.buzphone = other.getBuzphone();
        if (other.getBuzphone2() != null)
            this.buzphone2 = other.getBuzphone2();
        if (other.getBuzfax() != null)
            this.buzfax = other.getBuzfax();
        if (other.getAssiphone() != null)
            this.assiphone = other.getAssiphone();
        if (other.getCallbackphone() != null)
            this.callbackphone = other.getCallbackphone();
        if (other.getCarphone() != null)
            this.carphone = other.getCarphone();
        if (other.getUnitphone() != null)
            this.unitphone = other.getUnitphone();
        if (other.getHomephone() != null)
            this.homephone = other.getHomephone();
        if (other.getHomephone2() != null)
            this.homephone2 = other.getHomephone2();
        if (other.getHomephone3() != null)
            this.homephone3 = other.getHomephone3();
        if (other.getHomefax() != null)
            this.homefax = other.getHomefax();
        if (other.getMobilephone() != null)
            this.mobilephone = other.getMobilephone();
        if (other.getMobilephone2() != null)
            this.mobilephone2 = other.getMobilephone2();
        if (other.getMobilephone3() != null)
            this.mobilephone3 = other.getMobilephone3();
        if (other.getUnitzip() != null)
            this.unitzip = other.getUnitzip();
        if (other.getUnitprovince() != null)
            this.unitprovince = other.getUnitprovince();
        if (other.getUnitcity() != null)
            this.unitcity = other.getUnitcity();
        if (other.getUnitdistrict() != null)
            this.unitdistrict = other.getUnitdistrict();
        if (other.getUnitstreet() != null)
            this.unitstreet = other.getUnitstreet();
        if (other.getUnitaddress() != null)
            this.unitaddress = other.getUnitaddress();
        if (other.getHomezip() != null)
            this.homezip = other.getHomezip();
        if (other.getHomeprovince() != null)
            this.homeprovince = other.getHomeprovince();
        if (other.getHomecity() != null)
            this.homecity = other.getHomecity();
        if (other.getHomedistrict() != null)
            this.homedistrict = other.getHomedistrict();
        if (other.getHomestreet() != null)
            this.homestreet = other.getHomestreet();
        if (other.getHomeaddress() != null)
            this.homeaddress = other.getHomeaddress();
        if (other.getHome2zip() != null)
            this.home2zip = other.getHome2zip();
        if (other.getHome2province() != null)
            this.home2province = other.getHome2province();
        if (other.getHome2city() != null)
            this.home2city = other.getHome2city();
        if (other.getHome2district() != null)
            this.home2district = other.getHome2district();
        if (other.getHome2street() != null)
            this.home2street = other.getHome2street();
        if (other.getHome2address() != null)
            this.home2address = other.getHome2address();
        if (other.getInuseaddress() != null)
            this.inuseaddress = other.getInuseaddress();
        if (other.getSearchstring() != null)
            this.searchstring = other.getSearchstring();
        if (other.getIsprivate() != null)
            this.isprivate = other.getIsprivate();
        if (other.getMemo() != null)
            this.memo = other.getMemo();
        if (other.getLastmodifydate() != null)
            this.lastmodifydate = other.getLastmodifydate();
        if (other.getCreatedate() != null)
            this.createdate = other.getCreatedate();

    }

    public void clearProperties() {

        this.bodytype = null;
        this.bodycode = null;
        this.bodyname = null;
        this.unitname = null;
        this.unitcode = null;
        this.deptname = null;
        this.rankname = null;
        this.representation = null;
        this.email = null;
        this.email2 = null;
        this.email3 = null;
        this.homepage = null;
        this.qq = null;
        this.msn = null;
        this.wangwang = null;
        this.buzphone = null;
        this.buzphone2 = null;
        this.buzfax = null;
        this.assiphone = null;
        this.callbackphone = null;
        this.carphone = null;
        this.unitphone = null;
        this.homephone = null;
        this.homephone2 = null;
        this.homephone3 = null;
        this.homefax = null;
        this.mobilephone = null;
        this.mobilephone2 = null;
        this.mobilephone3 = null;
        this.unitzip = null;
        this.unitprovince = null;
        this.unitcity = null;
        this.unitdistrict = null;
        this.unitstreet = null;
        this.unitaddress = null;
        this.homezip = null;
        this.homeprovince = null;
        this.homecity = null;
        this.homedistrict = null;
        this.homestreet = null;
        this.homeaddress = null;
        this.home2zip = null;
        this.home2province = null;
        this.home2city = null;
        this.home2district = null;
        this.home2street = null;
        this.home2address = null;
        this.inuseaddress = null;
        this.searchstring = null;
        this.isprivate = null;
        this.memo = null;
        this.lastmodifydate = null;
        this.createdate = null;

    }
}
