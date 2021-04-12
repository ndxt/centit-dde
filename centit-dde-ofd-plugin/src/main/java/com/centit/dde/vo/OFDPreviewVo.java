package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OFDPreviewVo {
    @ApiModelProperty("节点id")
    private  String id;

    @ApiModelProperty("节点类型")
    private String type;

    @ApiModelProperty("数据集id")
    private String source;

    @ApiModelProperty("OFD服务地址")
    private String httpUrl;

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }
}
