package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.config.ElasticSearchConfig;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.entity.EsReadVo;
import com.centit.dde.entity.EsWriteVo;
import com.centit.dde.factory.PooledRestClientFactory;
import com.centit.dde.po.DataPacket;
import com.centit.dde.query.ElasticsearchReadUtils;
import com.centit.dde.test.QDataPacket;
import com.centit.dde.write.ElasticsearchWriteUtils;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.po.SourceInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "ES查询测试")
@RestController
@RequestMapping("/es/")
public class TestController {
    @Resource
    SourceInfoDao sourceInfoDao;

    @Autowired
    private DataPacketDao dataPacketDao;

    @PostMapping("matchall")
    @ApiOperation("全部查询测试")
    public Object matchAll(@RequestBody EsReadVo esReadVo){
        SourceInfo sourceInfo = sourceInfoDao.getDatabaseInfoById(esReadVo.getDataSourceId());
        GenericObjectPool<RestHighLevelClient> restHighLevelClientGenericObjectPool = PooledRestClientFactory.obtainclientPool(new ElasticSearchConfig(), sourceInfo);
        RestHighLevelClient restHighLevelClient=null;
        try {
            restHighLevelClient = restHighLevelClientGenericObjectPool.borrowObject();
            JSONObject query = ElasticsearchReadUtils.query(restHighLevelClient, esReadVo);
            return query;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            restHighLevelClientGenericObjectPool.returnObject(restHighLevelClient);
        }
        return null;
    }


    @GetMapping("importData")
    @ApiOperation("导入数据")
    public Object importData(){
        String ips = "127.0.0.1:9200,127.0.0.1:9201,127.0.0.1:9202";
        RestClientBuilder restClientBuilder = new ElasticSearchConfig().restClientBuilder(ips);
        RestHighLevelClient restHighLevelClient=new RestHighLevelClient(restClientBuilder);
        try {
            List<DataPacket> dataPackets = dataPacketDao.listObjects();
            List<String> list = new ArrayList<>();
            for (DataPacket dataPacket : dataPackets) {
                QDataPacket qDataPacket = new QDataPacket();
                BeanUtils.copyProperties(dataPacket,qDataPacket);
                list.add(JSON.toJSONString(qDataPacket));
            }
            EsWriteVo esWriteVo = new EsWriteVo();
            esWriteVo.setIndexName("q_data_packet");
            esWriteVo.setDocumentIds("packetId");
            return ElasticsearchWriteUtils.batchSaveDocuments(restHighLevelClient,list,esWriteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
