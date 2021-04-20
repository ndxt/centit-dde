package com.centit.dde.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 跳出循环  跳出本次循环   或者  结束整个循环  break  或者continue
 */
@Data
public class JumpOutVo {
    @ApiModelProperty("节点id")
    private String id;
    @ApiModelProperty(value = "节点类型")
    private String type;
    @ApiModelProperty(value = "结束类型 break  或者 continue ")
    private String endType;
    @ApiModelProperty(value = "起始节点id")
    private String endNodeId;
}
