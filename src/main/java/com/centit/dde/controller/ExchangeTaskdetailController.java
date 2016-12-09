package com.centit.dde.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;

@Controller
@RequestMapping("/exchangetaskdetail")
public class ExchangeTaskdetailController extends BaseEntityDwzAction<ExchangeTaskdetail> {
    private static final Log log = LogFactory.getLog(ExchangeTaskdetailController.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private ExchangeTaskdetailManager exchangeTaskdetailMag;

    @RequestMapping(value="/setExchangeTaskdetailManager")
    public void setExchangeTaskdetailManager(ExchangeTaskdetailManager basemgr) {
        exchangeTaskdetailMag = basemgr;
        this.setBaseEntityManager(exchangeTaskdetailMag);
    }

    @RequestMapping(value="/delete",method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
