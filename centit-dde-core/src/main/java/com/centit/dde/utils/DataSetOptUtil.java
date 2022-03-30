package com.centit.dde.utils;

import com.alibaba.fastjson.JSON;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.datarule.CheckRule;
import com.centit.dde.datarule.CheckRuleUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.security.model.StandardPasswordEncoderImpl;
import com.centit.support.algorithm.*;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.image.CaptchaImageUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.StatUtils;

import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 * @author zhf
 */
public abstract class DataSetOptUtil {

    private static Map<String, Function<Object[], Object>> extendFuncs = null;
    //扩展函数表达式
    public static  Map<String, Function<Object[], Object>> makeExtendFuns(){
        if(extendFuncs == null ){
            extendFuncs = new HashMap<>();
            extendFuncs.put("toJson", (a) -> JSON.parse(StringBaseOpt.castObjectToString(a[0])));
            extendFuncs.put("toByteArray", (a) -> {
                try {
                    return IOUtils.toByteArray((InputStream) a[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return a;
            });
            extendFuncs.put("uuid", (a) -> UuidOpt.getUuidAsString32());
            extendFuncs.put("random", (a) -> CaptchaImageUtil.getRandomString(NumberBaseOpt.castObjectToInteger(a[0])));
            extendFuncs.put("encode", (a) -> new StandardPasswordEncoderImpl().encode(StringBaseOpt.castObjectToString(a[0])));
            extendFuncs.put("dict", (a) -> {
                if (a != null && a.length > 1) {
                    String regex =",";
                    if (a.length>2) {
                        regex = StringBaseOpt.objectToString(a[2]);
                    }
                    String[] strings=StringBaseOpt.objectToString(a[1]).split(regex);
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String string : strings) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append(regex);
                        }
                        String value = CodeRepositoryUtil.getValue(StringBaseOpt.castObjectToString(a[0]), StringBaseOpt.castObjectToString(string));
                        value = !value.equals(string)?value:
                            CodeRepositoryUtil.getCode(StringBaseOpt.castObjectToString(a[0]),StringBaseOpt.castObjectToString(string));
                        stringBuilder.append(value);
                    }
                    return stringBuilder.toString();
                }
                else {
                    return a;
                }
            });
            extendFuncs.put("dictTrans", (a) -> {
                if (a != null && a.length > 1) {
                    return CodeRepositoryUtil.transExpression(
                        StringBaseOpt.castObjectToString(a[0]),
                        StringBaseOpt.castObjectToString(a[1]));
                }
                else {
                    return a;
                }
            });
            extendFuncs.put("replace", (a) -> {
                if (a != null && a.length > 2) {
                    return StringUtils.replace(StringBaseOpt.castObjectToString(a[0]),
                        StringBaseOpt.castObjectToString(a[1]),StringBaseOpt.castObjectToString(a[2]));
                }
                else if (a != null && a.length>0){
                    return a[0];
                } else{
                    return a;
                }
            });
            extendFuncs.put("size",(a)->{
                Object o = Arrays.stream(a).toArray()[0];
                if (o instanceof Collection){
                    return ((Collection<?>) o).size();
                }
                if (o instanceof Map){
                    return ((Map<?, ?>) o).size();
                }
                return "";
            });
            extendFuncs.put("startsWith",(a)->{
                Object[] objects = Arrays.stream(a).toArray();
                if (objects.length==2){
                    String regex= (String) objects[0];
                    String value=  (String)objects[1];
                    return value.startsWith(regex);
                }
                return false;
            });
            extendFuncs.put("remove",(a)->{
                Object[] objects = Arrays.stream(a).toArray();
                if (objects.length==2){
                    Object index= objects[0];
                    Object value=  objects[1];
                    //lsit<String>  list<Map>
                    if (value instanceof List){
                        List list = (List)value;
                        list.remove(index);
                        return list;
                    }
                }
                return false;
            });
        }
        return extendFuncs;
    }


    /**
     * 数据集 映射
     *
     * @param inRow      原始数据
     * @param formulaMap 字段映射关系
     * @return 新的数据集
     */
    static Map<String, Object> mapDataRow(Map<String, Object> inRow,
                                          Collection<Map.Entry<String, String>> formulaMap) {
        if (formulaMap == null) {
            return inRow;
        }
        VariableFormula formula = new VariableFormula();
        formula.setExtendFuncMap(makeExtendFuns());
        formula.setTrans(new ObjectTranslate(inRow));
        Map<String, Object> newRow = new LinkedHashMap<>(formulaMap.size());
        for (Map.Entry<String, String> ent : formulaMap) {
            formula.setFormula(ent.getValue());

            newRow.put(ent.getKey(), formula.calcFormula());
        }
        return newRow;
    }

    /**
     * 数据集 映射
     *
     * @param inData     原始数据集
     * @param formulaMap 字段映射关系， value为计算表达式
     * @return 新的数据集
     */
    public static DataSet mapDateSetByFormula(DataSet inData, Collection<Map.Entry<String, String>> formulaMap) {
        List<Map<String, Object>> data = inData.getDataAsList();
        List<Map<String, Object>> newData = new ArrayList<>(data.size());
        for (Map<String, Object> obj : data) {
            newData.add(mapDataRow(obj, formulaMap));
        }
        return new SimpleDataSet(newData);
    }

    /**
     * 数据集 增加派生字段
     *
     * @param inData     原始数据集
     * @param formulaMap 字段映射关系， value为计算表达式
     */
    public static void appendDeriveField(DataSet inData, Collection<Map.Entry<String, String>> formulaMap) {
        List<Map<String, Object>> data = inData.getDataAsList();
        for (Map<String, Object> obj : data) {
            obj.putAll(mapDataRow(obj, formulaMap));
        }
    }

    /**
     * 数据集 映射
     *
     * @param inData   原始数据集
     * @param formulas 逻辑表达式
     * @return 新的数据集
     */
    public static DataSet filterDateSet(DataSet inData, List<String> formulas) {
        List<Map<String, Object>> data = inData.getDataAsList();
        List<Map<String, Object>> newData = new ArrayList<>(data.size());
        for (Map<String, Object> obj : data) {
            boolean canAdd = true;
            for (String formula : formulas) {
                if (!BooleanBaseOpt.castObjectToBoolean(
                    VariableFormula.calculate(formula, obj), false)) {
                    canAdd = false;
                    break;
                }
            }
            if (canAdd) {
                newData.add(obj);
            }
        }
        SimpleDataSet outDataSet = new SimpleDataSet(newData);
        outDataSet.setDataSetName(inData.getDataSetName());
        return outDataSet;
    }

    /**
     * 对数据集进行排序
     *
     * @param inData 原始数据集
     * @param fields 排序字段
     */
    public static void sortDataSetByFields(DataSet inData, List<String> fields) {
        sortByFields(inData.getDataAsList(), fields);
    }

    private static double[] listDoubleToArray(List<Double> dblist) {
        double[] dbs = new double[dblist.size()];
        int i = 0;
        for (Double db : dblist) {
            dbs[i++] = db != null ? db : 0.0;
        }
        return dbs;
    }

    private static Map<String, Object> makeNewStatRow(List<String> groupByFields,
                                                      List<Triple<String, String, String>> statDesc,
                                                      Map<String, Object> preRow,
                                                      Map<String, List<Object>> tempData) {
        Map<String, Object> newRow = new HashMap<>(groupByFields.size());
        if (groupByFields.size() > 0) {
            for (String field : groupByFields) {
                newRow.put(field, preRow.get(field));
            }
        }
        Map<String, List<Double>> tempDataDouble = new HashMap<>(statDesc.size());
        for (Triple<String, String, String> tr : statDesc) {
          if (!"concat".equals(tr.getRight())){
              tempData.forEach((key, value) -> {
                  List<Double> doubleList = new ArrayList<>();
                  List<Object> list = value;
                  for (Object o : list) {
                      doubleList.add(NumberBaseOpt.castObjectToDouble(o));
                  }
                  tempDataDouble.put(key, doubleList);
              });
          }
        }
        for (Triple<String, String, String> tr : statDesc) {
            Object db;
            switch (tr.getRight()) {
                case "min":
                    db = StatUtils.min(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "max":
                    db = StatUtils.max(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "mean":
                    db = StatUtils.mean(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "sum":
                    db = StatUtils.sum(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "sumSq":
                    db = StatUtils.sumSq(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "prod":
                    db = StatUtils.product(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "sumLog":
                    db = StatUtils.sumLog(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "geometricMean":
                    db = StatUtils.geometricMean(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                case "variance":
                    db = StatUtils.variance(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    break;
                /* percentile 这个没有实现*/
                case "splitJ":
                    List<Object> objects = tempData.get(tr.getLeft());
                    StringBuilder builder = new StringBuilder();
                    for (Object object : objects) {
                        builder.append(object);
                    }
                    db=builder.toString();
                    break;
                default:
                    db = tempData.get(tr.getLeft()).size();
                    break;
            }
            newRow.put(tr.getLeft(), db);
        }
        return newRow;
    }

    public static DataSet statDataset(DataSet inData,
                                      List<String> groupByFields,
                                      Map<String, String> statDesc) {
        List<Triple<String, String, String>> sd = new ArrayList<>(statDesc.size());
        for (Map.Entry<String, String> s : statDesc.entrySet()) {
            String[] optDesc = StringUtils.split(s.getValue(), ":");
            if (optDesc != null && optDesc.length > 1) {
                sd.add(new MutableTriple<>(s.getKey(), optDesc[0], optDesc[1]));
            }
        }
        return statDataset(inData, groupByFields, sd);
    }

    /**
     * 分组统计 , 如果 List&gt;String&lt; groupbyFields 为null 或者 空 就是统计所有的（仅返回一行）
     *
     * @param inData        输入数据集
     * @param groupByFields 分组（排序）字段
     * @param statDesc      统计描述; 新字段名， 源字段名， 统计方式 （求和，最大，最小，平均，方差，标准差）
     * @return 返回数据集
     */
    private static DataSet statDataset(DataSet inData,
                                       List<String> groupByFields,
                                       List<Triple<String, String, String>> statDesc) {
        if (inData == null) {//|| groupByFields == null
            return inData;
        }
        List<Map<String, Object>> data = inData.getDataAsList();
        if (data == null || data.size() == 0) {
            return inData;
        }
        //按group by字段排序
        if (groupByFields != null && groupByFields.size() > 0) {
            sortByFields(data, groupByFields);
        }

        List<Map<String, Object>> newData = new ArrayList<>();
        Map<String, Object> preRow = null;

        Map<String, List<Object>> tempData = new HashMap<>(statDesc.size());
        for (Triple<String, String, String> tr : statDesc) {
            tempData.put(tr.getLeft(), new ArrayList<>());
        }

        for (Map<String, Object> row : data) {
            if (0 != compareTwoRow(preRow, row, groupByFields)) {
                if (preRow != null) {
                    //保存newRow
                    Map<String, Object> newRow = makeNewStatRow(groupByFields, statDesc, preRow, tempData);
                    newData.add(newRow);
                }
                // 新建数据临时数据空间
                for (Triple<String, String, String> tr : statDesc) {
                    tempData.get(tr.getLeft()).clear();
                }
            }
            for (Triple<String, String, String> tr : statDesc) {
                //tempData.get(tr.getLeft()).add(NumberBaseOpt.castObjectToDouble(row.get(tr.getMiddle())));
                tempData.get(tr.getLeft()).add(row.get(tr.getMiddle()));
            }
            preRow = row;
        }

        if (preRow != null) {
            //保存newRow
            Map<String, Object> newRow = makeNewStatRow(groupByFields, statDesc, preRow, tempData);
            newData.add(newRow);
        }
        return new SimpleDataSet(newData);
    }

    /*public static DataSet sumRollupDataset(DataSet inData,
                                       List<String> groupbyFields, List<String> orderbyFields) {
        List<Map<String, Object>> data = inData.getDataAsList();
        List<String> keyRows = ListUtils.union(groupbyFields, orderbyFields);
        //根据维度进行排序 行头、列头
        sortByFields(data, keyRows);
        Map<String, Object> preRow = null;
        int n = data.size();
        return inData;
    }*/

    private static void analyseDatasetGroup(List<Map<String, Object>> newData, List<Map<String, Object>> data,
                                            int offset, int endPos,
                                            DatasetVariableTranslate dvt,
                                            Collection<Map.Entry<String, String>> refDesc) {
        dvt.setOffset(offset);
        dvt.setLength(endPos - offset);
        for (int j = offset; j < endPos; j++) {
            Map<String, Object> newRow = new HashMap<>(data.get(j));
            dvt.setCurrentPos(j);
            for (Map.Entry<String, String> ref : refDesc) {
                newRow.put(ref.getKey(),
                    VariableFormula.calculate(ref.getValue(), dvt));
            }
            newData.add(newRow);
        }
    }

    /**
     * 分组统计 , 如果 List&gt;String&lt; groupByFields 为null 或者 空 就是统计所有的（仅返回一行）
     *
     * @param inData        输入数据集
     * @param groupByFields 分组字段
     * @param orderByFields 排序字段
     * @param refDesc       引用说明; 新字段名， 引用表达式
     * @return 返回数据集
     */
    public static DataSet analyseDataset(DataSet inData,
                                         List<String> groupByFields,
                                         List<String> orderByFields,
                                         Collection<Map.Entry<String, String>> refDesc) {
        List<Map<String, Object>> data = inData.getDataAsList();
        List<String> keyRows = ListUtils.union(groupByFields, orderByFields);
        //根据维度进行排序 行头、列头
        sortByFields(data, keyRows);
        Map<String, Object> preRow = null;
        int n = data.size();
        int prePos = 0;
        DatasetVariableTranslate dvt = new DatasetVariableTranslate(data);
        List<Map<String, Object>> newData = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map<String, Object> row = data.get(i);
            if (compareTwoRow(preRow, row, groupByFields) == 0) {
                if (preRow != null) {
                    analyseDatasetGroup(newData, data, prePos, i, dvt, refDesc);
                }
                prePos = i;
            }
            preRow = row;
        }
        analyseDatasetGroup(newData, data, prePos, n, dvt, refDesc);
        return new SimpleDataSet(newData);
    }

    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @param colHeaderFields 列头信息
     * @param rowHeaderFields 行头信息
     * @return 输出数据集
     */
    public static DataSet crossTabulation(DataSet inData, List<String> rowHeaderFields, List<String> colHeaderFields) {
        if (inData == null) {
            return inData;
        }
        List<Map<String, Object>> data = inData.getDataAsList();
        if (data == null || data.size() == 0) {
            return inData;
        }
        if (rowHeaderFields.size() + colHeaderFields.size() >= data.get(0).size()) {
            throw new RuntimeException("数据不合法");
        }
        List<String> keyRows = ListUtils.union(rowHeaderFields, colHeaderFields);
        //根据维度进行排序 行头、列头
        sortByFields(data, keyRows);
        List<Map<String, Object>> newData = new ArrayList<>();
        Map<String, Object> preRow = null;
        Map<String, Object> newRow = null;
        for (Map<String, Object> row : data) {
            if (row == null) {
                continue;
            }
            if (compareTwoRow(preRow, row, rowHeaderFields) != 0) {
                if (preRow != null && newRow != null) {
                    newData.add(newRow);
                }
                // 新建数据临时数据空间
                newRow = new HashMap<>(rowHeaderFields.size());
                for (String key : rowHeaderFields) {
                    newRow.put(key, row.get(key));
                }
            }
            StringBuilder colPrefix = new StringBuilder();
            for (String key : colHeaderFields) {
                colPrefix.append(key).append(":").append(row.get(key)).append(":");
            }
            String prefix = colPrefix.toString();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (!keyRows.contains(key)) {
                    if (newRow == null) {
                        newRow = new HashMap<>(row.size());
                    }
                    newRow.put(prefix + key, entry.getValue());
                }
            }
            preRow = row;
        }

        if (preRow != null && newRow != null) {
            newData.add(newRow);
        }
        return new SimpleDataSet(newData);
    }

    private static void appendData(Map<String, Object> newRow, Map<String, Object> oldData,
                                   List<String> primaryFields, String suffix, boolean appendKey) {

        for (Map.Entry<String, Object> entry : oldData.entrySet()) {
            String key = entry.getKey();
            if (primaryFields.contains(key)) {
                if (appendKey) {
                    newRow.put(key, entry.getValue());
                }
            } else {
                newRow.put(key + suffix, entry.getValue());
            }
        }
    }

    /**
     * 同环比转换 这个可以用 map + join + filter 代替 所以可以不需要了。
     *
     * @param currDataSet   本期数据集
     * @param lastDataSet   上期数据集
     * @param primaryFields 主键列
     * @return DataSet
     */
    @Deprecated
    public static DataSet compareTabulation(DataSet currDataSet,
                                            DataSet lastDataSet,
                                            List<Map.Entry<String, String>> primaryFields,
                                            Collection<Map.Entry<String, String>> formulaMap) {
        if (currDataSet == null || lastDataSet == null) {
            return new SimpleDataSet();
        }
        List<Map<String, Object>> currData = currDataSet.getDataAsList();
        List<Map<String, Object>> lastData = lastDataSet.getDataAsList();
        if (currData == null || lastData == null) {
            throw new RuntimeException("数据不合法");
        }

        List<Map<String, Object>> newData = new ArrayList<>();
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        // 根据主键排序
        sortByFields(currData, mainFields);
        sortByFields(lastData, slaveFields);
        int i = 0;
        int j = 0;
        while (i < currData.size() && j < lastData.size()) {
            int nc = compareTwoRowWithMap(currData.get(i), lastData.get(j), primaryFields);
            //匹配
            Map<String, Object> newRow = new LinkedHashMap<>();
            if (nc == 0) {
                appendData(newRow, currData.get(i), mainFields, "_l", true);
                appendData(newRow, lastData.get(j), slaveFields, "_r", false);
                i++;
                j++;
            } else if (nc < 0) {
                appendData(newRow, currData.get(i), mainFields, "_l", true);
                i++;
            } else {
                appendData(newRow, lastData.get(j), slaveFields, "_r", true);
                j++;
            }
            newData.add(mapDataRow(newRow, formulaMap));
        }

        while (i < currData.size()) {
            Map<String, Object> newRow = new LinkedHashMap<>();
            appendData(newRow, currData.get(i), mainFields, "_l", true);
            newData.add(mapDataRow(newRow, formulaMap));
            i++;
        }

        while (j < lastData.size()) {
            Map<String, Object> newRow = new LinkedHashMap<>();
            appendData(newRow, lastData.get(j), slaveFields, "_r", true);
            newData.add(mapDataRow(newRow, formulaMap));
            j++;
        }
        return new SimpleDataSet(newData);
    }

    private static void splitMainAndSlave(List<Map.Entry<String, String>> primaryFields, List<String> mainFields, List<String> slaveFields) {
        for (Map.Entry<String, String> field : primaryFields) {
            mainFields.add(field.getKey());
            slaveFields.add(field.getValue());
        }
    }

    /**
     * 合并两个数据集; 类似于 数据的 join， 但是不同的是 如果有相同的字段 以mainDataSet为准，
     * 连接字段最好是主键（或者唯一），否则系统会保证多的哪个dataSet被加入，但不一定会被连接
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @param join  innerJoin， leftjoin rightjoin alljoin
     * @return DataSet
     */
    public static DataSet joinTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet, List<Map.Entry<String, String>> primaryFields, String join) {
        if (mainDataSet == null) {
            return slaveDataSet;
        }
        if (slaveDataSet == null) {
            return mainDataSet;
        }
        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields);
        sortByFields(slaveData, slaveFields);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();

        final boolean leftJoin = ConstantValue.DATASET_JOIN_TYPE_LEFT.equalsIgnoreCase(join) || ConstantValue.DATASET_JOIN_TYPE_ALL.equalsIgnoreCase(join);
        final boolean rightJoin = ConstantValue.DATASET_JOIN_TYPE_RIGHT.equalsIgnoreCase(join) || ConstantValue.DATASET_JOIN_TYPE_ALL.equalsIgnoreCase(join);
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields);
            Map<String, Object> newRow = new LinkedHashMap<>();
            if (nc == 0) {
                newRow.putAll(slaveData.get(j));
                newRow.putAll(mainData.get(i));
                /** 这边如果需要实现 数据库jion 的 笛卡尔积，需要遍列所有相同的key ， 只确保每一条数据都有对应上
                 * 就是只能保证一对多的情况正确，不能保证多对多的情况正确*/
                boolean equalNextMain = i < mainData.size() - 1 && compareTwoRow(mainData.get(i), mainData.get(i + 1), mainFields) == 0;
                boolean equalNextSlave = j < slaveData.size() - 1 && compareTwoRow(slaveData.get(j), slaveData.get(j + 1), slaveFields) == 0;
                if(equalNextMain && !equalNextSlave){
                    i++;
                } else if(!equalNextMain && equalNextSlave){
                    j++;
                } else {
                    i++;
                    j++;
                }
            } else if (nc < 0) {
                if (leftJoin) {
                    newRow.putAll(mainData.get(i));
                }
                i++;
            } else {
                if (rightJoin) {
                    newRow.putAll(slaveData.get(j));
                }
                j++;
            }
            if (newRow.size() > 0) {
                newData.add(newRow);
            }
        }
        if (leftJoin) {
            while (i < mainData.size()) {
                Map<String, Object> newRow = new LinkedHashMap<>(mainData.get(i));
                newData.add(newRow);
                i++;
            }
        }
        if (rightJoin) {
            while (j < slaveData.size()) {
                Map<String, Object> newRow = new LinkedHashMap<>(slaveData.get(j));
                newData.add(newRow);
                j++;
            }
        }
        return new SimpleDataSet(newData);
    }

    /**
     * 这个没有什么意义；它 其实是 两个集合 先去交集，再更加条件过滤
     * 可以用 交集 和 过滤 替换，建议废弃
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @param formulas 过滤条件
     * @return newDataset
     */
    @Deprecated
    public static DataSet filterByOtherDataSet(DataSet mainDataSet, DataSet slaveDataSet,
                                               List<Map.Entry<String, String>> primaryFields, List<String> formulas) {
        if (mainDataSet == null || slaveDataSet == null) {
            return new SimpleDataSet();
        }
        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields);
        sortByFields(slaveData, slaveFields);

        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();
        // 根据主键排序
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields);
            //匹配
            Map<String, Object> newRow = new LinkedHashMap<>();
            if (nc == 0) {
                boolean canAdd = true;
                for (String formula : formulas) {
                    if (!BooleanBaseOpt.castObjectToBoolean(
                        VariableFormula.calculate(formula, slaveData.get(j)), false)) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    newRow.putAll(mainData.get(i));
                }
                i++;
                j++;
            } else if (nc < 0) {
                i++;
            } else {
                j++;
            }
            if (newRow.size() != 0) {
                newData.add(newRow);
            }
        }
        return new SimpleDataSet(newData);
    }

    /**
     * 这个是 连个集合 的 unionALL 操作；如果 需要去掉重复的数据集，用jion来代替
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @return newDataset
     */
    public static DataSet unionTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet) {

        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        if (mainData == null) {
            return new SimpleDataSet(slaveData);
        }
        if (slaveData == null) {
            return new SimpleDataSet(mainData);
        }
        List<Map<String, Object>> resultData = new ArrayList<>(mainData.size() + slaveData.size());
        resultData.addAll(mainData);
        resultData.addAll(slaveData);
        return new SimpleDataSet(resultData);
    }

    /**
     * 求两个集合的交集
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @param unionData 是否合并数据，是否用 slaveData中额外的数据项填补 mainData中的数据
     * @return newDataSet
     */
    public static DataSet intersectTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet,
                                              List<Map.Entry<String, String>> primaryFields, boolean unionData) {
        if (mainDataSet == null) {
            return null;
        }
        if (slaveDataSet == null) {
            return null;
        }
        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields);
        sortByFields(slaveData, slaveFields);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields);
            if (nc == 0) {
                Map<String, Object> newRow = new LinkedHashMap<>();
                if(unionData){
                    newRow.putAll(slaveData.get(j));
                }
                newRow.putAll(mainData.get(i));

                if (newRow.size() > 0) {
                    newData.add(newRow);
                }
                i++;
                j++;
            } else if (nc < 0) {
                i++;
            } else {
                j++;
            }

        }
        return new SimpleDataSet(newData);
    }

    /**
     * 求减集 mainDataSet - slaveDataSet
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @return newDataSet
     */
    public static DataSet minusTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet, List<Map.Entry<String, String>> primaryFields) {
        if (mainDataSet == null) {
            return null;
        }
        if (slaveDataSet == null) {
            return mainDataSet;
        }
        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields);
        sortByFields(slaveData, slaveFields);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields);
            if (nc == 0) {
                //newRow.putAll(slaveData.get(j));
                i++;
                j++;
            } else if (nc < 0) {
                Map<String, Object> newRow = new LinkedHashMap<>();
                newRow.putAll(mainData.get(i));
                if (newRow.size() > 0) {
                    newData.add(newRow);
                }
                i++;
            } else {
                j++;
            }
        }
        while (i < mainData.size()) {
            Map<String, Object> newRow = new LinkedHashMap<>(mainData.get(i));
            if (newRow.size() > 0) {
                newData.add(newRow);
            }
            i++;
        }
        return new SimpleDataSet(newData);
    }

    public static void checkDateSet(DataSet inData, Collection<CheckRule> rules) {
        List<Map<String, Object>> data = inData.getDataAsList();

        for (Map<String, Object> obj : data) {
            StringBuilder checkResult = new StringBuilder();
            for (CheckRule rule : rules) {
                if (!CheckRuleUtils.checkData(obj, rule)) {
                    checkResult.append(Pretreatment.mapTemplateString(rule.getErrormsg(), obj)).append(";");
                }
            }
            if (StringUtils.isBlank(checkResult.toString())) {
                checkResult = new StringBuilder("ok");
            }
            obj.put(CheckRuleUtils.CHECK_RULE_RESULT_TAG, checkResult.toString());
        }
    }

    private static int compareTwoRow(Map<String, Object> data1, Map<String, Object> data2, List<String> fields) {
        if (data1 == null && data2 == null) {
            return 0;
        }
        if (data1 == null) {
            return -1;
        }
        if (data2 == null) {
            return 1;
        }
        if (fields == null) {
            return 0;
        }
        for (String field : fields) {
            if (field.endsWith(" desc")) {
                String dataField = field.substring(0, field.length() - 5).trim();
                int cr;
                if (isChinese((String)data1.get(dataField))&&isChinese((String) data2.get(dataField))){
                    char c1 = ((String) data1.get(dataField)).charAt(0);
                    char c2 = ((String) data2.get(dataField)).charAt(0);
                    cr =concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c2)));
                }else {
                    cr = GeneralAlgorithm.compareTwoObject(data1.get(dataField), data2.get(dataField),false);
                }
                if (cr != 0) {
                    return 0 - cr;
                }
            } else {
                int cr;
                if (isChinese((String)data1.get(field))&&isChinese((String) data2.get(field))){
                    char c1 = ((String) data1.get(field)).charAt(0);
                    char c2 = ((String) data2.get(field)).charAt(0);
                    cr =concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c2)));
                }else {
                    cr = GeneralAlgorithm.compareTwoObject(data1.get(field), data2.get(field));
                }
                if (cr != 0) {
                    return cr;
                }
            }
        }
        return 0;
    }
    //判断字符串是否为纯中文 包括中文标点符号
    private static boolean isChinese(String str){
        if (str == null) {
            return false;
        }
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (c < 0x4E00 || c > 0x9FBF) {
                return false;
            }
        }
        return true;
    }
    //自定义中文排序方法
    private static String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinSbf = new StringBuffer();
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinSbf.append(pinyinArray[i]);
            }
        }
        return pinyinSbf.toString();
    }

    private static int compareTwoRowWithMap(Map<String, Object> data1, Map<String, Object> data2, List<Map.Entry<String, String>> fields) {
        if (data1 == null && data2 == null) {
            return 0;
        } else if (fields == null) {
            return 0;
        }

        if (data1 == null) {
            return -1;
        }

        if (data2 == null) {
            return 1;
        }

        for (Map.Entry<String, String> field : fields) {
            int cr = GeneralAlgorithm.compareTwoObject(
                data1.get(field.getKey()), data2.get(field.getValue()));
            if (cr != 0) {
                return cr;
            }
        }
        return 0;
    }

    private static void sortByFields(List<Map<String, Object>> data, List<String> fields) {
        data.sort((o1, o2) -> compareTwoRow(o1, o2, fields));
    }

    //从dataset 中获取流信息（页面上填写了“文件内容”参数时调用）
    public static List<InputStream> getInputStreamByFieldName(String fieldName, DataSet dataSet) {
        List<InputStream> inputStreams = new ArrayList<>();
        Map<String, String> mapInfo = new HashMap<>();
        mapInfo.put(fieldName, fieldName);
        if (dataSet != null) {
            DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
            List<Map<String, Object>> dataAsList = destDs.getDataAsList();
            for (Map<String, Object> objectMap : dataAsList) {
                InputStream inputStream;
                Object object = objectMap.get(fieldName);
                if (object instanceof byte[]) {
                    inputStream = new ByteArrayInputStream((byte[]) object);
                    inputStreams.add(inputStream);
                } else if (object instanceof InputStream) {
                    inputStream = (InputStream) object;
                    inputStreams.add(inputStream);
                }
            }
        }
        return inputStreams;
    }

    //从dataset 中获取流信息（页面上没填写“文件内容”参数时调用）
    public static List<InputStream> getInputStreamByFieldName(DataSet dataSet) {
        List<InputStream> inputStreams = new ArrayList<>();
        Object data = dataSet.getData();
        if (data instanceof List) {
            for (Object datum : (List) data) {//必须保证集合中全部都是byte[]或者InputStream流
                if (datum instanceof InputStream) {
                    inputStreams.add((InputStream) datum);
                } else if (datum instanceof byte[]) {
                    inputStreams.add(new ByteArrayInputStream((byte[]) datum));
                }
            }
        }
        if (data instanceof  Map){
            Map<String, Object> mapFirstRow = dataSet.getFirstRow();
            inputStreams.add((InputStream)mapFirstRow.get(ConstantValue.FILE_CONTENT));
        }
        if (data instanceof InputStream) {
            inputStreams.add((InputStream) data);
        } else if (data instanceof byte[]) {
            inputStreams.add(new ByteArrayInputStream((byte[]) data));
        }
        return inputStreams;
    }

    public static List<InputStream> getRequestFileInfo(BizModel bizModel) throws IOException {
        List<InputStream> inputStreamList = null;
        Map<String, Object> modelTag = bizModel.getInterimVariable();
        InputStream requestFile = (InputStream) modelTag.get("requestFile");
        if (requestFile != null && requestFile.available() > 0) {
            inputStreamList = new ArrayList<>();
            inputStreamList.add(requestFile);
        }
        return inputStreamList;
    }
}
