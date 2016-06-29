package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class Publicinfolog implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private PublicinfologId cid;


    private String operation;
    private String data1;
    private String data2;
    private Date operatorTime;

    // Constructors

    /**
     * default constructor
     */
    public Publicinfolog() {
    }

    /**
     * minimal constructor
     */
    public Publicinfolog(PublicinfologId id

            , String operation) {
        this.cid = id;


        this.operation = operation;
    }

    /**
     * full constructor
     */
    public Publicinfolog(PublicinfologId id

            , String operation, String data1, String data2, Date date) {
        this.cid = id;


        this.operation = operation;
        this.data1 = data1;
        this.data2 = data2;
        this.operatorTime = date;
    }

    public PublicinfologId getCid() {
        return this.cid;
    }

    public void setCid(PublicinfologId id) {
        this.cid = id;
    }

    public String getUsercode() {
        if (this.cid == null)
            this.cid = new PublicinfologId();
        return this.cid.getUsercode();
    }

    public void setUsercode(String usercode) {
        if (this.cid == null)
            this.cid = new PublicinfologId();
        this.cid.setUsercode(usercode);
    }

    public String getInfocode() {
        if (this.cid == null)
            this.cid = new PublicinfologId();
        return this.cid.getInfocode();
    }

    public void setInfocode(String infocode) {
        if (this.cid == null)
            this.cid = new PublicinfologId();
        this.cid.setInfocode(infocode);
    }


    // Property accessors

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getData1() {
        return this.data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return this.data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public void copy(Publicinfolog other) {

        this.setUsercode(other.getUsercode());
        this.setInfocode(other.getInfocode());

        this.operation = other.getOperation();
        this.data1 = other.getData1();
        this.data2 = other.getData2();
        this.operatorTime = other.getOperatorTime();
    }

    public Date getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Date operatorTime) {
        this.operatorTime = operatorTime;
    }

    public void copyNotNullProperty(Publicinfolog other) {

        if (other.getUsercode() != null)
            this.setUsercode(other.getUsercode());
        if (other.getInfocode() != null)
            this.setInfocode(other.getInfocode());

        if (other.getOperation() != null)
            this.operation = other.getOperation();
        if (other.getData1() != null)
            this.data1 = other.getData1();
        if (other.getData2() != null)
            this.data2 = other.getData2();
        if (other.getOperatorTime() != null)
            this.operatorTime = other.getOperatorTime();

    }

    public void clearProperties() {

        this.operation = null;
        this.data1 = null;
        this.data2 = null;
        this.operatorTime = null;
    }
}
