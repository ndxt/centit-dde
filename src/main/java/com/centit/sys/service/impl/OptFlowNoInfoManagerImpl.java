package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.dao.OptFlowNoInfoDao;
import com.centit.sys.po.OptFlowNoInfo;
import com.centit.sys.po.OptFlowNoInfoId;
import com.centit.sys.service.OptFlowNoInfoManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class OptFlowNoInfoManagerImpl extends BaseEntityManagerImpl<OptFlowNoInfo>
        implements OptFlowNoInfoManager {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(OptFlowNoInfoManager.class);

    private OptFlowNoInfoDao optFlowNoInfoDao;

    public void setOptFlowNoInfoDao(OptFlowNoInfoDao baseDao) {
        this.optFlowNoInfoDao = baseDao;
        setBaseDao(this.optFlowNoInfoDao);
    }

    @Override
    public synchronized long createNextLsh(String ownerCode, String codeCode,
                                           Date codeBaseDate) {
        java.sql.Date codeDate = DatetimeOpt.convertSqlDate(codeBaseDate);
        OptFlowNoInfoId noId = new OptFlowNoInfoId(ownerCode, codeDate, codeCode);
        OptFlowNoInfo noInfo = optFlowNoInfoDao.getObjectById(noId);
        long nextCode = 1l;
        if (noInfo == null) {
            noInfo = new OptFlowNoInfo(noId, 1l, DatetimeOpt.currentUtilDate());
        } else {
            nextCode = noInfo.getCurNo() + 1;
            noInfo.setCurNo(nextCode);
            noInfo.setLastCodeDate(DatetimeOpt.currentUtilDate());
        }
        optFlowNoInfoDao.saveObject(noInfo);
        return nextCode;
    }

    @Override
    public long createNextLshBaseDay(String ownerCode, String codeCode,
                                     Date codeBaseDate) {
        return createNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToDay(codeBaseDate));
    }

    @Override
    public long createNextLshBaseMonth(String ownerCode, String codeCode,
                                       Date codeBaseDate) {
        return createNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToMonth(codeBaseDate));
    }

    @Override
    public long createNextLshBaseYear(String ownerCode, String codeCode,
                                      Date codeBaseDate) {
        return createNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToYear(codeBaseDate));
    }

    @Override
    public long createNextLsh(String codeCode) {
        return createNextLsh(DefaultOwnerCode, codeCode, DefaultCodeDate);
    }

    @Override
    public long createNextLsh(String ownerCode, String codeCode) {
        return createNextLsh(ownerCode, codeCode, DefaultCodeDate);
    }

    @Override
    public synchronized long getNextLsh(String ownerCode, String codeCode,
                           Date codeBaseDate) {
        java.sql.Date codeDate = DatetimeOpt.convertSqlDate(codeBaseDate);
        OptFlowNoInfoId noId = new OptFlowNoInfoId(ownerCode, codeDate, codeCode);
        OptFlowNoInfo noInfo = optFlowNoInfoDao.getObjectById(noId);
        long nextCode = 1l;
        if (noInfo != null)
            nextCode = noInfo.getCurNo() + 1;
        return nextCode;
    }

    @Override
    public long getNextLshBaseDay(String ownerCode, String codeCode,
                                  Date codeBaseDate) {
        return getNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToDay(codeBaseDate));
    }

    @Override
    public long getNextLshBaseMonth(String ownerCode, String codeCode,
                                    Date codeBaseDate) {
        return getNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToMonth(codeBaseDate));
    }

    @Override
    public long getNextLshBaseYear(String ownerCode, String codeCode,
                                   Date codeBaseDate) {
        return getNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToYear(codeBaseDate));
    }

    @Override
    public long getNextLsh(String codeCode) {
        return getNextLsh(DefaultOwnerCode, codeCode, DefaultCodeDate);
    }

    @Override
    public long getNextLsh(String ownerCode, String codeCode) {
        return getNextLsh(ownerCode, codeCode, DefaultCodeDate);
    }

    @Override
    public synchronized void recordNextLsh(String ownerCode, String codeCode,
                                           Date codeBaseDate, long currCode) {
        java.sql.Date codeDate = DatetimeOpt.convertSqlDate(codeBaseDate);
        OptFlowNoInfoId noId = new OptFlowNoInfoId(ownerCode, codeDate, codeCode);
        OptFlowNoInfo noInfo = optFlowNoInfoDao.getObjectById(noId);
        if (noInfo == null) {
            noInfo = new OptFlowNoInfo(noId, currCode, DatetimeOpt.currentUtilDate());
            optFlowNoInfoDao.saveObject(noInfo);
        } else {
            if (noInfo.getCurNo() < currCode) {
                noInfo.setCurNo(currCode);
                noInfo.setLastCodeDate(DatetimeOpt.currentUtilDate());
                optFlowNoInfoDao.saveObject(noInfo);
            }
        }
    }

    @Override
    public void recordNextLshBaseDay(String ownerCode, String codeCode,
                                     Date codeBaseDate, long currCode) {
        recordNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToDay(codeBaseDate), currCode);
    }

    @Override
    public void recordNextLshBaseMonth(String ownerCode, String codeCode,
                                       Date codeBaseDate, long currCode) {
        recordNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToMonth(codeBaseDate), currCode);
    }

    @Override
    public void recordNextLshBaseYear(String ownerCode, String codeCode,
                                      Date codeBaseDate, long currCode) {
        recordNextLsh(ownerCode, codeCode, DatetimeOpt.truncateToYear(codeBaseDate), currCode);
    }

    @Override
    public void recordNextLsh(String codeCode, long currCode) {
        recordNextLsh(DefaultOwnerCode, codeCode, DefaultCodeDate, currCode);
    }

    @Override
    public void recordNextLsh(String ownerCode, String codeCode, long currCode) {
        recordNextLsh(ownerCode, codeCode, DefaultCodeDate, currCode);
    }

}

