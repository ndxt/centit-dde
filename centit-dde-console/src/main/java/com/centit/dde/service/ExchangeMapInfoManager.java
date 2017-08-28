package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapInfo;
import com.centit.dde.po.MapInfoDetail;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.hibernate.service.BaseEntityManager;
import com.centit.framework.ip.po.DatabaseInfo;

import java.util.List;
import java.util.Map;

public interface ExchangeMapInfoManager extends BaseEntityManager<ExchangeMapInfo,Long> {

    List<ExchangeMapInfo> listImportExchangeMapinfo(List<Long> mapinfoId);

    void save(ExchangeMapInfo object) throws SqlResolveException;

    List<ExchangeMapInfo> listObjectExcludeUsed(Map<String, Object> filterMap, PageDesc pageDesc);

    List<MapInfoDetail> resolveSQL(DatabaseInfo databaseInfo, String sql);
}
