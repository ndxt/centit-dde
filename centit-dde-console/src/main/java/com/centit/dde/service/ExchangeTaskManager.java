package com.centit.dde.service;

import com.centit.dde.po.ExchangeTask;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.framework.ip.po.DatabaseInfo;
import sun.rmi.runtime.Log;

import java.util.List;

public interface ExchangeTaskManager extends BaseEntityManager<ExchangeTask,Long> {
    List<List<Object>> getSqlValues(DatabaseInfo DatabaseInfo, String sql);

    List<Object> insertDatas(DatabaseInfo DatabaseInfo, String sql, List<List<Object>> datas);

    String getMapinfoName(Long mapinfoId);

    Long getNewTaskId();

    /**
     * 创建新的定时任务
     *
     * @param exchangeTask
     */
    void saveNewTimerTask(ExchangeTask exchangeTask);

    /**
     * 关闭原有定时任务
     *
     * @param exchangeTask
     * @return
     */
    boolean delTimerTask(ExchangeTask exchangeTask);

    /**
     * 更新原有定时任务
     *
     * @param exchangeTask
     * @return
     */
    boolean updateTimerTask(ExchangeTask exchangeTask);
    
    boolean executeTask(Long taskID,String userCode,String runType);

    void save(ExchangeTask exchangeTask, CentitUserDetails user);

    public ExchangeTask getObjectById(Long taskId);

    public void deleteObjectByIdInfo(Long taskId);

    public void editAndsave(ExchangeTask exchangeTask);
}
