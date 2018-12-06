package com.centit.dde.service;

import com.centit.dde.po.ExchangeTaskDetail;
import com.centit.dde.po.ExchangeTaskDetailId;
import com.centit.framework.jdbc.service.BaseEntityManager;

import java.util.List;

public interface ExchangeTaskdetailManager extends BaseEntityManager<ExchangeTaskDetail,ExchangeTaskDetailId> {
    public List<Long> getMapinfoIdUsed(Long taskId);

    public Long getMapinfoOrder(Long taskId);

    public void deleteDetails(Long taskId, Long mapinfoId);

    public String getMapinfoName(Long mapinfoId);

    public void deleteDetailsByMapinfoId(Long mapinfoId);

    public List<ExchangeTaskDetail> getTaskDetails(Long taskId);

    /**
     * 添加交换对应关系
     *
     * @param taskId
     * @param mapinfoId
     */
    void saveObject(Long taskId, List<Long> mapinfoId);
}
