package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.Map;

/**
 * es查询参数封装
 */
@ApiModel(value = "es查询参数配置")
public class EsSerachReadVo {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "索引名称")
    private String indexName;
    @ApiModelProperty(value = "查询类型")
    private String queryType;
}
