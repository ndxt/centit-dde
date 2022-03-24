package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.framework.common.ResponseData;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;

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
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String[] databaseCodes = bizOptJson.getObject("databaseName",String[].class);
        if (databaseCodes==null || databaseCodes.length==0){//默认提交当前线程所有事物
            AbstractSourceConnectThreadHolder.commitAll();
            return BuiltInOperation.getResponseSuccessData(0);
        }
        for (String databaseCode : databaseCodes) {//提交选中库的事物
            SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
            AbstractSourceConnectThreadHolder.commit(sourceInfo);
        }
        return BuiltInOperation.getResponseSuccessData(0);
    }
}
