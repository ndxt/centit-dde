package com.centit.app.service.impl;

import com.centit.app.dao.InnermsgRecipientDao;
import com.centit.app.po.InnermsgRecipient;
import com.centit.app.service.InnermsgRecipientManager;
import com.centit.core.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class InnermsgRecipientManagerImpl extends BaseEntityManagerImpl<InnermsgRecipient> implements
        InnermsgRecipientManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(InnermsgRecipientManager.class);

    private InnermsgRecipientDao innermsgRecipientDao;

    public void setInnermsgRecipientDao(InnermsgRecipientDao baseDao) {
        this.innermsgRecipientDao = baseDao;
        setBaseDao(this.innermsgRecipientDao);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InnermsgRecipient> listUnReaderInnermsg(InnermsgRecipient ir) {

        return this.innermsgRecipientDao.getHibernateTemplate().findByExample(ir);
    }

}
