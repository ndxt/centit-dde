package com.centit.dde.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;

public class DataOptInfoDao extends BaseDaoImpl<DataOptInfo> {
    private static final long serialVersionUID = 1L;

    public static final Log log = LogFactory.getLog(DataOptInfoDao.class);

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("dataOptId", CodeBook.EQUAL_HQL_ID);

            filterField.put("optDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put("lastUpdateTime", CodeBook.LIKE_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public void flush() {
        getHibernateTemplate().flush();
    }

    public Long getNextLongSequence() {
        return getNextLongSequence("D_MAPINFOID");
    }

    @SuppressWarnings("unchecked")
    public List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object) {
        String hql = "select new DataOptStep(dos.optStepId, dos.importId, dos.optType, dos.dataOptId, dos.osId, dos.mapinfoOrder, io.importName) "
                + "from DataOptStep dos, ImportOpt io, DataOptInfo doi "
                + "where dos.importId = io.importId and dos.dataOptId = doi.dataOptId and doi.dataOptId = :dataOptId order by dos.mapinfoOrder";

        return getHibernateTemplate().findByNamedParam(hql, "dataOptId", object.getDataOptId());
    }
}
