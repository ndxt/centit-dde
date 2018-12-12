package com.centit.dde.dao;

import com.centit.dde.po.MapInfoTrigger;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MapInfoTriggerDao extends BaseDaoImpl<MapInfoTrigger, Serializable> {

    public static final Log log = LogFactory.getLog(MapInfoTriggerDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();

            filterField.put("triggerId", CodeBook.EQUAL_HQL_ID);

            filterField.put("mapInfoId", CodeBook.EQUAL_HQL_ID);


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
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("mapinfoId",mapinfoId);
        return listObjects(filterMap);
    }

    public Long getTriggerId() {
        return DatabaseOptUtils.getSequenceNextValue(this, "D_TRIGGERID");
    }
}
