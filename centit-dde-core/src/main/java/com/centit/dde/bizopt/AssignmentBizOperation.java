package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.vo.AssignmentVo;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 赋值标签
 */
@Component
public class AssignmentBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        AssignmentVo assignmentVo =bizOptJson.toJavaObject(AssignmentVo.class);
        DataSet dataSet = bizModel.getDataSet(assignmentVo.getSource());
        String assignType = assignmentVo.getAssignType();
        DataSet newDataSet;
        switch (assignType){
            case "1"://复制数据集
                if (dataSet==null){
                    return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：未指定数据集，或指定数据集值为NULL！");
                }
                newDataSet=SerializationUtils.clone(new SimpleDataSet(dataSet.getData()));
                break;
            case "2"://手动赋值
                Object data = assignmentVo.getData();
                if (data==null){
                    return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：请输入赋值数据！");
                }
                newDataSet = new SimpleDataSet(data);
                break;
            default://引用数据集
                newDataSet = dataSet;
                break;
        }
        if (StringUtils.isNotBlank(assignmentVo.getExpression())){//根据表达式生成新的数据集
            Object data = JSONTransformer.transformer(assignmentVo.getExpression(), new BizModelJSONTransform(bizModel));
            newDataSet= new SimpleDataSet(data);
        }
        bizModel.putDataSet(assignmentVo.getId(),newDataSet);
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(assignmentVo.getId()).getSize());
    }
}
