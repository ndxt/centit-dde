package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketTemplateDao;
import com.centit.dde.adapter.po.DataPacketTemplate;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import org.springframework.stereotype.Repository;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketTemplateDao")
public class DataPacketTemplateDaoImpl extends BaseDaoImpl<DataPacketTemplate, String> implements DataPacketTemplateDao {

    @Override
    public void updateDataPacketTemplateContent(String id, String content) {
        DatabaseOptUtils.batchUpdateObject(this, DataPacketTemplate.class,
            CollectionsOpt.createHashMap("content", content),
            CollectionsOpt.createHashMap("id", id)
        );
    }

    @Override
    public JSONObject getDataPacketTemplateByType(Integer type) {
        String sql = "SELECT * FROM q_data_packet_template WHERE is_valid = 0  and template_type = ? ";
        return DatabaseOptUtils.getObjectBySqlAsJson(this, sql,new Object[]{type});
    }
}
