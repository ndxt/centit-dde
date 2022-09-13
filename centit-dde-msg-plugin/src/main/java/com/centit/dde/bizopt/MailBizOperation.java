package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.plugins.EMailMsgPusher;
import com.centit.msgpusher.plugins.IUserEmailSupport;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.support.security.AESSecurityUtils;

import java.util.Set;

/**
 * @author zhf
 */
public class MailBizOperation implements BizOperation {
    private static final String SEND_TYPE_MAIL = "M";
    private SourceInfoDao sourceInfoDao;

    public MailBizOperation(SourceInfoDao sourceInfoDao) {
        this.sourceInfoDao = sourceInfoDao;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sendType = bizOptJson.getString("sendType");
        String id=bizOptJson.getString("id");
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(bizOptJson.getString("mailServer"));
        BizModelJSONTransform jsonTransform = new BizModelJSONTransform(bizModel);
        Set<String> optUsers = CollectionsOpt.createHashSet(
            StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("optUsers"), jsonTransform)));
        String subject = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("subject"), jsonTransform));
        String content = StringBaseOpt.castObjectToString(JSONTransformer.transformer(bizOptJson.getString("content"), jsonTransform));
        if (SEND_TYPE_MAIL.equals(sendType)) {
            EMailMsgPusher messageManager = new EMailMsgPusher();
            messageManager.setEmailServerHost(sourceInfo.getDatabaseUrl());
            messageManager.setEmailServerHostUser(sourceInfo.getUsername());
            messageManager.setEmailServerHostPwd(AESSecurityUtils.decryptParameterString(sourceInfo.getPassword()));
            messageManager.setTopUnit(dataOptContext.getTopUnit());
            messageManager.setUserEmailSupport(new IUserEmailSupport() {
            });
            ResponseData responseData= messageManager.sendMessage("system", optUsers,
                NoticeMessage.create().operation(dataOptContext.getOsId()).method(dataOptContext.getOptId()).subject(subject)
                    .content(content));
            bizModel.putDataSet(id, new DataSet(responseData));
            return responseData;
        }
        return null;
    }
}
