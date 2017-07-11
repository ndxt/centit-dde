package com.centit.dde.dataio;

import com.centit.dde.po.ImportField;
import com.centit.dde.po.ImportOpt;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ImportSqlOpt {
    private String insertSql;
    private String updateSql;
    private String isExistSql;

    private String desTable;
    private String rowOptType;
    private int fieldCount; // 配对的fieldCount
    private int parameterSum; // insert语句和Update语句的参数个数
    private int keyCount;
    private List<Integer> insertFieldMap;// 记录字段在 insert语句中的顺序号
    private List<Integer> updateFieldMap;// 记录字段在 update语句中的顺序号
    private List<Integer> keyFieldMap;
    private ImportOpt importOpt;

    public ImportSqlOpt() {
        fieldCount = parameterSum = keyCount = 0;
        insertFieldMap = null;
        updateFieldMap = null;
        keyFieldMap = null;
    }

    public ImportField getImportField(int index) {
        return importOpt.getField(index);
    }

    public void loadMapFromData(ImportOpt importOpt) {
        this.importOpt = importOpt;
        try {

            desTable = importOpt.getTableName();
            fieldCount = importOpt.getImportFields().size();
            if ("1".equals(importOpt.getRecordOperate())) {
                rowOptType = "insert";
            } else if ("2".equals(importOpt.getRecordOperate())) {
                rowOptType = "update";
            } else {
                rowOptType = "merge";
            }


            makeSqlNoNamedParams();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String quotedstr(String ins) {
        String outs = ins.replaceAll("\"", "\'");
        return outs;
    }

    public void makeSqlNoNamedParams() {
        parameterSum = 0;
        String sInsertSql = "insert into " + desTable + " (";
        String sValues = "values(";
        String sIsExistSql = "select count(1) as isthere from " + desTable;
        String sUpdateSql = "update " + desTable + " set ";
        String sWhere = "";
        int nKey = 0;
        int nCol = 0;
        insertFieldMap = new ArrayList<Integer>(fieldCount);
        updateFieldMap = new ArrayList<Integer>(fieldCount);
        keyFieldMap = new ArrayList<Integer>(fieldCount);
        for (int length = 0; length < fieldCount; length++) {
            updateFieldMap.add(length, length);
        }

        int i = 0;
        //for (int i = 0; i < fieldCount; i++) {
        for (ImportField field : importOpt.getImportFields()) {
            if (i > 0) {
                sInsertSql = sInsertSql + ",";
                sValues = sValues + ",";
            }

            sInsertSql = sInsertSql + field.getDestFieldName();
            if (!StringUtils.hasText(field.getDestFieldDefault())) {
                insertFieldMap.add(i);
                parameterSum++;
                sValues = sValues + " ?";// ":prm" + i;
            } else {
                sValues = sValues
                        + quotedstr(field.getDestFieldDefault());
            }


            if (field.isKey()) {
                if (!"".equals(sWhere)) {
                    sWhere = " and " + sWhere;
                }

                if (!StringUtils.hasText(field.getDestFieldDefault())) {
                    sWhere = field.getDestFieldName() + " = ?"//  "=:prm" + i
                            + sWhere;
                    nKey++;
                    updateFieldMap.set(fieldCount - nKey, i);
                    //updateFieldMap.remove(fieldCount - nKey);
                    //updateFieldMap.add(fieldCount - nKey, i);
                } else {
                    sWhere = field.getDestFieldName() + " = "
                            + quotedstr(field.getDestFieldDefault())
                            + sWhere;
                }
            } else {
                if (nCol > 0) {
                    sUpdateSql = sUpdateSql + ',';
                }

                if (!StringUtils.hasText(field.getDestFieldDefault())) {
                    sUpdateSql = sUpdateSql + field.getDestFieldName()
                            + "=?";// "=:prm" + i;
                    updateFieldMap.set(nCol, i);
                    /*updateFieldMap.remove(nCol);
                    updateFieldMap.add(nCol, i);*/
                } else {
                    sUpdateSql = sUpdateSql + field.getDestFieldName()
                            + "="
                            + quotedstr(field.getDestFieldDefault());
                }
                nCol++;
            }
            i++;

        }
        insertSql = sInsertSql + ") " + sValues + ")";
        updateSql = sUpdateSql + " where " + sWhere;
        isExistSql = sIsExistSql + " where " + sWhere;

        if (parameterSum < fieldCount) {
            for (i = 0; i < nKey; i++) {
                updateFieldMap.set(nCol + i,
                        updateFieldMap.get(fieldCount - nKey + i));
            }
        }
        keyCount = nKey;
        for (i = 0; i < nKey; i++) {
            keyFieldMap.add(updateFieldMap.get(nCol + i));
        }

    }

    public void makeSqlWithNamedParams() {
        parameterSum = 0;
        String sInsertSql = "insert into " + desTable + " (";
        String sValues = "values(";
        String sIsExistSql = "select count(1) as isthere from " + desTable;
        String sUpdateSql = "update " + desTable + " set ";
        String sWhere = "";
        int nKey = 0;
        int nCol = 0;
        insertFieldMap = new ArrayList<Integer>(fieldCount);
        updateFieldMap = new ArrayList<Integer>(fieldCount);
        keyFieldMap = new ArrayList<Integer>(fieldCount);
        for (int length = 0; length < fieldCount; length++) {
            updateFieldMap.add(length, length);
        }

        int i = 0;
        for (ImportField field : importOpt.getImportFields()) {
            if (i > 0) {
                sInsertSql = sInsertSql + ",";
                sValues = sValues + ",";
            }

            sInsertSql = sInsertSql + field.getDestFieldName();
            if (!StringUtils.hasText(field.getDestFieldDefault())) {
                insertFieldMap.add(i);
                parameterSum++;
                sValues = sValues + ":prm" + i;
            } else {
                sValues = sValues
                        + quotedstr(field.getDestFieldDefault());
            }
            if (field.isKey()) {
                if (!"".equals(sWhere)) {
                    sWhere = " and " + sWhere;
                }

                if (!StringUtils.hasText(field.getDestFieldDefault())) {
                    sWhere = field.getDestFieldName() + "=:prm" + i
                            + sWhere;
                    nKey++;
                    //updateFieldMap.remove(fieldCount - nKey);
                    updateFieldMap.set(fieldCount - nKey, i);
                } else {
                    sWhere = field.getDestFieldName() + "="
                            + quotedstr(field.getDestFieldDefault())
                            + sWhere;
                }
            } else {
                if (nCol > 0) {
                    sUpdateSql = sUpdateSql + ',';
                }

                if (!StringUtils.hasText(field.getDestFieldDefault())) {
                    sUpdateSql = sUpdateSql + field.getDestFieldName()
                            + "=:prm" + i;
                    //updateFieldMap.remove(nCol);
                    updateFieldMap.set(nCol, i);
                } else {
                    sUpdateSql = sUpdateSql + field.getDestFieldName()
                            + "="
                            + quotedstr(field.getDestFieldDefault());
                }
                nCol++;
            }
            i++;
        }

        insertSql = sInsertSql + ") " + sValues + ")";
        updateSql = sUpdateSql + " where " + sWhere;
        isExistSql = sIsExistSql + " where " + sWhere;

        if (parameterSum < fieldCount) {
            for (i = 0; i < nKey; i++) {
                updateFieldMap.set(nCol + i,
                        updateFieldMap.get(fieldCount - nKey + i));
            }
        }
        keyCount = nKey;
        for (i = 0; i < nKey; i++) {
            keyFieldMap.add(updateFieldMap.get(nCol + i));
        }

    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public String getIsExistSql() {
        return isExistSql;
    }

    public void setIsExistSql(String isExistSql) {
        this.isExistSql = isExistSql;
    }


    public String getDesTable() {
        return desTable;
    }

    public void setDesTable(String desTable) {
        this.desTable = desTable;
    }

    public String getRowOptType() {
        return rowOptType;
    }

    public void setRowOptType(String rowOptType) {
        this.rowOptType = rowOptType;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getParameterSum() {
        return parameterSum;
    }

    public void setParameterSum(int parameterSum) {
        this.parameterSum = parameterSum;
    }


    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int kc) {
        this.keyCount = kc;
    }

    public List<Integer> getInsertFieldMap() {
        if (insertFieldMap == null)
            insertFieldMap = new ArrayList<Integer>();
        return insertFieldMap;
    }

    public void setInsertFieldMap(List<Integer> insertFieldMap) {
        this.insertFieldMap = insertFieldMap;
    }

    public List<Integer> getUpdateFieldMap() {
        if (updateFieldMap == null)
            updateFieldMap = new ArrayList<Integer>();
        return updateFieldMap;
    }

    public void setUpdateFieldMap(List<Integer> updateFieldMap) {
        this.updateFieldMap = updateFieldMap;
    }

    public List<Integer> getKeyFieldMap() {
        if (keyFieldMap == null)
            keyFieldMap = new ArrayList<Integer>();
        return keyFieldMap;
    }

    public void setKeyFieldMap(List<Integer> keyFieldMap) {
        this.keyFieldMap = keyFieldMap;
    }


}
