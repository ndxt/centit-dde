package com.centit.dde.service;

import java.util.List;

import com.centit.dde.po.ExchangeTask;
import com.centit.framework.core.service.BaseEntityManager;

public interface ExchangeTaskManager extends BaseEntityManager<ExchangeTask> {
    public List<List<Object>> getSqlValues(DatabaseInfo DatabaseInfo, String sql);

    public List<Object> insertDatas(DatabaseInfo DatabaseInfo, String sql, List<List<Object>> datas);

    public String getMapinfoName(Long mapinfoId);

    public Long getNewTaskId();

    /**
     * 创建新的定时任务
     *
     * @param exchangeTask
     */
    public void saveNewTimerTask(ExchangeTask exchangeTask);

    /**
     * 关闭原有定时任务
     *
     * @param exchangeTask
     * @return
     */
    public boolean delTimerTask(ExchangeTask exchangeTask);

    /**
     * 更新原有定时任务
     *
     * @param exchangeTask
     * @return
     */
    public boolean updateTimerTask(ExchangeTask exchangeTask);
    
    public boolean executeTask(Long taskID,String userCode,String runType);

}
