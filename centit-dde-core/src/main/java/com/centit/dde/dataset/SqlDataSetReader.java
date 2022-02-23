package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.DataSetReader;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.product.adapter.api.ISourceInfo;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.core.service.impl.DataScopePowerManagerImpl;

import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
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

    private String optId;

    private DataScopePowerManager queryDataScopeFilter;

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public SimpleDataSet load(final Map<String, Object> params) throws Exception {
        Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(databaseInfo);
        QueryAndNamedParams qap;
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit, WebOptUtils.getCurrentUserCode(request), optId, "search");
        if (filters != null) {
            DataScopePowerManager queryDataScopeFilter = new DataScopePowerManagerImpl();
            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                WebOptUtils.getCurrentUserInfo(request), topUnit, WebOptUtils.getCurrentUnitCode(request));
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
//        String orderBy = GeneralJsonObjectDao.fetchSelfOrderSql(qap.getQuery(), paramsMap);
//        final String querySql=StringUtils.replace(qap.getQuery(),":ORDER_BY",orderBy);
        JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(conn,qap.getQuery(), paramsMap);
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

    public void setQueryDataScopeFilter(DataScopePowerManager queryDataScopeFilter) {
        this.queryDataScopeFilter = queryDataScopeFilter;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }
}
