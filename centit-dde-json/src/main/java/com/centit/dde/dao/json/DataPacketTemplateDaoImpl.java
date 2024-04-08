package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketTemplateDao;
import com.centit.dde.adapter.po.DataPacketTemplate;
import com.centit.support.common.ObjectException;
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
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int updateObject(DataPacketTemplate dataPacket) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public DataPacketTemplate getObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateDataPacketTemplateContent(String id, String content) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public List<DataPacketTemplate> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public JSONObject getDataPacketTemplateByType(Integer type) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }
}
