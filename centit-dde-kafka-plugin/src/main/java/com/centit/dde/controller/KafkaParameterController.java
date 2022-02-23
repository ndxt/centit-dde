package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

@Api(value = "kafka参数管理接口", tags = "kafka参数管理接口")
@Controller
@ResponseBody
@RequestMapping(value = "/kafka")
public class KafkaParameterController {
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
}
