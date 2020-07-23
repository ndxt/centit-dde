package com.centit.dde.utils;

import com.centit.support.database.metadata.SimpleTableField;
import com.centit.support.database.metadata.SimpleTableInfo;
import com.centit.support.database.utils.FieldType;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 *      数据库连接信息 DatabaseInfo
 *      对应的表信息 SimpleTableInfo
 * @author zhf
 */

public abstract  class KeyValueTableUtil {
    public static SimpleTableInfo createKeyValueTableInfo(String tableName){
        SimpleTableInfo tableInfo = new SimpleTableInfo(tableName);
        SimpleTableField column = new SimpleTableField();
        column.setColumnName("OBJECT_ID");
        column.setPropertyName("objectId");
        column.setFieldType(FieldType.STRING);
        column.setColumnType("varchar(64)");
        column.setMaxLength(64);
        column.setPrimaryKey(true);
        tableInfo.addColumn(column);

        column = new SimpleTableField();
        column.setColumnName("OBJECT_JSON");
        column.setPropertyName("objectJson");
        column.setFieldType(FieldType.JSON_OBJECT);
        column.setColumnType("clob");
        tableInfo.addColumn(column);

        column = new SimpleTableField();
        column.setColumnName("LAST_MODIFY_TIME");
        column.setPropertyName("lastModifyTime");
        column.setFieldType(FieldType.DATETIME);
        column.setColumnType("date");
        tableInfo.addColumn(column);

        return tableInfo;
    }
}
