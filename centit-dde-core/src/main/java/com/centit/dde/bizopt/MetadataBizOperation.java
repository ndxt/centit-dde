package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.product.adapter.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataBizOperation implements BizOperation {
    private  MetaObjectService metaObjectService;

    private DataScopePowerManager queryDataScopeFilter;

    private MetaDataCache metaDataCache;

    public MetadataBizOperation(MetaObjectService metaObjectService, DataScopePowerManager queryDataScopeFilter, MetaDataCache metaDataCache) {
        this.metaObjectService = metaObjectService;
        this.queryDataScopeFilter=queryDataScopeFilter;
        this.metaDataCache=metaDataCache;
    }

    public MetadataBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");//数据集
        String tableId = bizOptJson.getString("tableLabelName");
        Map<String, String> mapString = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("parameterList"), "key", "value");
        Integer withChildrenDeep = bizOptJson.getInteger("withChildrenDeep");
        Object optInfo = bizModel.getStackData(ConstantValue.API_INFO_TAG);
        String optId = "";
        if(optInfo instanceof Map){
            optId = StringBaseOpt.castObjectToString(((Map)optInfo).get("optId"));
        }
        //String optId= StringBaseOpt.castObjectToString(bizModel.getInterimVariable().get(ConstantValue.METADATA_OPTID));
        SimpleDataSet dataSet = bizModel.getDataSet(source)==null?new SimpleDataSet():(SimpleDataSet)bizModel.getDataSet(source);//数据集参数
        Integer templateType = bizOptJson.getInteger("templateType");//操作类型
        if(source==null){
            dataSet = new SimpleDataSet(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
        }
        Map<String, Object> parames = dataSet.getDataAsList().size()>0?dataSet.getDataAsList().get(0):new HashMap<>();
        if (mapString != null) {
            for (Map.Entry<String, String> map : mapString.entrySet()) {
                if (!StringBaseOpt.isNvl(map.getValue())) {
                    parames.put(map.getKey(),  VariableFormula.calculate(map.getValue(),new BizModelJSONTransform(bizModel)));
                }
            }
        }
        //只有为自定义参数的时候才put进去
        if(bizOptJson.getString("sourceType") ==null || "customSource".equals(bizOptJson.getString("sourceType"))){
            parames.putAll(CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
        }
        switch (templateType){
            case 1://新建
                metaObjectService.saveObjectWithChildren(tableId, parames, withChildrenDeep == null ? 1 : withChildrenDeep);
                bizModel.putDataSet(id, new SimpleDataSet(parames));
                return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
            case 2://修改
                int upcount= metaObjectService.updateObjectWithChildren(tableId, parames, withChildrenDeep == null ? 1 : withChildrenDeep);
                bizModel.putDataSet(id, new SimpleDataSet(upcount));
                return BuiltInOperation.getResponseSuccessData(upcount);
            case 3://删除
                metaObjectService.deleteObjectWithChildren(tableId, parames, withChildrenDeep == null ? 1 : withChildrenDeep);
                bizModel.putDataSet(id, new SimpleDataSet(1));
                return BuiltInOperation.getResponseSuccessData(1);
            case 4://查询
                HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
                String topUnit = WebOptUtils.getCurrentTopUnit(request);
                List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit, WebOptUtils.getCurrentUserCode(request), optId, "api");
                String extFilter = null;
                PageDesc pageDesc = new PageDesc();
                if (parames.get("pageNo")!=null){
                    pageDesc.setPageNo(Integer.valueOf(String.valueOf(parames.get("pageNo"))));
                    parames.remove("pageNo");
                }
                if ( parames.get("pageSize")!=null){
                    pageDesc.setPageSize(Integer.valueOf(String.valueOf(parames.get("pageSize"))));
                    parames.remove("pageSize");
                }
                if (filters != null) {
                    MetaTable table = metaDataCache.getTableInfo(tableId);
                    DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                        WebOptUtils.getCurrentUserInfo(request), topUnit, WebOptUtils.getCurrentUnitCode(request));
                    dataPowerFilter.addSourceData(parames);
                    Map<String, String> tableAlias = new HashMap<>(3);
                    tableAlias.put(table.getTableName(), "");
                    QueryAndNamedParams qap = dataPowerFilter.translateQueryFilter(tableAlias, filters);
                    parames.putAll(qap.getParams());
                    extFilter = qap.getQuery();
                }
                JSONArray  jsonArray =metaObjectService.pageQueryObjects(tableId, extFilter, parames,null, pageDesc);
                PageQueryResult<Object>  result = PageQueryResult.createResult(jsonArray, pageDesc);
                if (parames.get("closePage")!=null){//返回接口元数数据，不含分页信息
                    bizModel.putDataSet(id, new SimpleDataSet(jsonArray));
                }else {
                    bizModel.putDataSet(id, new SimpleDataSet(result));//返回带分页的数据
                }
                return BuiltInOperation.getResponseSuccessData(jsonArray.size());
            case 5://查看
                Map<String, Object> data = metaObjectService.getObjectWithChildren(tableId, parames, withChildrenDeep == null ? 1 : withChildrenDeep);
                bizModel.putDataSet(id, new SimpleDataSet(data));
                return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(id).getSize());
            case 9://批量删除
                List<Map> delParames = JSON.parseArray(JSON.toJSONString(dataSet.getDataAsList()),Map.class);
                int delCount =0;
                for (Map parame : delParames) {
                    metaObjectService.deleteObjectWithChildren(tableId,parame,withChildrenDeep == null ? 1 : withChildrenDeep);
                    delCount++;
                }
                bizModel.putDataSet(id, new SimpleDataSet(delCount));
                return BuiltInOperation.getResponseSuccessData(delCount);
            case 10://根据条件修改字段值
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(parames), JSONObject.class);
                int count  = metaObjectService.updateObjectsByProperties(tableId, jsonObject,
                    CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));
                bizModel.putDataSet(id, new SimpleDataSet(count));
                return BuiltInOperation.getResponseSuccessData(count);
            case 11:
                int resultCount = metaObjectService.mergeObjectWithChildren(tableId, parames, withChildrenDeep == null ? 1 : withChildrenDeep);
                bizModel.putDataSet(id, new SimpleDataSet(resultCount));
                return BuiltInOperation.getResponseSuccessData(resultCount);
        }
        return null;
    }
}
