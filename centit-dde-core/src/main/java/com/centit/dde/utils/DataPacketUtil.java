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
import com.centit.support.algorithm.StringBaseOpt;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public abstract class DataPacketUtil {
    public static DataPacketSchema calcDataPacketSchema(DataPacketSchema sourceSchema, JSONObject bizOptJson){
        JSONArray optSteps = bizOptJson.getJSONArray("steps");
        if(optSteps==null || optSteps.isEmpty()){
            return sourceSchema;
        }
        DataPacketSchema result = sourceSchema;
        for(Object step : optSteps){
            if(step instanceof JSONObject){
                calcSchemaOneStep(result, (JSONObject)step);
            }
        }
        return result;
    }

    public static DataPacketSchema  calcSchemaOneStep(DataPacketSchema sourceSchema, JSONObject bizOptJson){
        String sOptType = bizOptJson.getString("operation");
        if(StringUtils.isBlank(sOptType)) {
            return sourceSchema;
        }
        switch (sOptType){
            case "map":
                return calcSchemaMap(sourceSchema, bizOptJson);
            case "filter":
                return calcSchemaFilter(sourceSchema, bizOptJson);
            case "append":
                return calcSchemaAppend(sourceSchema, bizOptJson);
            case "stat":
                return calcSchemaStat(sourceSchema, bizOptJson);
            case "analyse":
                return calcSchemaAnalyse(sourceSchema, bizOptJson);
            case "cross":
                return calcSchemaCross(sourceSchema, bizOptJson);
            case "compare":
                return calcSchemaCompare(sourceSchema, bizOptJson);
            case "join":
                return calcSchemaJoin(sourceSchema, bizOptJson);
            case "union":
                return calcSchemaUnion(sourceSchema, bizOptJson);
            case "static":
                return calcSchemaStatic(sourceSchema, bizOptJson);
             default:
                return sourceSchema;
        }
    }

    public static DataPacketSchema calcSchemaMap(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        if(mapInfo instanceof Map){
            DataSetSchema dss = new DataSetSchema(targetDSName);
            dss.setDataSetTitle(sourDSName+":map");
            for(Object s : ((Map)mapInfo).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
            sourceSchema.putDataSetSchema(dss);
        }
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaAppend(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        //String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        Object mapInfo = bizOptJson.get("fieldsMap");
        DataSetSchema dss = sourceSchema.fetchDataSetSchema(sourDSName);
        if(dss != null && mapInfo instanceof Map){
            for(Object s : ((Map)mapInfo).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
        }
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaFilter(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        DataSetSchema dss = sourceSchema.fetchDataSetSchema(sourDSName);
        dss.setDataSetTitle(sourDSName+":filter");
        dss.setDataSetId(targetDSName);
        dss.setDataSetName(targetDSName);
        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

    private static void copySchemaFields(DataSetSchema dist,DataSetSchema source, List<String>  fields){
        if(fields!=null){
            for(String f : fields){
                dist.addColumn(source.fetchColumn(f));
            }
        }
    }

    public static DataPacketSchema calcSchemaStat(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sourDSName);
        DataSetSchema dss = new DataSetSchema(targetDSName);
        copySchemaFields(dss,sdss,groupFields);
        dss.setDataSetTitle(sourDSName+":stat");
        Object stat = bizOptJson.get("fieldsMap");
        if(stat instanceof Map){
            for(Map.Entry<String, String> ent : ((Map<String,String>)stat).entrySet()) {
                String [] optDesc = ent.getValue().split(":");
                if(optDesc != null && optDesc.length>1) {
                    ColumnSchema col = sdss.fetchColumn(optDesc[0]);
                    col = col.duplicate();
                    col.setPropertyName(ent.getKey());
                    dss.addColumn(col);
                }
            }
        }
        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaAnalyse(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        Object groupBy = bizOptJson.get("groupBy");
        List<String> groupFields = StringBaseOpt.objectToStringList(groupBy);
        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sourDSName);
        DataSetSchema dss = new DataSetSchema(targetDSName);
        copySchemaFields(dss,sdss,groupFields);

        Object orderBy = bizOptJson.get("orderBy");
        List<String> orderFields = StringBaseOpt.objectToStringList(orderBy);
        copySchemaFields(dss,sdss,orderFields);

        dss.setDataSetTitle(sourDSName+":analyse");
        Object analyse = bizOptJson.get("fieldsMap");
        if(analyse instanceof Map){
            for(Object s : ((Map)analyse).keySet()){
                dss.addColumnIfNotExist(StringBaseOpt.castObjectToString(s));
            }
            sourceSchema.putDataSetSchema(dss);
        }
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaCross(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sourDSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", sourceSchema.getPacketName());
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourDSName);
        Object rowHeader = bizOptJson.get("rowHeader");
        List<String> rows = StringBaseOpt.objectToStringList(rowHeader);
        Object colHeader = bizOptJson.get("colHeader");
        List<String> cols = StringBaseOpt.objectToStringList(colHeader);

        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sourDSName);
        DataSetSchema dss = new DataSetSchema(targetDSName);
        dss.setDataSetTitle(sourDSName+":cross");
        copySchemaFields(dss,sdss,rows);
        String colName = StringUtils.join(cols,":*:");
        //copySchemaFields(dss,sdss,cols);
        ColumnSchema colSchema = new ColumnSchema(colName);
        colSchema.setIsStatData(BooleanBaseOpt.ONE_CHAR_TRUE);
        dss.addColumn(colSchema);

        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaCompare(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String sour1DSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", null);
        String sour2DSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source2", null);
        if(sour1DSName == null || sour2DSName ==null ){
            return sourceSchema;
        }

        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        Object primaryKey = bizOptJson.get("primaryKey");
        List<String> pks = StringBaseOpt.objectToStringList(primaryKey);

        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sour1DSName);
        DataSetSchema sdss2 = sourceSchema.fetchDataSetSchema(sour2DSName);

        DataSetSchema dss = new DataSetSchema(targetDSName);
        copySchemaFields(dss,sdss,pks);
        dss.setDataSetName(targetDSName);
        dss.setDataSetTitle(sour1DSName+":"+sour2DSName+":compare");
        for(ColumnSchema cs : sdss.getColumns()){
            if(! pks.contains(cs.getPropertyName())){
                ColumnSchema dup = cs.duplicate();
                dup.setPropertyName(cs.getPropertyName()+"_left");
                dss.addColumn(dup);
            }
        }
        for(ColumnSchema cs : sdss2.getColumns()){
            if(! pks.contains(cs.getPropertyName())){
                ColumnSchema dup = cs.duplicate();
                dup.setPropertyName(cs.getPropertyName()+"_right");
                dss.addColumn(dup);
            }
        }
        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

    private static DataPacketSchema mergeTwoSchemaJoin(DataPacketSchema sourceSchema, JSONObject bizOptJson, String prefix) {
        String sour1DSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source", null);
        String sour2DSName = BuiltInOperation.getJsonFieldString(bizOptJson,"source2", null);
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        DataSetSchema sdss = sourceSchema.fetchDataSetSchema(sour1DSName);
        DataSetSchema sdss2 = sourceSchema.fetchDataSetSchema(sour2DSName);

        DataSetSchema dss = new DataSetSchema(targetDSName);
        dss.setDataSetName(targetDSName);
        dss.setDataSetTitle(sour1DSName+":"+sour2DSName+ prefix);
        for(ColumnSchema cs : sdss.getColumns()){
            ColumnSchema dup = cs.duplicate();
            dup.setPropertyName(cs.getPropertyName());
            dss.addColumn(dup);
        }
        for(ColumnSchema cs : sdss2.getColumns()){
            ColumnSchema dup = cs.duplicate();
            dup.setPropertyName(cs.getPropertyName());
            if(!dss.existColumn(cs.getPropertyName())) {
                dss.addColumn(dup);
            }
        }
        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

    public static DataPacketSchema calcSchemaJoin(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        return mergeTwoSchemaJoin(sourceSchema, bizOptJson, ":join");
    }

    public static DataPacketSchema calcSchemaUnion(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        return mergeTwoSchemaJoin(sourceSchema, bizOptJson, ":union");
    }

    public static DataPacketSchema calcSchemaStatic(DataPacketSchema sourceSchema, JSONObject bizOptJson) {
        String targetDSName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", sourceSchema.getPacketName());
        JSONArray ja = bizOptJson.getJSONArray("data");
        DataSet destDS = BizOptUtils.castObjectToDataSet(ja);
        List<String> fields = DBBatchUtils.achieveAllFields(destDS.getData());
        DataSetSchema dss = new DataSetSchema(targetDSName);

        for(String s : fields){
            dss.addColumn(new ColumnSchema(s));
        }
        sourceSchema.putDataSetSchema(dss);
        return sourceSchema;
    }

}
