package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;

import java.util.Date;
import java.util.Map;

/**
 * @author zhf
 */
public class OptflowSerialNumberOperation implements BizOperation {
    private OptFlowNoInfoManager optFlowNoInfoManager;
    private final String PRODUCE = "A";
    private final String PREVIEW = "B";

    public OptflowSerialNumberOperation(OptFlowNoInfoManager optFlowNoInfoManager) {
        this.optFlowNoInfoManager = optFlowNoInfoManager;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String codeCode = bizOptJson.getString("codeCode");
        if (StringBaseOpt.isNvl(codeCode)) {
            return ResponseData.makeErrorMessage("类别代码没有填写！");
        }
        String type = BuiltInOperation.getJsonFieldString(bizOptJson, "lshType", PRODUCE);
        String baseDateType = BuiltInOperation.getJsonFieldString(bizOptJson, "baseDateType", "Y");
        String lshField = BuiltInOperation.getJsonFieldString(bizOptJson, "lshField", "lsh");
        String ownCode = dataOptContext.getOsId();
        boolean isBaseDate = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("isBaseDate"), false);
        Date codeBaseDate = null;
        if (isBaseDate) {
            codeBaseDate = DatetimeOpt.castObjectToDate(bizOptJson.get("codeBaseDate"));
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
                default:
                    lsh = optFlowNoInfoManager.viewNextLsh(ownCode, codeCode);
                    break;
            }
        }
        Map<String, Object> data = CollectionsOpt.createHashMap(lshField, lsh);
        bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(data));
        String template = bizOptJson.getString("template");
        if (!StringBaseOpt.isNvl(template)) {
            Object calculate = VariableFormula.calculate(template, new BizModelJSONTransform(bizModel), DataSetOptUtil.extendFuncs);
            Map<String, Object> dataTemplate = CollectionsOpt.createHashMap(lshField, calculate);
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(dataTemplate));
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
