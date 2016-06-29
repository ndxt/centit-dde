package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OaStatMonth implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String yearMonth;

    private Date beginDay;
    private String beginSchedule;
    private Date eendDay;
    private String endSchedule;

    // Constructors

    /**
     * default constructor
     */
    public OaStatMonth() {
    }

    /**
     * minimal constructor
     */
    public OaStatMonth(
            String yearMonth
            , Date beginDay, Date eendDay) {


        this.yearMonth = yearMonth;

        this.beginDay = beginDay;
        this.eendDay = eendDay;
    }

    /**
     * full constructor
     */
    public OaStatMonth(
            String yearMonth
            , Date beginDay, String beginSchedule, Date eendDay, String endSchedule) {


        this.yearMonth = yearMonth;

        this.beginDay = beginDay;
        this.beginSchedule = beginSchedule;
        this.eendDay = eendDay;
        this.endSchedule = endSchedule;
    }


    public String getYearMonth() {
        return this.yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
    // Property accessors

    public Date getBeginDay() {
        return this.beginDay;
    }

    public void setBeginDay(Date beginDay) {
        this.beginDay = beginDay;
    }

    public String getBeginSchedule() {
        return this.beginSchedule;
    }

    public void setBeginSchedule(String beginSchedule) {
        this.beginSchedule = beginSchedule;
    }

    public Date getEendDay() {
        return this.eendDay;
    }

    public void setEendDay(Date eendDay) {
        this.eendDay = eendDay;
    }

    public String getEndSchedule() {
        return this.endSchedule;
    }

    public void setEndSchedule(String endSchedule) {
        this.endSchedule = endSchedule;
    }


    public void copy(OaStatMonth other) {

        this.setYearMonth(other.getYearMonth());

        this.beginDay = other.getBeginDay();
        this.beginSchedule = other.getBeginSchedule();
        this.eendDay = other.getEendDay();
        this.endSchedule = other.getEndSchedule();

    }

    public void copyNotNullProperty(OaStatMonth other) {

        if (other.getYearMonth() != null)
            this.setYearMonth(other.getYearMonth());

        if (other.getBeginDay() != null)
            this.beginDay = other.getBeginDay();
        if (other.getBeginSchedule() != null)
            this.beginSchedule = other.getBeginSchedule();
        if (other.getEendDay() != null)
            this.eendDay = other.getEendDay();
        if (other.getEndSchedule() != null)
            this.endSchedule = other.getEndSchedule();

    }
}
