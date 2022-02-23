package com.centit.dde.consumer;

import lombok.Data;

@Data
public class ConsumerEntity {
    //标签唯一id
    private String id;
    //选择的数据集
    private String source;
    //数据源主键id(保存在数据库中的链接地址信息)
    private String databaseId;
    //topic
    private String topic;

    private String groupId;
}
