package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 循环节点信息实例
 */
@ApiModel
@Data
public class CycleVo {

    @ApiModelProperty("节点id")
    private String id;

    @ApiModelProperty(value = "节点类型")
    private String type;

    @ApiModelProperty(value = "循环类型: foreach, range")
    private String cycleType;

    @ApiModelProperty(value = "区间循环:循环初始值,默认0")
    private String rangeBegin;

    @ApiModelProperty(value = "区间循环:循环结束值，不包含这个值")
    private String rangeEnd;

    @ApiModelProperty(value = "区间循环:每次循环递增值，默认1")
    private String rangeStep;

    @ApiModelProperty(value = "数据对象")
    private String source;

    @ApiModelProperty(value = "数组迭代循环:子集数据字段名")
    private String  subsetFieldName;

    @ApiModelProperty(value = "赋值类型：复制或者引用  1:复制  2：引用")
    private String assignType;


    private Integer intRangeBegin;

    private Integer intRangeEnd;

    private Integer intRangeStep;

    public CycleVo(){
        intRangeBegin = 0;
        intRangeStep = 1;
        assignType = "2";
    }
}
