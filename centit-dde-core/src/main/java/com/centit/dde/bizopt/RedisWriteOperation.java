package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisWriteOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public RedisWriteOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        SourceInfo redisInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (redisInfo == null ){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.redis_db_not_found", databaseCode));
        }
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if(dataSet == null || dataSet.getData() ==null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found2", sourDsName));
        }

        String redisKey = BuiltInOperation.getJsonFieldString(bizOptJson, "redisKey", "");

        redisKey = StringBaseOpt.castObjectToString(
            DataSetOptUtil.fetchFieldValue(new BizModelJSONTransform(bizModel, dataSet.getData()), redisKey), redisKey);

        int expireTime = NumberBaseOpt.castObjectToInteger(
            BuiltInOperation.getJsonFieldString(bizOptJson, "expireTime", "30"), 30);
        String expireTimeUnit = BuiltInOperation.getJsonFieldString(bizOptJson, "expireTimeUnit", "D");

        StatefulRedisConnection<String, String> rc = AbstractSourceConnectThreadHolder.fetchRedisConnect(redisInfo);
        RedisCommands<String, String> commands = rc.sync();
        commands.set(redisKey, JSON.toJSONString(dataSet.getData()));
        //按分
        if ("M".equals(expireTimeUnit)) {
            commands.expire(redisKey, expireTime * 60);
        }
        //按时
        else if ("H".equals(expireTimeUnit)) {
            commands.expire(redisKey, expireTime * 3600);
        }
        //按天
        else if ("D".equals(expireTimeUnit)) {
            int days = expireTime * 24 * 3600;
            commands.expire(redisKey, days);
        }

        return BuiltInOperation.createResponseSuccessData(1);
    }
}
