package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.product.adapter.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryAndNamedParams;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteDbBizOperation implements BizOperation {
    private  MetaObjectService metaObjectService;

    private DataScopePowerManager queryDataScopeFilter;

    private MetaDataCache metaDataCache;

    public WriteDbBizOperation(MetaObjectService metaObjectService,DataScopePowerManager queryDataScopeFilter,MetaDataCache metaDataCache) {
        this.metaObjectService = metaObjectService;
        this.queryDataScopeFilter=queryDataScopeFilter;
        this.metaDataCache=metaDataCache;
    }

    public WriteDbBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String tableId = bizOptJson.getString("tableLabelName");
        Integer withChildrenDeep = bizOptJson.getInteger("withChildrenDeep");
        String optId=(String) bizModel.getModelTag().get("metadata_optId");
        Map<String, Object> modelTag = bizModel.getModelTag();
        modelTag.remove("runType");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet==null){
            SimpleDataSet simpleDataSet = new SimpleDataSet();
            simpleDataSet.setData(modelTag);
            dataSet = simpleDataSet;
        }
        Integer templateType = bizOptJson.getInteger("templateType");
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        try {
            switch (templateType){
                case 1://新建
                    metaObjectService.saveObjectWithChildren(tableId, dataAsList.get(0), withChildrenDeep == null ? 1 : withChildrenDeep);
                    bizModel.putDataSet(id, new SimpleDataSet(dataAsList.get(0)));
                    return BuiltInOperation.getResponseSuccessData(dataAsList.size());
                case 2://修改
                    int upcount= metaObjectService.updateObjectWithChildren(tableId, dataAsList.get(0), withChildrenDeep == null ? 1 : withChildrenDeep);
                    bizModel.putDataSet(id, new SimpleDataSet(upcount));
                    return BuiltInOperation.getResponseSuccessData(upcount);
                case 3://删除
                    metaObjectService.deleteObjectWithChildren(tableId, dataAsList.get(0), withChildrenDeep == null ? 1 : withChildrenDeep);
                    bizModel.putDataSet(id, new SimpleDataSet(dataAsList.size()));
                    return BuiltInOperation.getResponseSuccessData(dataAsList.size());
                case 4://查询
                    HttpServletRequest request = RequestThreadLocal.getLocalThreadWrapperRequest();
                    String topUnit = WebOptUtils.getCurrentTopUnit(request);
                    List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit, WebOptUtils.getCurrentUserCode(request), optId, "list");
                    String extFilter = null;
                    JSONArray jsonArray =new JSONArray();
                    PageDesc pageDesc = new PageDesc();
                    PageQueryResult<Object> result =null;
                    for (Map<String, Object> objectMap : dataAsList) {
                        if (objectMap.get("pageNo")!=null){
                            pageDesc.setPageNo(Integer.valueOf(String.valueOf(objectMap.get("pageNo"))));
                            objectMap.remove("pageNo");
                        }
                        if ( objectMap.get("pageSize")!=null){
                            pageDesc.setPageSize(Integer.valueOf(String.valueOf(objectMap.get("pageSize"))));
                            objectMap.remove("pageSize");
                        }
                        if (filters != null) {
                            MetaTable table = metaDataCache.getTableInfo(tableId);
                            DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(
                                WebOptUtils.getCurrentUserInfo(request), topUnit, WebOptUtils.getCurrentUnitCode(request));
                            dataPowerFilter.addSourceData(objectMap);
                            Map<String, String> tableAlias = new HashMap<>(3);
                            tableAlias.put(table.getTableName(), "");
                            QueryAndNamedParams qap = dataPowerFilter.translateQueryFilter(
                                tableAlias, filters);
                            objectMap.putAll(qap.getParams());
                            extFilter = qap.getQuery();
                        }
                        objectMap.remove("metadata_optId");
                        jsonArray =metaObjectService.pageQueryObjects(tableId, extFilter, objectMap,null, pageDesc);
                        result = PageQueryResult.createResult(jsonArray, pageDesc);
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(result));
                    return BuiltInOperation.getResponseSuccessData(jsonArray.size());
                case 5://查看
                    Map<String, Object> data = metaObjectService.getObjectWithChildren(tableId, dataAsList.get(0), withChildrenDeep == null ? 1 : withChildrenDeep);
                    bizModel.putDataSet(id, new SimpleDataSet(data));
                    return BuiltInOperation.getResponseSuccessData(data.size());
                case 9://批量删除
                    List<Map> parames = JSON.parseArray(JSON.toJSONString(dataAsList),Map.class);
                    int delCount =0;
                    for (Map parame : parames) {
                        metaObjectService.deleteObjectWithChildren(tableId,parame,withChildrenDeep == null ? 1 : withChildrenDeep);
                        delCount++;
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(delCount));
                    return BuiltInOperation.getResponseSuccessData(delCount);
                case 10://根据条件修改字段值
                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(dataAsList.get(0)), JSONObject.class);
                    int count  = metaObjectService.updateObjectsByProperties(tableId, jsonObject, bizModel.getModelTag());
                    bizModel.putDataSet(id, new SimpleDataSet(count));
                    return BuiltInOperation.getResponseSuccessData(count);
            }
        } catch (Exception e) {
            bizModel.putDataSet(id,new SimpleDataSet(e.getMessage()));
            return BuiltInOperation.getResponseData(0, 1, e.getMessage());
        }
        return null;
    }
}
