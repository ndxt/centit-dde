package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryAndParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.util.Map;

/**
 * @author zhf
 */
public class RunSqlsBizOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public RunSqlsBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String databaseCode = bizOptJson.getString("databaseName");
        String sql = bizOptJson.getString("sql");
        if (StringUtils.isBlank(databaseCode) || StringUtils.isBlank(sql)){
            return ResponseData.makeErrorMessage("数据库或sql不能为空！");
        }
        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
        QueryAndNamedParams qap = QueryUtils.translateQuery(sql, parames);
        QueryAndParams q = QueryAndParams.createFromQueryAndNamedParams(qap);
        //这个连接需要手动关闭吗？
        Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(sourceInfoDao.getDatabaseInfoById((databaseCode)));
        int count = DatabaseAccess.doExecuteSql(conn, q.getQuery(), q.getParams());
        return BuiltInOperation.createResponseSuccessData(count);
    }
}
