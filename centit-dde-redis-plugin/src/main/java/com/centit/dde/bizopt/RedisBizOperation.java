package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.RedisConfig;
import com.centit.dde.config.RedisTypeEnum;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.entity.RedisParamVo;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

public class RedisBizOperation implements BizOperation {

    private SourceInfoDao sourceInfoDao;

    public RedisBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception{
        RedisParamVo redisParamVo = JSON.parseObject(bizOptJson.toJSONString(), RedisParamVo.class);
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById("dataSourceId");
        RedisConfig redisConfig = new RedisConfig();
        String redisType = redisParamVo.getRedisType();
        RedisTypeEnum redisTypeEnum=
            redisType.equals("singleNode")?RedisTypeEnum.SINGLENODE
                :redisType.equals("cluster")?RedisTypeEnum.CLUSTER
                :redisType.equals("sentinel")?RedisTypeEnum.SENTINEL
                :RedisTypeEnum.SINGLENODE;
        RedisTemplate<String, Object> redisTemplate = redisConfig.redisTemplate(sourceInfo, redisTypeEnum);
        Class aClass = Class.forName("com.centit.dde.utils.RedisOperationUtils");
        Class<?>[] parameterTypes=null;
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().equals(redisParamVo.getMethodName())){
                parameterTypes = declaredMethod.getParameterTypes();
            }
        }
        //设置redisTemplate
        Method setRedisTemplate = aClass.getMethod("setRedisTemplate", RedisTemplate.class);
        Object instance = aClass.newInstance();
        setRedisTemplate.invoke(instance,redisTemplate);
        Method method=aClass.getMethod(redisParamVo.getMethodName(),parameterTypes);
        Object invoke = method.invoke(instance, redisParamVo.getMethodParam());
        bizModel.putDataSet(redisParamVo.getId(),new SimpleDataSet(invoke));
        return ResponseSingleData.makeResponseData(bizModel.getDataSet(redisParamVo.getId()).getSize());
    }
}
