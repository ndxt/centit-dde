package com.centit.app.po;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OaWorkingTime implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String worktype;

    private String worktypename;
    private String hasschedule1;
    private String schedule1begin;
    private String schedule1end;
    private String hasschedule2;
    private String schedule2begin;
    private String schedule2end;
    private String hasschedule3;
    private String schedule3begin;
    private String schedule3end;
    private String hasschedule4;
    private String schedule4begin;
    private String schedule4end;
    private String automatch;
    private String matchbeginday;
    private String matchendday;
    private String worktypedesc;

    // Constructors

    /**
     * default constructor
     */
    public OaWorkingTime() {
    }

    /**
     * minimal constructor
     */
    public OaWorkingTime(
            String worktype
            , String worktypename, String hasschedule1, String hasschedule2, String hasschedule3, String hasschedule4, String automatch) {


        this.worktype = worktype;

        this.worktypename = worktypename;
        this.hasschedule1 = hasschedule1;
        this.hasschedule2 = hasschedule2;
        this.hasschedule3 = hasschedule3;
        this.hasschedule4 = hasschedule4;
        this.automatch = automatch;
    }

    /**
     * full constructor
     */
    public OaWorkingTime(
            String worktype
            , String worktypename, String hasschedule1, String schedule1begin, String schedule1end, String hasschedule2, String schedule2begin, String schedule2end, String hasschedule3, String schedule3begin, String schedule3end, String hasschedule4, String schedule4begin, String schedule4end, String automatch, String matchbeginday, String matchendday, String worktypedesc) {


        this.worktype = worktype;

        this.worktypename = worktypename;
        this.hasschedule1 = hasschedule1;
        this.schedule1begin = schedule1begin;
        this.schedule1end = schedule1end;
        this.hasschedule2 = hasschedule2;
        this.schedule2begin = schedule2begin;
        this.schedule2end = schedule2end;
        this.hasschedule3 = hasschedule3;
        this.schedule3begin = schedule3begin;
        this.schedule3end = schedule3end;
        this.hasschedule4 = hasschedule4;
        this.schedule4begin = schedule4begin;
        this.schedule4end = schedule4end;
        this.automatch = automatch;
        this.matchbeginday = matchbeginday;
        this.matchendday = matchendday;
        this.worktypedesc = worktypedesc;
    }


    public String getWorktype() {
        return this.worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }
    // Property accessors

    public String getWorktypename() {
        return this.worktypename;
    }

    public void setWorktypename(String worktypename) {
        this.worktypename = worktypename;
    }

    public String getHasschedule1() {
        return this.hasschedule1;
    }

    public void setHasschedule1(String hasschedule1) {
        this.hasschedule1 = hasschedule1;
    }

    public String getSchedule1begin() {
        return this.schedule1begin;
    }

    public void setSchedule1begin(String schedule1begin) {
        this.schedule1begin = schedule1begin;
    }

    public String getSchedule1end() {
        return this.schedule1end;
    }

    public void setSchedule1end(String schedule1end) {
        this.schedule1end = schedule1end;
    }

    public String getHasschedule2() {
        return this.hasschedule2;
    }

    public void setHasschedule2(String hasschedule2) {
        this.hasschedule2 = hasschedule2;
    }

    public String getSchedule2begin() {
        return this.schedule2begin;
    }

    public void setSchedule2begin(String schedule2begin) {
        this.schedule2begin = schedule2begin;
    }

    public String getSchedule2end() {
        return this.schedule2end;
    }

    public void setSchedule2end(String schedule2end) {
        this.schedule2end = schedule2end;
    }

    public String getHasschedule3() {
        return this.hasschedule3;
    }

    public void setHasschedule3(String hasschedule3) {
        this.hasschedule3 = hasschedule3;
    }

    public String getSchedule3begin() {
        return this.schedule3begin;
    }

    public void setSchedule3begin(String schedule3begin) {
        this.schedule3begin = schedule3begin;
    }

    public String getSchedule3end() {
        return this.schedule3end;
    }

    public void setSchedule3end(String schedule3end) {
        this.schedule3end = schedule3end;
    }

    public String getHasschedule4() {
        return this.hasschedule4;
    }

    public void setHasschedule4(String hasschedule4) {
        this.hasschedule4 = hasschedule4;
    }

    public String getSchedule4begin() {
        return this.schedule4begin;
    }

    public void setSchedule4begin(String schedule4begin) {
        this.schedule4begin = schedule4begin;
    }

    public String getSchedule4end() {
        return this.schedule4end;
    }

    public void setSchedule4end(String schedule4end) {
        this.schedule4end = schedule4end;
    }

    public String getAutomatch() {
        return this.automatch;
    }

    public void setAutomatch(String automatch) {
        this.automatch = automatch;
    }

    public String getMatchbeginday() {
        return this.matchbeginday;
    }

    public void setMatchbeginday(String matchbeginday) {
        this.matchbeginday = matchbeginday;
    }

    public String getMatchendday() {
        return this.matchendday;
    }

    public void setMatchendday(String matchendday) {
        this.matchendday = matchendday;
    }

    public String getWorktypedesc() {
        return this.worktypedesc;
    }

    public void setWorktypedesc(String worktypedesc) {
        this.worktypedesc = worktypedesc;
    }


    public void copy(OaWorkingTime other) {

        this.setWorktype(other.getWorktype());

        this.worktypename = other.getWorktypename();
        this.hasschedule1 = other.getHasschedule1();
        this.schedule1begin = other.getSchedule1begin();
        this.schedule1end = other.getSchedule1end();
        this.hasschedule2 = other.getHasschedule2();
        this.schedule2begin = other.getSchedule2begin();
        this.schedule2end = other.getSchedule2end();
        this.hasschedule3 = other.getHasschedule3();
        this.schedule3begin = other.getSchedule3begin();
        this.schedule3end = other.getSchedule3end();
        this.hasschedule4 = other.getHasschedule4();
        this.schedule4begin = other.getSchedule4begin();
        this.schedule4end = other.getSchedule4end();
        this.automatch = other.getAutomatch();
        this.matchbeginday = other.getMatchbeginday();
        this.matchendday = other.getMatchendday();
        this.worktypedesc = other.getWorktypedesc();

    }

    public void copyNotNullProperty(OaWorkingTime other) {

        if (other.getWorktype() != null)
            this.setWorktype(other.getWorktype());

        if (other.getWorktypename() != null)
            this.worktypename = other.getWorktypename();
        if (other.getHasschedule1() != null)
            this.hasschedule1 = other.getHasschedule1();
        if (other.getSchedule1begin() != null)
            this.schedule1begin = other.getSchedule1begin();
        if (other.getSchedule1end() != null)
            this.schedule1end = other.getSchedule1end();
        if (other.getHasschedule2() != null)
            this.hasschedule2 = other.getHasschedule2();
        if (other.getSchedule2begin() != null)
            this.schedule2begin = other.getSchedule2begin();
        if (other.getSchedule2end() != null)
            this.schedule2end = other.getSchedule2end();
        if (other.getHasschedule3() != null)
            this.hasschedule3 = other.getHasschedule3();
        if (other.getSchedule3begin() != null)
            this.schedule3begin = other.getSchedule3begin();
        if (other.getSchedule3end() != null)
            this.schedule3end = other.getSchedule3end();
        if (other.getHasschedule4() != null)
            this.hasschedule4 = other.getHasschedule4();
        if (other.getSchedule4begin() != null)
            this.schedule4begin = other.getSchedule4begin();
        if (other.getSchedule4end() != null)
            this.schedule4end = other.getSchedule4end();
        if (other.getAutomatch() != null)
            this.automatch = other.getAutomatch();
        if (other.getMatchbeginday() != null)
            this.matchbeginday = other.getMatchbeginday();
        if (other.getMatchendday() != null)
            this.matchendday = other.getMatchendday();
        if (other.getWorktypedesc() != null)
            this.worktypedesc = other.getWorktypedesc();

    }
}
