package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.adapter.po.DataCheckRule;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.utils.DataCheckResult;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 数据持久化操作
 *
 * @author zhf
 */
@SuppressWarnings("unchecked")
public class BuiltInOperation implements BizOperation {

    private DataCheckRuleService dataCheckRuleService;

    public BuiltInOperation(DataCheckRuleService dataCheckRuleService) {
        this.dataCheckRuleService = dataCheckRuleService;
    }

    public static String getJsonFieldString(JSONObject bizOptJson, String fieldName, String defaultValue) {
        String targetDsName = bizOptJson.getString(fieldName);
        if (StringUtils.isBlank(targetDsName)) {
            return defaultValue;
        }
        return targetDsName;
    }

    static ResponseData createResponseSuccessData(int count) {
        JSONObject map = new JSONObject();
        map.put("info", "ok");
        map.put("success", count);
        map.put("error", 0);
        return ResponseSingleData.makeResponseData(map);
    }

    static ResponseData createResponseData(int success, int error, String info) {
        JSONObject map = new JSONObject();
        map.put("info", info);
        map.put("success", success);
        map.put("error", error == 0 ? 1 : error);
        ResponseSingleData result = ResponseSingleData.makeResponseData(map);
        result.setCode(ResponseData.ERROR_OPERATION);
        result.setMessage(info);
        return result;
    }

    public static Map<String, String> jsonArrayToMap(JSONArray json, String key, String... value) {
        if (json != null) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (Object o : json) {
                JSONObject temp = (JSONObject) o;
                if (!StringBaseOpt.isNvl(temp.getString(key))) {
                    StringBuilder values = new StringBuilder();
                    for (int i = 0; i < value.length; i++) {
                        values.append(temp.getString(value[i]));
                        if (i < value.length - 1) {
                            values.append(":");
                        }
                    }
                    map.put(temp.getString(key), values.toString());
                }
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    private static List<String> jsonArrayToList(JSONArray json, String key, String value, String compare) {
        if (json != null) {
            List<String> list = new ArrayList<>(json.size());
            for (Object o : json) {
                JSONObject temp = (JSONObject) o;
                if (!StringBaseOpt.isNvl(temp.getString(key))) {
                    if (compare.equalsIgnoreCase(temp.getString(value))) {
                        if (!list.contains(temp.getString(key) + " " + temp.getString(value))) {
                            list.add(temp.getString(key) + " " + temp.getString(value));
                        }
                    } else {
                        if (!list.contains(temp.getString(key))) {
                            list.add(temp.getString(key));
                        }
                    }
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public static ResponseData runStart(BizModel bizModel, JSONObject bizOptJson) {
        return createResponseSuccessData(0);
    }

    public static ResponseData runRequestFile(BizModel bizModel, JSONObject bizOptJson) throws IOException {
        InputStream inputStream = (InputStream) bizModel.getStackData(ConstantValue.REQUEST_FILE_TAG);
        DataSet destDs = BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", "",
            "fileSize", inputStream.available(), "fileContent", inputStream));
        bizModel.putDataSet(bizOptJson.getString("id"), destDs);
        return createResponseSuccessData(destDs.getSize());
    }

    public static ResponseData runRequestBody(BizModel bizModel, JSONObject bizOptJson) {
        DataSet destDs = bizModel.fetchDataSetByName(ConstantValue.REQUEST_BODY_TAG);
        if(destDs!=null) {
            bizModel.putDataSet(bizOptJson.getString("id"), destDs);
            return createResponseSuccessData(destDs.getSize());
        }
        return createResponseSuccessData(0);
    }

    public static ResponseData runMap(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        Map<String, String> mapInfo = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
        bizModel.putDataSet(targetDsName, destDs);
        return createResponseSuccessData(destDs.getSize());
    }

    public static ResponseData runAppend(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = bizOptJson.getString("source");
        Map<String, String> mapInfo = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        int count = 0;
        if (mapInfo != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                count = dataSet.getSize();
                DataSetOptUtil.appendDeriveField(dataSet, mapInfo.entrySet());
            }
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runFilter(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> formula = jsonArrayToList(bizOptJson.getJSONArray("configfield"), "paramValidateRegex", "column", "");
        int count = 0;
        if (formula != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.filterDateSet(dataSet, formula);
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runStat(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> groupFields = jsonArrayToList(bizOptJson.getJSONArray("exconfig"), "columnName", "index", "");
        Map<String, String> stat = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "cName", "nodeInstId");
        int count = 0;
        if (stat != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.statDataset(dataSet, groupFields, stat);
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runAnalyse(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> orderFields = jsonArrayToList(bizOptJson.getJSONArray("sortconfig"), "sortName", "order", "desc");
        List<String> groupFields = jsonArrayToList(bizOptJson.getJSONArray("exconfig"), "groupName", "groupName", "");
        int count = 0;
        Map<String, String> analyse = jsonArrayToMap(bizOptJson.getJSONArray("configfield"), "columnName", "expression");
        if (analyse != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.analyseDataset(dataSet,
                    groupFields, orderFields, ((Map) analyse).entrySet());
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runCross(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> rows = jsonArrayToList(bizOptJson.getJSONArray("RowField"), "primaryKey1", "unique", "");
        List<String> cols = jsonArrayToList(bizOptJson.getJSONArray("ColumnsField"), "primaryKey2", "unique", "");
        //是否保留旧字段
        Boolean isOldField = BooleanBaseOpt.castObjectToBoolean(bizOptJson.getBoolean("isOldField"),true);
        //连接符
        String concatStr = StringBaseOpt.castObjectToString(bizOptJson.getString("concatStr"),":");
        //统计类型
        int statisticalType = NumberBaseOpt.castObjectToInteger(bizOptJson.getIntValue("statisticalType"),0);
        int count = 0;
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet != null) {
            DataSet destDs = DataSetOptUtil.crossTabulation(dataSet, rows, cols, isOldField, concatStr, statisticalType);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runCompare(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return createResponseSuccessData(0);
        }
        String targetDsName = getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        Map<String, String> analyse = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        Map<String, String> pks = jsonArrayToMap(bizOptJson.getJSONArray("configfield"), "primaryKey1", "primaryKey2");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        int count = 0;
        if (dataSet != null && dataSet2 != null && pks != null) {
            DataSet destDs = DataSetOptUtil.compareTabulation(dataSet, dataSet2, new ArrayList<>(pks.entrySet()), analyse == null ? null : analyse.entrySet());
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runSort(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        List<String> orderByFields = jsonArrayToList(bizOptJson.getJSONArray("config"), "columnName", "orderBy", "desc");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSetOptUtil.sortDataSetByFields(dataSet, orderByFields);
        return createResponseSuccessData(dataSet.getSize());
    }

    public static ResponseData runClear(BizModel bizModel, JSONObject bizOptJson) {
        List<String> sets = jsonArrayToList(bizOptJson.getJSONArray("config"), "paramValidateRegex", "index", "");
        for (String s : sets) {
            bizModel.putDataSet(s, null);
        }
        if (sets.size() == 0) {
            bizModel.getBizData().clear();
        }
        return createResponseSuccessData(0);
    }

    public static ResponseData runJoin(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        String join = getJsonFieldString(bizOptJson, "operation", "join");
        Map<String, String> map = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("configfield"), "primaryKey1", "primaryKey2");
        if (map != null) {
            DataSet dataSet = getArray(bizModel.fetchDataSetByName(sour1DsName));
            DataSet dataSet2 = getArray(bizModel.fetchDataSetByName(sour2DsName));
            DataSet destDs = DataSetOptUtil.joinTwoDataSet(dataSet, dataSet2, new ArrayList<>(map.entrySet()), join);
            if (destDs != null) {
                bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDs);
                return createResponseSuccessData(destDs.getSize());
            }
        }
        return createResponseSuccessData(0);
    }

    public static ResponseData runUnion(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        DataSet dataSet = getArray(bizModel.fetchDataSetByName(sour1DsName));
        DataSet dataSet2 = getArray(bizModel.fetchDataSetByName(sour2DsName));
        DataSet destDs = DataSetOptUtil.unionTwoDataSet(dataSet, dataSet2);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDs);
        return createResponseSuccessData(destDs.getSize());
    }

    public static ResponseData runStaticData(BizModel bizModel, JSONObject bizOptJson) {
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDS = BizOptUtils.castObjectToDataSet(ja);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDS);
        return createResponseSuccessData(destDS.getSize());
    }

    /**
     * TODO 去掉这个组件，添加 交集 和 减集 组件
     * @param bizModel
     * @param bizOptJson
     * @return
     */
    public static ResponseData runFilterExt(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return createResponseSuccessData(0);
        }
        String targetDsName = getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        List<String> formulas = jsonArrayToList(bizOptJson.getJSONArray("config"), "expression", "expression2", "");
        Map<String, String> pks = jsonArrayToMap(bizOptJson.getJSONArray("primaryKeyList"), "primaryKey1", "primaryKey2");
        DataSet dataSet = getArray(bizModel.fetchDataSetByName(sour1DsName));
        DataSet dataSet2 = getArray(bizModel.fetchDataSetByName(sour2DsName));
        int count = 0;
        if (dataSet != null && dataSet2 != null) {
            DataSet destDs = DataSetOptUtil.filterByOtherDataSet(dataSet, dataSet2, new ArrayList<>(pks.entrySet()), formulas);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return createResponseSuccessData(count);
    }

    public  ResponseData runCheckData(BizModel bizModel, JSONObject bizOptJson) {
        String dataSetId = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        //校验信息所挂载的字段
        String checkRuleResultField =bizOptJson.getString("checkRuleResultField");
        //是否返回校验结果
        Boolean checkRuleResult =bizOptJson.getBoolean("checkRuleResult")==null?true:bizOptJson.getBoolean("checkRuleResult");
        //是否返回校验信息 默认不返回，如果默认返回的话会比较耗时
        Boolean checkRuleResultMsg = bizOptJson.getBoolean("checkRuleResultMsg")==null?false:bizOptJson.getBoolean("checkRuleResultMsg");
        DataSet dataSet = bizModel.getDataSet(dataSetId);
        JSONArray rulesJson = bizOptJson.getJSONArray("config");
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        DataCheckResult result = DataCheckResult.create();
        dataAsList.stream().forEach(data->{
            rulesJson.stream().forEach(ruleInfo->{
                if (ruleInfo instanceof  Map){
                    Map<String, Object> map = CollectionsOpt.objectToMap(ruleInfo);
                    String checkTypeId = StringBaseOpt.objectToString(map.get("checkTypeId"));
                    DataCheckRule dataCheckRule = dataCheckRuleService.getObjectById(checkTypeId);
                    List<Object> checkParams = CollectionsOpt.objectToList(map.get("checkParams"));
                    if (dataCheckRule!=null  && checkParams!=null){
                        Map<String, String>  paramMap = new HashMap<>();
                        String checkField = StringBaseOpt.objectToString(map.get("checkField"));
                        paramMap.put("checkValue",checkField);
                        for (int i = 0; i < checkParams.size(); i++) {
                            Map<String, Object> param = CollectionsOpt.objectToMap(checkParams.get(i));
                            for (Map.Entry<String, Object> entry : param.entrySet()) {
                                paramMap.put(entry.getKey(),StringBaseOpt.objectToString(entry.getValue()));
                            }
                        }
                        result.checkData(data,dataCheckRule,paramMap,checkRuleResultMsg);
                    }
                }
                if (data instanceof Map){
                    Map<String, Object> map = CollectionsOpt.objectToMap(data);
                    //设置校验结果信息
                    if (checkRuleResultMsg){
                        String errorMessage = result.getErrorMessage();
                        map.put(checkRuleResultField+"_msg",errorMessage);
                    }
                    //设置校验结果
                    if (checkRuleResult){
                        map.put(checkRuleResultField,result.getResult());
                    }
                }
            });
            //清除上个对象的校验结果信息
            result.reset();
        });
        return createResponseSuccessData(0);
    }

    private static DataSet getArray(DataSet dataSet) {
        if (dataSet.getData() instanceof List) {
            return dataSet;
        }
        for (Map<String, Object> map : dataSet.getDataAsList()) {
            for (String key : map.keySet()) {
                if (map.get(key) instanceof List) {
                    return new SimpleDataSet(map.get(key));
                }
            }
        }
        return new SimpleDataSet();
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        return  runCheckData( bizModel,  bizOptJson);
    }
}
