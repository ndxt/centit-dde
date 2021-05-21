package com.centit.dde.transaction.vo;

import com.centit.support.database.metadata.IDatabaseInfo;

/**
 * 数据库基本信息
 */
public interface ISourceInfo extends IDatabaseInfo {
    String DATABASE="D";
    String MONGO_DB="M";
    String REDIS="R";
    String ELS="E";
    String KAFKA="K";
    String RABBIT_MQ="B";
    String HTTP="H";
    String getSourceType();
}

