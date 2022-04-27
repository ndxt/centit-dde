package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;

/**
 * 提交当前线程的事物
 *      提交选中库的事物   没选默认提交当前线程的所有事物，但是不移除连接
 */
public class TransactionCommitOperation implements BizOperation {
    private SourceInfoDao sourceInfoDao;

    public TransactionCommitOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
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
            SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseCodeStr);
            AbstractSourceConnectThreadHolder.commit(sourceInfo);
        }
        return BuiltInOperation.createResponseSuccessData(databaseCodeArr.length);
    }
}
