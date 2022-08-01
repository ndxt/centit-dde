package com.centit.dde.dataset;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.product.adapter.api.ISourceInfo;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.utils.DatabaseAccess;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @author codefan@sina.com
 */
public class SqlDataSetReader implements DataSetReader {
    public static final Log log = LogFactory.getLog(SqlDataSetReader.class);
    private ISourceInfo databaseInfo;
    /**
     * 参数驱动sql
     */
    private String sqlSen;

    private String optId;
    private Map<String, Object> extendFilters;
    private boolean paging;
    //是否返回总数
    private boolean hasCount;

    //分页参数属性
    //页码参数属性
    private String pageNo;
    //每页数量属性
    private String pageSize;

    private DataScopePowerManager queryDataScopeFilter;

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(final Map<String, Object> params) throws Exception {
        buildExtendsSql();
        if (extendFilters!=null){
            params.putAll(extendFilters);
        }
        Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(databaseInfo);
        HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
        QueryAndNamedParams qap = QueryUtils.translateQuery(sqlSen, params);
        if (request != null) {
            String topUnit = WebOptUtils.getCurrentTopUnit(request);
            String userCode = WebOptUtils.getCurrentUserCode(request);
            if (StringUtils.isNotBlank(userCode)) {
                List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit, userCode, optId, "api");
                if (filters != null) {
                    DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                        WebOptUtils.getCurrentUserInfo(request), topUnit, WebOptUtils.getCurrentUnitCode(request));
                    dataPowerFilter.addSourceData(params);
                    qap = dataPowerFilter.translateQuery(sqlSen, filters);
                }
            }
        }
        Map<String, Object> paramsMap = new HashMap<>(params == null ? 0 : params.size() + 6);
        if (params != null) {
            paramsMap.putAll(params);
        }
        paramsMap.putAll(qap.getParams());

        DataSet dataSet = new DataSet();
        // 添加分页查询属性
        if(paging){
            int pn = NumberBaseOpt.castObjectToInteger(paramsMap.get(pageNo),1);
            int ps = NumberBaseOpt.castObjectToInteger(paramsMap.get(pageSize),20);
            PageDesc pageDesc = new PageDesc(pn, ps);
            String pagingSql = QueryUtils.buildLimitQuerySQL(qap.getQuery(), pageDesc.getRowStart(), pageDesc.getPageSize(),
                false, databaseInfo.getDBType());

            JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(conn,pagingSql, paramsMap);
            if(hasCount){
                String sGetCountSql = QueryUtils.buildGetCountSQL(qap.getQuery());
                Object obj = DatabaseAccess.getScalarObjectQuery(conn, sGetCountSql, params);
                pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(obj));
                PageQueryResult<Object> result = PageQueryResult.createResult(jsonArray, pageDesc);
                dataSet.setData(result);
            } else {
                dataSet.setData(jsonArray);
            }
        } else {
            JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(conn, qap.getQuery(), paramsMap);
            dataSet.setData(jsonArray);
        }
        return dataSet;
    }

    private void buildExtendsSql() {
        if (extendFilters != null) {
            String extendSql = " and " + GeneralJsonObjectDao.buildFilterSql(null, null, extendFilters);
            sqlSen = sqlSen.replaceAll("\\{condition\\}", extendSql);
            log.info(sqlSen);
        }
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

    public Map<String, Object> getExtendFilters() {
        return extendFilters;
    }

    public void setExtendFilters(Map<String, Object> extendFilters) {
        this.extendFilters = QueryUtils.pretreatParameters(extendFilters);
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public void setHasCount(boolean hasCount) {
        this.hasCount = hasCount;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
