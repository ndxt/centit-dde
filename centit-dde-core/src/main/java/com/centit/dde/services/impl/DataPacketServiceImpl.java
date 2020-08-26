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
import com.centit.dde.utils.Constant;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.product.dataopt.bizopt.BuiltInOperation;
import com.centit.product.dataopt.bizopt.JsMateObjectEventRuntime;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataPacketServiceImpl implements DataPacketService {
    @Autowired(required = false)
    private  JedisPool jedisPool;
    @Autowired(required = false)
    private  FileStore fileStore;
    @Autowired(required = false)
    private MetaObjectService metaObjectService;
    @Autowired(required = false)
    private DatabaseRunTime databaseRunTime;

    private final DataPacketDao dataPacketDao;

    private final DataSetDefineDao dataSetDefineDao;

    private final IntegrationEnvironment integrationEnvironment;

    @Autowired
    public DataPacketServiceImpl(  DataPacketDao dataPacketDao, DataSetDefineDao dataSetDefineDao, IntegrationEnvironment integrationEnvironment) {

        this.dataPacketDao = dataPacketDao;
        this.dataSetDefineDao = dataSetDefineDao;
        this.integrationEnvironment = integrationEnvironment;
    }

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
        StringBuffer temp;
        temp = new StringBuffer("packet:");
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
            if ((jedis.get(key.getBytes()) != null) && (jedis.get(key.getBytes()).length>0)) {
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
                    return (BizModel) object;
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
        if (jedis.get(key.getBytes())==null || (jedis.get(key.getBytes()).length==0)) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(bizModel);

                byte[] byt=bos.toByteArray();
                jedis.set(key.getBytes(),byt);
                int seconds;
                if (dataPacket.getBufferFreshPeriod() == Constant.ONE) {
                    //一日
                    seconds = 24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.TWO) {
                    //按周
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfWeek(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.THREE) {
                    //按月
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfMonth(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() == Constant.FOUR) {
                    //按年
                    seconds = DatetimeOpt.calcSpanDays(new Date(), DatetimeOpt.seekEndOfYear(new Date()))*24*3600;
                    jedis.expire(key.getBytes(),seconds);
                } else if (dataPacket.getBufferFreshPeriod() >= Constant.SIXTY) {
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
        return getBizModel(packetId, paramsMap,null);
    }



    @Override
    public BizModel fetchDataPacketData(String packetId, Map<String, Object> paramsMap,JSONObject optsteps){
        return getBizModel(packetId, paramsMap, optsteps);
    }

    private BizModel getBizModel(String packetId, Map<String, Object> paramsMap, JSONObject optsteps) {
        DataPacket dataPacket = this.getDataPacket(packetId);
        BizModel bizModel = fetchDataPacketDataFromBuf(dataPacket, paramsMap);
        if(bizModel==null) {
            bizModel = innerFetchDataPacketData(dataPacket, paramsMap);
        }
        if(optsteps==null){
            optsteps = dataPacket.getDataOptDescJson();
        }
        if(optsteps!=null) {
            BuiltInOperation builtInOperation = new BuiltInOperation(optsteps);
            JsMateObjectEventRuntime jsMateObjectEventRuntime=
                new JsMateObjectEventRuntime(metaObjectService,databaseRunTime);
            jsMateObjectEventRuntime.setParms(paramsMap);
            builtInOperation.setJsMateObjectEvent(jsMateObjectEventRuntime);
            bizModel = builtInOperation.apply(bizModel);
        }
        setDataPacketBuf(bizModel, dataPacket, paramsMap);
        return bizModel;
    }
}
