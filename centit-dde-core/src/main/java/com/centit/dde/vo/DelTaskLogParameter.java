package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 删除某个时间段之前的数据
 */
@Data
@ApiModel(value = "删除某个时间段之前的数据")
public class DelTaskLogParameter {
    @ApiModelProperty("API主键")
    private  String packetId;

    @ApiModelProperty("指定时间")
    private  String runBeginTime;

    @ApiModelProperty("是否包含错误信息")
    private  Boolean isError;
}
