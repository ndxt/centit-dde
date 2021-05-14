package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.MetaObjectService;

import java.util.List;
import java.util.Map;

public class WriteDbBizOperation  implements BizOperation {
    MetaObjectService metaObjectService;

    public WriteDbBizOperation(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }

    public WriteDbBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String id = bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        String tableId = bizOptJson.getString("tableLabelName");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：未指定数据集,或指定的数据集为NULL！");
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        int count=0;
        for (Map<String, Object> objectMap : dataAsList) {
            count += metaObjectService.mergeObjectWithChildren(tableId, objectMap);
        }
        bizModel.putDataSet(id,new SimpleDataSet(count));
        return BuiltInOperation.getResponseSuccessData(count);
    }
}
