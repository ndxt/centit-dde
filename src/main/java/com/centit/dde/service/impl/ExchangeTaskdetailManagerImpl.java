package com.centit.dde.service.impl;

import com.centit.dde.dao.ExchangeTaskDetailDao;
import com.centit.dde.po.ExchangeTaskDetail;
import com.centit.dde.po.ExchangeTaskDetailId;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
@Service
public class ExchangeTaskdetailManagerImpl
        extends BaseEntityManagerImpl<ExchangeTaskDetail,ExchangeTaskDetailId, ExchangeTaskDetailDao>
        implements ExchangeTaskdetailManager {

    public static final Log log = LogFactory.getLog(ExchangeTaskdetailManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();
   
    private ExchangeTaskDetailDao exchangeTaskDetailDao;

    @Resource
    @NotNull
    public void setExchangeTaskDetailDao(ExchangeTaskDetailDao baseDao) {
        this.exchangeTaskDetailDao = baseDao;
        setBaseDao(this.exchangeTaskDetailDao);
    }

    public List<Long> getMapinfoIdUsed(Long taskId) {
        return this.exchangeTaskDetailDao.getMapinfoIdUsed(taskId);
    }

    public Long getMapinfoOrder(Long taskId) {
        return this.exchangeTaskDetailDao.getMapinfoOrder(taskId);
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        this.exchangeTaskDetailDao.deleteDetails(taskId, mapinfoId);
    }

    public String getMapinfoName(Long mapinfoId) {
        // return this.getMapInfoName(mapinfoId);
        return String.valueOf(mapinfoId);
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        this.exchangeTaskDetailDao.deleteDetailsByMapinfoId(mapinfoId);
    }

    @Override
    public void deleteObjectById(ExchangeTaskDetailId id) {
        ExchangeTaskDetail dbObject = getObjectById(id);
        Long mapinfoOrder = dbObject.getMapInfoOrder();
        exchangeTaskDetailDao.updateDetailOrder(dbObject.getTaskId(), mapinfoOrder);
        super.deleteObjectById(id);
    }

    @Override
    public void saveObject(Long taskId, List<Long> mapinfoId) {
        //exchangeTaskdetailDao.deleteDetailsByTaskId(taskId);

        List<Long> used = getMapinfoIdUsed(taskId);

        for (int i = 0; i < mapinfoId.size(); i++) {
            Long id = mapinfoId.get(i);

            ExchangeTaskDetail exchangeTaskDetail = new ExchangeTaskDetail();
            ExchangeTaskDetailId exchangeTaskDetailId = new ExchangeTaskDetailId();
            exchangeTaskDetailId.setMapInfoId(id);
            exchangeTaskDetailId.setTaskId(taskId);
            exchangeTaskDetail.setCid(exchangeTaskDetailId);
            exchangeTaskDetail.setMapInfoOrder(used.size() + i + 1L);
            saveObject(exchangeTaskDetail);
        }

    }

}
