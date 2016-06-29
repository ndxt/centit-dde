package com.centit.sys.service;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-5-17
 * Time: 下午2:51
 * 数据同步异常
 */
public class DataSyncException extends Exception {
    public DataSyncException(String message) {
        super(message);
    }


    public DataSyncException(String message, Throwable cause) {
        super(message, cause);
    }
}
