package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.vo.AssignmentVo;
import com.centit.framework.common.ResponseData;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 赋值标签
 */
@Component
public class AssignmentBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        AssignmentVo assignmentVo =bizOptJson.toJavaObject(AssignmentVo.class);
        DataSet dataSet = bizModel.getDataSet(assignmentVo.getSource());
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：未指定数据集！");
        }
        DataSet newDataSet;//默认引用数据集
        if ("1".equals(assignmentVo.getAssignType())){//复制
            newDataSet=SerializationUtils.clone(new SimpleDataSet(dataSet.getData()));
        }else if ("3".equals(assignmentVo.getAssignType())){//手动赋值
            newDataSet=new SimpleDataSet(assignmentVo.getData());
        }else {
            newDataSet=dataSet;
        }
        if (StringUtils.isNotBlank(assignmentVo.getExpression())){
            Object data = JSONTransformer.transformer(assignmentVo.getExpression(), new BizModelJSONTransform(bizModel));
            newDataSet= new SimpleDataSet(data);
        }
        bizModel.putDataSet(assignmentVo.getId(),newDataSet);
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(assignmentVo.getId()).getSize());
    }
}
