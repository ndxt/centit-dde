package com.centit.dde.dao;

import com.centit.dde.po.DataSetDefine;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class DataSetDefineDao extends BaseDaoImpl<DataSetDefine, String> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
