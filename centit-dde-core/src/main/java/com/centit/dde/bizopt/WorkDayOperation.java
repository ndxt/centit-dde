package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.oa.po.WorkDay;
import com.centit.product.oa.service.WorkDayManager;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.DateTimeSpan;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author codefan@sina.com
 * 工作日组件，暂未启用，根据业务的需求再做优化
 */
public class WorkDayOperation implements BizOperation {
    private WorkDayManager workDayManager;

    public WorkDayOperation(WorkDayManager workDayManager) {
        this.workDayManager = workDayManager;
    }
    /*
        boolean isWorkDay(String topUnit, Date startDate);
        int calcHolidays(String topUnit, Date startDate, Date endDate);
        int calcWorkDays(String topUnit, Date startDate, Date endDate);
        Date calcWorkingDeadline(String topUnit, Date startDate, DateTimeSpan timeLimit);
        List<WorkDay> rangeHolidays(String topUnit, Date startDate, Date endDate);
        List<WorkDay> rangeWorkDays(String topUnit, Date startDate, Date endDate);
    */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) {
        String calcType = bizOptJson.getString("calcType");
        if(!StringUtils.equalsAny(calcType, "isWorkDay","calcHolidays","calcWorkDays",
            "calcWorkingDeadline","rangeHolidays","rangeWorkDays")) {
            return ResponseData.makeErrorMessage(
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "calcType"));
        }
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        String topUnit = dataOptContext.getTopUnit();
        // WorkDay
        Date currDate = DatetimeOpt.castObjectToDate(
            DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("startDate")));
        if(currDate == null) {
            return BuiltInOperation.createResponseData(0,1, 701, "参数startDate不能转换为时间");
        }
        if(calcType.equals("isWorkDay")) {
            boolean isWorkDay = workDayManager.isWorkDay(topUnit, currDate);
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(isWorkDay));
            return BuiltInOperation.createResponseSuccessData(1);
        }
        if(calcType.equals("calcWorkingDeadline")) {
            String timeLimit = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("timeLimit")));
            if(StringUtils.isBlank(timeLimit)) {
                return BuiltInOperation.createResponseData(0,1, 701, "参数timeLimit不完整");
            }
            Date deadlineDate = workDayManager.calcWorkingDeadline(topUnit, currDate,new DateTimeSpan(timeLimit));
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(deadlineDate));
            return BuiltInOperation.createResponseSuccessData(1);
        }
        Date endDate = DatetimeOpt.castObjectToDate(
            DataSetOptUtil.fetchFieldValue(transform, bizOptJson.getString("endDate")));
        if(endDate == null) {
            return BuiltInOperation.createResponseData(0,1, 701, "参数endDate不能转换为时间");
        }
        switch (calcType) {
            case "calcHolidays":{
                int holidays = workDayManager.calcHolidays(topUnit, currDate, endDate);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(holidays));
                return BuiltInOperation.createResponseSuccessData(1);
            }
            case "calcWorkDays":{
                int workdays = workDayManager.calcWorkDays(topUnit, currDate, endDate);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(workdays));
                return BuiltInOperation.createResponseSuccessData(1);
            }
            case "rangeHolidays":{
                List<WorkDay> workdays = workDayManager.rangeHolidays(topUnit, currDate, endDate);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(workdays));
                return BuiltInOperation.createResponseSuccessData(1);
            }
            case "rangeWorkDays":{
                List<WorkDay> workdays = workDayManager.rangeWorkDays(topUnit, currDate, endDate);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(workdays));
                return BuiltInOperation.createResponseSuccessData(1);
            }
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
