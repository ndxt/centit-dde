package com.centit.dde.transfer;

public class FieldMapInfo {

    private String leftName;
    private String rightName;
    private int rightColType;
    private int leftColType;
    private boolean isKey;
    private boolean isNullable;
    private String defaultValue;
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
