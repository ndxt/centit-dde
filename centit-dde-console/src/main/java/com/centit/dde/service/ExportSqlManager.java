package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.framework.jdbc.service.BaseEntityManager;
import com.centit.framework.model.basedata.IUserInfo;

import java.util.List;

public interface ExportSqlManager extends BaseEntityManager<ExportSql,Long> {

    String getMapinfoName(Long mapinfoId);
    /**
     * 保存导出Sql语句时更新相关的查询字段信息
     *
     * @param object
     * @param userDetail
     * @throws SqlResolveException
     */
    void saveObject(ExportSql object, IUserInfo userDetail);

    /**
     * 根据 querysql 检查sql语句的有效性并且返回字段信息
     *
     * @param object
     * @return
     * @throws SqlResolveException
     */
    List<ExportField> listExportFieldsByQuerysql(ExportSql object) throws SqlResolveException;

    public ExportSql getObjectById(Long importId);

    public void deleteObjectById(Long importId);
}
