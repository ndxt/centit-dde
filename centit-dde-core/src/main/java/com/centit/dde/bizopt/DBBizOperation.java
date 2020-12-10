package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.SQLDataSetReader;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.DataSourceDescription;


/**
 * @author zhf
 */
public class DBBizOperation implements BizOperation {
    private IntegrationEnvironment integrationEnvironment;

    public DBBizOperation(IntegrationEnvironment integrationEnvironment) {
        this.integrationEnvironment = integrationEnvironment;
    }

    @Override
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String sql = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
        if (databaseInfo == null) {
            throw new ObjectException("找不到对应的集成数据库：" + databaseCode);
        }
        SQLDataSetReader sqlDsr = new SQLDataSetReader();
        sqlDsr.setDataSource(DataSourceDescription.valueOf(databaseInfo));
        sqlDsr.setSqlSen(sql);
        SimpleDataSet dataSet = sqlDsr.load(bizModel.getModelTag());
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getJsonObject(dataSet.size());
    }
}