package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataOperation implements BizOperation {
    private  MetaObjectService metaObjectService;

    private DataScopePowerManager queryDataScopeFilter;

    private MetaDataCache metaDataCache;

    public MetadataOperation(MetaObjectService metaObjectService, DataScopePowerManager queryDataScopeFilter, MetaDataCache metaDataCache) {
        this.metaObjectService = metaObjectService;
        this.queryDataScopeFilter=queryDataScopeFilter;
        this.metaDataCache=metaDataCache;
    }

    public MetadataOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");

        String tableId = bizOptJson.getString("tableLabelName");

        //操作类型
        Integer templateType = bizOptJson.getInteger("templateType");
        String paramsType = bizOptJson.getString("sourceType");
        Map<String, Object> parames;
        // 自定义参数类型
        if("customSource".equals(paramsType)){
            Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
            parames = new HashMap<>(16);
            if (mapString != null) {
                for (Map.Entry<String, String> map : mapString.entrySet()) {
                    if (!StringBaseOpt.isNvl(map.getValue())) {
                        parames.put(map.getKey(),  VariableFormula.calculate(map.getValue(),new BizModelJSONTransform(bizModel)));
                    }
                }
            }
            //添加 url参数
            Map<String, Object> urlParams = CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
            parames = CollectionsOpt.unionTwoMap(parames, urlParams);
        } else {
            //数据集参数
            String source = bizOptJson.getString("source");
            DataSet dataSet = bizModel.getDataSet(StringUtils.isBlank(source)?ConstantValue.REQUEST_PARAMS_TAG:source);
            parames = dataSet.getFirstRow(); //数据集
        }
        Integer withChildrenDeep = NumberBaseOpt.castObjectToInteger(bizOptJson.getInteger("withChildrenDeep"),1);
        switch (templateType){
            case 1://新建
                metaObjectService.saveObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
                return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
            case 2://修改
                int upcount= metaObjectService.updateObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(upcount));
                return BuiltInOperation.createResponseSuccessData(upcount);
            case 3://删除
                metaObjectService.deleteObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(1));
                return BuiltInOperation.createResponseSuccessData(1);
            case 4://查询 这个已经迁移走了，但是为了兼容前面的遗留项目暂时不能删除，2023年6月删除
                CentitUserDetails userDetails = dataOptContext.getCurrentUserDetail();
                String extFilter = null;
                if(userDetails!=null) {
                    String topUnit = dataOptContext.getTopUnit();
                    if(StringUtils.isBlank(topUnit)) {
                        topUnit = bizModel.fetchTopUnit();
                        if (StringUtils.isBlank(topUnit)) {
                            topUnit = userDetails.getTopUnitCode();
                        }
                    }
                    List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit,
                        userDetails.getUserCode(), dataOptContext.getOptId(), "api");

                    if (filters != null) {
                        MetaTable table = metaDataCache.getTableInfo(tableId);
                        DataPowerFilter dataPowerFilter =
                            queryDataScopeFilter.createUserDataPowerFilter(userDetails);

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
                    parames.remove("pageNo");
                }
                if (parames.get("pageSize") != null) {
                    pageDesc.setPageSize(NumberBaseOpt.castObjectToInteger(parames.get("pageSize")));
                    parames.remove("pageSize");
                }
                JSONArray  jsonArray =metaObjectService.pageQueryObjects(tableId, extFilter, parames,null, pageDesc);
                PageQueryResult<Object>  result = PageQueryResult.createResult(jsonArray, pageDesc);
                if (parames.get("closePage")!=null){//返回接口元数数据，不含分页信息
                    bizModel.putDataSet(id, new DataSet(jsonArray));
                }else {
                    bizModel.putDataSet(id, new DataSet(result));//返回带分页的数据
                }
                return BuiltInOperation.createResponseSuccessData(jsonArray.size());
            case 5://查看 这个已经迁移走了，但是为了兼容前面的遗留项目暂时不能删除，2023年6月删除
                Map<String, Object> data = metaObjectService.getObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(data));
                return BuiltInOperation.createResponseSuccessData(bizModel.getDataSet(id).getSize());
            case 9://批量删除
            {
                String source = bizOptJson.getString("source");
                if("customSource".equals(paramsType) || StringUtils.isBlank(source)){
                    throw new ObjectException("批量删除时不能设置为自定义参数形式");
                }
                DataSet dataSet = bizModel.getDataSet(source);
                List<Map<String, Object>> delParames = dataSet.getDataAsList();
                int delCount =0;
                for (Map parame : delParames) {
                    metaObjectService.deleteObjectWithChildren(tableId,parame,withChildrenDeep);
                    delCount++;
                }
                bizModel.putDataSet(id, new DataSet(delCount));
                return BuiltInOperation.createResponseSuccessData(delCount);
            }
            case 11: // 合并
                int resultCount = metaObjectService.mergeObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(resultCount));
                return BuiltInOperation.createResponseSuccessData(resultCount);
        }
        return null;
    }
}
