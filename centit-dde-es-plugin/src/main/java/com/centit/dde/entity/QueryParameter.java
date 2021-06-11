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

    @ApiModelProperty(value = "查询条数")
    private Integer pageSize=10;

    @ApiModelProperty(value = "页码")
    private Integer pageNo=0;

    //查询最小匹配度   可以是百分比   也可以是整型  这里设置的是百分比 直观
    @ApiModelProperty(value = "查询最小匹配度（百分比）")
    private Object minimumShouldMatch;

    @ApiModelProperty(value = "排序字段(fieldName(排序字段)，sortValue（排序字段值，DESC或者ASC）) ")
    private List<JSONObject> sortField;

    @ApiModelProperty(value = "查询结果返回字段")
    private String[] returnField;

    @ApiModelProperty(value = "查询结果不返回的字段")
    private String[] notReturnField;

    @ApiModelProperty(value = "查询超时时间,单位：秒")
    private Integer timeOut;

    @ApiModelProperty(value = "是否返回分页信息")
    private Boolean isReturnPageInfo;
}
