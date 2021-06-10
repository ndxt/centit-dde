package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.MetaObjectService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null) {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName") + "：未指定数据集,或指定的数据集为NULL！");
        }
        try {
            List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
            int count = 0;
            for (Map<String, Object> objectMap : dataAsList) {
                count += metaObjectService.mergeObjectWithChildren(tableId, objectMap, withChildrenDeep == null ? 1 : withChildrenDeep);
            }
            bizModel.putDataSet(id, new SimpleDataSet(count));
            return BuiltInOperation.getResponseSuccessData(count);
        } catch (Exception e) {
            bizModel.putDataSet(id,new SimpleDataSet(e.getMessage()));
            return BuiltInOperation.getResponseData(0, 0, e.getMessage());
        }
    }
}
