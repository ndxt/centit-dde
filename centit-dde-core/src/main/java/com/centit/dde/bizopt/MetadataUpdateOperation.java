package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.product.metadata.utils.SessionDataUtils;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元数据更新组件
 */
public class MetadataUpdateOperation implements BizOperation {
    private  MetaObjectService metaObjectService;

    private DataScopePowerManager queryDataScopeFilter;

    public MetadataUpdateOperation(MetaObjectService metaObjectService, DataScopePowerManager queryDataScopeFilter) {
        this.metaObjectService = metaObjectService;
        this.queryDataScopeFilter=queryDataScopeFilter;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id = bizOptJson.getString("id");
        Integer updateType = bizOptJson.getInteger("updateType");
        boolean isBatchOpt = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("batchOpt"), false);
        List<Map<String, Object>> optData;

        if(isBatchOpt){
            optData = DataSetOptUtil.fetchDataSet(bizModel, bizOptJson);
        } else {
            Map<String, Object> objectMap = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
            optData = new ArrayList<>(2);
            optData.add(objectMap);
        }

        String tableId = bizOptJson.getString("tableId");
        Integer withChildrenDeep = NumberBaseOpt.castObjectToInteger(bizOptJson.getInteger("withChildrenDeep"),1);

        CentitUserDetails currentUserDetails = dataOptContext.getCurrentUserDetail();

        if(currentUserDetails!=null) {
            String currentUserCode = currentUserDetails.getUserCode();
            String topUnit = dataOptContext.getTopUnit();
            if(StringUtils.isBlank(topUnit)) {
                topUnit = bizModel.fetchTopUnit();
                if (StringUtils.isBlank(topUnit)) {
                    topUnit = currentUserDetails.getTopUnitCode();
                }
            }
            List<String> filters = queryDataScopeFilter.listUserDataFiltersByOptIdAndMethod(topUnit,
                currentUserCode, dataOptContext.getOptId(), "api");

            if (filters != null && filters.size() > 0) {
                DataPowerFilter dataPowerFilter = queryDataScopeFilter.createUserDataPowerFilter(currentUserDetails);
                MetaTable tableInfo = metaObjectService.fetchTableInfo(tableId);
                for(Map<String, Object> objectMap : optData) {
                    if (tableInfo != null && !dataPowerFilter.checkObject(objectMap, tableInfo.getTableName(), filters)) {
                        throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR,
                            "数据范围权限校验不通过，用户：" + currentUserCode + "，校验条件" + JSON.toJSONString(filters) + "。");
                    }
                }
            }
        }

        Map<String, Object> extParams = SessionDataUtils.createSessionDataMap(currentUserDetails);
        int updateCount = 0;
        switch (updateType){
            case 1://新建
                for(Map<String, Object> objectMap : optData) {
                    updateCount += metaObjectService.saveObjectWithChildren(tableId, objectMap, extParams, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            case 2://修改
                for(Map<String, Object> objectMap : optData) {
                    updateCount += metaObjectService.updateObjectWithChildren(tableId, objectMap, extParams, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            case 3://删除
                for(Map<String, Object> objectMap : optData) {
                    metaObjectService.deleteObjectWithChildren(tableId, objectMap, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                    updateCount++;
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            case 4://合并
                for(Map<String, Object> objectMap : optData) {
                    updateCount += metaObjectService.mergeObjectWithChildren(tableId, objectMap, extParams, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            case 5://逻辑删除
                for(Map<String, Object> objectMap : optData) {
                    metaObjectService.softDeleteObjectWithChildren(tableId, objectMap, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                    updateCount++;
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            case 6://带版本更新
                for(Map<String, Object> objectMap : optData) {
                    updateCount += metaObjectService.updateObjectWithChildrenCheckVersion(tableId, objectMap, extParams, withChildrenDeep);
                    bizModel.putDataSet(id, new DataSet(objectMap));
                }
                return BuiltInOperation.createResponseSuccessData(updateCount);
            default:
                return ResponseData.makeErrorMessage("未知操作类型！");
        }
    }
}
