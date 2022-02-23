package com.centit.dde.consumer;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ConsumerEntity {
    //标签唯一id
    private String id;
    //kafka服务地址
    private String databaseId;
    //topic
    private JSONArray topic;

    private String groupId;
}
