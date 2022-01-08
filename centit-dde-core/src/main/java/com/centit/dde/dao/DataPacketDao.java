package com.centit.dde.dao;

import com.centit.dde.po.DataPacket;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zc
 */
@Repository
public class DataPacketDao extends BaseDaoImpl<DataPacket, String> {
    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>(1);
        filterField.put("(splitforin)optids", "opt_id in (:optids)");
        return filterField;
    }

}
