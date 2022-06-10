package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IUserUnit;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 数据持久化操作
 *
 * @author zhf
 */
@SuppressWarnings("unchecked")
public abstract class BuiltInOperation {

    public static String getJsonFieldString(JSONObject bizOptJson, String fieldName, String defaultValue) {
        String targetDsName = bizOptJson.getString(fieldName);
        if (StringUtils.isBlank(targetDsName)) {
            return defaultValue;
        }
        return targetDsName;
    }

    public static ResponseData createResponseSuccessData(int count) {
        JSONObject map = new JSONObject();
        map.put("info", "ok");
        map.put("success", count);
        map.put("error", 0);
        return ResponseSingleData.makeResponseData(map);
    }

    public static ResponseData createResponseData(int success, int error, int errorCode, String info) {
        JSONObject map = new JSONObject();
        map.put("success", success);
        map.put("error", error);
        ResponseSingleData result = ResponseSingleData.makeResponseData(map);
        result.setCode(errorCode);
        result.setMessage(info);
        return result;
    }

    public static Map<String, String> jsonArrayToMap(JSONArray json, String key, String value) {
        if (json != null) {
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (Object o : json) {
                JSONObject temp = (JSONObject) o;
                if (!StringBaseOpt.isNvl(temp.getString(key))) {
                    map.put(temp.getString(key), temp.getString(value));
                }
            }
            return map;
        }
        return Collections.EMPTY_MAP;
    }

    public static Map<String, Object> makeCalcParam(Object ud) {
        Map<String, Object> dpf = new HashMap<>(10);
        if (ud == null) {
            return dpf;
        }
        JSONObject userInfo = null;
        CentitUserDetails userDetails = null;
        String userCode = null;
        String topUnit = null;
        if (ud instanceof CentitUserDetails) {
            userDetails = (CentitUserDetails) ud;
            userInfo = userDetails.getUserInfo();
            userCode = userDetails.getUserCode();
            topUnit = userDetails.getTopUnitCode();
        }
        //当前用户信息
        dpf.put("currentUser", userInfo);
        if (userDetails != null) {
            dpf.put("currentStation", userDetails.getCurrentStation());
            //当前用户主机构信息
            dpf.put("primaryUnit", CodeRepositoryUtil
                .getUnitInfoByCode(userDetails.getUserInfo().getString("topUnit"),
                    userDetails.getUserInfo().getString("primaryUnit")));
            //当前用户的角色信息
            dpf.put("userRoles", userDetails.getUserRoles());
        }
        //当前用户所有机构关联关系信息
        if (StringUtils.isNotBlank(userCode)) {
            List<? extends IUserUnit> userUnits = CodeRepositoryUtil
                .listUserUnits(topUnit, userCode);
            if (userUnits != null) {
                dpf.put("userUnits", userUnits);
                Map<String, List<IUserUnit>> rankUnits = new HashMap<>(5);
                Map<String, List<IUserUnit>> stationUnits = new HashMap<>(5);
                for (IUserUnit uu : userUnits) {
                    List<IUserUnit> rankUnit = rankUnits.get(uu.getUserRank());
                    if (rankUnit == null) {
                        rankUnit = new ArrayList<>(4);
                    }
                    rankUnit.add(uu);
                    rankUnits.put(uu.getUserRank(), rankUnit);

                    List<IUserUnit> stationUnit = stationUnits.get(uu.getUserStation());
                    if (stationUnit == null) {
                        stationUnit = new ArrayList<>(4);
                    }
                    stationUnit.add(uu);
                    stationUnits.put(uu.getUserStation(), rankUnit);
                }
                dpf.put("rankUnits", rankUnits);
                dpf.put("stationUnits", stationUnits);
            }
        }
        return dpf;
    }

    public static ResponseData runStart() {
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
        if (destDs != null) {
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
        List<String> formula = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("configfield"),
            (a) -> ((JSONObject) a).getString("paramValidateRegex"), true);

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
        List<String> groupFields = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("exconfig"),
            (a) -> ((JSONObject) a).getString("columnName"), true);
        List<Triple<String, String, String>> statDesc = CollectionsOpt.mapCollectionToList(
            bizOptJson.getJSONArray("config"),
            (a) -> new MutableTriple<>(((JSONObject) a).getString("columnName"),
                ((JSONObject) a).getString("cName"),
                ((JSONObject) a).getString("statType")));

        int count = 0;
        if (statDesc != null) {
            DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
            DataSet destDs = DataSetOptUtil.statDataset(dataSet, groupFields, statDesc);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runAnalyse(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = getJsonFieldString(bizOptJson, "id", sourDsName);
        List<String> orderFields = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("sortconfig"),
            (a) -> ((JSONObject) a).getString("sortName"), true);

        List<String> groupFields = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("exconfig"),
            (a) -> ((JSONObject) a).getString("groupName"), true);

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
        List<String> rows = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("RowField"),
            (a) -> ((JSONObject) a).getString("primaryKey1"), true);

        List<String> cols = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("ColumnsField"),
            (a) -> ((JSONObject) a).getString("primaryKey2"), true);

        //是否保留旧字段
        Boolean isOldField = BooleanBaseOpt.castObjectToBoolean(bizOptJson.getBoolean("isOldField"), true);
        //连接符
        String concatStr = StringBaseOpt.castObjectToString(bizOptJson.getString("concatStr"), ":");
        //统计类型
        int statisticalType = NumberBaseOpt.castObjectToInteger(bizOptJson.getIntValue("statisticalType"), 0);
        int count = 0;
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet != null) {
            DataSet destDs = DataSetOptUtil.crossTabulation(dataSet, rows, cols, isOldField, concatStr, statisticalType);
            count = destDs.getSize();
            bizModel.putDataSet(targetDsName, destDs);
        }
        return createResponseSuccessData(count);
    }

    public static ResponseData runSort(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        List<String> orderByFields = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("config"),
            (a) -> {
                JSONObject obj = (JSONObject) a;
                String fieldName = obj.getString("columnName");
                if (StringUtils.isBlank(fieldName)) {
                    return null;
                }
                String orderBy = obj.getString("orderBy");
                if ("desc".equalsIgnoreCase(orderBy)) {
                    fieldName = fieldName + " desc";
                }
                return fieldName;
            }, true);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSetOptUtil.sortDataSetByFields(dataSet, orderByFields);
        return createResponseSuccessData(dataSet.getSize());
    }

    public static ResponseData runSortAsTree(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String parentExpress = getJsonFieldString(bizOptJson, "parentExpress", null);
        String childExpress = getJsonFieldString(bizOptJson, "childExpress", null);
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSetOptUtil.sortDataSetAsTree(dataSet, parentExpress, childExpress);
        return createResponseSuccessData(dataSet.getSize());
    }

    public static ResponseData runToTree(BizModel bizModel, JSONObject bizOptJson) {
        String sour1DsName = getJsonFieldString(bizOptJson, "source", null);
        String targetDsName = getJsonFieldString(bizOptJson, "id", sour1DsName);
        String parentExpress = getJsonFieldString(bizOptJson, "parentExpress", null);
        String childExpress = getJsonFieldString(bizOptJson, "childExpress", null);
        String childrenFiled = getJsonFieldString(bizOptJson, "childrenFiled", "children");
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet destDs = DataSetOptUtil.toTreeDataSet(dataSet, parentExpress, childExpress, childrenFiled);
        bizModel.putDataSet(targetDsName, destDs);
        return createResponseSuccessData(destDs.getSize());
    }


    public static ResponseData runClear(BizModel bizModel, JSONObject bizOptJson) {
        List<String> sets = CollectionsOpt.mapCollectionToList(bizOptJson.getJSONArray("config"),
            (a) -> ((JSONObject) a).getString("paramValidateRegex"), true);

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
            DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
            DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
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
        DataSet dataSet = bizModel.fetchDataSetByName(sour1DsName);
        DataSet dataSet2 = bizModel.fetchDataSetByName(sour2DsName);
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

}
