package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSONArray;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDraftDao")
public class DataPacketDraftDaoImpl implements DataPacketDraftDao{

    @Override
    public void saveNewObject(DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int saveObjectReferences(DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int updateObject(DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int updateObject(String[] fields, DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public DataPacketDraft getObjectWithReferences(Object packetId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectReferences(DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectById(Object packetId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public JSONArray listDataPacketDraft(Map<String, Object> params, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateDisableStatus(String packetId, String disable) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int clearTrashStand(String osId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void publishDataPacket(String optCode, DataPacketDraft dataPacketCopy) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }
}
