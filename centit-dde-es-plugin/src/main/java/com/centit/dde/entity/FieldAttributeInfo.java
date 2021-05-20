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
@ApiModel(value = "条件字段属性信息")
public class FieldAttributeInfo {
    @ApiModelProperty(value = "查询字段")
    private String fieldName;

    @ApiModelProperty(value = "查询字段值")
    private Object value;

    @ApiModelProperty(value = "组合类型")
    private String combinationType;

    @ApiModelProperty(value = "查询类型")
    private String queryType;

    @ApiModelProperty(value = "查询权重，提升文档搜索评分的重要因素,精确查询和匹配查询可用")
    private Float boots;

    @ApiModelProperty(value = "指定查询分词器（匹配查询时才有效,默认入库时的分词器）")
    private String analyzer;

    @ApiModelProperty(value = "是否高亮显示")
    private Boolean isHighligh;

    @ApiModelProperty(value = "match_phrase查询：位置距离容差值")
    private Integer slop;

    @ApiModelProperty(value = "match_phrase_prefix查询：模糊匹配值偏移个数")
    private Integer maxExpansions;

}
