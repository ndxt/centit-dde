package com.centit.dde.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 生产者标签参数
 */
public class ProducerEntity {
    //标签唯一id
    private String id;
    //选择的数据集
    private String sourceID;
    //数据源主键id(保存在数据库中的链接地址信息)
    private String dataSourceID;
    //topic
    private String topic;

    //是否异步发送
    private Boolean isAsyn=true;

    //指定分区
    private Integer partition;

    //创建操作对象时的配置信息
    private JSONObject jsonObject;

    public Boolean getAsyn() {
        return isAsyn;
    }

    public void setAsyn(Boolean asyn) {
        isAsyn = asyn;
    }

    public Integer getPartition() {
        return partition;
    }

    public void setPartition(Integer partition) {
        this.partition = partition;
    }

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
