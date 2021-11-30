package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.database.utils.PageDesc;

import java.util.*;

public class WriteDbBizOperation implements BizOperation {
    MetaObjectService metaObjectService;

    public WriteDbBizOperation(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }

    public WriteDbBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String tableId = bizOptJson.getString("tableLabelName");
        Integer withChildrenDeep = bizOptJson.getInteger("withChildrenDeep");
        Map<String, Object> modelTag = bizModel.getModelTag();
        modelTag.remove("runType");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet==null){
            SimpleDataSet simpleDataSet = new SimpleDataSet();
            simpleDataSet.setData(modelTag);
            dataSet = simpleDataSet;
        }
        JSONObject properties = bizOptJson.getJSONObject("properties");
        Integer templateType = properties.getInteger("templateType");
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        try {
            switch (templateType){
                case 1://新建
                    int saveCount = 0;
                    for (Map<String, Object> objectMap : dataAsList) {
                        saveCount += metaObjectService.saveObjectWithChildren(tableId, objectMap, withChildrenDeep == null ? 1 : withChildrenDeep);
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(saveCount));
                    return BuiltInOperation.getResponseSuccessData(saveCount);
                case 2://修改
                    int updateCount = 0;
                    for (Map<String, Object> objectMap : dataAsList) {
                        updateCount += metaObjectService.updateObjectWithChildren(tableId, objectMap, withChildrenDeep == null ? 1 : withChildrenDeep);
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(updateCount));
                    return BuiltInOperation.getResponseSuccessData(updateCount);
                case 3://删除
                    int deleteCount = 0;
                    for (Map<String, Object> objectMap : dataAsList) {
                        metaObjectService.deleteObjectWithChildren(tableId, objectMap, withChildrenDeep == null ? 1 : withChildrenDeep);
                        deleteCount++;
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(deleteCount));
                    return BuiltInOperation.getResponseSuccessData(deleteCount);
                case 4://查询
                    JSONArray jsonArray =new JSONArray();
                    PageDesc pageDesc=null;
                    for (Map<String, Object> objectMap : dataAsList) {
                        if (objectMap.get("pageNo")!=null && objectMap.get("pageSize")!=null){
                            pageDesc = new PageDesc();
                            pageDesc.setPageNo(Integer.valueOf(String.valueOf(objectMap.get("pageNo"))));
                            pageDesc.setPageSize(Integer.valueOf(String.valueOf(objectMap.get("pageSize"))));
                        }
                        jsonArray = metaObjectService.pageQueryObjects(tableId, objectMap,pageDesc);
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(jsonArray));
                    return BuiltInOperation.getResponseSuccessData(jsonArray.size());
                case 5://查看
                    Map<String, Object> objectById = new HashMap<>();
                    for (Map<String, Object> objectMap : dataAsList) {
                        objectById= metaObjectService.getObjectById(tableId, objectMap);
                    }
                    bizModel.putDataSet(id, new SimpleDataSet(objectById));
                    return BuiltInOperation.getResponseSuccessData(objectById.size());
            }
        } catch (Exception e) {
            bizModel.putDataSet(id,new SimpleDataSet(e.getMessage()));
            return BuiltInOperation.getResponseData(0, 1, e.getMessage());
        }
        return null;
    }
}
