package com.centit.dde.utils;

/**
 * 用于创建元数据或者http调用类型的API使用
 */
public class MetaDataOrHttpParams {
    //库id
    private String dataBaseCode;

    //表id
    private String tableId;
    //创建类型  新建   删除  查询  查看   等
    private Integer[] createType;

    private String osId;

    private String optId;


    public String getDataBaseCode() {
        return dataBaseCode;
    }

    public void setDataBaseCode(String dataBaseCode) {
        this.dataBaseCode = dataBaseCode;
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
