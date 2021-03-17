package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.transaction.AbstractSourceConnectThreadHolder;
import com.centit.dde.utils.Constant;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;
import com.centit.product.metadata.vo.ISourceInfo;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 * 数据库连接信息 DatabaseInfo
 * 对应的表信息 SimpleTableInfo
 *
 * @author zhf
 */
public class SqlDataSetReader implements DataSetReader {

    private static final Logger logger = LoggerFactory.getLogger(SqlDataSetReader.class);
    private ISourceInfo databaseInfo;
    /**
     * 参数驱动sql
     */
    private String sqlSen;

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public SimpleDataSet load(final Map<String, Object> params) throws Exception {
        Connection conn = (Connection) AbstractSourceConnectThreadHolder.fetchConnect(databaseInfo);
        QueryAndNamedParams qap;
        if (params != null && params.get(Constant.CURRENT_USER) != null) {
            DataScopePowerManager queryDataScopeFilter = new DataScopePowerManagerImpl();
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                (JSONObject) (params.get("currentUser")), params.get("currentUnitCode").toString());
            dataPowerFilter.addSourceData(params);
            qap = dataPowerFilter.translateQuery(sqlSen, null);
        } else {
            qap = QueryUtils.translateQuery(sqlSen, params);
        }
        Map<String, Object> paramsMap = new HashMap<>(params == null ? 0 : params.size() + 6);
        if (params != null) {
            paramsMap.putAll(params);
        }
        paramsMap.putAll(qap.getParams());
        JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(
            conn, qap.getQuery(), paramsMap);
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.setData(jsonArray);
        return dataSet;
    }

    public void setDataSource(ISourceInfo databaseInfo) {
        this.databaseInfo = databaseInfo;
    }

    public void setSqlSen(String sqlSen) {
        this.sqlSen = sqlSen;
    }

}
