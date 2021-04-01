package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.datarule.CheckRule;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.utils.SystemTempFileUtils;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import com.centit.support.network.UrlOptUtils;
import com.centit.support.report.ExcelExportUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据持久化操作
 *
 * @author zhf
 */
@SuppressWarnings("unchecked")
public class BuiltInOperation {
    public static String getJsonFieldString(JSONObject bizOptJson, String fieldName, String defaultValue) {
        String targetDsName = bizOptJson.getString(fieldName);
        if (StringUtils.isBlank(targetDsName)) {
            return defaultValue;
        }
        return targetDsName;
    }

    static ResponseData getResponseSuccessData(int count) {
        JSONObject map = new JSONObject();
        map.put("info", "ok");
        map.put("success", count);
        map.put("error", 0);
        return ResponseSingleData.makeResponseData(map);
    }

    static ResponseData getResponseData(int success, int error, String info) {
        JSONObject map = new JSONObject();
        map.put("info", info);
        map.put("success", success);
        map.put("error", error);
        return ResponseSingleData.makeResponseData(map);
    }

    public static Map<String, String> jsonArrayToMap(JSONArray json, String key, String... value) {
        if (json != null) {
            Map<String, String> map = new HashMap<>(json.size());
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
        return null;
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
        return null;
    }

    public static ResponseData runStart(BizModel bizModel, JSONObject bizOptJson) {
        return getResponseSuccessData(0);
    }

    public static ResponseData runRequestBody(BizModel bizModel, JSONObject bizOptJson) {
        String bodyString = (String) bizModel.getModelTag().get("requestBody");
        DataSet destDs = BizOptUtils.castObjectToDataSet(bodyString);
        bizModel.putDataSet("requestBody", destDs);
        return getResponseSuccessData(destDs.getSize());
    }

    public static ResponseData runRequestFile(BizModel bizModel, JSONObject bizOptJson) throws IOException {
        String bodyString = (String) bizModel.getModelTag().get("requestFile");
        DataSet destDs = BizOptUtils.castObjectToDataSet(bodyString);
        bizModel.putDataSet("requestFile", destDs);
        return getResponseSuccessData(destDs.getSize());
    }
    public static Object returnExcel(BizModel bizModel, JSONObject bizOptJson) throws Exception{
        String path = BuiltInOperation.getJsonFieldString(bizOptJson, "source", "");
        File excel= new File(SystemTempFileUtils.getRandomTempFilePath());
        for(Map.Entry<String,DataSet> set:bizModel.getBizData().entrySet()) {
            if(set.getKey().equals(path) || StringBaseOpt.isNvl(path)) {
                String[] head=CollectionsOpt.listToArray(set.getValue().getFirstRow().keySet());
                ExcelExportUtil.appendDataToExcelSheet(excel.getPath(), set.getKey(), (List<Object>) set.getValue().getData(), head,head);
            }
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        FileIOOpt.writeInputStreamToOutputStream(new FileInputStream(excel),outStream);
        FileSystemOpt.deleteFile(excel);
        return BizOptUtils.castObjectToDataSet(CollectionsOpt.createHashMap("fileName", bizModel.getModelName()+".xlsx",
             "fileContent", outStream));
    }
    public static ResponseData runMap(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        Map<String, String> mapInfo = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        int count = 0;
        if (mapInfo != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getResponseSuccessData(count);
    }

    public static ResponseData runAppend(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString((JSONObject) bizOptJson.get("source"), "value", bizModel.getModelName());
        Map<String, String> mapInfo = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        int count = 0;
        if (mapInfo != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                count = dataSet.getSize();
                DataSetOptUtil.appendDeriveField(dataSet, mapInfo.entrySet());
            }
        }
        return getResponseSuccessData(count);
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
        return getResponseSuccessData(count);
    }

    public static ResponseData runStat(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> groupFields = jsonArrayToList(bizOptJson.getJSONArray("exconfig"), "columnName", "index", "");
        Map<String, String> stat = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "sourcefd", "nodeInstId");
        int count = 0;
        if (stat != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.statDataset(dataSet, groupFields, stat);
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getResponseSuccessData(count);
    }

    public static ResponseData runAnalyse(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> orderFields = jsonArrayToList(bizOptJson.getJSONArray("sortconifg"), "sortName", "order", "desc");
        List<String> groupFields = jsonArrayToList(bizOptJson.getJSONArray("exconfig"), "groupName", "groupName", "");
        int count = 0;
        Map<String, String> analyse = jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        if (analyse != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.analyseDataset(dataSet,
                    groupFields, orderFields, ((Map) analyse).entrySet());
                count = destDs.getSize();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getResponseSuccessData(count);
    }

    public static ResponseData runCross(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> rows = jsonArrayToList(bizOptJson.getJSONArray("RowField"), "columnName", "index", "");
        List<String> cols = jsonArrayToList(bizOptJson.getJSONArray("ColumnField"), "columnName", "index", "");
        int count = 0;
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet != null) {
            DataSet destDs = DataSetOptUtil.crossTabulation(dataSet, rows, cols);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return getResponseSuccessData(count);
    }

    public static ResponseData runCompare(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return getResponseSuccessData(0);
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
        return getResponseSuccessData(count);
    }

    public static ResponseData runSort(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        List<String> orderByFields = jsonArrayToList(bizOptJson.getJSONArray("config"), "columnName", "orderBy", "desc");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        if (orderByFields != null) {
            DataSetOptUtil.sortDataSetByFields(dataSet, orderByFields);
        }
        return getResponseSuccessData(dataSet.getSize());
    }

    public static ResponseData runClear(BizModel bizModel, JSONObject bizOptJson) {
        List<String> sets = jsonArrayToList(bizOptJson.getJSONArray("config"), "paramValidateRegex", "index", "");
        for (String s : sets) {
            bizModel.putDataSet(s, null);
        }
        if (sets.size() == 0) {
            bizModel.getBizData().clear();
        }
        return getResponseSuccessData(0);
    }

    public static ResponseData runJoin(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        String join = getJsonFieldString(bizOptJson, "operation", "join");
        Map<String, String> map = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("configfield"), "primaryKey1", "primaryKey2");
        if (map != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
            DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
            DataSet destDs = DataSetOptUtil.joinTwoDataSet(dataSet, dataSet2, new ArrayList<>(map.entrySet()), join);
            if (destDs != null) {
                bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDs);
                return getResponseSuccessData(destDs.getSize());
            }
        }
        return getResponseSuccessData(0);
    }

    public static ResponseData runUnion(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        DataSet destDs = DataSetOptUtil.unionTwoDataSet(dataSet, dataSet2);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDs);
        return getResponseSuccessData(destDs.getSize());
    }

    public static ResponseData runStaticData(BizModel bizModel, JSONObject bizOptJson) {
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDS = BizOptUtils.castObjectToDataSet(ja);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "id", bizModel.getModelName()), destDS);
        return getResponseSuccessData(destDS.getSize());
    }

    public static ResponseData runFilterExt(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source1", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return getResponseSuccessData(0);
        }
        String targetDsName = getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        List<String> formulas = jsonArrayToList(bizOptJson.getJSONArray("config"), "expression", "expression2", "");
        Map<String, String> pks = jsonArrayToMap(bizOptJson.getJSONArray("primaryKeyList"), "primaryKey1", "primaryKey2");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        int count = 0;
        if (dataSet != null && dataSet2 != null) {
            DataSet destDs = DataSetOptUtil.filterByOtherDataSet(dataSet, dataSet2, new ArrayList<>(pks.entrySet()), formulas);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return getResponseSuccessData(count);
    }

    public static ResponseData runCheckData(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        Object rulesJson = bizOptJson.get("config");
        int count = 0;
        if (rulesJson instanceof JSONArray) {
            List<CheckRule> rules = ((JSONArray) rulesJson).toJavaList(CheckRule.class);
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSetOptUtil.checkDateSet(dataSet, rules);
                count = dataSet.getSize();
            }
        }
        return getResponseSuccessData(count);
    }

    private static HttpExecutorContext getHttpClientContext(Map<String, Object> map) throws IOException {
        HttpClientContext context = HttpClientContext.create();
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        if(map.get("loginUrl")!=null) {
            HttpExecutor.formPost(HttpExecutorContext.create(httpClient).context(context),
                (String) map.get("loginUrl"),
                map, false);
        }
        context.setCookieStore(cookieStore);
        return HttpExecutorContext.create(httpClient).context(context);
    }

    public static ResponseData runHttpData(BizModel bizModel, JSONObject bizOptJson) throws IOException {
        String sourDsName = getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String httpMethod = getJsonFieldString(bizOptJson, "requestMode", "post");
        String httpUrl = getJsonFieldString(bizOptJson, "httpUrl", "");
        Object requestBody = VariableFormula.calculate(getJsonFieldString(bizOptJson, "requestText", ""),
            new BizModelJSONTransform(bizModel));
        Map<String, String> params = jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "urlname", "urlvalue");
        Map<String, Object> mapObject = new HashMap<>();
        if (params != null) {
            for (Map.Entry<String, String> map : params.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), map.getValue());
                }
            }
        }
        mapObject.putAll(bizModel.getModelTag());
        DataSet dataSet = new SimpleDataSet();
        HttpReceiveJSON receiveJSON;
        HttpExecutorContext httpExecutorContext = getHttpClientContext(mapObject);
        switch (httpMethod.toLowerCase()) {
            case "post":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPost(httpExecutorContext, UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                break;
            case "put":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.jsonPut(httpExecutorContext, UrlOptUtils.appendParamsToUrl(httpUrl, mapObject), requestBody));
                dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                break;
            case "get":
                receiveJSON= HttpReceiveJSON.valueOfJson(HttpExecutor.simpleGet(httpExecutorContext, httpUrl, mapObject));
                if (receiveJSON.getCode() != ResponseData.RESULT_OK) {
                    return getResponseData(0, receiveJSON.getCode(), receiveJSON.getMessage());
                } else {
                    dataSet = BizOptUtils.castObjectToDataSet(receiveJSON.getData());
                }
                break;
            case "delete":
                dataSet = BizOptUtils.castObjectToDataSet(HttpExecutor.simpleDelete(httpExecutorContext, httpUrl, mapObject));
            default:
                break;
        }
        bizModel.putDataSet(sourDsName, dataSet);
        return getResponseSuccessData(dataSet.getSize());
    }

}
