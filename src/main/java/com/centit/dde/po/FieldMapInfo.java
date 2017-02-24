package com.centit.dde.po;

import javax.persistence.Transient;

public class FieldMapInfo {

    @Transient
    private String leftName;
    @Transient
    private String rightName;
    @Transient
    private int rightColType;
    @Transient
    private int leftColType;
    @Transient
    private boolean isKey;
    @Transient
    private boolean isNullable;
    @Transient
    private String defaultValue;
    @Transient
    private int order;

    public FieldMapInfo() {
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public int getRightColType() {
        return rightColType;
    }

    public void setRightColType(int colType) {
        this.rightColType = colType;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getLeftColType() {
        return leftColType;
    }

    public void setLeftColType(int leftColType) {
        this.leftColType = leftColType;
    }

}
