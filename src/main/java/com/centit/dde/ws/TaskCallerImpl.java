package com.centit.dde.ws;

import com.centit.dde.service.ExchangeTaskManager;

import javax.jws.WebService;

@WebService(targetNamespace = "http://dde.centit.com/ws/")
public class TaskCallerImpl implements  TaskCaller{

    private ValidatorWs validatorWs;

    public void setValidatorWs(ValidatorWs validatorWs) {
        this.validatorWs = validatorWs;
    }

    
    ExchangeTaskManager exchangeTaskMag;
    public void setExchangeTaskManager(ExchangeTaskManager basemgr) {
        exchangeTaskMag = basemgr;
    }
    /**
     * 通过webService调用任务
     * @param userName
     * @param userPin
     * @param exportId 导出数据的ID，这个需要通过权限控制
     * @return
     */
    @Override
    public String callExchageTask(String userName, String userPin, long taskId){
        
        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }
        
        if(exchangeTaskMag.executeTask(taskId, userName, "2")){
            return "OK";
        }else{
            return "ERROR";
        }
    }

}
