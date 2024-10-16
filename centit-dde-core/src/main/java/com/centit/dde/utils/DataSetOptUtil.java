package com.centit.dde.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.basedata.DataDictionary;
import com.centit.framework.security.StandardPasswordEncoderImpl;
import com.centit.search.utils.TikaTextExtractor;
import com.centit.support.algorithm.*;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.compiler.VariableTranslate;
import com.centit.support.database.utils.QueryUtils;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.StatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

/**
 * @author codefan@sina.com
 */
public abstract class DataSetOptUtil {
    protected static final Logger logger = LoggerFactory.getLogger(DataSetOptUtil.class);
    private static final boolean SORT_NULL_AS_FIRST = false;
    public static final Map<String, Function<Object[], Object>> extendFuncs = new HashMap<>();

    static {
        extendFuncs.put("uuid", (a) -> UuidOpt.getUuidAsString32());
        extendFuncs.put("encode", (a) -> a==null || a.length<1 || a[0] == null ? null
             : new StandardPasswordEncoderImpl().encode(StringBaseOpt.castObjectToString(a[0])));
        extendFuncs.put("dict", (a) -> {
            String topUnit = WebOptUtils.getCurrentTopUnit(RequestThreadLocal.getLocalThreadWrapperRequest());
            String lang = WebOptUtils.getCurrentLang(RequestThreadLocal.getLocalThreadWrapperRequest());

            if (a != null && a.length > 1) {
                String regex = ",";
                String strData = StringBaseOpt.objectToString(a[1]);
                if(StringUtils.isBlank(strData))
                    return null;
                String[] strings = strData.split(regex);
                StringBuilder stringBuilder = new StringBuilder();
                String dictField = "auto";
                if(a.length > 2 ) {
                    dictField = StringBaseOpt.castObjectToString(a[2]);
                    if(StringUtils.isBlank(dictField)){
                        dictField = "auto";
                    }
                }
                DataDictionary dataPiece;
                for (String tempStr : strings) {
                    String str = tempStr.trim();
                    String value = null;
                    switch (dictField){
                        case "dataTag":
                            dataPiece = CodeRepositoryUtil.getDataPiece(StringBaseOpt.castObjectToString(a[0]), str, topUnit);
                            if(dataPiece!=null) value = dataPiece.getDataTag();
                            break;

                        case "extraCode":
                            dataPiece = CodeRepositoryUtil.getDataPiece(StringBaseOpt.castObjectToString(a[0]), str, topUnit);
                            if(dataPiece!=null) value = dataPiece.getExtraCode();
                            break;

                        case "extraCode2":
                            dataPiece = CodeRepositoryUtil.getDataPiece(StringBaseOpt.castObjectToString(a[0]), str, topUnit);
                            if(dataPiece!=null) value = dataPiece.getExtraCode2();
                            break;

                        case "dataDesc":
                            dataPiece = CodeRepositoryUtil.getDataPiece(StringBaseOpt.castObjectToString(a[0]), str, topUnit);
                            if(dataPiece!=null) value = dataPiece.getDataDesc();
                            break;

                        case "dataCode":
                            value = CodeRepositoryUtil.getCode(StringBaseOpt.castObjectToString(a[0]), str, topUnit, lang);
                            break;

                        case "auto":
                            value = CodeRepositoryUtil.getValue(StringBaseOpt.castObjectToString(a[0]), str, topUnit, lang);
                            if(StringUtils.isBlank(value) || StringUtils.equals(str, value))
                                value = CodeRepositoryUtil.getCode(StringBaseOpt.castObjectToString(a[0]), str, topUnit, lang);
                            break;

                        case "dataValue":
                        case "localDataValue":
                        default:
                            value = CodeRepositoryUtil.getValue(StringBaseOpt.castObjectToString(a[0]), str, topUnit, lang);
                            break;
                    }

                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(regex);
                    }
                    if(StringUtils.isBlank(value))
                        stringBuilder.append(str);
                    else
                        stringBuilder.append(value);
                }
                return stringBuilder.toString();

            } else {
                return null;
            }
        });

        extendFuncs.put("dictTrans", (a) -> {
            if (a != null && a.length > 1) {
                return CodeRepositoryUtil.transExpression(
                    StringBaseOpt.castObjectToString(a[0]),
                    StringBaseOpt.castObjectToString(a[1]));
            } else {
                return null;
            }
        });
        extendFuncs.put("size", (a) -> {
            if (a == null || a.length < 1)
                return 0;
            Object o = a[0];
            if (o instanceof Collection) {
                return ((Collection<?>) o).size();
            }
            if (o instanceof Map) {
                return ((Map<?, ?>) o).size();
            }
            if (o instanceof Object[]) {
                Object[] objs = (Object[]) o;
                return objs.length;
            }
            return o==null? 0 : 1;
        });
        extendFuncs.put("fileToText", (a) -> {
            if (a == null || a.length < 1)
                return null;
            Object file = a[0];
            try {
                return TikaTextExtractor.extractInputStreamText(FileIOOpt.castObjectToInputStream(file));
            } catch (Exception e) {
                logger.error("扩展函数fileToText中文件转换失败："+e.getMessage(), e);
                return null;
            }
        });
    }

    /**
     * 数据集 映射
     *
     * @param inRow      原始数据
     * @param formulaMap 字段映射关系
     * @return 新的数据集
     */
    static Map<String, Object> mapDataRow(BizModel bizModel, Map<String, Object> inRow, int rowInd, int rowCount,
                                          Collection<Map.Entry<String, String>> formulaMap) {
        if (formulaMap == null) {
            return inRow;
        }
        VariableFormula formula = new VariableFormula();
        formula.setExtendFuncMap(extendFuncs);
        formula.setTrans(new DataRowVariableTranslate(bizModel, inRow, rowInd, rowCount));
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
    public static DataSet mapDateSetByFormula(BizModel bizModel, DataSet inData, Collection<Map.Entry<String, String>> formulaMap) {
        if(inData==null || inData.getSize() == 0){
            return new DataSet(null);
        }
        if(DataSet.DATA_SET_TYPE_TABLE.equals(inData.getDataSetType())) {
            List<Map<String, Object>> data = inData.getDataAsList();
            List<Map<String, Object>> newData = new ArrayList<>(data.size());
            int rowCount = data.size();
            int rowIndex = 0;
            for (Map<String, Object> obj : data) {
                newData.add(mapDataRow(bizModel, obj, rowIndex, rowCount, formulaMap));
                rowIndex++;
            }
            return new DataSet(newData);
        } else {
            Map<String, Object> firstRow = inData.getFirstRow();
            return new DataSet(mapDataRow(bizModel, firstRow, 0, 1, formulaMap));
        }
    }

    /**
     * 数据集 增加派生字段
     *
     * @param inData     原始数据集
     * @param formulaMap 字段映射关系， value为计算表达式
     */
    public static void appendDeriveField(BizModel bizModel, DataSet inData, Collection<Map.Entry<String, String>> formulaMap) {
        if(DataSet.DATA_SET_TYPE_TABLE.equals(inData.getDataSetType())) {
            List<Map<String, Object>> data = inData.getDataAsList();
            int rowCount = data.size();
            int rowIndex = 0;
            for (Map<String, Object> obj : data) {
                obj.putAll(mapDataRow(bizModel, obj, rowIndex, rowCount, formulaMap));
                rowIndex++;
            }
            inData.setData(data);
        } else {
            if(inData.getSize() == 0){
                return;
            }
            Map<String, Object> firstRow = inData.getFirstRow();
            firstRow.putAll(mapDataRow(bizModel, firstRow, 0, 1, formulaMap));
            inData.setData(firstRow);
        }
    }

    /**
     * 数据集 映射
     *
     * @param inData   原始数据集
     * @param formulas 逻辑表达式
     * @return 新的数据集
     */
    public static DataSet filterDateSet(BizModel bizModel, DataSet inData, List<String> formulas) {
        List<Map<String, Object>> data = inData.getDataAsList();
        int rowCount = data.size();
        int rowIndex = 0;
        List<Map<String, Object>> newData = new ArrayList<>(rowCount+1);
        for (Map<String, Object> obj : data) {
            boolean canAdd = true;
            for (String formula : formulas) {
                if (!BooleanBaseOpt.castObjectToBoolean(
                    VariableFormula.calculate(formula,
                        new DataRowVariableTranslate(bizModel, obj, rowIndex, rowCount), extendFuncs), false)) {
                    canAdd = false;
                    break;
                }
            }
            rowIndex ++;
            if (canAdd) {
                newData.add(obj);
            }
        }
        DataSet outDataSet = new DataSet(newData);
        outDataSet.setDataSetName(inData.getDataSetName());
        return outDataSet;
    }

    public static void sortDataSetAsTree(DataSet dataSet, String parentExpress, String childExpress){
        CollectionsOpt.sortAsTree(dataSet.getDataAsList(),
            (p, c) -> GeneralAlgorithm.equals(
                VariableFormula.calculate(parentExpress, p),
                VariableFormula.calculate(childExpress, c),
                false
            )
        );
    }

    public static DataSet toTreeDataSet(DataSet dataSet, String parentExpress, String childExpress, String childrenFiled){
        JSONArray treeJSON = CollectionsOpt.sortAsTreeAndToJSON2(dataSet.getDataAsList(),
            (p, c) -> GeneralAlgorithm.equals(
                VariableFormula.calculate(parentExpress, p),
                VariableFormula.calculate(childExpress, c),
                false
            ), childrenFiled
        );
        return new DataSet(treeJSON);
    }
    /**
     * 对数据集进行排序
     *
     * @param inData 原始数据集
     * @param fields 排序字段
     */
    public static void sortDataSetByFields(DataSet inData, List<String> fields) {
        sortDataSetByFields(inData, fields, SORT_NULL_AS_FIRST);
    }

    public static void sortDataSetByFields(DataSet inData, List<String> fields, boolean nullAsFirst) {
        sortByFields(inData.getDataAsList(), fields, nullAsFirst);
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
            if (!"concat".equals(tr.getRight()) && !"gather".equals(tr.getRight())) {
                tempData.forEach((key, value) -> {
                    List<Double> doubleList = new ArrayList<>();
                    for (Object o : value) {
                        Double db = NumberBaseOpt.castObjectToDouble(o);
                        if(db != null) {
                            doubleList.add(db);
                        }
                    }
                    tempDataDouble.put(key, doubleList);
                });
            }
        }
        List<Double> doubleList;
        for (Triple<String, String, String> tr : statDesc) {
            Object db;
            switch (tr.getRight()) {
                case "min":
                    doubleList = tempDataDouble.get(tr.getLeft());
                    if(doubleList!=null && !doubleList.isEmpty()) {
                        db = StatUtils.min(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    } else {
                        db = StringBaseOpt.minString(tempData.get(tr.getLeft()));
                    }
                    break;
                case "max":
                    doubleList = tempDataDouble.get(tr.getLeft());
                    if(doubleList!=null && !doubleList.isEmpty()) {
                        db = StatUtils.max(listDoubleToArray(tempDataDouble.get(tr.getLeft())));
                    } else {
                        db = StringBaseOpt.maxString(tempData.get(tr.getLeft()));
                    }
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
                case "gather": // 作为集合提供
                    db = tempData.get(tr.getLeft());
                    break;
                /* percentile 这个没有实现*/
                case "concat": //连接字符串
                case "splitJ":
                    List<Object> objects = tempData.get(tr.getLeft());
                    StringBuilder builder = new StringBuilder();
                    for (Object object : objects) {
                        builder.append(object);//.append(";");
                    }
                    db = builder.toString();
                    break;
                default: // count
                    db = tempData.get(tr.getLeft()).size();
                    break;
            }
            newRow.put(tr.getLeft(), db);
        }
        return newRow;
    }

    /*public static DataSet statDataset(DataSet inData,
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
    }*/

    /**
     * 分组统计 , 如果 List&gt;String&lt; groupbyFields 为null 或者 空 就是统计所有的（仅返回一行）
     *
     * @param inData        输入数据集
     * @param groupByFields 分组（排序）字段
     * @param statDesc      统计描述; 新字段名， 源字段名， 统计方式 （求和，最大，最小，平均，方差，标准差）
     * @return 返回数据集
     */
    public static DataSet statDataset(DataSet inData,
                                      List<String> groupByFields,
                                      List<Triple<String, String, String>> statDesc) {
        if (inData == null) {//|| groupByFields == null
            return inData;
        }
        List<Map<String, Object>> data = inData.getDataAsList();
        if (data == null || data.size() == 0) {
            return inData;
        }
        if(groupByFields==null){
            groupByFields=new ArrayList<>(0);
        }
        //按group by字段排序
        if (groupByFields != null && groupByFields.size() > 0) {
            sortByFields(data, groupByFields, false);
        }

        List<Map<String, Object>> newData = new ArrayList<>();
        Map<String, Object> preRow = null;

        Map<String, List<Object>> tempData = new HashMap<>(statDesc.size());
        for (Triple<String, String, String> tr : statDesc) {
            tempData.put(tr.getLeft(), new ArrayList<>());
        }

        for (Map<String, Object> row : data) {
            if (0 != compareTwoRow(preRow, row, groupByFields, SORT_NULL_AS_FIRST)) {
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
        return new DataSet(newData);
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
                                            DataRowGroupVariableTranslate dvt,
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
    public static DataSet analyseDataset(BizModel bizModel, DataSet inData,
                                         List<String> groupByFields,
                                         List<String> orderByFields,
                                         Collection<Map.Entry<String, String>> refDesc) {
        List<Map<String, Object>> data = inData.getDataAsList();
        List<String> keyRows = ListUtils.union(groupByFields, orderByFields);
        //根据维度进行排序 行头、列头
        sortByFields(data, keyRows, SORT_NULL_AS_FIRST);
        Map<String, Object> preRow = null;
        int n = data.size();
        int prePos = 0;
        DataRowGroupVariableTranslate dvt = new DataRowGroupVariableTranslate(bizModel, data);
        List<Map<String, Object>> newData = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Map<String, Object> row = data.get(i);
            //主键不相等，开启新的一组
            if (compareTwoRow(preRow, row, groupByFields, SORT_NULL_AS_FIRST) != 0) {
                if (preRow != null) {
                    analyseDatasetGroup(newData, data, prePos, i, dvt, refDesc);
                }
                prePos = i;
            }
            preRow = row;
        }
        analyseDatasetGroup(newData, data, prePos, n, dvt, refDesc);
        return new DataSet(newData);
    }

    /***
     * 交叉制表 数据处理
     * @param inData 输入数据集
     * @param colHeaderFields 列头信息
     * @param rowHeaderFields 行头信息
     * @oaram fieldContainColumn 生成的新字段中是否保留原有老字段部分
     * 比如： 新字段值为  name:动态值:fieldName  当fieldContainColumn=false时  name:部分将不会被保留
     * @param concatStr 字段名连接字符
     * @param statType 统计方式 0 none 没有 1 only total 只有总计 2 cube 明细和和总计， 有多个分组时有效
     * @return 输出数据集
     */
    public static DataSet crossTabulation(DataSet inData, List<String> rowHeaderFields, List<String> colHeaderFields,
                                          boolean fieldContainColumn, String concatStr, int statType) {
        if (inData == null) {
            return inData;
        }
        List<Map<String, Object>> data = inData.getDataAsList();
        if (data == null || data.isEmpty()) {
            return inData;
        }
        if (rowHeaderFields.size() + colHeaderFields.size() >= data.get(0).size()) {
            throw new RuntimeException("数据不合法");
        }
        int colHeaderCount = colHeaderFields.size();
        List<String> keyRows = ListUtils.union(rowHeaderFields, colHeaderFields);
        //根据维度进行排序 行头、列头
        sortByFields(data, keyRows, SORT_NULL_AS_FIRST);
        List<Map<String, Object>> newData = new ArrayList<>();
        Map<String, Object> preRow = null;
        Map<String, Object> newRow = null;
        for (Map<String, Object> row : data) {
            if (row == null) {
                continue;
            }
            if (compareTwoRow(preRow, row, rowHeaderFields, SORT_NULL_AS_FIRST) != 0) {
                if (preRow != null && newRow != null) {
                    newData.add(newRow);
                }
                // 新建数据临时数据空间
                newRow = new HashMap<>(rowHeaderFields.size());
                // rowHeaderFields 一般为分组信息
                for (String key : rowHeaderFields) {
                    newRow.put(key, row.get(key));
                }
            }
            // 一般为统计信息
            StringBuilder colPrefix = new StringBuilder(128);
            for (String key : colHeaderFields) {
                if(fieldContainColumn){
                    colPrefix.append(key).append(concatStr);
                }
                colPrefix.append(row.get(key)).append(concatStr);
            }
            String prefix = colPrefix.toString();
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                String key = entry.getKey();
                if (!keyRows.contains(key)) {
                    if (newRow == null) { // 这一句应该永远运行不到
                        newRow = new HashMap<>(row.size());
                    }
                    newRow.put(prefix + key, entry.getValue());
                    // 添加行统计行统计信息
                    if(statType != 0){ // _totalSum
                        String fieldName = key + concatStr + "_totalSum";
                        Object preSum = newRow.get(fieldName);
                        newRow.put(fieldName, GeneralAlgorithm.addTwoObject(preSum, entry.getValue(),false));
                    }
                    if(statType == 2 && colHeaderCount > 1) { //_detailSum colHeaderCount == 2
                        for (String colKek : colHeaderFields) {
                            StringBuilder detailSumField = new StringBuilder(80);
                            if(fieldContainColumn){
                                detailSumField.append(colKek).append(concatStr);
                            }
                            detailSumField.append(row.get(colKek)).append(concatStr)
                                .append(key).append(concatStr).append("_detailSum");
                            String fieldName = detailSumField.toString();
                            Object preSum = newRow.get(fieldName);
                            newRow.put(fieldName, GeneralAlgorithm.addTwoObject(preSum, entry.getValue(), false));
                        }
                    }
                }
            }
            preRow = row;
        }

        if (preRow != null && newRow != null) {
            newData.add(newRow);
        }
        return new DataSet(newData);
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

    private static void splitMainAndSlave(List<Map.Entry<String, String>> primaryFields, List<String> mainFields, List<String> slaveFields) {
        for (Map.Entry<String, String> field : primaryFields) {
            mainFields.add(field.getKey());
            slaveFields.add(field.getValue());
        }
    }

    private static boolean matchToRow(Map<String, Object> data1, Map<String, Object> data2, List<Map.Entry<String, String>> fields) {
        if (data1 == null || data2 == null || fields == null) {
            return false;
        }
        for (Map.Entry<String, String> field : fields) {
            Object object1 = data1.get(field.getKey());
            Object object2 = data2.get(field.getKey());
            if(GeneralAlgorithm.compareTwoObject(object1, object2, false) != 0) {
                if (object2 instanceof String) {
                    boolean mch = Pattern.compile((String) object2)
                        .matcher(StringBaseOpt.objectToString(object1)).find();
                    if (!mch) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static DataSet leftAppendTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet, List<Map.Entry<String, String>> primaryFields) {
        if (slaveDataSet == null || mainDataSet == null) {
            return mainDataSet;
        }
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        List<Map<String, Object>> newData = new ArrayList<>();
        for(Map<String, Object> leftRow : mainDataSet.getDataAsList()){
            Map<String, Object> newRow = new LinkedHashMap<>();
            for(Map<String, Object> rightRow : slaveData){
                // 不是严格相等，相等或者 模式匹配
                if(matchToRow(leftRow, rightRow, primaryFields)) {
                    newRow.putAll(rightRow);
                    break;
                }
            }
            newRow.putAll(leftRow);
            newData.add(newRow);
        }
        return new DataSet(newData);
    }

    /**
     * 合并两个数据集; 类似于 数据的 join， 但是不同的是 如果有相同的字段 以mainDataSet为准，
     * 连接字段最好是主键（或者唯一），否则系统会保证多的哪个dataSet被加入，但不一定会被连接
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @param join          innerJoin， leftjoin rightjoin alljoin
     * @return DataSet
     */
    public static DataSet joinTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet, List<Map.Entry<String, String>> primaryFields, String join) {
        final boolean leftJoin = ConstantValue.DATASET_JOIN_TYPE_LEFT.equalsIgnoreCase(join) || ConstantValue.DATASET_JOIN_TYPE_ALL.equalsIgnoreCase(join);
        final boolean rightJoin = ConstantValue.DATASET_JOIN_TYPE_RIGHT.equalsIgnoreCase(join) || ConstantValue.DATASET_JOIN_TYPE_ALL.equalsIgnoreCase(join);
        if (mainDataSet == null) {
            if(rightJoin)
                return slaveDataSet;
            return null;
        }
        if (slaveDataSet == null) {
            if(leftJoin)
                return mainDataSet;
            return null;
        }
        List<Map<String, Object>> mainData = CollectionsOpt.cloneList(mainDataSet.getDataAsList());
        List<Map<String, Object>> slaveData = CollectionsOpt.cloneList(slaveDataSet.getDataAsList());
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields, SORT_NULL_AS_FIRST);
        sortByFields(slaveData, slaveFields, SORT_NULL_AS_FIRST);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();

        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields, SORT_NULL_AS_FIRST);
            Map<String, Object> newRow = new LinkedHashMap<>();
            if (nc == 0) {
                newRow.putAll(slaveData.get(j));
                newRow.putAll(mainData.get(i));
                /** 这边如果需要实现 数据库jion 的 笛卡尔积，需要遍列所有相同的key ， 只确保每一条数据都有对应上
                 * 就是只能保证一对多的情况正确，不能保证多对多的情况正确*/
                boolean equalNextMain = i < mainData.size() - 1 && compareTwoRow(mainData.get(i), mainData.get(i + 1), mainFields, SORT_NULL_AS_FIRST) == 0;
                boolean equalNextSlave = j < slaveData.size() - 1 && compareTwoRow(slaveData.get(j), slaveData.get(j + 1), slaveFields, SORT_NULL_AS_FIRST) == 0;
                if (equalNextMain && !equalNextSlave) {
                    i++;
                } else if (!equalNextMain && equalNextSlave) {
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
        return new DataSet(newData);
    }


    /**
     * 这个是 连个集合 的 unionALL 操作；如果 需要去掉重复的数据集，用jion来代替
     *
     * @param mainDataSet  主数据集
     * @param slaveDataSet 次数据集
     * @return newDataset
     */
    public static DataSet unionTwoDataSet(DataSet mainDataSet, DataSet slaveDataSet) {

        List<Map<String, Object>> mainData = mainDataSet.getDataAsList();
        List<Map<String, Object>> slaveData = slaveDataSet.getDataAsList();
        if (mainData == null) {
            return new DataSet(slaveData);
        }
        if (slaveData == null) {
            return new DataSet(mainData);
        }
        List<Map<String, Object>> resultData = new ArrayList<>(mainData.size() + slaveData.size());
        resultData.addAll(mainData);
        resultData.addAll(slaveData);
        return new DataSet(resultData);
    }

    /**
     * 求两个集合的交集
     *
     * @param mainDataSet   主数据集
     * @param slaveDataSet  次数据集
     * @param primaryFields 主键列
     * @param unionData     是否合并数据，是否用 slaveData中额外的数据项填补 mainData中的数据
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
        List<Map<String, Object>> mainData = CollectionsOpt.cloneList(mainDataSet.getDataAsList());
        List<Map<String, Object>> slaveData = CollectionsOpt.cloneList(slaveDataSet.getDataAsList());
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields, SORT_NULL_AS_FIRST);
        sortByFields(slaveData, slaveFields, SORT_NULL_AS_FIRST);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields, SORT_NULL_AS_FIRST);
            if (nc == 0) {
                Map<String, Object> newRow = new LinkedHashMap<>();
                if (unionData) {
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
        return new DataSet(newData);
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
        List<Map<String, Object>> mainData = CollectionsOpt.cloneList(mainDataSet.getDataAsList());
        List<Map<String, Object>> slaveData = CollectionsOpt.cloneList(slaveDataSet.getDataAsList());
        List<String> mainFields = new ArrayList<>();
        List<String> slaveFields = new ArrayList<>();
        splitMainAndSlave(primaryFields, mainFields, slaveFields);
        sortByFields(mainData, mainFields, SORT_NULL_AS_FIRST);
        sortByFields(slaveData, slaveFields, SORT_NULL_AS_FIRST);
        int i = 0;
        int j = 0;
        List<Map<String, Object>> newData = new ArrayList<>();
        while (i < mainData.size() && j < slaveData.size()) {
            int nc = compareTwoRowWithMap(mainData.get(i), slaveData.get(j), primaryFields, SORT_NULL_AS_FIRST);
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
        return new DataSet(newData);
    }

    private static int compareTwoRow(Map<String, Object> data1, Map<String, Object> data2, List<String> fields, boolean nullAsFirst) {
        if ((data1 == null && data2 == null)|| (fields == null)) {
            return 0;
        }

        if (data1 == null) {
            return nullAsFirst?-1:1;
        }

        if (data2 == null) {
            return nullAsFirst?1:-1;
        }
        /*
        if (isChinese((String)data1.get(dataField))&&isChinese((String) data2.get(dataField))){
            char c1 = ((String) data1.get(dataField)).charAt(0);
            char c2 = ((String) data2.get(dataField)).charAt(0);
            cr =concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(concatPinyinStringArray(PinyinHelper.toHanyuPinyinStringArray(c2)));
        }*/
        for (String field : fields) {
            if (field.endsWith(" desc")) {
                String dataField = field.substring(0, field.length() - 5).trim();
                int cr = GeneralAlgorithm.compareTwoObject(data1.get(dataField), data2.get(dataField), !nullAsFirst);
                if (cr != 0) {
                    return 0 - cr;
                }
            } else {
                int cr = GeneralAlgorithm.compareTwoObject(data1.get(field), data2.get(field), nullAsFirst);
                if (cr != 0) {
                    return cr;
                }
            }
        }
        return 0;
    }

    private static int compareTwoRowWithMap(Map<String, Object> data1, Map<String, Object> data2, List<Map.Entry<String, String>> fields, boolean nullAsFirst) {
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
                data1.get(field.getKey()), data2.get(field.getValue()), nullAsFirst);
            if (cr != 0) {
                return cr;
            }
        }
        return 0;
    }

    private static void sortByFields(List<Map<String, Object>> data, List<String> fields, boolean nullAsFirst) {
        data.sort((o1, o2) -> compareTwoRow(o1, o2, fields, nullAsFirst));
    }

    public static FileDataSet mapDataToFile(Map<String, Object> objectMap,
                                            String fileNameDesc, String fileContentDesc){
        if(objectMap==null)
           return null;

        String fileName = StringUtils.isNotBlank(fileNameDesc)?
            StringBaseOpt.castObjectToString(objectMap.get(fileNameDesc)) : null;

        if (StringUtils.isBlank(fileName)) {
            fileName = StringBaseOpt.castObjectToString(objectMap.get(ConstantValue.FILE_NAME));
        }

        Object fileData =null;
        if(StringUtils.isNotBlank(fileContentDesc)) {
            fileData = objectMap.get(fileContentDesc);
        }
        if (fileData == null) {
            fileData = objectMap.get(ConstantValue.FILE_CONTENT);
        }

        if(fileData == null)
            return null;

        HashMap<String, Object> fileInfo = new HashMap<>();
        for(Map.Entry<String, Object> entry : objectMap.entrySet()){
            if(! StringUtils.equalsAny(entry.getKey(),
                    ConstantValue.FILE_NAME, ConstantValue.FILE_CONTENT, fileNameDesc, fileContentDesc)){
                fileInfo.put(entry.getKey(), entry.getValue());
            }
        }
        FileDataSet fileDataset =  new FileDataSet();
        fileDataset.setFileInfo(fileInfo);

        fileDataset.setFileContent(fileName,
            NumberBaseOpt.castObjectToLong(objectMap.get(ConstantValue.FILE_SIZE), -1l),
            fileData);
        return fileDataset;
    }
    public static FileDataSet castToFileDataSet(DataSet dataSet){
        if(dataSet==null){
            return null;
        }
        if(dataSet instanceof FileDataSet)
            return (FileDataSet) dataSet;
        return mapDataToFile(dataSet.getFirstRow(), null, null);
    }

    public static FileDataSet attainFileDataset(BizModel bizModel, DataSet dataSet, JSONObject jsonStep, boolean singleFile){

        String fileNameDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_NAME, "");
        BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel, dataSet.getData());
        String fileName = null;

        if(StringUtils.isNotBlank(fileNameDesc)){
            fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
        }

        if(dataSet instanceof FileDataSet){
            FileDataSet fileDataSet = (FileDataSet) dataSet;
            String currentFileName =  fileDataSet.getFileName();
            if(StringUtils.isNotBlank(fileName) && (StringUtils.isBlank(currentFileName) || StringUtils.equals(
                FileType.getFileExtName(currentFileName), FileType.getFileExtName(fileName)))){
                fileDataSet.setFileName(fileName);
            }
            return fileDataSet;
        }

        String fileContentDesc = BuiltInOperation.getJsonFieldString(jsonStep, ConstantValue.FILE_CONTENT, "");

        if(singleFile || dataSet.getSize()==1) {
            Map<String, Object> mapFirstRow = dataSet.getFirstRow();
            FileDataSet fileDataset = mapDataToFile(mapFirstRow, fileNameDesc, fileContentDesc);
            if(fileDataset==null){
                Object fileData = JSONTransformer.transformer(fileContentDesc, transformer);
                if(fileData==null){
                    throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
                }
                fileDataset = new FileDataSet(fileName,
                    NumberBaseOpt.castObjectToLong(jsonStep.get(ConstantValue.FILE_SIZE), -1l),
                    fileData);
            } else if (StringUtils.isNotBlank(fileName)) {
                fileDataset.setFileName(fileName);
            }
            return fileDataset;
        }

        List<FileDataSet> files = new ArrayList<>();
        for(Map<String, Object> objectMap : dataSet.getDataAsList()){
            FileDataSet fileDataSet = mapDataToFile(objectMap, fileNameDesc, fileContentDesc);
            if(fileDataSet != null){
                files.add(fileDataSet);
            }
        }
        if(files.isEmpty()){
            throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
        }

        if(files.size() == 1){
            FileDataSet ds = files.get(0);
            String currentFileName = ds.getFileName();
            if(StringUtils.isNotBlank(fileName) && (StringUtils.isBlank(currentFileName) || StringUtils.equals(
                FileType.getFileExtName(currentFileName), FileType.getFileExtName(fileName))) ){
                ds.setFileName(fileName);
            }
            return ds;
        }

        if(StringUtils.isBlank(fileName)){
            fileName ="download.zip";
        }
        FileDataSet fileDataset = new FileDataSet();
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
        try(ZipOutputStream out = ZipCompressor.convertToZipOutputStream(outBuf)) {
            Map<String, Integer> fileNameMap = new HashMap<>(files.size() + 4);
            for (FileDataSet ds : files) {
                InputStream inputStream = ds.getFileInputStream();
                String fn = ds.getFileName();
                while (fileNameMap.containsKey(fn)) {
                    int copies = fileNameMap.get(fn) + 1;
                    fileNameMap.put(fn, copies);
                    fn = FileType.truncateFileExtName(fn) + "(" + copies + ")." + FileType.getFileExtName(fn);
                }
                fileNameMap.put(fn, 1);
                ZipCompressor.compressFile(inputStream, fn, out, "");

            }
        } catch (IOException e) {
            throw new ObjectException(ObjectException.DATA_NOT_INTEGRATED, "压缩多个文件时报错："+ e.getMessage(), e);
        }
        fileDataset.setFileContent(fileName, outBuf.size(), outBuf);
        return fileDataset;
    }

    public static InputStream getInputStreamFormFile(Map<String, Object> fileInfo){
        Object data = fileInfo.get(ConstantValue.FILE_CONTENT);
        return FileIOOpt.castObjectToInputStream(data);
    }

    public static InputStream getInputStreamFormDataSet(DataSet dataSet){
        if(dataSet instanceof FileDataSet){
            return ((FileDataSet)dataSet).getFileInputStream();
        }
        return getInputStreamFormFile(dataSet.getFirstRow());
    }

    public static Map<String, Object> getDataSetParames(BizModel bizModel, JSONObject bizOptJson){
        //参数类型
        String paramsType = bizOptJson.getString("sourceType");
        //if (paramsType == null) return new HashMap<>();
        Map<String, Object> parames;
        // 自定义参数类型
        if("customSource".equals(paramsType)){
            Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
            parames = new HashMap<>(16);
            if (mapString != null) {
                BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
                for (Map.Entry<String, String> map : mapString.entrySet()) {
                    if (StringUtils.isNotBlank(map.getValue())) {
                        parames.put(map.getKey(), transform.attainExpressionValue(map.getValue()));
                    }
                }
            }
            //添加 url参数
            boolean appendRequest = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("appendRequest"), false);
            if(appendRequest) {
                Map<String, Object> urlParams = CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
                parames = CollectionsOpt.unionTwoMap(parames, urlParams);
            }
        } else {
            //数据集参数
            String source = bizOptJson.getString("source");
            if(StringUtils.isBlank(source) || ConstantValue.REQUEST_PARAMS_TAG.equals(source)){
                Map<String, Object> requestParams = CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)); //数据集
                parames = new HashMap<>(16);
                for (Map.Entry<String, Object> ent : requestParams.entrySet()) {
                    Object paramValue = ent.getValue();
                    String pretreatmentSql = ent.getKey();

                    ImmutableTriple<String, String, String> paramDesc = QueryUtils.parseParameter(pretreatmentSql);
                    String pretreatment = paramDesc.getRight();
                    String valueName = StringUtils.isBlank(paramDesc.getMiddle()) ? paramDesc.getLeft() : paramDesc.getMiddle();

                    if(StringUtils.isNotBlank(pretreatment)){
                        paramValue = QueryUtils.pretreatParameter(pretreatment, paramValue);
                    }
                    parames.put(valueName, paramValue);
                }
            } else {
                DataSet dataSet = bizModel.getDataSet(source);
                if (dataSet != null) {
                    parames = dataSet.getFirstRow();
                } else {
                    parames = new HashMap<>();
                }
            }
        }
        return parames;
    }


    //获取数据集参数或者自定义参数
    public static List<Map<String, Object>> fetchDataSet(BizModel bizModel, JSONObject bizOptJson) {
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(StringUtils.isBlank(source) ? ConstantValue.REQUEST_PARAMS_TAG : source);
        if(dataSet==null)
            return null;
        return dataSet.getDataAsList();
    }

    public static Object getCallModuleParams(BizModel bizModel, JSONObject bizOptJson, String defaultDataSet){
        //参数类型
        String paramsType = bizOptJson.getString("sourceType");
        if("paramSource".equals(paramsType)){
            //数据集参数
            String source = bizOptJson.getString("source");
            DataSet dataSet = bizModel.getDataSet( StringUtils.isBlank(source)? defaultDataSet :source);
            return dataSet.getData(); //数据集
        } else {  //customSource 默认值
            Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(
                bizOptJson.getJSONArray("config"), "paramName", "paramDefaultValue");
            Map<String, Object> parames = new HashMap<>(16);
            //if (mapString != null) {
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (StringUtils.isNotBlank(map.getValue())) {
                    parames.put(map.getKey(), transform.attainExpressionValue(map.getValue()));
                }
            }
            //}
            Map<String, Object> urlParams = CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
            return CollectionsOpt.unionTwoMap(parames, urlParams);
        }
    }

    public static Object fetchFieldValue(Object data, String fieldFormula){
        if(StringUtils.isBlank(fieldFormula)){
            return null;
        }

        if(StringUtils.equalsAny(fieldFormula, ".",ConstantValue.ROOT_NODE_TAG, VariableTranslate.THE_DATA_SELF_LABEL)){
            return data;
        }

        if (fieldFormula.startsWith("@")){
            return Pretreatment.mapTemplateStringAsFormula(fieldFormula.substring(1),
                data, null, true);
        }
        VariableTranslate translate = data instanceof VariableTranslate ? (VariableTranslate) data : new ObjectTranslate(data);
        VariableFormula variableFormula = new VariableFormula();
        variableFormula.setExtendFuncMap(DataSetOptUtil.extendFuncs);
        variableFormula.setTrans(translate);
        variableFormula.setFormula(fieldFormula);
        return variableFormula.calcFormula();
    }
}
