package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.SQLDataSetReader;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.DatabaseInfoDao;
import com.centit.product.metadata.po.DatabaseInfo;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.DataSourceDescription;

import java.util.HashMap;
import java.util.Map;


/**
 * @author zhf
 */
public class DBBizOperation implements BizOperation {
    private DatabaseInfoDao databaseInfoDao;

    public DBBizOperation(DatabaseInfoDao databaseInfoDao) {
        this.databaseInfoDao = databaseInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String sql = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        Map<String, String> mapString=BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"),"key","value");
        Map<String,Object> mapObject=new HashMap<>();
        if(mapString!=null) {
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    mapObject.put(map.getKey(), map.getValue());
                }
            }
        }
        mapObject.putAll(bizModel.getModelTag());
        DatabaseInfo databaseInfo = databaseInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.getResponseData(0,0,"找不到对应的集成数据库：" + databaseCode);
        }
        SQLDataSetReader sqlDsr = new SQLDataSetReader();
        sqlDsr.setDataSource(DataSourceDescription.valueOf(databaseInfo));
        sqlDsr.setSqlSen(sql);
        SimpleDataSet dataSet = sqlDsr.load(mapObject);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getResponseSuccessData(dataSet.size());
    }
}
