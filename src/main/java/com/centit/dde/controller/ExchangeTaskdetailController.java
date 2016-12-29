package com.centit.dde.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;


@Controller
@RequestMapping("/exchangetaskdetail")
public class ExchangeTaskdetailController extends BaseController {
    private static final Log log = LogFactory.getLog(ExchangeTaskdetailController.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");
    private static final long serialVersionUID = 1L;
    @Resource

    private ExchangeTaskdetailManager exchangeTaskdetailMag;

    @RequestMapping(value="/delete/{{mapinfoId}}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable Long mapinfoId,HttpServletRequest request,HttpServletResponse response) {
        exchangeTaskdetailMag.deleteDetailsByMapinfoId(mapinfoId);
//        super.delete();
//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
