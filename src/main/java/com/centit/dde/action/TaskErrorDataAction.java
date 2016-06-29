package com.centit.dde.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.TaskErrorDataManager;


public class TaskErrorDataAction extends BaseEntityDwzAction<TaskErrorData> {
    private static final Log log = LogFactory.getLog(TaskErrorDataAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private TaskErrorDataManager taskErrorDataMag;

    public void setTaskErrorDataManager(TaskErrorDataManager basemgr) {
        taskErrorDataMag = basemgr;
        this.setBaseEntityManager(taskErrorDataMag);
    }


    public String delete() {
        super.delete();

        return "delete";
    }
}
