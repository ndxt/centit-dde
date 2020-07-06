package com.centit.dde.dao;

import com.centit.dde.po.DataPacket;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DataPacketDao extends BaseDaoImpl<DataPacket, String> {
    @Override
    public Map<String, String> getFilterField() {
        return null;
    }
}
