package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.core.service.BaseEntityManager;

import java.util.List;
import java.util.Map;

public interface ExchangeMapinfoManager extends BaseEntityManager<ExchangeMapinfo,Long> {

    public List<ExchangeMapinfo> listImportExchangeMapinfo(List<Long> mapinfoId);


    /**
     * 保存导出Sql语句时更新相关的查询字段信息
     *
     * @param object
     * @throws SqlResolveException
     */
    void save(ExchangeMapinfo object);

    List<ExchangeMapinfo> listObjectExcludeUsed(Map<String, Object> filterMap, PageDesc pageDesc);
}
