package com.centit.dde.adapter.dao;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacketTemplate;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface DataPacketTemplateDao {

    void saveNewObject(DataPacketTemplate dataPacket);

    int updateObject(DataPacketTemplate dataPacket);

    DataPacketTemplate getObjectById(Object id);

    int deleteObjectById(Object id);


    void updateDataPacketTemplateContent(String id, String content);

    List<DataPacketTemplate> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc);

    JSONObject getDataPacketTemplateByType(Integer type);
}
