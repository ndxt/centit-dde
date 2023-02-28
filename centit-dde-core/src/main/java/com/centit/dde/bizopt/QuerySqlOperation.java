package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.SqlDataSetReader;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.BooleanBaseOpt;

import java.util.Map;


/**
 * @author zhf
 */
public class QuerySqlOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    private DataScopePowerManager queryDataScopeFilter;

    public QuerySqlOperation(SourceInfoDao sourceInfoDao, DataScopePowerManager queryDataScopeFilter) {
        this.sourceInfoDao = sourceInfoDao;
        this.queryDataScopeFilter = queryDataScopeFilter;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String condition = BuiltInOperation.getJsonFieldString(bizOptJson, "condition", "false");
        String sql = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");

        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);

        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION, "找不到对应的集成数据库：" + databaseCode);
        }
        SqlDataSetReader sqlDsr = new SqlDataSetReader();
        sqlDsr.setDataSource(databaseInfo);
        sqlDsr.setSqlSen(sql);
        sqlDsr.setQueryDataScopeFilter(queryDataScopeFilter);
        sqlDsr.setOptId(dataOptContext.getOptId());
        if ("true".equals(condition) ){//&& !StringBaseOpt.isNvl(conditionSet) && bizModel.getDataSet(conditionSet) != null) {
            String conditionSet = BuiltInOperation.getJsonFieldString(bizOptJson, "conditionSet", "");
            DataSet dataSet = bizModel.getDataSet(conditionSet);
            if(dataSet != null){
                sqlDsr.setExtendFilters(dataSet.getFirstRow());
            }
        }
        //是否分页
        sqlDsr.setPaging(BooleanBaseOpt.castObjectToBoolean(
            BuiltInOperation.getJsonFieldString(bizOptJson, "paging", "false"), false));
        //是否返回总数
        sqlDsr.setHasCount(BooleanBaseOpt.castObjectToBoolean(
            BuiltInOperation.getJsonFieldString(bizOptJson, "hasCount", "false"), false));

        //分页参数属性
        //页码参数属性
        sqlDsr.setPageNoField(BuiltInOperation.getJsonFieldString(bizOptJson, "pageNo", "pageNo"));
        //每页数量属性
        sqlDsr.setPageSizeField(BuiltInOperation.getJsonFieldString(bizOptJson, "pageSize", "pageSize"));

        sqlDsr.setReturnFirstRowAsObject(BooleanBaseOpt.castObjectToBoolean(
            BuiltInOperation.getJsonFieldString(bizOptJson, "returnAsObject", "false"), false));
        DataSet dataSet = sqlDsr.load(parames);

        bizModel.putDataSet(id, dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
