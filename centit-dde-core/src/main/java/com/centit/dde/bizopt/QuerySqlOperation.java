package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.SqlDataSetReader;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * @author zhf
 */
public class QuerySqlOperation implements BizOperation {
    private SourceInfoMetadata sourceInfoMetadata;

    private DataScopePowerManager queryDataScopeFilter;

    public QuerySqlOperation(SourceInfoMetadata sourceInfoMetadata, DataScopePowerManager queryDataScopeFilter) {
        this.sourceInfoMetadata = sourceInfoMetadata;
        this.queryDataScopeFilter = queryDataScopeFilter;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", "");
        String condition = BuiltInOperation.getJsonFieldString(bizOptJson, "condition", "false");
        boolean isDynamicSql = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("isDynamicSql"), false);
        String sqlSen = BuiltInOperation.getJsonFieldString(bizOptJson, "querySQL", "");
        if(isDynamicSql){
            sqlSen = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue( new BizModelJSONTransform(bizModel), sqlSen));
            if(StringUtils.isBlank(sqlSen) ||
                !StringUtils.startsWithIgnoreCase(sqlSen, "select") ){
                throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "动态sql语句必须以select开头!");
            }
        }
        if(StringUtils.isBlank(sqlSen) ||
            StringUtils.containsAnyIgnoreCase(sqlSen, ";",
                "update", "delete", "insert", "drop", "create")) {
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "查询语句中不能包括dml关键字!");
        }
        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);

        SourceInfo databaseInfo = sourceInfoMetadata.fetchSourceInfo(databaseCode);
        if (databaseInfo == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.database_not_found", databaseCode));
        }
        SqlDataSetReader sqlDsr = new SqlDataSetReader(bizOptJson);
        sqlDsr.setDataSource(databaseInfo);
        sqlDsr.setSqlSen(sqlSen);
        sqlDsr.setQueryDataScopeFilter(queryDataScopeFilter);
        sqlDsr.setOptId(dataOptContext.getOptId());
        if ("true".equals(condition) ){//&& StringUtils.isNotBlank(conditionSet) && bizModel.getDataSet(conditionSet) != null) {
            String conditionSet = BuiltInOperation.getJsonFieldString(bizOptJson, "conditionSet", "");
            DataSet dataSet = bizModel.getDataSet(conditionSet);
            if(dataSet != null){
                sqlDsr.setExtendFilters(dataSet.getFirstRow());
            }
        }
        //是否分页
        sqlDsr.setPaging(BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("paging"), false));
        //是否返回总数
        sqlDsr.setHasCount(BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("hasCount"), false));

        //分页参数属性
        //页码参数属性
        sqlDsr.setPageNoField(BuiltInOperation.getJsonFieldString(bizOptJson, "pageNo", "pageNo"));
        //每页数量属性
        sqlDsr.setPageSizeField(BuiltInOperation.getJsonFieldString(bizOptJson, "pageSize", "pageSize"));

        sqlDsr.setReturnFirstRowAsObject(BooleanBaseOpt.castObjectToBoolean(
            BuiltInOperation.getJsonFieldString(bizOptJson, "returnAsObject", "false"), false));
        DataSet dataSet = sqlDsr.load(parames, dataOptContext);

        bizModel.putDataSet(id, dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
