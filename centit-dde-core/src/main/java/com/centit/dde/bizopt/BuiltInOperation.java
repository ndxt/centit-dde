package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.datarule.CheckRule;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.RestfulHttpRequest;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

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

    static JSONObject getJsonObject(int count) {
        JSONObject map = new JSONObject();
        map.put("info", "ok");
        map.put("success", count);
        map.put("error", 0);
        return map;
    }

    private static Object jsonArrayToMap(Object json, String key, String value) {
        if (json instanceof JSONArray) {
            Map<String, String> map = new HashMap<>(((JSONArray) json).size());
            for (Object o : (JSONArray) json) {
                JSONObject temp = (JSONObject) o;
                map.put(temp.getString(key), temp.getString(value));
            }
            return map;
        }
        return json;
    }

    public static JSONObject runMap(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        Object mapInfo = jsonArrayToMap(bizOptJson.get("config"), "columnName", "paramValidateRegex");
        int count = 0;
        if (mapInfo instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.mapDateSetByFormula(dataSet, ((Map) mapInfo).entrySet());
                count = destDs.size();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getJsonObject(count);
    }

    public static JSONObject runAppend(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        Object mapInfo = jsonArrayToMap(bizOptJson.get("config"), "columnName", "paramValidateRegex");
        int count = 0;
        if (mapInfo instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                count = dataSet.size();
                DataSetOptUtil.appendDeriveField(dataSet, ((Map) mapInfo).entrySet());
            }
        }
        return getJsonObject(count);
    }

    public static JSONObject runFilter(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        String formula = bizOptJson.getString("filter");
        int count = 0;
        if (StringUtils.isNotBlank(formula)) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.filterDateSet(dataSet, formula);
                count = destDs.size();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getJsonObject(count);
    }

    public static JSONObject runStat(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        Object stat = bizOptJson.get("fieldsMap");
        int count = 0;
        if (stat instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.statDataset2(dataSet, groupFields, (Map) stat);
                count = destDs.size();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getJsonObject(count);
    }

    public static JSONObject runAnalyse(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        Object orderBy = bizOptJson.get("orderBy");
        List<String> orderFields = StringBaseOpt.objectToStringList(orderBy);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        int count = 0;
        Object analyse = bizOptJson.get("fieldsMap");
        if (analyse instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSet destDs = DataSetOptUtil.analyseDataset(dataSet,
                    groupFields, orderFields, ((Map) analyse).entrySet());
                count = destDs.size();
                bizModel.putDataSet(targetDsName, destDs);
            }
        }
        return getJsonObject(count);
    }

    public static JSONObject runCross(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "target", sourDsName);
        Object rowHeader = bizOptJson.get("rowHeader");
        List<String> rows = StringBaseOpt.objectToStringList(rowHeader);
        Object colHeader = bizOptJson.get("colHeader");
        List<String> cols = StringBaseOpt.objectToStringList(colHeader);
        int count = 0;
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet != null) {
            DataSet destDs = DataSetOptUtil.crossTabulation(dataSet, rows, cols);
            count = destDs.size();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return getJsonObject(count);
    }

    public static JSONObject runCompare(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return getJsonObject(0);
        }

        String targetDsName = getJsonFieldString(bizOptJson, "target", bizModel.getModelName());
        Object primaryKey = bizOptJson.get("primaryKey");
        Object analyse = bizOptJson.get("fieldsMap");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        int count = 0;
        if (dataSet != null && dataSet2 != null) {
            DataSet destDS = DataSetOptUtil.compareTabulation(dataSet, dataSet2, pks, ((Map) analyse).entrySet());
            count = destDS.size();
            bizModel.putDataSet(targetDsName, destDS);
        }
        return getJsonObject(count);
    }

    public static JSONObject runSort(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        List<String> orderByFields = StringBaseOpt.objectToStringList(bizOptJson.get("orderBy"));
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSetOptUtil.sortDataSetByFields(dataSet, orderByFields);
        return getJsonObject(dataSet.size());
    }

    public static JSONObject runJoin(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        List<String> pks = StringBaseOpt.objectToStringList(bizOptJson.get("primaryKey"));
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        DataSet destDS = DataSetOptUtil.joinTwoDataSet(dataSet, dataSet2, pks);
        if (destDS != null) {
            bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDS);
            return getJsonObject(destDS.size());
        }
        return getJsonObject(0);
    }

    public static JSONObject runUnion(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        DataSet destDs = DataSetOptUtil.unionTwoDataSet(dataSet, dataSet2);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDs);
        return getJsonObject(destDs.size());
    }

    public static JSONObject runStaticData(BizModel bizModel, JSONObject bizOptJson) {
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDS = BizOptUtils.castObjectToDataSet(ja);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDS);
        return getJsonObject(destDS.size());
    }

    public static JSONObject runFilterExt(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DsName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DsName == null || sour2DsName == null) {
            return getJsonObject(0);
        }
        String targetDsName = getJsonFieldString(bizOptJson, "target", bizModel.getModelName());
        Object primaryKey = bizOptJson.get("primaryKey");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);
        String formula = bizOptJson.getString("filter");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
        int count = 0;
        if (dataSet != null && dataSet2 != null) {
            DataSet destDS = DataSetOptUtil.filterByOtherDataSet(dataSet, dataSet2, pks, formula);
            count = destDS.size();
            bizModel.putDataSet(targetDsName, destDS);
        }
        return getJsonObject(count);
    }

    public static JSONObject runCheckData(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        Object rulesJson = bizOptJson.get("rules");
        int count = 0;
        if (rulesJson instanceof JSONArray) {
            List<CheckRule> rules = ((JSONArray) rulesJson).toJavaList(CheckRule.class);
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            if (dataSet != null) {
                DataSetOptUtil.checkDateSet(dataSet, rules);
                count = dataSet.size();
            }
        }
        return getJsonObject(count);
    }


    public static JSONObject runHttpData(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String httpMethod = getJsonFieldString(bizOptJson, "httpMethod", "post");
        String httpUrl = getJsonFieldString(bizOptJson, "httpUrl", "");
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        AppSession appSession = new AppSession();
        RestfulHttpRequest restfulHttpRequest = new RestfulHttpRequest();
        dataSet.getDataAsList().forEach(data -> {
            switch (httpMethod.toLowerCase()) {
                case "post":
                    RestfulHttpRequest.jsonPost(appSession, httpUrl, data);
                    break;
                case "put":
                    RestfulHttpRequest.jsonPut(appSession, httpUrl, data);
                    break;
                case "delete":
                    restfulHttpRequest.doDelete(appSession, httpUrl, data);
                default:
                    break;
            }
        });
        return getJsonObject(dataSet.size());
    }

}
