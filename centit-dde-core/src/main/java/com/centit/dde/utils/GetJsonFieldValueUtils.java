package com.centit.dde.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSet;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 获取json串中字段的值   格式：A.B.C.D   获取字段D的值
 */
public class GetJsonFieldValueUtils {

    public static JSONArray getJsonFieldValue(String fields, DataSet dataSet){
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        JSONArray jsonArrayData = JSONArray.parseArray(JSON.toJSONString(dataAsList));
        String[] fieldArr = fields.split("\\.");
        JSONArray jsonArray = getValByKey(jsonArrayData, fieldArr[fieldArr.length - 1]);
        return jsonArray;
    }
    /**
     * 通过KEY查询VALUE
     * @param obj
     * @param fieldName
     * @return
     */
    private static JSONArray getValByKey(Object obj, String fieldName) {
        JSONArray jsonArray = new JSONArray();
        getValForObj(obj, fieldName, jsonArray);
        return jsonArray;
    }

    /**
     * 查询值
     * @param obj
     * @param fieldName
     * @param jsonArray
     */
    private static void getValForObj(Object obj, String fieldName, JSONArray jsonArray) {
        if(obj instanceof JSONObject) {
            getValForJObj((JSONObject)obj, fieldName, jsonArray);
        }
        if(obj instanceof JSONArray) {
            getValForJArr((JSONArray)obj, fieldName, jsonArray);
        }
    }

    /**
     * 处理JsonObject 对象查值
     * @param jobj
     * @param fieldName
     * @param jsonArray
     */
    private static void getValForJObj(JSONObject jobj, String fieldName, JSONArray jsonArray) {
        for(Map.Entry<String, Object> entry : jobj.entrySet()) {
            if(StringUtils.equals(entry.getKey(), fieldName)) {
               if (entry.getValue() instanceof JSONArray){
                   JSONArray arrayData = ((JSONArray) entry.getValue());
                   jsonArray.addAll(arrayData);
               }else {
                   jsonArray.add(entry);
               }
            }
            getValForObj(entry.getValue(), fieldName, jsonArray);
        }
    }

    /**
     * 处理JsonAarray 对象查值
     * @param arr
     * @param fieldName
     * @param jsonArray
     */
    private static void getValForJArr(JSONArray arr, String fieldName, JSONArray jsonArray) {
        Iterator<Object> iner = arr.iterator();
        while(iner.hasNext()) {
            getValForObj(iner.next(), fieldName, jsonArray);
        }
    }
}
