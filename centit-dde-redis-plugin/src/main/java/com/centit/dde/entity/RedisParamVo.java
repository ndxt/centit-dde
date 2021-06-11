package com.centit.dde.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("redis操作参数")
public class RedisParamVo {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "数据集")
    private String source;

    @ApiModelProperty(value = "集成资源中的资源id")
    private String dataSourceId;

    @ApiModelProperty(value = "redis部署类型，单节点（singleNode）、集群(cluster)、哨兵(sentinel)")
    private String redisType;

    @ApiModelProperty(value = "redis操作方法")
    private String methodName;

    @ApiModelProperty(value = "方法参数")
    private String[] methodParam;

    public String[] getMethodParam() {
        return methodParam;
    }

    public void setMethodParam(String[] methodParam) {
        this.methodParam = methodParam;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRedisType() {
        return redisType;
    }

    public void setRedisType(String redisType) {
        this.redisType = redisType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
