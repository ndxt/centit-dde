package com.centit.dde.dao;

import com.centit.dde.po.DataPacketParam;
import com.centit.framework.jdbc.dao.BaseDaoImpl;

import java.util.HashMap;
import java.util.Map;

/**
 *  HashMap String,Object 为符合主键，
 */
public class DataPacketParamDao extends BaseDaoImpl<DataPacketParam, HashMap<String,Object>> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
