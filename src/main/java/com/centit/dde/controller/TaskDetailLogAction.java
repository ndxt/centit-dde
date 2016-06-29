package com.centit.dde.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.TaskDetailLogManager;

public class TaskDetailLogAction extends BaseEntityDwzAction<TaskDetailLog> {
    private static final Log log = LogFactory.getLog(TaskDetailLogAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private TaskDetailLogManager taskDetailLogMag;
    private ExchangeMapinfoManager exchangeMapinfoManager;


    public ExchangeMapinfoManager getExchangeMapinfoManager() {
        return exchangeMapinfoManager;
    }

    public void setExchangeMapinfoManager(
            ExchangeMapinfoManager exchangeMapinfoManager) {
        this.exchangeMapinfoManager = exchangeMapinfoManager;
    }

    public void setTaskDetailLogManager(TaskDetailLogManager basemgr) {
        taskDetailLogMag = basemgr;
        this.setBaseEntityManager(taskDetailLogMag);
    }

    private List<TaskErrorData> taskErrorDatas;

    public List<TaskErrorData> getNewTaskErrorDatas() {
        return this.taskErrorDatas;
    }

    public void setNewTaskErrorDatas(List<TaskErrorData> taskErrorDatas) {
        this.taskErrorDatas = taskErrorDatas;
    }


    @Override
    public String edit() {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                TaskDetailLog o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }
            if (null != object.getMapinfoId()) {
                ServletActionContext.getContext().put("exchangeMapinfo", exchangeMapinfoManager.getObjectById(object.getMapinfoId()));
            }
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String save() {
        object.replaceTaskErrorDatas(taskErrorDatas);

        return super.save();
    }


    public String delete() {
        super.delete();

        return "delete";
    }
}
