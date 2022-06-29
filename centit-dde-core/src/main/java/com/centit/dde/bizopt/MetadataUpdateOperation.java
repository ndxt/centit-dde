package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;

import java.util.Map;

/**
 * 元数据更新组件
 */
public class MetadataUpdateOperation implements BizOperation {
    private  MetaObjectService metaObjectService;


    public MetadataUpdateOperation(MetaObjectService metaObjectService) {
        this.metaObjectService = metaObjectService;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        Integer updateType = bizOptJson.getInteger("updateType");
        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel,bizOptJson);
        String tableId = bizOptJson.getString("tableId");
        Integer withChildrenDeep = NumberBaseOpt.castObjectToInteger(bizOptJson.getInteger("withChildrenDeep"),1);
        switch (updateType){
            case 1://新建
                int count = metaObjectService.saveObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
                return BuiltInOperation.createResponseSuccessData(count);
            case 2://修改
                int upcount= metaObjectService.updateObjectWithChildren(tableId, parames, withChildrenDeep);
                return BuiltInOperation.createResponseSuccessData(upcount);
            case 3://删除
                metaObjectService.deleteObjectWithChildren(tableId, parames, withChildrenDeep);
                return BuiltInOperation.createResponseSuccessData(1);
            case 4://合并
                int resultCount = metaObjectService.mergeObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
                return BuiltInOperation.createResponseSuccessData(resultCount);
            default:
                return ResponseData.makeErrorMessage("未知操作类型！");
        }
    }
}
