package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OFDConvertVo {
    @ApiModelProperty("节点id")
    private  String id;

    @ApiModelProperty("节点类型")
    private String type;

    @ApiModelProperty("文件id")
    private String fileid;

    @ApiModelProperty("OFD服务地址")
    private String httpUrl;

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
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
}
