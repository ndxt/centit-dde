package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.impl.BizOptFlowImpl;
import com.centit.dde.utils.Constant;
import com.centit.dde.vo.CycleVo;
import com.centit.framework.common.ResponseData;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 跳出循环
 */
@Component
public class JumpOutBizOperation implements BizOperation {

    @Resource
    BizOptFlowImpl bizOptFlow;

    //注册服务
    @PostConstruct
    private void init(){
        bizOptFlow.registerOperation(Constant.CYCLE_JUMP_OUT,new JumpOutBizOperation());
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        return BuiltInOperation.getResponseSuccessData(0);
    }
}
