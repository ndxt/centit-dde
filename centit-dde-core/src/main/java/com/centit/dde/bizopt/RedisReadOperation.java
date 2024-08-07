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
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.commons.lang3.StringUtils;

public class RedisReadOperation implements BizOperation {

    private SourceInfoMetadata sourceInfoMetadata;

    public RedisReadOperation(SourceInfoMetadata sourceInfoMetadata) {
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        SourceInfo redisInfo = sourceInfoMetadata.fetchSourceInfo(databaseCode);
        if (redisInfo == null ) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.redis_db_not_found", databaseCode));
        }

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", "redisRead");
        String redisKey = BuiltInOperation.getJsonFieldString(bizOptJson, "redisKey", "");

        redisKey = StringBaseOpt.castObjectToString(
            DataSetOptUtil.fetchFieldValue(new BizModelJSONTransform(bizModel), redisKey), redisKey);

        StatefulRedisConnection<String, String> rc = AbstractSourceConnectThreadHolder.fetchRedisConnect(redisInfo);
        RedisCommands<String, String> commands = rc.sync();
        String strData = commands.get(redisKey);
        if(StringUtils.isBlank(strData)){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.PARAMETER_NOT_CORRECT,
                dataOptContext.getI18nMessage("dde.614.parameter_not_correct", "redisKey"));
        }

        DataSet dataSet = new DataSet(JSON.parse(strData));
        bizModel.putDataSet(targetDsName, dataSet);

        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
