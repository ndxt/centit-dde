package com.centit.dde.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.service.ExchangeTaskdetailManager;


public class ExchangeTaskdetailAction extends BaseEntityDwzAction<ExchangeTaskdetail> {
    private static final Log log = LogFactory.getLog(ExchangeTaskdetailAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private ExchangeTaskdetailManager exchangeTaskdetailMag;

    public void setExchangeTaskdetailManager(ExchangeTaskdetailManager basemgr) {
        exchangeTaskdetailMag = basemgr;
        this.setBaseEntityManager(exchangeTaskdetailMag);
    }

    @Override
    public String delete() {
        super.delete();
        return "delete";
    }
}
