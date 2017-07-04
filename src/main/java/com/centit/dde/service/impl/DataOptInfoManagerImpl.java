package com.centit.dde.service.impl;

import com.centit.dde.dao.DataOptInfoDao;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.service.DataOptInfoManager;
import com.centit.framework.hibernate.service.BaseEntityManagerImpl;
import com.centit.framework.model.basedata.IUserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Service
public class DataOptInfoManagerImpl
        extends BaseEntityManagerImpl<DataOptInfo,String,DataOptInfoDao>
        implements DataOptInfoManager {

    public static final Log log = LogFactory.getLog(DataOptInfoManager.class);

    private DataOptInfoDao dataOptInfoDao;

    @Resource(name = "dataOptInfoDao")
    @NotNull
    public void setDataOptInfoDao(DataOptInfoDao baseDao) {
        this.dataOptInfoDao = baseDao;
        setBaseDao(this.dataOptInfoDao);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveObject(DataOptInfo object, IUserInfo userDetail) {
        DataOptInfo dbObject = dataOptInfoDao.getObjectById(object.getDataOptId());
        if (null == dbObject) {
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());

            dataOptInfoDao.saveObject(object);
        } else {
            dbObject.setLastUpdateTime(new Date());

            dbObject.copyNotNullProperty(object);
            dbObject.replaceDataOptSteps(object.getDataOptSteps());

            dataOptInfoDao.saveObject( dbObject);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object) {
        return dataOptInfoDao.listDataOptStepByDataOptInfo(object);
    }
}
