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
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
public class RunSqlOperation implements BizOperation {
    private SourceInfoMetadata sourceInfoMetadata;

    public RunSqlOperation(SourceInfoMetadata sourceInfoMetadata) {
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        JSONArray jsonArray = bizOptJson.getJSONArray("config");
        int count = 0;
        if (jsonArray != null) { // 这个批量sql的已经废弃
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
                Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(sourceInfoMetadata.fetchSourceInfo((databaseCode)));
                if(count==0) {
                    bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(q));
                }
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
            List<Map<String, Object>> optData;
            if(!"customSource".equals(bizOptJson.getString("sourceType"))){ // 数据集
                //考虑批量操作
                optData = DataSetOptUtil.fetchDataSet(bizModel, bizOptJson);
                if(optData==null)
                    throw new ObjectException(ObjectException.PARAMETER_NOT_CORRECT,
                        dataOptContext.getI18nMessage("dde.614.parameter_not_correct", "source data"));
            } else { //自定义参数
                // 获取单个参数
                Map<String, Object> params = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
                optData = new ArrayList<>(2);
                optData.add(params);
            }
            Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(
                sourceInfoMetadata.fetchSourceInfo((databaseCode)));

            for(Map<String, Object> params : optData) {
                QueryAndNamedParams qap = QueryUtils.translateQuery(sql, params);
                Map<String, Object> paramsMap = new HashMap<>(params == null ? 0 : params.size() + 6);
                if (params != null) {
                    paramsMap.putAll(params);
                    paramsMap.putAll(qap.getParams());
                    qap.setParams(paramsMap);
                }

                QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
                if(count==0) {
                    bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(qap));
                }
                count += DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
            }
        }
        return BuiltInOperation.createResponseSuccessData(count);
    }
}
