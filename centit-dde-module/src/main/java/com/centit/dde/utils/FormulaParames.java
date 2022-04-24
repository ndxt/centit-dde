package com.centit.dde.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 表达式计算参数
 */
@ApiModel("表达式测试参数")
public class FormulaParames {
    @ApiModelProperty("表达式")
    private String formula;
    @ApiModelProperty("需要测试的对象，json格式")
    private String jsonString;

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
