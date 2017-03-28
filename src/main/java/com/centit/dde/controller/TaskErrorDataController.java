package com.centit.dde.controller;

import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.TaskErrorDataManager;
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
@RequestMapping("/taskerrordata")
public class TaskErrorDataController extends BaseController {
    private static final Log log = LogFactory.getLog(TaskErrorDataController.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    @Resource

    private TaskErrorDataManager taskErrorDataMag;

    @RequestMapping(value="/delete",method = {RequestMethod.PUT})
    public void delete(TaskErrorData object,HttpServletRequest request,HttpServletResponse response) {
        taskErrorDataMag.deleteObjectById(object.getDataId());;

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
