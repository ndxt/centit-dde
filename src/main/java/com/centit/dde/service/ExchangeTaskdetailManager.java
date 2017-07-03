package com.centit.dde.service;

import java.util.List;

import com.centit.dde.po.ExchangeTaskDetail;
import com.centit.dde.po.ExchangeTaskdetailId;
import com.centit.framework.core.service.BaseEntityManager;

public interface ExchangeTaskdetailManager extends BaseEntityManager<ExchangeTaskDetail,ExchangeTaskdetailId> {
    public List<Long> getMapinfoIdUsed(Long taskId);

    public Long getMapinfoOrder(Long taskId);

    public void deleteDetails(Long taskId, Long mapinfoId);

    public String getMapinfoName(Long mapinfoId);

    public void deleteDetailsByMapinfoId(Long mapinfoId);

    /**
     * 添加交换对应关系
     *
     * @param taskId
     * @param mapinfoId
     */
    void saveObject(Long taskId, List<Long> mapinfoId);
}
