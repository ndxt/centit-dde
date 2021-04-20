package com.centit.dde.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 结束循环  相当于end 符号
 */
@Data
public class FinishVo {
    @ApiModelProperty("节点id")
    private String id;
    @ApiModelProperty(value = "节点类型")
    private String type;
    @ApiModelProperty(value = "起始节点的id")
    private String startNodeId;
   /* @ApiModelProperty(value = "嵌套循环：父循环下个节点计数（内部使用）")
    private Integer parentNextDataIndex;
    @ApiModelProperty(value = "区间循环:循环初始值,默认0")
    private Integer nextDataIndex;
    @ApiModelProperty(value = "区间循环:每次循环递增值，默认1")
    private Integer increasingValue;*/
}
