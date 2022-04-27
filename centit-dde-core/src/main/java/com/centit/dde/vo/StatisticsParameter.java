package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 日志统计参数
 */
@Data
@ApiModel(value = "日志统计参数")
public class StatisticsParameter {

    @ApiModelProperty(value = "菜单ID")
    private String optId;

    @ApiModelProperty(value = "API网关ID")
    private String packetId;
}
