package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.dao.DataPacketTemplateDao;
import com.centit.dde.po.DataPacketTemplate;
import com.centit.dde.services.DataPacketTemplateService;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataPacketTemplateServiceImpl implements DataPacketTemplateService {

    @Autowired
    DataPacketTemplateDao dataPacketTemplateDao;


    @Override
    public void createDataPacketTemplate(DataPacketTemplate dataPacketTemplate) {
        dataPacketTemplateDao.saveNewObject(dataPacketTemplate);
    }

    @Override
    public void updateDataPacketTemplate(DataPacketTemplate dataPacketTemplate) {
        dataPacketTemplateDao.updateObject(dataPacketTemplate);
    }

    @Override
    public void updateDataPacketTemplateContent(String id, String content) {
        DatabaseOptUtils.batchUpdateObject(dataPacketTemplateDao, DataPacketTemplate.class,
            CollectionsOpt.createHashMap("content", content),
            CollectionsOpt.createHashMap("id", id)
        );
    }

    @Override
    public DataPacketTemplate getDataPacketTemplateById(String id) {
        return dataPacketTemplateDao.getObjectById(id);
    }

    @Override
    public JSONObject getDataPacketTemplateByType(Integer type) {
        StringBuilder sql = new StringBuilder("SELECT * FROM q_data_packet_template WHERE is_valid = 0  and template_type = ? ");
        return DatabaseOptUtils.getObjectBySqlAsJson(dataPacketTemplateDao, sql.toString(),new Object[]{type});
    }

    @Override
    public void deleteDataPacketTemplate(String id) {
        dataPacketTemplateDao.deleteObjectById(id);
    }

    @Override
    public List<DataPacketTemplate> listDataPacketTemplate(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketTemplateDao.listObjectsByProperties(params, pageDesc);
    }
}
