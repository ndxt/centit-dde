package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataSetDefineDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataSetColumnDesc;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.services.DBPacketBizSupplier;
import com.centit.dde.services.DataPacketService;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.dataopt.bizopt.BuiltInOperation;
import com.centit.product.dataopt.core.BizModel;

import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.Md5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DataPacketServiceImpl implements DataPacketService {

    private static final Logger logger = LoggerFactory.getLogger(DataPacketServiceImpl.class);

    @Autowired(required = false)
    private JedisPool jedisPool;

    @Autowired(required = false)
    private FileStore fileStore;

    @Autowired
    private DataPacketDao dataPacketDao;

    @Autowired
    private DataSetDefineDao dataSetDefineDao;

    @Autowired
    private IntegrationEnvironment integrationEnvironment;

    @Override
    public void createDataPacket(DataPacket dataPacket) {
        dataPacketDao.saveNewObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
        mergeDataPacket(dataPacket);
    }

    @Override
    public void updateDataPacket(DataPacket dataPacket) {
        dataPacketDao.updateObject(dataPacket);
        dataPacketDao.saveObjectReferences(dataPacket);
        mergeDataPacket(dataPacket);
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson){
        DatabaseOptUtils.batchUpdateObject(dataPacketDao, DataPacket.class,
            CollectionsOpt.createHashMap("dataOptDescJson",dataPacketOptJson ),
            CollectionsOpt.createHashMap("packetId",packetId )
        );
    }

    private void mergeDataPacket(DataPacket dataPacket) {
        if (dataPacket.getDataSetDefines()!=null && dataPacket.getDataSetDefines().size() > 0) {
            for (DataSetDefine db : dataPacket.getDataSetDefines()) {
                if (db.getColumns() != null && db.getColumns().size() >0) {
                    for (DataSetColumnDesc column : db.getColumns()) {
                        column.setPacketId(db.getPacketId());
                    }
                    dataSetDefineDao.saveObjectReferences(db);
                }
            }
        }
    }

    @Override
    public void deleteDataPacket(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        if (dataPacket!=null && dataPacket.getDataSetDefines()!=null && dataPacket.getDataSetDefines().size() > 0) {
            for (DataSetDefine db : dataPacket.getDataSetDefines()) {
                dataSetDefineDao.deleteObjectReferences(db);
            }
        }
        dataPacketDao.deleteObjectById(packetId);
        dataPacketDao.deleteObjectReferences(dataPacket);
    }

    @Override
    public List<DataPacket> listDataPacket(Map<String, Object> params, PageDesc pageDesc) {
        return dataPacketDao.listObjectsByProperties(params, pageDesc);
    }

    @Override
    public DataPacket getDataPacket(String packetId) {
        DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
        if (dataPacket!=null && dataPacket.getDataSetDefines()!=null && dataPacket.getDataSetDefines().size() >0 ) {
            for (DataSetDefine db : dataPacket.getDataSetDefines()) {
                dataSetDefineDao.fetchObjectReferences(db);
            }
        }
        return dataPacket;
    }

    private BizModel innerFetchDataPacketData(DataPacket dataPacket, Map<String, Object>  params){
        DBPacketBizSupplier bizSupplier = new DBPacketBizSupplier(dataPacket);
        bizSupplier.setFileStore(fileStore);
        bizSupplier.setIntegrationEnvironment(integrationEnvironment);
        bizSupplier.setQueryParams(params);
        return bizSupplier.get();
    }

    private String makeDataPacketBufId(DataPacket dataPacket, Map<String, Object>  paramsMap){
        String dateString = DatetimeOpt.convertTimestampToString(dataPacket.getRecordDate());
        String params = JSON.toJSONString(paramsMap, SerializerFeature.MapSortField);
        StringBuffer temp = new StringBuffer("packet:");
        temp.append(dataPacket.getPacketId())
            .append(":")
            .append(params)
            .append(dateString);
        return Md5Encoder.encode(temp.toString());
    }

    private BizModel fetchDataPacketDataFromBuf(DataPacket dataPacket, Map<String, Object>  paramsMap){
        if(jedisPool==null){
            return null;
        }
        String key =makeDataPacketBufId(dataPacket, paramsMap);
        Object object = null;
        if (dataPacket.getBufferFreshPeriod() >= 0) {
            Jedis jedis = jedisPool.getResource();
            if (jedis.get(key.getBytes())!=null && !"".equals(jedis.get(key.getBytes()))) {
                try {
                    byte[] byt = jedis.get(key.getBytes());
                    ByteArrayInputStream bis = new ByteArrayInputStream(byt);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    object = ois.readObject();
                    bis.close();
                    ois.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                jedis.close();
                if (object instanceof BizModel) {
                    BizModel bizModel = (BizModel) object;
                    return bizModel;
                }
            }
        }
        return null;
    }

    private void setDataPacketBuf(BizModel bizModel, DataPacket dataPacket, Map<String, Object>  paramsMap){
        if(jedisPool==null){
            return;
        }
        String key =makeDataPacketBufId(dataPacket, paramsMap);
        Jedis jedis = jedisPool.getResource();
        if (jedis.get(key.getBytes())==null || "".equals(jedis.get(key.getBytes()))) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(bizModel);

                byte[] byt=bos.toByteArray();
                jedis.set(key.getBytes(),byt);
                int seconds = 0;
                if (dataPacket.getBufferFreshPeriod() == 1) {
                    //一日
                    seconds = 24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == 2) {
                    //按周
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfWeek(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == 3) {
                    //按月
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfMonth(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == 4) {
                    //按年
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfYear(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() >= 60) {
                    //按秒
                    jedis.expire(key.getBytes(),dataPacket.getBufferFreshPeriod());
                }
                bos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        jedis.close();
    }

    @Override
    public BizModel fetchDataPacketData(String packetId, Map<String, Object> paramsMap){
        DataPacket dataPacket = this.getDataPacket(packetId);
        BizModel bizModel = fetchDataPacketDataFromBuf(dataPacket, paramsMap);
        if(bizModel==null) {
            bizModel = innerFetchDataPacketData(dataPacket, paramsMap);
        }
        JSONObject obj = dataPacket.getDataOptDescJson();
        if(obj!=null) {
            BuiltInOperation builtInOperation = new BuiltInOperation(obj);
            bizModel = builtInOperation.apply(bizModel);
        }
        setDataPacketBuf(bizModel, dataPacket, paramsMap);
        return bizModel;
    }
    @Override
    public BizModel fetchDataPacketData(String packetId, Map<String, Object> paramsMap,JSONObject optsteps){
        DataPacket dataPacket = this.getDataPacket(packetId);
        BizModel bizModel = fetchDataPacketDataFromBuf(dataPacket, paramsMap);
        if(bizModel==null) {
            bizModel = innerFetchDataPacketData(dataPacket, paramsMap);
        }
        if(optsteps!=null) {
            BuiltInOperation builtInOperation = new BuiltInOperation(optsteps);
            bizModel = builtInOperation.apply(bizModel);
        }
        setDataPacketBuf(bizModel, dataPacket, paramsMap);
        return bizModel;
    }
}
