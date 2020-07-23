package com.centit.dde.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf
 */
@Data
@NoArgsConstructor
public class DataSetSchema implements Serializable {
    private static final long serialVersionUID = 1L;

    public DataSetSchema(String dataSetName) {
        this.dataSetId = dataSetName;
        this.dataSetName = dataSetName;
        this.dataSetTitle = dataSetName;
        //this.isOnlyInterVariable = BooleanBaseOpt.ONE_CHAR_FALSE;
    }

    @ApiModelProperty(value = "数据ID，一般和数据集名一样")
    String dataSetId;
    @ApiModelProperty(value = "数据集名")
    String dataSetName;
    @ApiModelProperty(value = "数据集标题")
    String dataSetTitle;
    @ApiModelProperty(value = "数据集字段列表")
    List<ColumnSchema> columns;
    //@ApiModelProperty(value = "是否仅作为中间数据")
    //String isOnlyInterVariable;

    public void addColumn(ColumnSchema column) {
        if (column == null) {
            return;
        }
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }

    public boolean existColumn(String propertyName) {
        if (columns == null) {
            return false;
        } else {
            for (ColumnSchema column : columns) {
                if (StringUtils.equals(column.getPropertyName(), propertyName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public ColumnSchema fetchColumn(String propertyName) {
        if (columns == null) {
            return null;
        } else {
            for (ColumnSchema column : columns) {
                if (StringUtils.equals(column.getPropertyName(), propertyName)) {
                    return column;
                }
            }
            return null;
        }
    }

    public void addColumnIfNotExist(String propertyName) {
        if (!this.existColumn(propertyName)) {
            this.addColumn(new ColumnSchema(propertyName));
        }
    }

    public void addColumnDup(String propertyName, String dupPropertyName) {
        this.addColumn(new ColumnSchema(this.existColumn(propertyName) ? dupPropertyName : propertyName));
    }

    public DataSetSchema duplicate() {
        DataSetSchema dup = new DataSetSchema(this.getDataSetName() + ":dup");
        if (this.columns != null) {
            List<ColumnSchema> dupcolumns = new ArrayList<>(this.columns.size());
            dupcolumns.addAll(this.columns);
            dup.setColumns(dupcolumns);
        }
        return dup;
    }
}
