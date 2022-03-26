package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;

import java.sql.Connection;

/**
 * @author zhf
 */
public class RunSqlsBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public RunSqlsBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
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
                String databaseCode="";
                if (jsonObject.get("databaseName") instanceof String){
                    databaseCode=jsonObject.getString("databaseName");
                }else {
                    databaseCode = jsonObject.getJSONObject("databaseName").getString("value");
                }
                Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(sourceInfoDao.getDatabaseInfoById((databaseCode)));
                count += DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
            }
            return BuiltInOperation.getResponseSuccessData(count);
        }
        return BuiltInOperation.getResponseSuccessData(0);
    }
}
