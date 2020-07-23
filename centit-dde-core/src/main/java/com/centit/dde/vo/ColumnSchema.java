package com.centit.dde.vo;

import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.database.utils.FieldType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhf
 */
@Data
@NoArgsConstructor
public class ColumnSchema {

    public ColumnSchema(String propertyName) {
        this.columnCode = propertyName;
        this.propertyName = propertyName;
        this.columnName = propertyName;
        this.dataType = FieldType.STRING;
        this.isStatData = BooleanBaseOpt.ONE_CHAR_FALSE;
    }

    @ApiModelProperty(value = "字段代码")
    String columnCode;
    @ApiModelProperty(value = "字段属性名")
    String propertyName;
    @ApiModelProperty(value = "字段名")
    String columnName;
    @ApiModelProperty(value = "字段类型")
    String dataType;
    @ApiModelProperty(value = "是否为统计字段")
    String isStatData;

    public ColumnSchema duplicate() {
        ColumnSchema dup = new ColumnSchema();
        dup.columnCode = this.columnCode;
        dup.propertyName = this.propertyName + ":dup";
        dup.columnName = this.columnName;
        dup.dataType = this.dataType;
        dup.isStatData = this.isStatData;
        return dup;
    }
}
