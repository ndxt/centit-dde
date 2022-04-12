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

    @ApiModelProperty(value = "查询类型")
    private String queryType;

    @ApiModelProperty(value = "运算符")
    private String operator;

    @ApiModelProperty(value = "取值表达式")
    private String expression;

    @ApiModelProperty(value = "分词器")
    private String analyze;

    @ApiModelProperty(value = "是否高亮显示")
    private Boolean isHighligh=false;

    @ApiModelProperty(value = "设置查询权重(增加评分)")
    private Float boost;

    @ApiModelProperty(value = "控制查询条件的匹配关系(match使用)，可选项为or 和 and")
    private String matchOperator;

    //查询最小匹配度   可以是百分比   也可以是整型  这里设置的是百分比 直观
    //即查询条件被分词后去查询的返回结果中必须满足minimumShouldMatch的个数或者百分比才返回，
    //例如：查询条件：张三  李四   原始数据：我看见张三和李四为了一瓶水在打架  此刻minimumShouldMatch=2 返回结果中至少包含张三 李四2个条件才返回
    @ApiModelProperty(value = "查询最小匹配度（百分比）")
    private String minimumShouldMatch;

    @ApiModelProperty(value = "match_phrase_prefix查询：模糊匹配值偏移个数")
    private Integer maxExpansions;

    @ApiModelProperty(value = "match_phrase查询：模糊匹配值偏移个数")
    private Integer slop;

}
