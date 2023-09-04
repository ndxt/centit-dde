package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import com.centit.support.security.DesensitizeOptUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元数据查询组件
 */
public class MetadataQueryOperation implements BizOperation {
    private  MetaObjectService metaObjectService;

    private DataScopePowerManager queryDataScopeFilter;

    private MetaDataCache metaDataCache;

    public MetadataQueryOperation(MetaObjectService metaObjectService, DataScopePowerManager queryDataScopeFilter, MetaDataCache metaDataCache) {
        this.metaObjectService = metaObjectService;
        this.queryDataScopeFilter=queryDataScopeFilter;
        this.metaDataCache=metaDataCache;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        String tableId = bizOptJson.getString("tableId");
        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
        Integer queryType = bizOptJson.getInteger("queryType");
        boolean desensitize = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("desensitize"), false);
        Integer withChildrenDeep = NumberBaseOpt.castObjectToInteger(bizOptJson.getInteger("withChildrenDeep"),1);
        switch (queryType){
            case 1://查询列表
                CentitUserDetails userDetails = dataOptContext.getCurrentUserDetail();
                String extFilter = null;
                if(userDetails!=null) {
                    String currentUserCode = userDetails.getUserCode();
                    String topUnit = dataOptContext.getTopUnit();
                    if(StringUtils.isBlank(topUnit)) {
                        topUnit = bizModel.fetchTopUnit();
                        if (StringUtils.isBlank(topUnit)) {
                            topUnit = userDetails.getTopUnitCode();
                        }
                    }
                    List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit,
                        currentUserCode, dataOptContext.getOptId(), "api");

                    if (filters != null) {
                        MetaTable table = metaDataCache.getTableInfo(tableId);
                        DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(userDetails);
                        dataPowerFilter.addSourceData(parames);
                        Map<String, String> tableAlias = new HashMap<>(3);
                        tableAlias.put(table.getTableName(), "");
                        QueryAndNamedParams qap = dataPowerFilter.translateQueryFilter(tableAlias, filters);
                        parames.putAll(qap.getParams());
                        extFilter = qap.getQuery();
                    }
                }
                PageDesc pageDesc = new PageDesc();
                if (parames.get("pageNo") != null) {
                    pageDesc.setPageNo(NumberBaseOpt.castObjectToInteger(parames.get("pageNo")));
                }
                if (parames.get("pageSize") != null) {
                    pageDesc.setPageSize(NumberBaseOpt.castObjectToInteger(parames.get("pageSize")));
                }
                JSONArray jsonArray = metaObjectService.pageQueryObjects(tableId, extFilter, parames,null, pageDesc);
                // 脱敏操作
                if(desensitize){
                    MetaTable table = metaDataCache.getTableInfo(tableId);
                    DesensitizeOptUtils.desensitize(jsonArray, table.fetchDesensitizeOpt());
                }
                Boolean isReturnPageInfo = bizOptJson.getBoolean("isReturnPageInfo");
                if (isReturnPageInfo){
                    PageQueryResult<Object>  result = PageQueryResult.createResult(jsonArray, pageDesc);
                    bizModel.putDataSet(id, new DataSet(result));//返回带分页的数据
                }else {
                    //返回接口元数数据，不含分页信息
                    bizModel.putDataSet(id, new DataSet(jsonArray));
                }
                return BuiltInOperation.createResponseSuccessData(jsonArray.size());
            case 2://查看对象，返回一个记录
                Map<String, Object> data = metaObjectService.getObjectWithChildren(tableId, parames, withChildrenDeep);
                // 脱敏操作
                if(desensitize){
                    MetaTable table = metaDataCache.getTableInfo(tableId);
                    data = DesensitizeOptUtils.desensitize(data, table.fetchDesensitizeOpt());
                }
                DataSet dataSet = new DataSet(data);
                bizModel.putDataSet(id, dataSet);
                return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
        return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
            "配置信息不正确，未知查询类型！");
    }
}
