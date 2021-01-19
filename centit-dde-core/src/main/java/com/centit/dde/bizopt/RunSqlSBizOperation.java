package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.support.database.transaction.ConnectThreadHolder;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;

import java.sql.SQLException;

/**
 * @author zhf
 */
public class RunSqlSBizOperation implements BizOperation {
    private DatabaseRunTime databaseRunTime;

    public RunSqlSBizOperation(DatabaseRunTime databaseRunTime) {
        this.databaseRunTime = databaseRunTime;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        JSONArray jsonArray = bizOptJson.getJSONArray("config");
        if (jsonArray != null) {
            int count = 0;
            try {
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    QueryAndNamedParams qap = QueryUtils.translateQuery(jsonObject.getString("sql"), new BizModelJSONTransform(bizModel));
                    QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
                    count += databaseRunTime.execute(jsonObject.getString("databaseName"), q.getQuery(), q.getParams());
                }
                ConnectThreadHolder.commitAndRelease();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return BuiltInOperation.getResponseSuccessData(count);
        }
        return BuiltInOperation.getResponseSuccessData(0);
    }
}
