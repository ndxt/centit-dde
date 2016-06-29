package com.centit.dde.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.dde.dao.ExchangeTaskdetailDao;
import com.centit.dde.po.ExchangeTaskdetail;
import com.centit.dde.po.ExchangeTaskdetailId;
import com.centit.dde.service.ExchangeTaskdetailManager;

public class ExchangeTaskdetailManagerImpl extends BaseEntityManagerImpl<ExchangeTaskdetail> implements
        ExchangeTaskdetailManager {
    private static final long serialVersionUID = 1L;

    public static final Log log = LogFactory.getLog(ExchangeTaskdetailManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();

    private ExchangeTaskdetailDao exchangeTaskdetailDao;

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
        // return this.getMapinfoName(mapinfoId);
        return String.valueOf(mapinfoId);
    }

    public void deleteDetailsByMapinfoId(Long mapinfoId) {
        this.exchangeTaskdetailDao.deleteDetailsByMapinfoId(mapinfoId);
    }

    @Override
    public void deleteObjectById(Serializable id) {
        ExchangeTaskdetail dbObject = getObjectById(id);
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

            ExchangeTaskdetail exchangeTaskdetail = new ExchangeTaskdetail();
            ExchangeTaskdetailId exchangeTaskdetailId = new ExchangeTaskdetailId();
            exchangeTaskdetailId.setMapinfoId(id);
            exchangeTaskdetailId.setTaskId(taskId);
            exchangeTaskdetail.setCid(exchangeTaskdetailId);
            exchangeTaskdetail.setMapinfoOrder(used.size() + i + 1L);
            saveObject(exchangeTaskdetail);
        }

    }

}
