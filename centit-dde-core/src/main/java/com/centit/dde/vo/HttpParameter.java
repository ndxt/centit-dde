package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "HTTP模板参数")
public class HttpParameter {

    @ApiModelProperty(value = "网关名称")
    private String packetName;

    @ApiModelProperty(value = "资源管理中登录接口code")
    private String loginUrlCode;

    //请求接口资源code（接口前缀，如：http:127.0.0.1:8080/conctext/methodName）
    @ApiModelProperty(value = "资源管理中请求接口code")
    private String httpUrlCode;

    @ApiModelProperty(value = "详细接口地址(具体的方法)")
    private String methodName;

    @ApiModelProperty(value = "请求类型")
    private String methodType;

    @ApiModelProperty(value = "请求体数据")
    private String requestBody;

    @ApiModelProperty(value = "参数列表  [{key:value},{key:value}]")
    private Object[] paramesList;

    @ApiModelProperty(value = "应用ID")
    private String osId;

    @ApiModelProperty(value = "菜单ID")
    private String optId;
}
