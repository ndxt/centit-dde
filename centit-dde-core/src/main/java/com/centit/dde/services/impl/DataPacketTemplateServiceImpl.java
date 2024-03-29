package com.centit.dde.services.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketTemplateDao;
import com.centit.dde.adapter.po.DataPacketTemplate;
import com.centit.dde.services.DataPacketTemplateService;
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
        dataPacketTemplateDao.updateDataPacketTemplateContent(id, content);
    }

    @Override
    public DataPacketTemplate getDataPacketTemplateById(String id) {
        return dataPacketTemplateDao.getObjectById(id);
    }

    @Override
    public JSONObject getDataPacketTemplateByType(Integer type) {
        return dataPacketTemplateDao.getDataPacketTemplateByType(type);
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
