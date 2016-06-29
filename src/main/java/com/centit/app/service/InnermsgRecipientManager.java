package com.centit.app.service;

import java.util.List;

import com.centit.app.po.InnermsgRecipient;
import com.centit.core.service.BaseEntityManager;

public interface InnermsgRecipientManager extends BaseEntityManager<InnermsgRecipient> {
    /**
     * 当前用户未读消息
     *
     * @param ir
     * @return
     */
    List<InnermsgRecipient> listUnReaderInnermsg(InnermsgRecipient ir);
}
