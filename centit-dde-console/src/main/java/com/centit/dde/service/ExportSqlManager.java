package com.centit.dde.service;

import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.security.model.CentitUserDetails;

import java.util.List;

public interface ExportSqlManager extends BaseEntityManager<ExportSql,Long> {

    String getMapinfoName(Long mapinfoId);
    /**
     * 保存导出Sql语句时更新相关的查询字段信息
     *
     * @param object
     * @param userDetail
     */
    void saveObject(ExportSql object, CentitUserDetails userDetail);

    /**
     * 根据 querysql 检查sql语句的有效性并且返回字段信息
     *
     * @param object
     * @return
     */
    List<ExportField> listExportFieldsByQuerysql(ExportSql object);

    public ExportSql getObjectById(Long importId);

    public void deleteObjectById(Long importId);
}
