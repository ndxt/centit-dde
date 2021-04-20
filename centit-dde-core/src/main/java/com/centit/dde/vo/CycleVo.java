package com.centit.dde.vo;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
    @ApiModelProperty(value = "数据对象")
    private String source;
    @ApiModelProperty(value = "嵌套循环：父循环下个节点计数(后台使用)")
    private Integer parentNextDataIndex;
    @ApiModelProperty(value = "区间循环:循环初始值,默认0")
    private Integer nextDataIndex;
    @ApiModelProperty(value = "区间循环:每次循环递增值，默认1")
    private Integer increasingValue;
    @ApiModelProperty(value = "赋值类型：复制或者引用  1:复制   2：引用")
    private String assignType="2";
    @ApiModelProperty(value = "子集数据字段名")
    private String  subsetFieldName;
    @ApiModelProperty(value = "标识是对象还是集合（程序内部使用）")
    private String  flag;
    @ApiModelProperty(value = "是否结束（程序内部使用）")
    private Boolean  isEnd;
}
