package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;

/**
 * 提交当前线程的事物
 *      提交选中库的事物   没选默认提交当前线程的所有事物，但是不移除连接
 */
public class TransactionCommitOperation implements BizOperation {
    private SourceInfoMetadata sourceInfoMetadata;

    public TransactionCommitOperation(SourceInfoMetadata sourceInfoMetadata) {
        this.sourceInfoMetadata = sourceInfoMetadata;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        JSONArray databaseName = bizOptJson.getJSONArray("databaseName");
        Object[] databaseCodeArr = CollectionsOpt.listToArray(databaseName);
        if (databaseCodeArr==null || databaseCodeArr.length==0){//默认提交当前线程所有事物
            AbstractSourceConnectThreadHolder.commitAll();
            return BuiltInOperation.createResponseSuccessData(0);
        }
        for (Object databaseCode : databaseCodeArr) {//提交选中库的事物
            String databaseCodeStr = StringBaseOpt.castObjectToString(databaseCode);
            SourceInfo sourceInfo = sourceInfoMetadata.fetchSourceInfo(databaseCodeStr);
            AbstractSourceConnectThreadHolder.commit(sourceInfo);
        }
        return BuiltInOperation.createResponseSuccessData(databaseCodeArr.length);
    }
}
