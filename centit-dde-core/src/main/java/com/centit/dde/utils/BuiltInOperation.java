package com.centit.dde.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.datarule.CheckRule;
import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.RestfulHttpRequest;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 数据持久化操作
 *
 * @author zhf
 */
public abstract class BuiltInOperation  {

    public static String getJsonFieldString(JSONObject bizOptJson, String fieldName, String defaultValue) {
        String targetDsName = bizOptJson.getString(fieldName);
        if (StringUtils.isBlank(targetDsName)) {
            return defaultValue;
        }
        return targetDsName;
    }

    public static void runMap(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        if (mapInfo instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                DataSet destDS = DataSetOptUtil.mapDateSetByFormula(dataSet, ((Map) mapInfo).entrySet());
                //if(destDS != null){
                bizModel.putDataSet(targetDSName, destDS);
                //}
            }
        }
        //return bizModel;
    }

    public static void runAppend(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        //String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        if (mapInfo instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                /*DataSet destDS = */
                DataSetOptUtil.appendDeriveField(dataSet, ((Map) mapInfo).entrySet());
                //bizModel.addDataSet(targetDSName, destDS);
            }
        }
        //return bizModel;
    }

    public static void runFilter(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        String formula = bizOptJson.getString("filter");
        if (StringUtils.isNotBlank(formula)) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                DataSet destDS = DataSetOptUtil.filterDateSet(dataSet, formula);
                bizModel.putDataSet(targetDSName, destDS);
            }
        }
        //return bizModel;
    }

    public static void runStat(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        Object stat = bizOptJson.get("fieldsMap");
        if (stat instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                DataSet destDS = DataSetOptUtil.statDataset2(dataSet, groupFields, (Map) stat);
                bizModel.putDataSet(targetDSName, destDS);
            }
        }
        //return bizModel;
    }

    public static void runAnalyse(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);

        Object orderBy = bizOptJson.get("orderBy");
        List<String> orderFields = StringBaseOpt.objectToStringList(orderBy);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);

        Object analyse = bizOptJson.get("fieldsMap");
        if (analyse instanceof Map) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                DataSet destDS = DataSetOptUtil.analyseDataset(dataSet,
                    groupFields, orderFields, ((Map) analyse).entrySet());
                bizModel.putDataSet(targetDSName, destDS);
            }
        }
        //return bizModel;
    }

    public static void runCross(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        Object rowHeader = bizOptJson.get("rowHeader");
        List<String> rows = StringBaseOpt.objectToStringList(rowHeader);
        Object colHeader = bizOptJson.get("colHeader");
        List<String> cols = StringBaseOpt.objectToStringList(colHeader);

        DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
        if (dataSet != null) {
            DataSet destDS = DataSetOptUtil.crossTabulation(dataSet, rows, cols);
            bizModel.putDataSet(targetDSName, destDS);
        }
        //return bizModel;
    }

    public static void runCompare(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DSName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DSName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DSName == null || sour2DSName == null) {
            return ;//bizModel;
        }

        String targetDSName = getJsonFieldString(bizOptJson, "target", bizModel.getModelName());
        Object primaryKey = bizOptJson.get("primaryKey");
        Object analyse = bizOptJson.get("fieldsMap");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DSName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DSName);
        if (dataSet != null && dataSet2 != null) {
            DataSet destDS = DataSetOptUtil.compareTabulation(dataSet, dataSet2, pks, ((Map) analyse).entrySet());
            bizModel.putDataSet(targetDSName, destDS);
        }
        //return bizModel;
    }

    public static void runSort(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DSName = getJsonFieldString(bizOptJson, "source", null);
        List<String> orderByFields = StringBaseOpt.objectToStringList(bizOptJson.get("orderBy"));
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DSName);
        /*DataSet destDS = */
        DataSetOptUtil.sortDataSetByFields(dataSet, orderByFields);
        //return bizModel;
    }

    public static void runJoin(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DSName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DSName = getJsonFieldString(bizOptJson, "source2", null);
        List<String> pks = StringBaseOpt.objectToStringList(bizOptJson.get("primaryKey"));
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DSName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DSName);
        DataSet destDS = DataSetOptUtil.joinTwoDataSet(dataSet, dataSet2, pks);

        if (destDS != null) {
            bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDS);
        }
        //return bizModel;
    }

    public static void runUnion(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DSName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DSName = getJsonFieldString(bizOptJson, "source2", null);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DSName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DSName);
        DataSet destDS = DataSetOptUtil.unionTwoDataSet(dataSet, dataSet2);
        if (destDS != null) {
            bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDS);
        }
        //return bizModel;
    }

    public static void runStaticData(BizModel bizModel, JSONObject bizOptJson) {
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDS = BizOptUtils.castObjectToDataSet(ja);
        bizModel.putDataSet(getJsonFieldString(bizOptJson, "target", bizModel.getModelName()), destDS);
        //return bizModel;
    }

    public static void runFilterExt(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DSName = getJsonFieldString(bizOptJson, "source", null);
        String sour2DSName = getJsonFieldString(bizOptJson, "source2", null);
        if (sour1DSName == null || sour2DSName == null) {
            return ;//bizModel;
        }
        String targetDSName = getJsonFieldString(bizOptJson, "target", bizModel.getModelName());
        Object primaryKey = bizOptJson.get("primaryKey");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);
        String formula = bizOptJson.getString("filter");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DSName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DSName);
        if (dataSet != null && dataSet2 != null) {
            DataSet destDS = DataSetOptUtil.filterByOtherDataSet(dataSet, dataSet2, pks, formula);
            bizModel.putDataSet(targetDSName, destDS);
        }
        //return bizModel;
    }

    public static void runCheckData(BizModel bizModel, JSONObject bizOptJson) {
        String sourDSName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        //String targetDSName = getJsonFieldString(bizOptJson, "target", sourDSName);
        Object rulesJson = bizOptJson.get("rules");
        if (rulesJson instanceof JSONArray) {
            List<CheckRule> rules = ((JSONArray) rulesJson).toJavaList(CheckRule.class);
            DataSet dataSet = bizModel.fetchDataSetByName(sourDSName);
            if (dataSet != null) {
                DataSetOptUtil.checkDateSet(dataSet, rules);
            }
        }
        //return bizModel;
    }

   /* protected BizModel runOneStep(BizModel bizModel, JSONObject bizOptJson) {
        String sOptType = bizOptJson.getString("operation");
        if (StringUtils.isBlank(sOptType)) {
            return bizModel;
        }
        switch (sOptType) {
            case "map":
                return runMap(bizModel, bizOptJson);
            case "filter":
                return runFilter(bizModel, bizOptJson);
            case "append":
                return runAppend(bizModel, bizOptJson);
            case "stat":
                return runStat(bizModel, bizOptJson);
            case "analyse":
                return runAnalyse(bizModel, bizOptJson);
            case "cross":
                return runCross(bizModel, bizOptJson);
            case "compare":
                return runCompare(bizModel, bizOptJson);
            case "sort":
                return runSort(bizModel, bizOptJson);
            case "join":
                return runJoin(bizModel, bizOptJson);
            case "union":
                return runUnion(bizModel, bizOptJson);
            case "filterExt":
                return runFilterExt(bizModel, bizOptJson);
            case "check":
                return runCheckData(bizModel, bizOptJson);
            case "static":
                return runStaticData(bizModel, bizOptJson);
            case "js":
                return runJsData(bizModel, bizOptJson);
            case "http":
                return runHttpData(bizModel, bizOptJson);
            *//*case "persistence":
                return runPersistence(bizModel, bizOptJson);*//*
            default:
                return bizModel;
        }
    }*/

    public static void runHttpData(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String httpMethod = getJsonFieldString(bizOptJson, "httpMethod", "post");
        String httpUrl = getJsonFieldString(bizOptJson, "httpUrl", "");
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        AppSession appSession = new AppSession();
        RestfulHttpRequest restfulHttpRequest = new RestfulHttpRequest();
        dataSet.getData().forEach(data -> {
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
        //return bizModel;
    }


}
