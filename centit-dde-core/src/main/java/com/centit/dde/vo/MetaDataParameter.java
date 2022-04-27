package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于创建元数据或者http调用类型的API使用
 */
@Data
@ApiModel(value = "元数据模板参数")
public class MetaDataParameter {

    @ApiModelProperty(value = "数据库主键")
    private String databaseCode;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表ID")
    private String tableId;

    @ApiModelProperty(value = "创建类型")
    private Integer[] createType;

    @ApiModelProperty(value = "应用ID")
    private String osId;

    @ApiModelProperty(value = "菜单ID")
    private String optId;
}
