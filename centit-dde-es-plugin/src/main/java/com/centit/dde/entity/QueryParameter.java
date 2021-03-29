package com.centit.dde.entity;

import io.swagger.annotations.ApiModelProperty;

//查询参数封装
public class QueryParameter {
    @ApiModelProperty(value = "查询权重，提升文档搜索评分的重要因素,精确查询和匹配查询可用")
    private Float boots;
    @ApiModelProperty(value = "查询字段")
    private String field;
    @ApiModelProperty(value = "查询字段值")
    private String value;
    @ApiModelProperty(value = "指定查询分词器（匹配查询时才有效,默认入库时的分词器）")
    private String analyzer;
    //查询最小匹配度   可以是百分比   也可以是整型  这里设置的是百分比 直观
    @ApiModelProperty(value = "查询最小匹配度（百分比）")
    private String queryMinimumProportion;

    public String getQueryMinimumProportion() {
        return queryMinimumProportion;
    }

    public void setQueryMinimumProportion(String queryMinimumProportion) {
        this.queryMinimumProportion = queryMinimumProportion;
    }

    public Float getBoots() {
        return boots;
    }

    public void setBoots(Float boots) {
        this.boots = boots;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }
}
