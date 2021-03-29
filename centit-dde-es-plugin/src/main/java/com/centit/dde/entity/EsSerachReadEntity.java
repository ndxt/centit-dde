package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * es查询参数封装
 */
@ApiModel
public class EsSerachReadEntity {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;
    @ApiModelProperty(value = "索引名称")
    private String indexName;
    @ApiModelProperty(value = "查询类型   1：精确查询   2条件查询  3 范围查询  不传值为：分页查询（查询时填写）")
    private String queryType;
    @ApiModelProperty(value = "查询字段 field  查询字段值（查询时填写）")
    private List<QueryParameter> queryFieldMap;
    @ApiModelProperty(value = "当前页码（分页查询时填写）")
    private Integer pageNo;
    @ApiModelProperty(value = "查询调试（分页查询时填写）")
    private Integer pageSize;
    @ApiModelProperty(value = "查询返回字段")
    private String returnField[];
    @ApiModelProperty(value = "查询不返回字段")
    private String notReturnField[];
    @ApiModelProperty(value = "排序字段")
    private Map<String,String> sortFieldMap;
    @ApiModelProperty(value = "元数据库中对于业务的数据库连接信息id")
    private String dataSourceId;
    @ApiModelProperty(value = "范围查询时使用，范围字段名")
    private String rangeField;
    @ApiModelProperty(value = "范围查询起始值")
    private String rangeStartValue;
    @ApiModelProperty(value = "范围查询结束值")
    private String rangeEndValue;

    public List<QueryParameter> getQueryFieldMap() {
        return queryFieldMap;
    }

    public void setQueryFieldMap(List<QueryParameter> queryFieldMap) {
        this.queryFieldMap = queryFieldMap;
    }

    public String getRangeStartValue() {
        return rangeStartValue;
    }

    public void setRangeStartValue(String rangeStartValue) {
        this.rangeStartValue = rangeStartValue;
    }

    public String getRangeEndValue() {
        return rangeEndValue;
    }

    public void setRangeEndValue(String rangeEndValue) {
        this.rangeEndValue = rangeEndValue;
    }

    public String getRangeField() {
        return rangeField;
    }

    public void setRangeField(String rangeField) {
        this.rangeField = rangeField;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getReturnField() {
        return returnField;
    }

    public void setReturnField(String[] returnField) {
        this.returnField = returnField;
    }

    public String[] getNotReturnField() {
        return notReturnField;
    }

    public void setNotReturnField(String[] notReturnField) {
        this.notReturnField = notReturnField;
    }

    public Map<String, String> getSortFieldMap() {
        return sortFieldMap;
    }

    public void setSortFieldMap(Map<String, String> sortFieldMap) {
        this.sortFieldMap = sortFieldMap;
    }
}
