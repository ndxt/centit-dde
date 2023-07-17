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
import com.centit.support.algorithm.StringBaseOpt;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.lang3.StringUtils;

public class RedisReadOperation implements BizOperation {

    private SourceInfoDao sourceInfoDao;

    public RedisReadOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        SourceInfo redisInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        if (redisInfo == null ) {
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "配置信息不正确，没有对应的Redis数据库：" +  databaseCode);
        }


        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", "redisRead");
        String redisKey = BuiltInOperation.getJsonFieldString(bizOptJson, "redisKey", "");

        redisKey = StringBaseOpt.castObjectToString(
            DataSetOptUtil.fetchFieldValue(new BizModelJSONTransform(bizModel), redisKey), redisKey);

        StatefulRedisConnection<String, String> rc = AbstractSourceConnectThreadHolder.fetchRedisConnect(redisInfo);
        RedisCommands<String, String> commands = rc.sync();
        String strData = commands.get(redisKey);
        if(StringUtils.isBlank(strData)){
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION,
                "读取数据失败或者对应的值为空：" + redisKey);
        }

        DataSet dataSet = new DataSet(JSON.parse(strData));
        bizModel.putDataSet(targetDsName, dataSet);

        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
