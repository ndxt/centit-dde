package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

/**
 * @author zhf
 */
public class OptflowSerialNumberOperation implements BizOperation {
    private OptFlowNoInfoManager optFlowNoInfoManager;
    private static final String PRODUCE = "A";
    private static final String PREVIEW = "B";

    public OptflowSerialNumberOperation(OptFlowNoInfoManager optFlowNoInfoManager) {
        this.optFlowNoInfoManager = optFlowNoInfoManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        if(optFlowNoInfoManager==null){
            throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT, "平台不支持获取序列号服务。");
        }
        String codeCode = bizOptJson.getString("codeCode");
        if (StringUtils.isBlank(codeCode)) {
            return ResponseData.makeErrorMessage(
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "codeCode"));
        }
        String type = BuiltInOperation.getJsonFieldString(bizOptJson, "lshType", PRODUCE);
        String baseDateType = BuiltInOperation.getJsonFieldString(bizOptJson, "baseDateType", "Y");
        String lshField = BuiltInOperation.getJsonFieldString(bizOptJson, "lshField", "lsh");
        String owner=BuiltInOperation.getJsonFieldString(bizOptJson, "owner","");
        if(StringUtils.isNotBlank(owner)){
            owner=StringBaseOpt.castObjectToString(JSONTransformer.transformer(owner,new BizModelJSONTransform(bizModel)));
        }
        String ownCode = owner+dataOptContext.getOsId();
        boolean isBaseDate = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("isBaseDate"), false);
        Date codeBaseDate = null;
        if (isBaseDate) {
            codeBaseDate = DatetimeOpt.castObjectToDate(JSONTransformer.transformer(bizOptJson.get("codeBaseDate"),
                new BizModelJSONTransform(bizModel)));
        }
        if(codeBaseDate == null){
            codeBaseDate = DatetimeOpt.currentUtilDate();
        }
        long lsh = 0L;
        if (PRODUCE.equals(type)) {
            switch (baseDateType) {
                case "M":
                    lsh = optFlowNoInfoManager.newNextLshBaseMonth(ownCode, codeCode, codeBaseDate);
                    break;
                case "D":
                    lsh = optFlowNoInfoManager.newNextLshBaseDay(ownCode, codeCode, codeBaseDate);
                    break;
                case "Y":
                    lsh = optFlowNoInfoManager.newNextLshBaseYear(ownCode, codeCode, codeBaseDate);
                    break;
                case "W":
                    lsh = optFlowNoInfoManager.newNextLshBaseWeek(ownCode, codeCode, codeBaseDate);
                    break;
                default:
                    lsh = optFlowNoInfoManager.newNextLsh(ownCode, codeCode);
                    break;
            }
        } else if (PREVIEW.equals(type)) {
            switch (baseDateType) {
                case "M":
                    lsh = optFlowNoInfoManager.viewNextLshBaseMonth(ownCode, codeCode, codeBaseDate);
                    break;
                case "D":
                    lsh = optFlowNoInfoManager.viewNextLshBaseDay(ownCode, codeCode, codeBaseDate);
                    break;
                case "Y":
                    lsh = optFlowNoInfoManager.viewNextLshBaseYear(ownCode, codeCode, codeBaseDate);
                    break;
                case "W":
                    lsh = optFlowNoInfoManager.viewNextLshBaseWeek(ownCode, codeCode, codeBaseDate);
                    break;
                default:
                    lsh = optFlowNoInfoManager.viewNextLsh(ownCode, codeCode);
                    break;
            }
        }
        Map<String, Object> data = CollectionsOpt.createHashMap(
            "no", lsh,
            lshField, lsh,
            "owner", owner,
            "codeCode", codeCode,
            "baseDate", codeBaseDate);
        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(data));
        String template = bizOptJson.getString("template");
        if (StringUtils.isNotBlank(template)) {
            Object calculate = JSONTransformer.transformer(template, new BizModelJSONTransform(bizModel, data));
            String serialNo = StringBaseOpt.castObjectToString(calculate);
            if(StringUtils.isNotBlank(serialNo)){
                data.put(lshField, serialNo);
            }
        }
        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(data));

        return BuiltInOperation.createResponseSuccessData(1);
    }

}
