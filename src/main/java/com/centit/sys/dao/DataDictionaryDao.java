package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.HQLUtils;
import com.centit.sys.po.FDatacatalog;
import com.centit.sys.po.FDatadictionary;

public class DataDictionaryDao extends BaseDaoImpl<FDatadictionary> {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<FDatadictionary> findByCdtbnm(String cdtbnm) {
        return getHibernateTemplate().find("FROM FDatadictionary WHERE id.catalogcode = ? ORDER BY extracode2", cdtbnm);
    }


    //转换主键中的 字段描述 对应关系
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("datacode", "id.datacode=?");
            filterField.put("catalogcode", "id.catalogcode=?");
        }
        return filterField;
    }


    public String getNextPrimarykey() {
        return HQLUtils
                .object2String(getNextKeyByHqlStrOfMax(
                        "fDatadictionary.id.datacode",
                        "FDatadictionary fDatadictionary WHERE length(fDatadictionary.id.datacode)=12"));
    }

    public void deleteDictionary(String catalog) {
        try {
            doExecuteHql("delete from FDatadictionary where id.catalogcode =?", catalog);
            log.debug("delete dictionary successful");
        } catch (RuntimeException re) {
            log.error("delete dictionary failed", re);
            throw re;
        }
    }

    @SuppressWarnings("unused")
    public List<FDatadictionary> getList(String code, String name) {
        String sql = "";

        if (code.equals("") && name.equals("")) {
            sql = "FROM FDatadictionary";
        }
        if (!code.equals("")) {

            sql = "FROM FDatadictionary where id.catalogcode ='" + code + "'";
        }
        if (code.equals("") && !name.equals("")) {
            sql = "FROM FDatadictionary where id.catalogcode in " +
                    "(select catalogcode from FDatacatalog where catalogname ='" + name + "')";
        }
        return getHibernateTemplate().find(sql);
    }

    public List<FDatadictionary> getcodeList(String code) {

        String sql = "select * from F_DATADICTIONARY where catalogcode='" + code + "'";
        //return getHibernateTemplate().find("FROM FDatadictionary WHERE id.catalogcode = ? ",code);
        return (List<FDatadictionary>) findObjectsBySql(sql, FDatadictionary.class);
    }

    public List<FDatadictionary> findByJsh() {
        String sql = "select * from F_DATADICTIONARY";

        return (List<FDatadictionary>) findObjectsBySql(sql, FDatadictionary.class);

    }

    public List<FDatacatalog> getcatalog(String code, String time) {
        String sql = "";
        if (time.equals("")) {
            sql = "FROM FDatacatalog";
        } else {
            if (code.equals("")) {
                sql = "FROM FDatacatalog where  lastModifyDate >= To_Date('" + time + "', 'yyyy-mm-dd')";
            } else {
                sql = "FROM FDatacatalog where catalogcode='" + code + "' and lastModifyDate >= To_Date('" + time + "', 'yyyy-mm-dd')";
            }
        }
        return getHibernateTemplate().find(sql);
    }

}
