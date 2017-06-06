package com.centit.dde.service;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportSql;
import com.centit.framework.core.service.BaseEntityManager;
import com.centit.framework.model.basedata.IUserInfo;
import com.centit.framework.security.model.CentitUserDetails;

import java.util.List;

public interface ExportSqlManager extends BaseEntityManager<ExportSql,Long> {

    /**
     * 验证Sql有效性
     *
     * @param object
     * @throws SqlResolveException
     */
    void validator(ExportSql object) throws SqlResolveException;
    public String getMapinfoName(Long mapinfoId);
    /**
     * 保存导出Sql语句时更新相关的查询字段信息
     *
     * @param object
     * @param userDetail
     * @throws SqlResolveException
     */
    void saveObject(ExportSql object, IUserInfo userDetail) throws SqlResolveException;

    /**
     * 根据 querysql 检查sql语句的有效性并且返回字段信息
     *
     * @param object
     * @return
     * @throws SqlResolveException
     */
    List<ExportField> listExportFieldsByQuerysql(ExportSql object) throws SqlResolveException;

    void save(ExportSql object, CentitUserDetails user);
}
