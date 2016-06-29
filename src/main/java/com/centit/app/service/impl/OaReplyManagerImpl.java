package com.centit.app.service.impl;

import com.centit.app.dao.OaReplyDao;
import com.centit.app.po.OaReply;
import com.centit.app.service.OaReplyManager;
import com.centit.core.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OaReplyManagerImpl extends BaseEntityManagerImpl<OaReply>
        implements OaReplyManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaReplyManager.class);

    private OaReplyDao oaReplyDao;

    public void setOaReplyDao(OaReplyDao baseDao) {
        this.oaReplyDao = baseDao;
        setBaseDao(this.oaReplyDao);
    }

}

