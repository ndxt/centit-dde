package com.centit.dde.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//查询参数封装
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ES查询条件公共信息")
public class QueryParameter {
    @ApiModelProperty(value = "索引名称")
    private String indexName;

    @ApiModelProperty(value = "Boolean组合查询 must   should    must_not filter ")
    private String boolQueryType;

    @ApiModelProperty(value = "查询条数")
    private Integer pageSize=10;

    @ApiModelProperty(value = "页码")
    private Integer pageNo=0;

    @ApiModelProperty(value = "查询超时时间,单位：秒")
    private Integer timeOut;

    @ApiModelProperty(value = "是否返回分页信息")
    private Boolean isReturnPageInfo=false;

    @ApiModelProperty(value = "是否返回文档的评分解释 默认false")
    private Boolean explain=false;

    //以下内部逻辑使用
    @ApiModelProperty(value = "排序字段(fieldName(排序字段)，sortValue（排序字段值，DESC或者ASC)")
    private List<JSONObject> sortField;

    @ApiModelProperty(value = "查询结果返回字段")
    private String[] returnField;

    @ApiModelProperty(value = "查询结果不返回的字段")
    private String[] notReturnField;
}
