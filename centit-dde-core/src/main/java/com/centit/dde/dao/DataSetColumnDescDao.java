package com.centit.dde.dao;

import com.centit.dde.po.DataSetColumnDesc;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DataSetColumnDescDao extends BaseDaoImpl<DataSetColumnDesc, HashMap<String,Object>> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
