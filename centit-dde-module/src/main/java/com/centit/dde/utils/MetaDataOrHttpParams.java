package com.centit.dde.utils;

/**
 * 用于创建元数据或者http调用类型的API使用
 */
public class MetaDataOrHttpParams {
    //库id
    private String databaseCode;
    //表名
    private String tableName;
    //资源名 http调用使用
    private String databaseName;
    //表id
    private String tableId;
    //创建类型  新建   删除  查询  查看   等
    private Integer[] createType;

    private String osId;

    private String optId;


    public String getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(String databaseCode) {
        this.databaseCode = databaseCode;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }


    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer[] getCreateType() {
        return createType;
    }

    public void setCreateType(Integer[] createType) {
        this.createType = createType;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }
}
