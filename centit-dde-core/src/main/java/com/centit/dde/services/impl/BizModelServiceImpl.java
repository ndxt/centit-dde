package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.BizModelService;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhf
 */
@Service
public class BizModelServiceImpl implements BizModelService {

    @Autowired(required = false)
    private RedisTemplate<String, Object> objectRedisTemplate;

    @Autowired
    private TaskRun taskRun;

    @Override
    public DataOptResult runBizModel(DataPacketInterface dataPacket, DataOptContext optContext) {
        if (notNeedBuf(dataPacket)) {
            return taskRun.runTask(dataPacket, optContext);
        }
        Map<String,Object> bufferStack = optContext.getCallStackData();
        bufferStack.remove(ConstantValue.REQUEST_COOKIES_TAG);
        bufferStack.remove(ConstantValue.REQUEST_HEADERS_TAG);
        String key = makeDataPacketBufId(dataPacket, optContext.getCallStackData());
        DataOptResult optResult;
        Object bufData = fetchBizModelFromBuf(key);
        //第一次执行或者换成失效的时候执行
        if (bufData == null) {
            optResult = taskRun.runTask(dataPacket, optContext);
            if (optResult.getResultType() == DataOptResult.RETURN_OPT_DATA) {
                setBizModelBuf(optResult.getResultObject(), dataPacket, key);
            }
        } else {
            optResult = new DataOptResult();
            optResult.setResultObject(bufData);
        }
        return optResult;
    }

    private boolean notNeedBuf(DataPacketInterface dataPacket) {
        return objectRedisTemplate == null
            //|| ! ConstantValue.TASK_TYPE_GET.equals(dataPacket.getTaskType())
            || dataPacket.getBufferFreshPeriodType() == null
            || dataPacket.getBufferFreshPeriodType() == ConstantValue.MINUS_ONE
            || dataPacket.getBufferFreshPeriod() == null
            || dataPacket.getBufferFreshPeriod() <= 0;
    }

    private Object fetchBizModelFromBuf(String key) {
        return objectRedisTemplate.opsForValue().get(key);
    }

    private String makeDataPacketBufId(DataPacketInterface dataPacket, Map<String, Object> paramsMap) {
        //String dateString = DatetimeOpt.convertTimestampToString(dataPacket.getRecordDate());
        String params = JSON.toJSONString(paramsMap, JSONWriter.Feature.MapSortField);
        StringBuilder temp = new StringBuilder("packet:");
        temp.append(dataPacket.getPacketId())
            .append(":")
            .append(params);
        //.append(dateString);
        return Md5Encoder.encode(temp.toString());
    }

    private void setBizModelBuf(Object returnData, DataPacketInterface dataPacket, String key) {
        if (notNeedBuf(dataPacket) || returnData == null) {
            return;
        }
        int seconds = dataPacket.getBufferFreshPeriod();
        int iPeriod = dataPacket.getBufferFreshPeriodType();
        //按分
        if (iPeriod == ConstantValue.ONE) {
            objectRedisTemplate.opsForValue().set(key, returnData, seconds, TimeUnit.MINUTES);
        }
        //按时
        else if (iPeriod == ConstantValue.TWO) {
            objectRedisTemplate.opsForValue().set(key, returnData, seconds, TimeUnit.HOURS);
        }
        //按天
        else if (iPeriod == ConstantValue.THREE) {
            objectRedisTemplate.opsForValue().set(key, returnData, seconds, TimeUnit.DAYS);
        } else {
            objectRedisTemplate.opsForValue().set(key, returnData);
        }
    }

}
