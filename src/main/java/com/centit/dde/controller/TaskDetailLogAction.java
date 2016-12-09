package com.centit.dde.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.po.TaskErrorData;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.TaskDetailLogManager;
import com.centit.framework.core.common.JsonResultUtils;

@Controller
@RequestMapping("/TaskDetailLog")
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


    @RequestMapping(value="edit",method = {RequestMethod.PUT})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
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
//            return EDIT;
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="save",method = {RequestMethod.PUT})
    public void save(HttpServletRequest request,HttpServletResponse response) {
        object.replaceTaskErrorDatas(taskErrorDatas);
        super.save();
        
//        return super.save();
        JsonResultUtils.writeSuccessJson(response);
    }


    @RequestMapping(value="delete",method = {RequestMethod.DELETE})
    public String delete(HttpServletRequest request,HttpServletResponse response) {
        super.delete();

//        return "delete";
        JsonResultUtils.writeSuccessJson(response);
    }
}
