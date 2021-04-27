package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class EsSearchWriteEntity {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "索引名称")
    private String indexName;
    @ApiModelProperty(value = "数据来源id")
    private String source;
    @ApiModelProperty(value = "文档id 必填 由一个或者多个字段组成 （新增时填写，多个字段逗号隔开）")
    private  String documentIds;
    @ApiModelProperty(value = "元数据库中对于业务的数据库连接信息id")
    private String dataSourceId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(String documentIds) {
        this.documentIds = documentIds;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
