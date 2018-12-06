package com.centit.dde.po;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.util.ItemValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class TableMapInfo {

    private static final Log logger = LogFactory.getLog(TableMapInfo.class);

    @Transient
    private String insertSql;

    @Transient
    private String updateSql;

    @Transient
    private String isExistSql;

    @Transient
    private String sourceSql;

    @Transient
    private String desTable;

    @Transient
    private String rowOptType;

    @Transient
    private int fieldCount; // 配对的fieldCount

    @Transient
    private int parameterSum; // insert语句和Update语句的参数个数

    @Transient
    private int destFieldCount;

    @Transient
    private int souFieldCount;

    @Transient
    private List<FieldMapInfo> fieldsMap;

    @Transient
    private int keyCount;

    @Transient
    private List<Integer> insertFieldMap;// 记录字段在 insert语句中的顺序号

    @Transient
    private List<Integer> updateFieldMap;// 记录字段在 update语句中的顺序号

    @Transient
    private List<Integer> keyFieldMap;

    @Transient
    private boolean repeatRun;

    public TableMapInfo() {
        fieldCount = parameterSum = destFieldCount = souFieldCount = keyCount = 0;
        fieldsMap = null;
        insertFieldMap = null;
        updateFieldMap = null;
        keyFieldMap = null;
    }

    private static String quotedstr(String ins) {
        String outs = ins.replaceAll("\"", "\'");
        return outs;
    }

    public void loadMapFromData(ExchangeMapInfo exchangeMapInfo) throws SqlResolveException {
        if (logger.isDebugEnabled()) {
            logger.debug("解析数据交换对应关系属性值...");
        }
        try {
            sourceSql = exchangeMapInfo.getQuerySql();
            desTable = exchangeMapInfo.getDestTableName();
            
            if ("1".equals(exchangeMapInfo.getRecordOperate())) {
                rowOptType = "insert";
            } else if ("2".equals(exchangeMapInfo.getRecordOperate())) {
                rowOptType = "update";
            } else {
                rowOptType = "merge";
            }
            if ("1".equals(exchangeMapInfo.getIsRepeat())) {
                repeatRun = true;
            } else {
                repeatRun = false;
            }
            fieldsMap = new ArrayList<>();
            /*
			 * 这一段应该是排序，这个其实是没有必要的在hbm.xml中应该可以指定顺序，添加order-by属性就可以
			 * List<MapInfoDetail> mapinfoDetails = new
			 * ArrayList<MapInfoDetail>(); int length =
			 * exchangeMapInfo.getMapInfoDetails().size(); for(int
			 * i=1;i<=length;i++){ for (MapInfoDetail mapinfoDetailtemp :
			 * exchangeMapInfo .getMapInfoDetails()){
			 * if(mapinfoDetailtemp.getCid(
			 * ).getColumnNo().equals(Long.valueOf(i))){
			 * mapinfoDetails.add(mapinfoDetailtemp); } } }
			 */
            for (MapInfoDetail mapInfoDetail : exchangeMapInfo.getMapInfoDetails()) {
                //添加对目标内容的过滤条件，没有目标字段的对应关系可以忽略
                if(!StringUtils.hasText( mapInfoDetail.getDestFieldName()))
                    continue;
                FieldMapInfo map = new FieldMapInfo();
                map.setRightColType(ItemValue.mapDbType(mapInfoDetail.getDestFieldType()));
                map.setLeftColType(ItemValue.mapDbType(mapInfoDetail.getSourceFieldType()));
                map.setDefaultValue(mapInfoDetail.getDestFieldDefault());
                if ("1".equals(mapInfoDetail.getIsPk())) {
                    map.setKey(true);
                } else {
                    map.setKey(false);
                }
                map.setLeftName(mapInfoDetail.getSourceFieldName());
                if ("1".equals(mapInfoDetail.getIsNull())) {
                    map.setNullable(true);
                } else {
                    map.setNullable(false);
                }
                map.setOrder(mapInfoDetail.getColumnNo().intValue());
                map.setRightName(mapInfoDetail.getDestFieldName());
                fieldsMap.add(map);
            }
            
            fieldCount = fieldsMap.size();// exchangeMapInfo.getMapInfoDetails().size();
            
            makeSqlNoNamedParams();
        } catch (Exception e) {
            throw new SqlResolveException(e.getMessage(), e);
        }
    }

    public void makeSqlNoNamedParams() {
        if (logger.isDebugEnabled()) {
            logger.debug("解析数据交换对应关系中字段值，生成 insert, update merge 执行的sql语句...");
        }

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
        for (int i = 0; i < fieldCount; i++) {
            if (i > 0) {
                sInsertSql = sInsertSql + ",";
                sValues = sValues + ",";
            }
            sInsertSql = sInsertSql + fieldsMap.get(i).getRightName();
            if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                insertFieldMap.add(i);
                parameterSum++;
                sValues = sValues + " ?";// ":prm" + i;
            } else {
                sValues = sValues + quotedstr(fieldsMap.get(i).getDefaultValue());
            }
            if (fieldsMap.get(i).isKey()) {
                if (!"".equals(sWhere)) {
                    sWhere = " and " + sWhere;
                }

                if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                    sWhere = fieldsMap.get(i).getRightName() + " = ?"// "=:prm"
                            // + i
                            + sWhere;
                    nKey++;
                    updateFieldMap.set(fieldCount - nKey, i);
                    // updateFieldMap.remove(fieldCount - nKey);
                    // updateFieldMap.add(fieldCount - nKey, i);
                } else {
                    sWhere = fieldsMap.get(i).getRightName() + " = " + quotedstr(fieldsMap.get(i).getDefaultValue())
                            + sWhere;
                }
            } else {
                if (nCol > 0) {
                    sUpdateSql = sUpdateSql + ',';
                }
                if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                    sUpdateSql = sUpdateSql + fieldsMap.get(i).getRightName() + "=?";// "=:prm"
                    // +
                    // i;
                    updateFieldMap.set(nCol, i);
					/*
					 * updateFieldMap.remove(nCol); updateFieldMap.add(nCol, i);
					 */
                } else {
                    sUpdateSql = sUpdateSql + fieldsMap.get(i).getRightName() + "="
                            + quotedstr(fieldsMap.get(i).getDefaultValue());
                }
                nCol++;
            }

        }
        insertSql = sInsertSql + ") " + sValues + ")";
        updateSql = sUpdateSql + " where " + sWhere;
        isExistSql = sIsExistSql + " where " + sWhere;

        if (parameterSum < fieldCount) {
            for (int i = 0; i < nKey; i++) {
                updateFieldMap.set(nCol + i, updateFieldMap.get(fieldCount - nKey + i));
            }
        }
        keyCount = nKey;
        for (int i = 0; i < nKey; i++) {
            keyFieldMap.add(updateFieldMap.get(nCol + i));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insertsql = " + insertSql + " 参数 = " + insertFieldMap);
            logger.debug("updatesql = " + updateSql + " 参数 = " + updateFieldMap);
            logger.debug("isExistSql = " + isExistSql);
        }

    }

    public void makeSqlWithNamedParams() {
        if (logger.isDebugEnabled()) {
            logger.debug("解析数据交换对应关系中字段值，生成 insert, update margen 执行的sql语句...");
        }

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
        for (int i = 0; i < fieldCount; i++) {
            if (i > 0) {
                sInsertSql = sInsertSql + ",";
                sValues = sValues + ",";
            }
            sInsertSql = sInsertSql + fieldsMap.get(i).getRightName();
            if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                insertFieldMap.add(i);
                parameterSum++;
                sValues = sValues + ":prm" + i;
            } else {
                sValues = sValues + quotedstr(fieldsMap.get(i).getDefaultValue());
            }
            if (fieldsMap.get(i).isKey()) {
                if (!"".equals(sWhere)) {
                    sWhere = " and " + sWhere;
                }

                if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                    sWhere = fieldsMap.get(i).getRightName() + "=:prm" + i + sWhere;
                    nKey++;
                    // updateFieldMap.remove(fieldCount - nKey);
                    updateFieldMap.set(fieldCount - nKey, i);
                } else {
                    sWhere = fieldsMap.get(i).getRightName() + "=" + quotedstr(fieldsMap.get(i).getDefaultValue())
                            + sWhere;
                }
            } else {
                if (nCol > 0) {
                    sUpdateSql = sUpdateSql + ',';
                }
                if (!StringUtils.hasText(fieldsMap.get(i).getDefaultValue())) {
                    sUpdateSql = sUpdateSql + fieldsMap.get(i).getRightName() + "=:prm" + i;
                    // updateFieldMap.remove(nCol);
                    updateFieldMap.set(nCol, i);
                } else {
                    sUpdateSql = sUpdateSql + fieldsMap.get(i).getRightName() + "="
                            + quotedstr(fieldsMap.get(i).getDefaultValue());
                }
                nCol++;
            }

        }
        insertSql = sInsertSql + ") " + sValues + ")";
        updateSql = sUpdateSql + " where " + sWhere;
        isExistSql = sIsExistSql + " where " + sWhere;

        if (parameterSum < fieldCount) {
            for (int i = 0; i < nKey; i++) {
                updateFieldMap.set(nCol + i, updateFieldMap.get(fieldCount - nKey + i));
            }
        }
        keyCount = nKey;
        for (int i = 0; i < nKey; i++) {
            keyFieldMap.add(updateFieldMap.get(nCol + i));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("insertsql = " + insertSql + " 参数 = " + insertFieldMap);
            logger.debug("updatesql = " + updateSql + " 参数 = " + updateFieldMap);
            logger.debug("isExistSql = " + isExistSql);
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

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
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

    public int getDestFieldCount() {
        return destFieldCount;
    }

    public void setDestFieldCount(int destFieldCount) {
        this.destFieldCount = destFieldCount;
    }

    public int getSouFieldCount() {
        return souFieldCount;
    }

    public void setSouFieldCount(int souFieldCount) {
        this.souFieldCount = souFieldCount;
    }

    public List<FieldMapInfo> getFieldsMap() {
        if (fieldsMap == null)
            fieldsMap = new ArrayList<FieldMapInfo>();
        return fieldsMap;
    }

    public void setFieldsMap(List<FieldMapInfo> fieldsMap) {
        this.fieldsMap = fieldsMap;
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

    public boolean isRepeatRun() {
        return repeatRun;
    }

    public void setRepeatRun(boolean repeatRun) {
        this.repeatRun = repeatRun;
    }

}
