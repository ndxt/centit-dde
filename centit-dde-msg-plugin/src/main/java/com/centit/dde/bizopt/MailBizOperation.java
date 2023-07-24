package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.model.basedata.NoticeMessage;
import com.centit.msgpusher.plugins.EMailMsgPusher;
import com.centit.msgpusher.plugins.SystemUserEmailSupport;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.support.security.SecurityOptUtils;
import org.apache.commons.lang3.StringUtils;

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
            messageManager.setEmailServerUser(sourceInfo.getUsername());
            messageManager.setEmailServerPwd(SecurityOptUtils.decodeSecurityString(sourceInfo.getPassword()));
            String topUnit = dataOptContext.getTopUnit();
            if(StringUtils.isBlank(topUnit)){
                topUnit = bizModel.fetchTopUnit();
            }
            messageManager.setTopUnit(topUnit);
            messageManager.setUserEmailSupport(new SystemUserEmailSupport());
            ResponseData responseData= messageManager.sendMessage("system", optUsers,
                NoticeMessage.create().operation(dataOptContext.getOsId()).method(dataOptContext.getOptId()).subject(subject)
                    .content(content));
            bizModel.putDataSet(id, new DataSet(responseData));
            return responseData;
        }
        return ResponseData.makeErrorMessage(500, "IM 发送没有实现");
    }
}
