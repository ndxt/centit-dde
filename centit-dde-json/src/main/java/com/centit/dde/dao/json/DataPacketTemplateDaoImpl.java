package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketTemplateDao;
import com.centit.dde.adapter.po.DataPacketTemplate;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketTemplateDao")
public class DataPacketTemplateDaoImpl implements DataPacketTemplateDao {

    @Override
    public void saveNewObject(DataPacketTemplate dataPacket) {

    }

    @Override
    public int updateObject(DataPacketTemplate dataPacket) {
        return 0;
    }

    @Override
    public DataPacketTemplate getObjectById(Object id) {
        return null;
    }

    @Override
    public int deleteObjectById(Object id) {
        return 0;
    }

    @Override
    public void updateDataPacketTemplateContent(String id, String content) {

    }

    @Override
    public List<DataPacketTemplate> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc) {
        return null;
    }

    @Override
    public JSONObject getDataPacketTemplateByType(Integer type) {
        return null;
    }
}
