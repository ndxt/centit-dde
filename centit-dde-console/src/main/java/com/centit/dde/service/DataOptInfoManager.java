package com.centit.dde.service;

import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.security.model.CentitUserDetails;

import java.util.List;

public interface DataOptInfoManager extends BaseEntityManager<DataOptInfo,String> {
    /**
     * 保存时更新相关的查询字段信息
     *
     * @param object
     * @param userDetail
     */
    void saveObject(DataOptInfo object, CentitUserDetails userDetail);

    List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object);

    public DataOptInfo getObjectById(String dataOptId);

    public void deleteObjectById(String dataOptId);
}
