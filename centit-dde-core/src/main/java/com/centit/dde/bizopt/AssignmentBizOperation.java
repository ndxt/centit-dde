package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DatasetVariableTranslate;
import com.centit.framework.common.ResponseData;

public class AssignmentBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //赋值目标
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "target", null);
        //数据来源
        String sourceDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", null);
        //复制类型
        //assignType 复制类型， assign 直接赋值， property 对属性复制 ， append 追加元素
        String assignType = BuiltInOperation.getJsonFieldString(bizOptJson, "assignType", "assign");
        //取值表达式
        String formula = BuiltInOperation.getJsonFieldString(bizOptJson, "formula", ".");
        DataSet dataSet =bizModel.fetchDataSetByName(sourceDsName);
        Object sourceData = new DatasetVariableTranslate(dataSet).getVarValue(formula);

        DataSet targetDataSet =bizModel.fetchDataSetByName(targetDsName);
        if("property".equals(assignType)){
            //复制属性
            String property = BuiltInOperation.getJsonFieldString(bizOptJson, "property", ".");
            // property
            if( targetDataSet.getSize() < 1){

            } else if( targetDataSet.getSize() == 1){

            } else {

            }
        } else {

        }
        return BuiltInOperation.createResponseSuccessData(targetDataSet.getSize());
    }
}
