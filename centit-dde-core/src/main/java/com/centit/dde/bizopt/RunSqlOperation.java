package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class RunSqlOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public RunSqlOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        JSONArray jsonArray = bizOptJson.getJSONArray("config");
        int count = 0;
        if (jsonArray != null) {
            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                QueryAndNamedParams qap = QueryUtils.translateQuery(jsonObject.getString("sql"), new BizModelJSONTransform(bizModel));
                QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
                String databaseCode = "";
                if (jsonObject.get("databaseName") instanceof String) {
                    databaseCode = jsonObject.getString("databaseName");
                } else {
                    databaseCode = jsonObject.getJSONObject("databaseName").getString("value");
                }
                Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(sourceInfoDao.getDatabaseInfoById((databaseCode)));
                count += DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
            }
        } else {
            String databaseCode = bizOptJson.getString("databaseName");
            String sql = bizOptJson.getString("sql");
            if (StringUtils.isBlank(databaseCode) || StringUtils.isBlank(sql)) {
                return ResponseData.makeErrorMessage(
                    ObjectException.PARAMETER_NOT_CORRECT,
                    dataOptContext.getI18nMessage("dde.614.parameter_not_correct", "sql sentence"));
            }
            Map<String, Object> params = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
            QueryAndNamedParams qap = QueryUtils.translateQuery(sql, params);
            Map<String, Object> paramsMap = new HashMap<>(params == null ? 0 : params.size() + 6);
            if (params != null) {
                paramsMap.putAll(params);
                paramsMap.putAll(qap.getParams());
                qap.setParams(paramsMap);
            }

            QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
            Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(sourceInfoDao.getDatabaseInfoById((databaseCode)));
            count = DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(qap));
        }
        return BuiltInOperation.createResponseSuccessData(count);
    }
}
