package com.centit.dde.controller;

import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/exchangetaskdetail")
public class ExchangeTaskdetailController extends BaseController {
    private static final Log log = LogFactory.getLog(ExchangeTaskdetailController.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");
    private static final long serialVersionUID = 1L;
    @Resource

    private ExchangeTaskdetailManager exchangeTaskdetailMag;

    @RequestMapping(value="/delete",method = {RequestMethod.DELETE})
    public void delete(ExchangeTaskdetail exchangeTaskdetail,HttpServletRequest request,HttpServletResponse response) {
        exchangeTaskdetailMag.deleteObject(exchangeTaskdetail);
        JsonResultUtils.writeSuccessJson(response);
    }
}
