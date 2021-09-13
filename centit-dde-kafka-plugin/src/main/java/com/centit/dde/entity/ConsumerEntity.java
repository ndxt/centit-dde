package com.centit.dde.entity;

import com.alibaba.fastjson.JSONObject;

public class ConsumerEntity {
    //标签唯一id
    private String id;
    //选择的数据集
    private String sourceID;
    //数据源主键id(保存在数据库中的链接地址信息)
    private String dataSourceID;
    //topic
    private String topic;

    //创建操作对象时的配置信息
    private JSONObject jsonObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }

    public String getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(String dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
