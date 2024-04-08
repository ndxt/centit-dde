package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketParam;
import com.centit.framework.components.CodeRepositoryCache;
import com.centit.support.common.CachedMap;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDao")
public class DataPacketDaoImpl implements DataPacketDao {

    @Value("${app.home:./}")
    private String appHome;

    private CachedMap<String, DataPacket> dataPacketCache =
        new CachedMap<>(
            (packetId)->  this.loadDataPacket(packetId),
            CodeRepositoryCache.CACHE_EXPIRE_EVERY_DAY );

    private DataPacket loadDataPacket(String packetId){
        String apiFile = appHome + File.separator + "config" +
            File.separator +  "apis" + File.separator + packetId +".json";
        try {
            JSONObject apiJson = JSON.parseObject(new FileInputStream(apiFile));
            DataPacket packet = apiJson.toJavaObject(DataPacket.class);
            JSONArray params = apiJson.getJSONArray("params");
            if(params !=null){
                packet.setPacketParams(params.toJavaList(DataPacketParam.class));
            }

            return packet;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public DataPacket getObjectById(Object packetId) {
        return dataPacketCache.getCachedValue((String) packetId);
    }

    @Override
    public DataPacket getObjectWithReferences(Object packetId) {
        return dataPacketCache.getCachedValue((String) packetId);
    }

    @Override
    public List<DataPacket> listObjectsByProperties(Map<String, Object> params, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public List<DataPacket> listObjectsByProperties(Map<String, Object> params) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void saveNewObject(DataPacket dataPacket) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int saveObjectReferences(DataPacket dataPacket) {
        return 1;
    }

    @Override
    public int mergeObject(DataPacket dataPacket) {
        return 1;
    }

    @Override
    public int updateObject(String[] fields, DataPacket dataPacket) {
        return 1;
    }

    @Override
    public int deleteObjectReferences(DataPacket dataPacket) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int deleteObjectById(Object dataPacket) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void publishDataPacket(DataPacket dataPacket) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
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
    public void updatePackedLogLevel(int logLevel, List<String> packetIds) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public void updateApplicationLogLevel(int logLevel, String osId) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "Runtime 运行时环境，不支持元数据的修改!");
    }

    @Override
    public int clearTrashStand(String osId) {
        return 1;
    }
}
