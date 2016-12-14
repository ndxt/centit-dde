package com.centit.dde.util;

import java.util.Date;

import com.centit.support.algorithm.DatetimeOpt;

public class ItemValue {

    public static final int unknown = 0;
    public static final int number = 1;
    public static final int varchar = 2;
    public static final int datetime = 3;
    public static final int clob = 4;
    public static final int blob = 5;

    private String colName;

    private int colType;
    private boolean isNull;
    private Date dateValue;
    private String strValue;
    private byte[] blobValue;

    public ItemValue() {

    }

    public ItemValue(String cName) {
        this.colName = cName;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public int getColType() {
        return colType;
    }

    public void setColType(int colType) {
        this.colType = colType;
    }

    public Integer getIntValue() {

        return Integer.valueOf(strValue);
    }

    public Long getLongValue() {
        return Long.valueOf(strValue);
    }

    public Double getDoubleValue() {
        return Double.valueOf(strValue);
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public String getStrValue() {
        return strValue;
    }

    public String getClobValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    public byte[] getBlobValue() {
        return blobValue;
    }

    public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
    }

    public String toString() {
        switch (colType) {
            case ItemValue.datetime:
                return DatetimeOpt.convertDatetimeToString(this.dateValue);
            case ItemValue.blob:
                return new String(blobValue);
            default:
                return strValue;
        }
    }

    public Object getValue() {
        switch (colType) {
            case ItemValue.datetime:
                return this.dateValue;
            case ItemValue.blob:
                return blobValue;
            case ItemValue.number:
                return Double.valueOf(strValue);
            default:
                return strValue;
        }
    }

    public static int mapDbType(String typeDesc) {

        String sType = "";
        int iPos = 0;
        if (typeDesc == null)
            return unknown;

        iPos = typeDesc.indexOf("(");
        if (iPos > 0) {
            sType = typeDesc.substring(0, iPos).toUpperCase();
        } else {
            sType = typeDesc.toUpperCase();
        }
        if (sType.equals("CHAR") || sType.equals("VARCHAR")
                || sType.equals("VARCHAR2") || sType.equals("NCHAR")
                || sType.equals("NVARCHAR") || "NVARCHAR2".equals(sType)
                || sType.equals("BYTE") || sType.equals("BIT")
                || sType.equals("BINARY")) {
            return varchar;
        } else if (sType.equals("NUMBER") || sType.equals("DECIMAL")
                || sType.equals("NUMERIC") || sType.equals("MONEY")
                || sType.equals("INT") || sType.equals("INTEGER")
                || sType.equals("FLOAT")|| sType.equals("DOUBLE")
                || sType.equals("BINARY_DOUBLE")
                || sType.equals("BINARY_FLOAT")) {
            return number;
        } else if (sType.equals("DATE") || sType.equals("TIME")
                || sType.equals("DATETIME") || sType.equals("TIMESTAMP")) {
            return datetime;
        } else if (sType.equals("BLOB") || sType.equals("IMAGE")
                || sType.equals("VARBINAY")
                || sType.equals("LONGBLOB")) {
            return blob;
        } else if (sType.equals("CLOB") || sType.equals("TEXT") 
                || sType.equals("NTEXT")
                || sType.equals("LONGTEXT")
                || sType.equals("MEDIUMTEXT")) {
            return clob;
        } else {
            return unknown;
        }

    }


}
