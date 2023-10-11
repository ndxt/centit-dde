package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.adapter.po.TaskLog;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetReader;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.product.metadata.api.ISourceInfo;
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

import java.sql.Connection;
import java.util.Date;
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

    /**
     以对象形式返回首行;
     */
    private boolean returnFirstRowAsObject;
    //分页参数属性
    //页码参数属性
    private String pageNoField;
    //每页数量属性
    private String pageSizeField;

    private DataScopePowerManager queryDataScopeFilter;

    /**
     * 读取 dataSet 数据集
     *
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    public DataSet load(final Map<String, Object> params, DataOptContext dataOptContext) throws Exception {
        buildExtendsSql();
        if (extendFilters!=null){
            params.putAll(extendFilters);
        }
        Connection conn = AbstractSourceConnectThreadHolder.fetchConnect(databaseInfo);
        CentitUserDetails currentUserDetails = dataOptContext.getCurrentUserDetail();
        QueryAndNamedParams qap = null;
        if (currentUserDetails != null) {
            String topUnit = dataOptContext.getTopUnit();
            if(StringUtils.isBlank(topUnit)){
                topUnit = currentUserDetails.getTopUnitCode();
            }
            String userCode = currentUserDetails.getUserCode();
            if (StringUtils.isNotBlank(userCode)) {
                List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit, userCode, optId, "api");
                if (filters != null) {
                    DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(currentUserDetails);
                    dataPowerFilter.addSourceData(params);
                    qap = dataPowerFilter.translateQuery(sqlSen, filters);
                }
            }
        }

        if(qap==null){
            qap = QueryUtils.translateQuery(sqlSen, params);
        }
        //debug模式下，添加日志，显示sql语句
        if( ConstantValue.RUN_TYPE_DEBUG.equals(dataOptContext.getRunType()) ||
            (ConstantValue.LOGLEVEL_CHECK_DEBUG & dataOptContext.getLogLevel()) != 0){
            TaskLog taskLog = dataOptContext.getTaskLog();
            TaskDetailLog detailLog = new TaskDetailLog();
            detailLog.setRunBeginTime(new Date());
            detailLog.setTaskId(taskLog.getTaskId());
            detailLog.setLogId(taskLog.getLogId());
            detailLog.setOptNodeId("sqlTrace");
            detailLog.setLogType("info");
            detailLog.setLogInfo(qap.toString());
            detailLog.setStepNo(dataOptContext.getStepNo());
            detailLog.setRunEndTime(new Date());
            taskLog.addDetailLog(detailLog);
        }

        Map<String, Object> paramsMap = new HashMap<>(params == null ? 0 : params.size() + 6);
        if (params != null) {
            paramsMap.putAll(params);
        }
        paramsMap.putAll(qap.getParams());
        DataSet dataSet = new DataSet();
        // 添加分页查询属性
        if(paging){
            int pn = NumberBaseOpt.castObjectToInteger(paramsMap.get(pageNoField),1);
            int ps = NumberBaseOpt.castObjectToInteger(paramsMap.get(pageSizeField),20);
            PageDesc pageDesc = new PageDesc(pn, ps);

            String pagingSql = ps < 1 ? qap.getQuery() :
                QueryUtils.buildLimitQuerySQL(qap.getQuery(), pageDesc.getRowStart(), pageDesc.getPageSize(),
                false, databaseInfo.getDBType());

            JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(conn, pagingSql, paramsMap);
            int resSize = jsonArray == null ? 0 : jsonArray.size();
            if(hasCount){
                // 分页，并且不是最后一页
                if( ps > 0 && resSize == ps) {
                    String sGetCountSql = QueryUtils.buildGetCountSQL(qap.getQuery());
                    Object obj = DatabaseAccess.getScalarObjectQuery(conn, sGetCountSql, paramsMap);
                    pageDesc.setTotalRows(NumberBaseOpt.castObjectToInteger(obj));
                } else {
                    if(ps > 0){ // 最后一页
                        pageDesc.setTotalRows( (pn-1) * ps + resSize);
                    } else { //错误的分页数据
                        pageDesc.setTotalRows(resSize);
                    }
                }
                PageQueryResult<Object> result = PageQueryResult.createResult(jsonArray, pageDesc);
                dataSet.setData(result);
            } else {
                dataSet.setData(jsonArray);
            }
        } else {
            if(this.returnFirstRowAsObject){
                JSONObject jsonObject = DatabaseAccess.getObjectAsJSON(conn, qap.getQuery(), paramsMap);
                dataSet.setData(jsonObject);
            } else {
                JSONArray jsonArray = DatabaseAccess.findObjectsByNamedSqlAsJSON(conn, qap.getQuery(), paramsMap);
                dataSet.setData(jsonArray);
            }
        }
        return dataSet;
    }

    public SqlDataSetReader(){
        returnFirstRowAsObject = false;
        paging = false;
        hasCount = false;
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

    public void setReturnFirstRowAsObject(boolean returnAsObject){
        this.returnFirstRowAsObject = returnAsObject;
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

    public void setPageNoField(String pageNo) {
        this.pageNoField = pageNo;
    }

    public void setPageSizeField(String pageSize) {
        this.pageSizeField = pageSize;
    }
}
