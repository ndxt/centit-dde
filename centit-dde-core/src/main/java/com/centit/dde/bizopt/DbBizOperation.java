package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.SqlDataSetReader;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.algorithm.StringBaseOpt;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zhf
 */
public class DbBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public DbBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String sql = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
        Map<String, Object> mapObject = new HashMap<>();
        if (mapString != null) {
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), map.getValue());
                }
            }
        }
        mapObject.putAll(bizModel.getModelTag());
        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.getResponseData(0, 0, "找不到对应的集成数据库：" + databaseCode);
        }
        SqlDataSetReader sqlDsr = new SqlDataSetReader();
        sqlDsr.setDataSource(databaseInfo);
        sqlDsr.setSqlSen(sql);
        SimpleDataSet dataSet = sqlDsr.load(mapObject);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.size());
    }
}
