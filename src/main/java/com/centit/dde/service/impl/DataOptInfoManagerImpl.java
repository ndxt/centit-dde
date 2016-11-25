package com.centit.dde.service.impl;

import java.util.Date;
import java.util.List;

import com.centit.framework.model.basedata.IUserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.dde.dao.DataOptInfoDao;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.service.DataOptInfoManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;

public class DataOptInfoManagerImpl
        extends BaseEntityManagerImpl<DataOptInfo,String,DataOptInfoDao>
        implements DataOptInfoManager {

    public static final Log log = LogFactory.getLog(DataOptInfoManager.class);

    // private static final SysOptLog sysOptLog =
    // SysOptLogFactoryImpl.getSysOptLog();

    private DataOptInfoDao dataOptInfoDao;

    public void setDataOptInfoDao(DataOptInfoDao baseDao) {
        this.dataOptInfoDao = baseDao;
        setBaseDao(this.dataOptInfoDao);
    }

    @Override
    public void saveObject(DataOptInfo object, IUserInfo userDetail) throws SqlResolveException {
        DataOptInfo dbObject = dataOptInfoDao.getObjectById(object.getDataOptId());
        if (null == dbObject) {
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());

            setDataOptStep(object);
            saveObject(object);
        } else {
            dbObject.getDataOptSteps().clear();
            dataOptInfoDao.flush();
            dbObject.setLastUpdateTime(new Date());

            dbObject.copyNotNullProperty(object);
            // copy database fields to convert fields

            setDataOptStep(object);

            for (DataOptStep dos : object.getDataOptSteps()) {
                dbObject.addDataOptStep(dos);
            }
            saveObject( dbObject);
        }
    }

    private void setDataOptStep(DataOptInfo object) {
        for (DataOptStep dos : object.getDataOptSteps()) {
            dos.setDataOptId(object.getDataOptId());
            dos.setOptStepId(dataOptInfoDao.getNextLongSequence());
        }

    }

    @Override
    public List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object) {
        return dataOptInfoDao.listDataOptStepByDataOptInfo(object);
    }

}
