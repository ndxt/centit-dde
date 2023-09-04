package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.dao.DataPowerFilter;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.ObjectException;
import org.apache.commons.lang3.StringUtils;

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
        Map<String, Object> parames = DataSetOptUtil.getDataSetParames(bizModel, bizOptJson);
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

                if (!dataPowerFilter.checkObject(parames, filters)) {
                    throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR,
                        "数据范围权限校验不通过，用户：" + currentUserCode + "，校验条件" + JSON.toJSONString(filters) + "。");
                }
            }
        }

        switch (updateType){
            case 1://新建
                int count = metaObjectService.saveObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
                return BuiltInOperation.createResponseSuccessData(count);
            case 2://修改
                int upcount= metaObjectService.updateObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
                return BuiltInOperation.createResponseSuccessData(upcount);
            case 3://删除
                metaObjectService.deleteObjectWithChildren(tableId, parames, withChildrenDeep);
                bizModel.putDataSet(id, new DataSet(parames));
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
