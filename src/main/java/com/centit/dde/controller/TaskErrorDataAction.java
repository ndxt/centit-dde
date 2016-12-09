package com.centit.dde.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.TaskErrorDataManager;
import com.centit.framework.core.common.JsonResultUtils;

@Controller
@RequestMapping(name="taskerrordata")
public class TaskErrorDataAction extends BaseEntityDwzAction<TaskErrorData> {
    private static final Log log = LogFactory.getLog(TaskErrorDataAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private TaskErrorDataManager taskErrorDataMag;

    public void setTaskErrorDataManager(TaskErrorDataManager basemgr) {
        taskErrorDataMag = basemgr;
        this.setBaseEntityManager(taskErrorDataMag);
    }


    @RequestMapping(value="/delete",method = {RequestMethod.PUT})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
