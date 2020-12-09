package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.support.database.transaction.ConnectThreadHolder;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhf
 */
public class RunSqlSBizOperation implements BizOperation {
    private DatabaseRunTime databaseRunTime;
    public RunSqlSBizOperation(DatabaseRunTime databaseRunTime){
        this.databaseRunTime=databaseRunTime;
    }

    @Override
    @SuppressWarnings("unchecked")
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        Map<String,String> mapInfo = (Map<String, String>) BuiltInOperation.jsonArrayToMap(bizOptJson.get("config"), "columnName", "expression");
        AtomicInteger count= new AtomicInteger();
        try {
            mapInfo.forEach((k,v)-> {
                QueryAndNamedParams qap = QueryUtils.translateQuery(v, new BizModelJSONTransform(bizModel));
                count.addAndGet(databaseRunTime.execute(k, qap.getQuery()));
                }
            );
            ConnectThreadHolder.commitAndRelease();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BuiltInOperation.getJsonObject(count.get());
    }
}
