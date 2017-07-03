package com.centit.dde.dao;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.dde.po.MapinfoTriggerId;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MapinfoTriggerDao extends BaseDaoImpl<MapInfoTrigger,MapinfoTriggerId> {

    public static final Log log = LogFactory.getLog(MapinfoTriggerDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("cid.triggerId", CodeBook.EQUAL_HQL_ID);

            filterField.put("cid.mapinfoId", CodeBook.EQUAL_HQL_ID);


            filterField.put("triggerSql", CodeBook.LIKE_HQL_ID);

            filterField.put("triggerDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("triggerType", CodeBook.LIKE_HQL_ID);

            filterField.put("triggerTime", CodeBook.LIKE_HQL_ID);

            filterField.put("triggerDatabase", CodeBook.LIKE_HQL_ID);

            filterField.put("tiggerOrder", CodeBook.LIKE_HQL_ID);

            filterField.put("isprocedure", CodeBook.LIKE_HQL_ID);

        }
        return filterField;
    }

    public List<MapInfoTrigger> listTriggerByMapinfoId(Long mapinfoId) {
        StringBuffer sql = new StringBuffer();
        sql.append("from MapInfoTrigger t where  t.cid.mapinfoId=");
        sql.append(mapinfoId);
        sql.append(" order by t.triggerTime, t.tiggerOrder");
        return listObjects(sql.toString());
    }

    public Long getTriggerId() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_TRIGGERID");
    }
}
