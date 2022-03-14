package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.adapter.po.SourceInfo;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.AESSecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Api(value = "kafka参数管理接口", tags = "kafka参数管理接口")
@Controller
@ResponseBody
@RequestMapping(value = "/kafka")
public class KafkaParameterController {
    @Resource
    private SourceInfoDao sourceInfoDao;

    @ApiOperation(value = "消费者参数配置")
    @GetMapping("/consumer")
    public JSONObject consumerParame() throws IOException {
        ClassPathResource seResource = new ClassPathResource("kafka/consumerParameter.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(seInputStream,"UTF-8"));
        return JSON.parseObject(seJson, JSONObject.class);
    }

    @ApiOperation(value = "生产者参数配置")
    @GetMapping("/producer")
    public JSONObject producerParame() throws IOException {
        ClassPathResource seResource = new ClassPathResource("kafka/producerParameter.json");
        InputStream seInputStream = seResource.getInputStream();
        String seJson = String.join("\n", IOUtils.readLines(seInputStream,"UTF-8"));
        return JSON.parseObject(seJson,JSONObject.class);
    }

    @ApiOperation(value = "获取topic列表")
    @GetMapping("/topics/{databaseCode}")
    public ResponseData topics(@PathVariable String databaseCode) throws Exception {
        SourceInfo databaseInfo = sourceInfoDao.getDatabaseInfoById(databaseCode);
        JSONArray topicArr = new JSONArray();
        Properties props = new Properties();
        String username = databaseInfo.getUsername();
        String passwd = databaseInfo.getPassword();
        //添加用户验证
        if(StringUtils.isNotBlank(username)&&StringUtils.isNotBlank(passwd)){
            String jassc = "org.apache.kafka.common.security.plain.PlainLoginModule required\n"
                + "username=\"" + username + "\"\n"
                + "password=\"" + AESSecurityUtils.decryptBase64String(passwd, "0123456789abcdefghijklmnopqrstuvwxyzABCDEF") + "\";";
            props.setProperty("sasl.jaas.config", jassc);
        }
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, databaseInfo.getDatabaseUrl());
        AdminClient adminClient = KafkaAdminClient.create(props);
        try{
            KafkaFuture<Set<String>> topicSet = adminClient.listTopics().names();
            for (String topicName : topicSet.get()) {
                JSONObject topicInfo = new JSONObject();
                topicInfo.put("name", topicName);
                topicArr.add(topicInfo);
            }
        }finally {
            adminClient.close();
        }
        PageQueryResult<Object> result = PageQueryResult.createResult(topicArr, new PageDesc());
        return ResponseSingleData.makeResponseData(result);
    }
}
