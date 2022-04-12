package com.centit.dde.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * es查询参数封装
 */
@ApiModel(value = "节点基本信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsQueryVo {
    @ApiModelProperty(value = "本节点数据集id")
    private String id;

    @ApiModelProperty(value = "es服务地址")
    private String databaseId;

    @ApiModelProperty(value = "查询索引,多个索引就夸索引查询")
    private String[] queryIndex;

    @ApiModelProperty(value = "查询条件")
    private List<JSONObject> queryCondition;

    @ApiModelProperty(value = "排序字段")
    private List<JSONObject> sortField = new ArrayList<>();

    @ApiModelProperty(value = "页码")
    private Integer pageNo;

    @ApiModelProperty(value = "查询条数")
    private Integer pageSize;

    @ApiModelProperty(value = "返回字段")
    private String[] returnField;

    @ApiModelProperty(value = "查询超时时间")
    private Integer timeout;

    @ApiModelProperty(value = "是否返回评分")
    private Boolean explain=new Boolean(false);

    @ApiModelProperty(value = "查询条件信息")
    List<FieldAttributeInfo> fieldAttributeInfos;
}
