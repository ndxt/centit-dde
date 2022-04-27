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
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 元数据更新组件
 */
public class MetadataUpdateBizOperation implements BizOperation {
    private  MetaObjectService metaObjectService;


    public MetadataUpdateBizOperation(MetaObjectService metaObjectService) {
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
            case 4://合并
                int resultCount = metaObjectService.mergeObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(resultCount));
                return BuiltInOperation.createResponseSuccessData(resultCount);
            case 9://批量删除
            {
                String source = bizOptJson.getString("source");
                if("customSource".equals(bizOptJson.getString("sourceType")) || StringUtils.isBlank(source)){
                    throw new ObjectException("批量删除时不能设置为自定义参数形式");
                }
                DataSet dataSet = bizModel.fetchDataSetByName(source);
                List<Map<String, Object>> delParames = dataSet.getDataAsList();
                int delCount =0;
                for (Map parame : delParames) {
                    metaObjectService.deleteObjectWithChildren(tableId,parame,withChildrenDeep == null ? 1 : withChildrenDeep);
                    delCount++;
                }
                bizModel.putDataSet(id, new DataSet(delCount));
                return BuiltInOperation.createResponseSuccessData(delCount);
            }
        }
        return ResponseData.makeErrorMessage("未知操作类型！");
    }
}
