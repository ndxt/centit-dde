package com.centit.dde.producer;

import lombok.Data;

/**
 * 生产者标签参数
 */
@Data
public class ProducerEntity {
    //标签唯一id
    private String id;
    //选择的数据集
    private String source;
    //数据源主键id(保存在数据库中的链接地址信息)
    private String databaseId;
    //topic
    private String topic;
    //指定发送分区
    private Integer partition;
    //指定发送key
    private String key;
    //是否异步发送
    private Boolean isAsyn=true;
}
