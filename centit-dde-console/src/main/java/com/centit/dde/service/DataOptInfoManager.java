package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.framework.model.basedata.IUserInfo;

import java.util.List;

public interface DataOptInfoManager extends BaseEntityManager<DataOptInfo,String> {
    /**
     * 保存时更新相关的查询字段信息
     *
     * @param object
     * @param userDetail
     * @throws SqlResolveException
     */
    void saveObject(DataOptInfo object, IUserInfo userDetail);

    List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object);
}
