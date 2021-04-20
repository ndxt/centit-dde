package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.impl.BizOptFlowImpl;
import com.centit.dde.utils.Constant;
import com.centit.dde.vo.CycleVo;
import com.centit.dde.vo.FinishVo;
import com.centit.dde.vo.JumpOutVo;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结束循环
 */
@Component
public class FinishBizOperation implements BizOperation {

    @Resource
    BizOptFlowImpl bizOptFlow;

    //注册服务
    @PostConstruct
    private void init(){
        bizOptFlow.registerOperation(Constant.CYCLE_FINISH,new FinishBizOperation());
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        //跳出循环节点信息
        String endJumpOutNodeData = bizOptJson.getString("endJumpOutNodeData");
        JumpOutVo  jumpOutVo = JSONObject.parseObject(endJumpOutNodeData, JumpOutVo.class);
        if (jumpOutVo!=null){
            if (Constant.CYCLE_END_BREAK.equals(jumpOutVo.getEndType())){//break 结束整个循环
                bizOptJson.put("isEnd",true);
                return BuiltInOperation.getResponseSuccessData(0);
            }else if (Constant.CYCLE_END_CONTINUE.equals(jumpOutVo.getEndType())){//continue  什么也不做，继续往下执行

            }else {
                return ResponseData.makeErrorMessage("跳出循环异常，未选择结束类型参数！");
            }
        }
        //循环节点信息
        String startNodeData = bizOptJson.getString("startNodeData");
        CycleVo cycleVo = JSONObject.parseObject(startNodeData,CycleVo.class);
        int nextDataIndex = cycleVo.getNextDataIndex()==null?0:cycleVo.getNextDataIndex();
        int parentNextDataIndex = cycleVo.getParentNextDataIndex()==null?0:cycleVo.getParentNextDataIndex();
        String flag = cycleVo.getFlag();
        String sourceId ="";
        if ("1".equals(cycleVo.getAssignType())){
            sourceId = cycleVo.getSource()+"_temp";
        }else {
            sourceId = cycleVo.getSource();
        }
        //获取原始数据集
        Map<String, Object> dataMap = new HashMap<>();
        if ("isObject".equals(flag)){
            dataMap = bizModel.getDataSet(sourceId).getDataAsList().get(0);
        }else if ("isArray".equals(flag) && cycleVo.getIsEnd()){
            dataMap = bizModel.getDataSet(sourceId).getDataAsList().get(parentNextDataIndex-1);
        }else {
            dataMap = bizModel.getDataSet(sourceId).getDataAsList().get(parentNextDataIndex);
        }
        Object object = dataMap.get(cycleVo.getSubsetFieldName());
        if (object instanceof JSONObject){
            bizOptJson.put("isEnd",true);
        }else if (object instanceof JSONArray){
            JSONArray jsonArray = (JSONArray)object;
            //当2个值相等的时候说明全部数据已经遍历完，结束循环
            if (nextDataIndex>=jsonArray.size()){
                bizOptJson.put("isEnd",true);
            }
        }else {
            return ResponseData.makeErrorMessage("未知类型，转换失败！");
        }
        return BuiltInOperation.getResponseSuccessData(nextDataIndex);
    }
}
