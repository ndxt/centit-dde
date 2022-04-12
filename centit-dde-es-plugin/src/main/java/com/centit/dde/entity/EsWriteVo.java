package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsWriteVo {
    @ApiModelProperty(value = "节点id")
    private String id;
    @ApiModelProperty(value = "索引名称")
    private String indexName;
    @ApiModelProperty(value = "数据集id")
    private String source;
    @ApiModelProperty(value = "文档id 由一个或者多个字段组成 （新增时填写，多个字段逗号隔开） 不填自动生成")
    private  List<String> documentIds = new ArrayList<>();
    @ApiModelProperty(value = "集成资源中的资源id")
    private String databaseId;
}
