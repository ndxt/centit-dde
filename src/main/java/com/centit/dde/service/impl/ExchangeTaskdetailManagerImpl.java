package com.centit.dde.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import com.centit.dde.po.ExchangeTaskDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.centit.dde.dao.ExchangeTaskdetailDao;
import com.centit.dde.po.ExchangeTaskdetailId;
import com.centit.dde.service.ExchangeTaskdetailManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
@Service
public class ExchangeTaskdetailManagerImpl
        extends BaseEntityManagerImpl<ExchangeTaskDetail,ExchangeTaskdetailId,ExchangeTaskdetailDao>
        implements ExchangeTaskdetailManager {

    public static final Log log = LogFactory.getLog(ExchangeTaskdetailManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();
   
    private ExchangeTaskdetailDao exchangeTaskdetailDao;

    @Resource(name = "exchangeTaskdetailDao")
    @NotNull
    public void setExchangeTaskdetailDao(ExchangeTaskdetailDao baseDao) {
        this.exchangeTaskdetailDao = baseDao;
        setBaseDao(this.exchangeTaskdetailDao);
    }

    public List<Long> getMapinfoIdUsed(Long taskId) {
        return this.exchangeTaskdetailDao.getMapinfoIdUsed(taskId);
    }

    public Long getMapinfoOrder(Long taskId) {
        return this.exchangeTaskdetailDao.getMapinfoOrder(taskId);
    }

    public void deleteDetails(Long taskId, Long mapinfoId) {
        this.exchangeTaskdetailDao.deleteDetails(taskId, mapinfoId);
    }

    public String getMapinfoName(Long mapinfoId) {
        // return this.getMapInfoName(mapinfoId);
        return String.valueOf(mapinfoId);
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        this.exchangeTaskdetailDao.deleteDetailsByMapinfoId(mapinfoId);
    }

    @Override
    public void deleteObjectById(ExchangeTaskdetailId id) {
        ExchangeTaskDetail dbObject = getObjectById(id);
        Long mapinfoOrder = dbObject.getMapinfoOrder();
        exchangeTaskdetailDao.updateDetailOrder(dbObject.getTaskId(), mapinfoOrder);
        super.deleteObjectById(id);
    }

    @Override
    public void saveObject(Long taskId, List<Long> mapinfoId) {
        //exchangeTaskdetailDao.deleteDetailsByTaskId(taskId);

        List<Long> used = getMapinfoIdUsed(taskId);

        for (int i = 0; i < mapinfoId.size(); i++) {
            Long id = mapinfoId.get(i);

            ExchangeTaskDetail exchangeTaskDetail = new ExchangeTaskDetail();
            ExchangeTaskdetailId exchangeTaskdetailId = new ExchangeTaskdetailId();
            exchangeTaskdetailId.setMapinfoId(id);
            exchangeTaskdetailId.setTaskId(taskId);
            exchangeTaskDetail.setCid(exchangeTaskdetailId);
            exchangeTaskDetail.setMapinfoOrder(used.size() + i + 1L);
            saveObject(exchangeTaskDetail);
        }

    }

}
