package com.centit.dde.services;

import com.centit.dde.po.DataPacketTemplate;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface DataPacketTemplateService {

    void createDataPacketTemplate(DataPacketTemplate dataPacketTemplate);

    void updateDataPacketTemplate(DataPacketTemplate dataPacketTemplate);

    void updateDataPacketTemplateContent(String id, String content);

    void deleteDataPacketTemplate(String id);

    List<DataPacketTemplate> listDataPacketTemplate(Map<String, Object> params, PageDesc pageDesc);
}
