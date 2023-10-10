package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.BizModelService;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.Md5Encoder;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhf
 */
@Service
public class BizModelServiceImpl implements BizModelService {
    @Autowired(required = false)
    private RedisClient redisClient;

    @Autowired
    private TaskRun taskRun;

    @Override
    public DataOptResult runBizModel(DataPacketInterface dataPacket, DataOptContext optContext) {
        if (notNeedBuf(dataPacket)) {
            return taskRun.runTask(dataPacket, optContext);
        }
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
        return redisClient == null
            //|| ! ConstantValue.TASK_TYPE_GET.equals(dataPacket.getTaskType())
            || dataPacket.getBufferFreshPeriodType() == null
            || dataPacket.getBufferFreshPeriodType() == ConstantValue.MINUS_ONE
            || dataPacket.getBufferFreshPeriod() == null
            || dataPacket.getBufferFreshPeriod() <= 0;
    }

    private Object fetchBizModelFromBuf(String key) {
        String strObj;
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();
        strObj = commands.get(key);
        connection.close();
        return JSON.parse(strObj);
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
        if (notNeedBuf(dataPacket)) {
            return;
        }
        StatefulRedisConnection<String, String> connection = redisClient.connect();
        RedisCommands<String, String> commands = connection.sync();
        commands.set(key, StringBaseOpt.objectToString(returnData));
        int seconds = dataPacket.getBufferFreshPeriod();
        int iPeriod = dataPacket.getBufferFreshPeriodType();
        //按分
        if (iPeriod == ConstantValue.ONE) {
            commands.expire(key, seconds * 60);
        }
        //按时
        else if (iPeriod == ConstantValue.TWO) {
            commands.expire(key, seconds * 3600);
        }
        //按天
        else if (iPeriod == ConstantValue.THREE) {
            int days = seconds * 24 * 3600;
            commands.expire(key, days);
        }
        connection.close();
    }

}
