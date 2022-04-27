package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.*;
import com.centit.dde.dataset.SqlDataSetReader;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhf
 */
public class DbBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    private DataScopePowerManager queryDataScopeFilter;

    public DbBizOperation(SourceInfoDao sourceInfoDao, DataScopePowerManager queryDataScopeFilter) {
        this.sourceInfoDao = sourceInfoDao;
        this.queryDataScopeFilter = queryDataScopeFilter;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String dataSetId = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String condition = BuiltInOperation.getJsonFieldString(bizOptJson, "condition", "false");
        String sql = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
        // 这个地方可以设置更多的内容


        Map<String, Object> mapObject = new HashMap<>();
        if (mapString != null) {
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), VariableFormula.calculate(map.getValue(), new BizModelJSONTransform(bizModel)));
                }
            }
        }
        //选择数据集作为参数
        if(!StringBaseOpt.isNvl(dataSetId)){
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            if (dataSet != null){
                List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
                dataAsList.stream().forEach(map->{
                    mapObject.putAll(map);
                });
            }
        }
        //只有为自定义参数的时候才put进去
        if(bizOptJson.getString("sourceType") ==null || "customSource".equals(bizOptJson.getString("sourceType"))){
            mapObject.putAll(
                CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        }

        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.createResponseData(0, 0, "找不到对应的集成数据库：" + databaseCode);
        }
        SqlDataSetReader sqlDsr = new SqlDataSetReader();
        sqlDsr.setDataSource(databaseInfo);
        sqlDsr.setSqlSen(sql);
        sqlDsr.setQueryDataScopeFilter(queryDataScopeFilter);
        sqlDsr.setOptId(dataOptContext.getOptId());
        if ("true".equals(condition) ){//&& !StringBaseOpt.isNvl(conditionSet) && bizModel.getDataSet(conditionSet) != null) {
            String conditionSet = BuiltInOperation.getJsonFieldString(bizOptJson, "conditionSet", "");
            DataSet dataSet = bizModel.getDataSet(conditionSet);
            if(dataSet != null){
                sqlDsr.setExtendFilters(dataSet.getFirstRow());
            }
        }
        SimpleDataSet dataSet = sqlDsr.load(mapObject);
        bizModel.putDataSet(id, dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
