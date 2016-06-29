package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OaWorkDay implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Date workday;

    private String daytype;
    private String worktimetype;
    private String workdaydesc;
    private String hasschedule1;
    private String hasschedule2;
    private String hasschedule3;
    private String hasschedule4;

    // Constructors

    /**
     * default constructor
     */
    public OaWorkDay() {
    }

    /**
     * minimal constructor
     */
    public OaWorkDay(
            Date workday
            , String daytype, String hasschedule1, String hasschedule2, String hasschedule3, String hasschedule4) {


        this.workday = workday;

        this.daytype = daytype;
        this.hasschedule1 = hasschedule1;
        this.hasschedule2 = hasschedule2;
        this.hasschedule3 = hasschedule3;
        this.hasschedule4 = hasschedule4;
    }

    /**
     * full constructor
     */
    public OaWorkDay(
            Date workday
            , String daytype, String worktimetype, String workdaydesc, String hasschedule1, String hasschedule2, String hasschedule3, String hasschedule4) {


        this.workday = workday;

        this.daytype = daytype;
        this.worktimetype = worktimetype;
        this.workdaydesc = workdaydesc;
        this.hasschedule1 = hasschedule1;
        this.hasschedule2 = hasschedule2;
        this.hasschedule3 = hasschedule3;
        this.hasschedule4 = hasschedule4;
    }


    public Date getWorkday() {
        return this.workday;
    }

    public void setWorkday(Date workday) {
        this.workday = workday;
    }
    // Property accessors

    public String getDaytype() {
        return this.daytype;
    }

    public void setDaytype(String daytype) {
        this.daytype = daytype;
    }

    public String getWorktimetype() {
        return this.worktimetype;
    }

    public void setWorktimetype(String worktimetype) {
        this.worktimetype = worktimetype;
    }

    public String getWorkdaydesc() {
        return this.workdaydesc;
    }

    public void setWorkdaydesc(String workdaydesc) {
        this.workdaydesc = workdaydesc;
    }

    public String getHasschedule1() {
        return this.hasschedule1;
    }

    public void setHasschedule1(String hasschedule1) {
        this.hasschedule1 = hasschedule1;
    }

    public String getHasschedule2() {
        return this.hasschedule2;
    }

    public void setHasschedule2(String hasschedule2) {
        this.hasschedule2 = hasschedule2;
    }

    public String getHasschedule3() {
        return this.hasschedule3;
    }

    public void setHasschedule3(String hasschedule3) {
        this.hasschedule3 = hasschedule3;
    }

    public String getHasschedule4() {
        return this.hasschedule4;
    }

    public void setHasschedule4(String hasschedule4) {
        this.hasschedule4 = hasschedule4;
    }


    public void copy(OaWorkDay other) {

        this.setWorkday(other.getWorkday());

        this.daytype = other.getDaytype();
        this.worktimetype = other.getWorktimetype();
        this.workdaydesc = other.getWorkdaydesc();
        this.hasschedule1 = other.getHasschedule1();
        this.hasschedule2 = other.getHasschedule2();
        this.hasschedule3 = other.getHasschedule3();
        this.hasschedule4 = other.getHasschedule4();

    }

    public void copyNotNullProperty(OaWorkDay other) {

        if (other.getWorkday() != null)
            this.setWorkday(other.getWorkday());

        if (other.getDaytype() != null)
            this.daytype = other.getDaytype();
        if (other.getWorktimetype() != null)
            this.worktimetype = other.getWorktimetype();
        if (other.getWorkdaydesc() != null)
            this.workdaydesc = other.getWorkdaydesc();
        if (other.getHasschedule1() != null)
            this.hasschedule1 = other.getHasschedule1();
        if (other.getHasschedule2() != null)
            this.hasschedule2 = other.getHasschedule2();
        if (other.getHasschedule3() != null)
            this.hasschedule3 = other.getHasschedule3();
        if (other.getHasschedule4() != null)
            this.hasschedule4 = other.getHasschedule4();

    }
}
