package com.centit.dde.dao;

import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExportField;
import com.centit.dde.po.ExportFieldId;
import com.centit.dde.po.ExportSql;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.centit.dde.util.ConnPool.getConn;

@Repository
public class ExportSqlDao extends BaseDaoImpl<ExportSql,Long> {

    public static final Log logger = LogFactory.getLog(ExportSqlDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("exportId", CodeBook.EQUAL_HQL_ID);

            filterField.put("sourceDatabaseName", CodeBook.LIKE_HQL_ID);

            filterField.put("sourceOsId", CodeBook.LIKE_HQL_ID);

            filterField.put("exportName", CodeBook.LIKE_HQL_ID);
            filterField.put("eqExportName", "exportName = ?");

            filterField.put("querySql", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put("afterSqlBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("exportDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

            filterField.put("beforeSqlBlock", CodeBook.LIKE_HQL_ID);

            filterField.put("mapinfoId", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public void flush() {
        //DatabaseOptUtils.flush(this.getCurrentSession());
    }

    //@Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void saveObject(ExportSql o) {
        if (null == o.getExportId()) {
            o.setExportId(getNextLongSequence());
        }
        super.saveNewObject(o);
    }

    @SuppressWarnings("unchecked")
    public String getMapinfoName(Long mapinfoId) {
        String hql = "select t.exportName from ExportSql t where t.exportId=?" ;
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("exportId",mapinfoId);
        List<Object[]> listObjects = DatabaseOptUtils.listObjectsByNamedSql(
            this, hql, filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0).toString();
        }
        return "";

    }
    public Long getNextLongSequence() {
        return DatabaseOptUtils.getSequenceNextValue(this,"D_MAPINFOID");
    }

    public List<String> listDbTables(DatabaseInfo dbinfo) throws SqlResolveException {
        List<String> tableNames = new ArrayList<String>();
        Connection conn = null;
        try {
            conn = getConn(dbinfo);
            DatabaseMetaData dbMetaData = conn.getMetaData();

            ResultSet rs = dbMetaData.getTables(null, dbinfo.getUsername().toUpperCase(), null,
                    new String[]{"TABLE", "VIEW"});
            //

            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            logger.error("读取表和视图出错", e);

            throw new SqlResolveException(20000, e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return tableNames;
    }

    public ExportSql getTableMetadata(ExportSql exportSql, DatabaseInfo dbinfo) throws SqlResolveException {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = getConn(dbinfo);

            statement = conn.createStatement();

            rs = statement.executeQuery(exportSql.getQuerySql());

            //while (rs.next()) {
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> sqlFileds = QueryUtils.getSqlFiledNames(exportSql.getQuerySql());
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                //select * from talbeName 时sqlFields解析字段长度为1 * ，与rsmd.getColumnCount() 解析长度不一致
                ExportField ef = new ExportField(new ExportFieldId(exportSql.getExportId(), (long) (i - 1)), columnName,
                        sqlFileds.size() != rsmd.getColumnCount() ? columnName : sqlFileds.get(i - 1));
                ef.setFieldType(rsmd.getColumnTypeName(i));
                ef.setIsNull(ResultSetMetaData.columnNoNulls == rsmd.isNullable(i) ? "0" : "1");

                exportSql.addExportField(ef);
            }

            return exportSql;
            //}
        } catch (SQLException e) {
            logger.error("读取表中的数据列及数据类型出错", e);
            throw new SqlResolveException(20000, e);
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //return null;
    }


    public ExportSql fetchObjectById(Long exportId) {
        String hql = "select distinct s from ExportSql s join fetch s.exportTriggers join fetch s.exportFields where s.exportId = ?";
        Map<String,Object> filterMap = new HashMap<>();
        filterMap.put("exportId",exportId);
        List<ExportSql> listObjects = this.listObjectsBySql(hql, filterMap);

        if (!CollectionUtils.isEmpty(listObjects)) {
            return listObjects.get(0);
        }

        hql = "select distinct s from ExportSql s join fetch s.exportFields where s.exportId = ?";

        listObjects = this.listObjectsBySql(hql, filterMap);

        if (CollectionUtils.isEmpty(listObjects)) {
            ExportSql exportSql = getObjectById(exportId);
            exportSql.setExportTriggers(null);
            exportSql.setExportFields(null);

            return exportSql;
        }

        ExportSql exportSql = listObjects.get(0);
        exportSql.setExportTriggers(null);
        return exportSql;
    }

    @Transactional
    public ExportSql loadObjectById(Long id) {
        if (id == null)
            return null;
        // Type[] params = getClass().getTypeParameters();
        try {
            //return (ExportSql) getCurrentSession().load(ExportSql.class, id);
            return  this.getObjectById((Object)id);
            //return (T) getCurrentSession().get(getClassTName(), id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
            //throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION,e);
        }
    }
}
