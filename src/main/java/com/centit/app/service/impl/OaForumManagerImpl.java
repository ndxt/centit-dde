package com.centit.app.service.impl;

import com.centit.app.dao.OaForumDao;
import com.centit.app.po.OaForum;
import com.centit.app.service.OaForumManager;
import com.centit.core.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OaForumManagerImpl extends BaseEntityManagerImpl<OaForum>
        implements OaForumManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OaForumManager.class);

    private OaForumDao oaForumDao;

    public void setOaForumDao(OaForumDao baseDao) {
        this.oaForumDao = baseDao;
        setBaseDao(this.oaForumDao);
    }


    public void saveInnermsg(OaForum innermsg) {
        Long forumid = innermsg.getForumid();
        if (forumid == null || forumid == 0) {
            forumid = oaForumDao.getNextForumID();
            innermsg.setForumid(forumid);
        }

        oaForumDao.saveObject(innermsg);
    }

}

