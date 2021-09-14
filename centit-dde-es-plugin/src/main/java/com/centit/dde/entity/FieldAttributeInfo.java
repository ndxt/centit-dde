package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 条件字段属性信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "条件字段属性信息父类")
public class FieldAttributeInfo {
    @ApiModelProperty(value = "查询字段")
    private String fieldName;

    @ApiModelProperty(value = "查询字段值")
    private Object value;

    @ApiModelProperty(value = "组合类型 must   should    must_not filter ")
    private String combinationType;

    @ApiModelProperty(value = "组合查询类型，term  terms  match  matchall等")
    private String combQueryType;

    @ApiModelProperty(value = "查询权重，提升文档搜索评分的重要因素,精确查询和匹配查询可用")
    private Float boots;

    @ApiModelProperty(value = "指定查询分词器（匹配查询时才有效,默认入库时的分词器）")
    private String analyzer;

    @ApiModelProperty(value = "是否高亮显示")
    private Boolean isHighligh=false;
    //位置距离容差值 即查询条件分词后的位置差值   如果位置差值<=slop则返回该条结果
    @ApiModelProperty(value = "match_phrase查询：位置距离容差值")
    private Integer slop;

    @ApiModelProperty(value = "match_phrase_prefix查询：模糊匹配值偏移个数")
    private Integer maxExpansions;


    @ApiModelProperty(value = "控制查询条件的匹配关系(match使用)，可选项为or 和 and")
    private String operator;

    //查询最小匹配度   可以是百分比   也可以是整型  这里设置的是百分比 直观
    //即查询条件被分词后去查询的返回结果中必须满足minimumShouldMatch的个数或者百分比才返回，
    //例如：查询条件：张三  李四   原始数据：我看见张三和李四为了一瓶水在打架  此刻minimumShouldMatch=2 返回结果中至少包含张三 李四2个条件才返回
    @ApiModelProperty(value = "查询最小匹配度（百分比）")
    private String minimumShouldMatch;
}
