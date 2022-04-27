package com.centit.dde.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 表达式计算参数
 */
@Data
@ApiModel(value = "表达式测试参数")
public class FormulaParameter {

    @ApiModelProperty(value = "表达式")
    private String formula;

    @ApiModelProperty(value = "需要测试的对象，json格式")
    private String jsonString;

}
