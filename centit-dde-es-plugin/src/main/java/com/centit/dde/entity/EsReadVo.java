package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * es查询参数封装
 */
@ApiModel(value = "节点基本信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsReadVo {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "集成资源中的资源id")
    private String dataSourceId;

    @ApiModelProperty(value = "查询公共参数")
    private QueryParameter queryParameter;

    @ApiModelProperty(value = "查询条件字段集合")
    List<FieldAttributeInfo> fieldAttributeInfos;

    @ApiModelProperty(value = "查询类型")
    private String queryType;
}
