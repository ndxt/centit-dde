package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 赋值节点
 */
@ApiModel
@Data
public class AssignmentVo {
    @ApiModelProperty("节点id")
    private String id;
    @ApiModelProperty(value = "节点类型")
    private String type;
    @ApiModelProperty(value = "赋值类型：复制或者引用  1:复制   2：引用，3：手动赋值")
    private String assignType="2";
    @ApiModelProperty(value = "给modelTag中设置值")
    private String data;
    @ApiModelProperty(value = "数据节点id")
    private String source;
    @ApiModelProperty(value = "表达式")
    private String expression;
}
