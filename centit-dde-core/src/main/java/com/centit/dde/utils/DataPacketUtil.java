package com.centit.dde.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.vo.ColumnSchema;
import com.centit.dde.vo.DataPacketSchema;
import com.centit.dde.vo.DataSetSchema;
import com.centit.product.dataopt.bizopt.BuiltInOperation;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.utils.BizOptUtils;
import com.centit.product.dataopt.utils.DBBatchUtils;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public abstract class DataPacketUtil {
    public static DataPacketSchema calcDataPacketSchema(DataPacketSchema sourceSchema, JSONObject bizOptJson){
        JSONObject object=bizOptJson.getJSONObject("optsteps");
        if (object!=null) {
            bizOptJson = object;
        }
        JSONArray optSteps = bizOptJson.getJSONArray("steps");
        if(optSteps==null || optSteps.isEmpty()){
            return sourceSchema;
        }
        for(Object step : optSteps){
            if(step instanceof JSONObject){
                calcSchemaOneStep(sourceSchema, (JSONObject)step);
            }
        }
        return sourceSchema;
    }

    private static void calcSchemaOneStep(DataPacketSchema sourceSchema, JSONObject bizOptJson){
        String sOptType = bizOptJson.getString("operation");
        if(StringUtils.isBlank(sOptType)) {
            return;
        }
        switch (sOptType){
            case "map":
                calcSchemaMap(sourceSchema, bizOptJson);
                return;
            case "filter":
                calcSchemaFilter(sourceSchema, bizOptJson);
                return;
            case "append":
                calcSchemaAppend(sourceSchema, bizOptJson);
                return;
            case "stat":
                calcSchemaStat(sourceSchema, bizOptJson);
                return;
            case "analyse":
                calcSchemaAnalyse(sourceSchema, bizOptJson);
                return;
            case "cross":
                calcSchemaCross(sourceSchema, bizOptJson);
                return;
            case "compare":
                calcSchemaCompare(sourceSchema, bizOptJson);
                return;
            case "join":
                calcSchemaJoin(sourceSchema, bizOptJson);
                return;
            case "union":
                calcSchemaUnion(sourceSchema, bizOptJson);
                return;
            case "static":
                calcSchemaStatic(sourceSchema, bizOptJson);
                return;
             default:
        }
    }

    private static void calcSchemaMap(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDsName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        if(mapInfo instanceof Map){
            DataSetSchema dss = new DataSetSchema(targetDsName);
            dss.setDataSetTitle(sourDsName+":map");
            for(Object s : ((Map)mapInfo).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
            sourceSchema.putDataSetSchema(dss);
        }
    }

    private static void calcSchemaAppend(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        Object mapInfo = bizOptJson.get("fieldsMap");
        DataSetSchema dss = sourceSchema.fetchDataSetSchema(sourDsName);
        if(dss != null && mapInfo instanceof Map){
            for(Object s : ((Map)mapInfo).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
        }
    }

    private static void calcSchemaFilter(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDsName);
        DataSetSchema dss = sourceSchema.fetchDataSetSchema(sourDsName);
        dss.setDataSetTitle(sourDsName+":filter");
        dss.setDataSetId(targetDsName);
        dss.setDataSetName(targetDsName);
        sourceSchema.putDataSetSchema(dss);
    }

    private static void copySchemaFields(DataSetSchema dist,DataSetSchema source, List<String>  fields){
        if(fields!=null){
            for(String f : fields){
                dist.addColumn(source.fetchColumn(f));
            }
        }
    }

    private static void calcSchemaStat(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDsName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        DataSetSchema sdes = sourceSchema.fetchDataSetSchema(sourDsName);
        DataSetSchema dss = new DataSetSchema(targetDsName);
        copySchemaFields(dss,sdes,groupFields);
        dss.setDataSetTitle(sourDsName+":stat");
        Object stat = bizOptJson.get("fieldsMap");
        if(stat instanceof Map){
            CollectionsOpt.objectToMap(stat).forEach((key, value) -> {
                String[] optDesc = value.toString().split(":");
                if (optDesc.length > 1) {
                    ColumnSchema col = sdes.fetchColumn(optDesc[0]);
                    col = col.duplicate();
                    col.setPropertyName(key);
                    dss.addColumn(col);
                }
            });
        }
        sourceSchema.putDataSetSchema(dss);
    }

    private static void calcSchemaAnalyse(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDsName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sourDsName);
        DataSetSchema dss = new DataSetSchema(targetDsName);
        copySchemaFields(dss,sdss,groupFields);

        Object orderBy = bizOptJson.get("orderBy");
        List<String> orderFields = StringBaseOpt.objectToStringList(orderBy);
        copySchemaFields(dss,sdss,orderFields);

        dss.setDataSetTitle(sourDsName+":analyse");
        Object analyse = bizOptJson.get("fieldsMap");
        if(analyse instanceof Map){
            for(Object s : ((Map)analyse).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
            sourceSchema.putDataSetSchema(dss);
        }
    }

    private static void calcSchemaCross(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDsName);
        Object rowHeader = bizOptJson.get("rowHeader");
        List<String> rows = StringBaseOpt.objectToStringList(rowHeader);
        Object colHeader = bizOptJson.get("colHeader");
        List<String> cols = StringBaseOpt.objectToStringList(colHeader);

        DataSetSchema sDss = sourceSchema.fetchDataSetSchema(sourDsName);
        DataSetSchema dss = new DataSetSchema(targetDsName);
        dss.setDataSetTitle(sourDsName+":cross");
        copySchemaFields(dss,sDss,rows);
        String colName = StringUtils.join(cols,":*:");
        ///copySchemaFields(dss,sdss,cols);
        ColumnSchema colSchema = new ColumnSchema(colName);
        colSchema.setIsStatData(BooleanBaseOpt.ONE_CHAR_TRUE);
        dss.addColumn(colSchema);

        sourceSchema.putDataSetSchema(dss);
    }

    private static void calcSchemaCompare(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sour1DsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", null);
        String sour2DsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source2", null);
        if(sour1DsName == null || sour2DsName ==null ){
            return ;
        }

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        Object primaryKey = bizOptJson.get("primaryKey");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);

        DataSetSchema sDss = sourceSchema.fetchDataSetSchema(sour1DsName);
        DataSetSchema sDss2 = sourceSchema.fetchDataSetSchema(sour2DsName);

        DataSetSchema dss = new DataSetSchema(targetDsName);
        copySchemaFields(dss,sDss,pks);
        dss.setDataSetName(targetDsName);
        dss.setDataSetTitle(sour1DsName+":"+sour2DsName+":compare");
        for(ColumnSchema cs : sDss.getColumns()){
            if(! pks.contains(cs.getPropertyName())){
                ColumnSchema dup = cs.duplicate();
                dup.setPropertyName(cs.getPropertyName()+"_left");
                dss.addColumn(dup);
            }
        }
        for(ColumnSchema cs : sDss2.getColumns()){
            if(! pks.contains(cs.getPropertyName())){
                ColumnSchema dup = cs.duplicate();
                dup.setPropertyName(cs.getPropertyName()+"_right");
                dss.addColumn(dup);
            }
        }
        sourceSchema.putDataSetSchema(dss);
    }

    private static void mergeTwoSchemaJoin(DataPacketSchema sourceSchema, JSONObject bizOptJson, String prefix) {
        String sour1DsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", null);
        String sour2DsName = BuiltInOperation.getJsonFieldString(bizOptJson,"source2", null);
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        DataSetSchema sDss = sourceSchema.fetchDataSetSchema(sour1DsName);
        DataSetSchema sDss2 = sourceSchema.fetchDataSetSchema(sour2DsName);

        DataSetSchema dss = new DataSetSchema(targetDsName);
        dss.setDataSetName(targetDsName);
        dss.setDataSetTitle(sour1DsName+":"+sour2DsName+ prefix);
        for(ColumnSchema cs : sDss.getColumns()){
            ColumnSchema dup = cs.duplicate();
            dup.setPropertyName(cs.getPropertyName());
            dss.addColumn(dup);
        }
        for(ColumnSchema cs : sDss2.getColumns()){
            ColumnSchema dup = cs.duplicate();
            dup.setPropertyName(cs.getPropertyName());
            if(!dss.existColumn(cs.getPropertyName())) {
                dss.addColumn(dup);
            }
        }
        sourceSchema.putDataSetSchema(dss);
    }

    private static void calcSchemaJoin(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        mergeTwoSchemaJoin(sourceSchema, bizOptJson, ":join");
    }

    private static void calcSchemaUnion(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        mergeTwoSchemaJoin(sourceSchema, bizOptJson, ":union");
    }

    private static void calcSchemaStatic(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDs = BizOptUtils.castObjectToDataSet(ja);
        List<String> fields = DBBatchUtils.achieveAllFields(destDs.getData());
        DataSetSchema dss = new DataSetSchema(targetDsName);

        for(String s : fields){
            dss.addColumn(new ColumnSchema(s));
        }
        sourceSchema.putDataSetSchema(dss);
    }

}
