package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.transaction.AbstractSourceConnectThreadHolder;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.DatabaseInfoDao;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.support.database.transaction.ConnectThreadHolder;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhf
 */
public class RunSqlSBizOperation implements BizOperation {
    private DatabaseInfoDao databaseInfoDao;
    public RunSqlSBizOperation(DatabaseInfoDao databaseInfoDao){
        this.databaseInfoDao=databaseInfoDao;
    }
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        JSONArray jsonArray = bizOptJson.getJSONArray("config");
        if (jsonArray != null) {
            int count = 0;
                for (Object object : jsonArray) {
                    JSONObject jsonObject = (JSONObject) object;
                    QueryAndNamedParams qap = QueryUtils.translateQuery(jsonObject.getString("sql"), new BizModelJSONTransform(bizModel));
                    QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
                    Connection conn = (Connection) AbstractSourceConnectThreadHolder.fetchConnect(databaseInfoDao.getDatabaseInfoById((jsonObject.getString("databaseName"))));
                    count += DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
                }
            return BuiltInOperation.getResponseSuccessData(count);
        }
        return BuiltInOperation.getResponseSuccessData(0);
    }
}
