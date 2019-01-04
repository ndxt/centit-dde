package com.centit.dde.dao;

import com.centit.dde.po.DataOptInfo;
import com.centit.dde.po.DataOptStep;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataOptInfoDao extends BaseDaoImpl<DataOptInfo,String> {

    public static final Log log = LogFactory.getLog(DataOptInfoDao.class);
    @Override
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
        //DatabaseOptUtils.flush(this.getCurrentSession());
    }

    public Long getNextLongSequence() {
        return DatabaseOptUtils.getSequenceNextValue(this,"D_MAPINFOID");
    }

    @SuppressWarnings("unchecked")
    public List<DataOptStep> listDataOptStepByDataOptInfo(DataOptInfo object) {
        String hql = "select new DataOptStep(dos.optStepId, dos.importId, dos.optType, dos.dataOptId, dos.osId, dos.mapinfoOrder, io.importName) "
                + "from DataOptStep dos, ImportOpt io, DataOptInfo doi "
                + "where dos.importId = io.importId and dos.dataOptId = doi.dataOptId and doi.dataOptId = :dataOptId order by dos.mapinfoOrder";

        return (List<DataOptStep>) DatabaseOptUtils.getObjectBySqlAsJson(this,hql,
                QueryUtils.createSqlParamsMap("dataOptId", object.getDataOptId()));
    }

    public DataOptInfo getObjectById(String dataOptId) {
        DataOptInfo dataOptInfo = null;
        if (dataOptId != null && !"".equals(dataOptId)) {
            dataOptInfo = getObjectWithReferences(dataOptId);
        }
        StringBuilder sql = new StringBuilder();
        Object obj;
        sql.append("select import_name from d_import_opt t where t.import_id = ? ");
        for (DataOptStep dataOptStep : dataOptInfo.getDataOptSteps()) {
            //obj = DatabaseOptUtils.getScalarObjectQuery(this,sql.toString(),new Object[] {dataOptStep.getImportId()});
            try {
                if (dataOptStep.getImportId() != null && !"".equals(dataOptStep.getImportId())) {
                    obj = DatabaseOptUtils.getScalarObjectQuery(this, sql.toString(), (Object) dataOptStep.getImportId());
                    if (obj != null) {
                        dataOptStep.setImportName(obj.toString());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dataOptInfo;
    }
}
