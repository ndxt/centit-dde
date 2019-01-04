package com.centit.dde.service.impl;

import com.centit.dde.dao.DataOptInfoDao;
import com.centit.dde.dao.DataOptStepDao;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.dde.service.DataOptInfoManager;
import com.centit.framework.jdbc.service.BaseEntityManagerImpl;
import com.centit.framework.security.model.CentitUserDetails;
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

    @Resource
    private DataOptStepDao dataOptStepDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveObject(DataOptInfo object, CentitUserDetails userDetail) {
        DataOptInfo dbObject = dataOptInfoDao.getObjectById(object.getDataOptId());
        if (null == dbObject) {
            object.setDataOptId(String.valueOf(dataOptInfoDao.getNextLongSequence()));
            object.setCreated(userDetail.getUserCode());
            object.setCreateTime(new Date());
            setOptStepCid(object);
            dataOptInfoDao.saveNewObject(object);
            dataOptInfoDao.saveObjectReferences(object);
        } else {
            dbObject.setLastUpdateTime(new Date());

            dbObject.copyNotNullProperty(object);
            setOptStepCid(dbObject);
            dbObject.replaceDataOptSteps(object.getDataOptSteps());

            dataOptInfoDao.updateObject(dbObject);
            dataOptInfoDao.saveObjectReferences(dbObject);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object) {
        return dataOptInfoDao.listDataOptStepByDataOptInfo(object);
    }

    private void setOptStepCid(DataOptInfo object) {
        DataOptStep os = null;
        for (int i = 0; i < object.getDataOptSteps().size();i++) {
            os = object.getDataOptSteps().get(i);
            os.setDataOptId(object.getDataOptId());
            if (os.getOptStepId()==null ||"".equals(os.getOptStepId())) {
                os.setOptStepId(dataOptStepDao.getNextLongSequence());
            }
            os.setMapinfoOrder((long) i);
        }
    }

    public DataOptInfo getObjectById(String dataOptId) {
        return dataOptInfoDao.getObjectById(dataOptId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteObjectById(String dataOptId) {
        DataOptInfo dataOptInfo = dataOptInfoDao.getObjectById(dataOptId);
        if (dataOptInfo != null) {
            dataOptInfoDao.deleteObjectById(dataOptId);
            dataOptInfoDao.deleteObjectReferences(dataOptInfo);
        }
    }
}
