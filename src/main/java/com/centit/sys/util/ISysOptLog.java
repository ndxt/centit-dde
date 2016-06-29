package com.centit.sys.util;

import java.util.List;
import java.util.Map;

import com.centit.core.utils.PageDesc;
import com.centit.sys.po.OptLog;

/**
 * 记录系统日志接口
 *
 * @author sx
 * @create 2012-12-7
 */
public interface ISysOptLog {

    /**
     * 记录系统日志
     *
     * @param usercode   用户代码
     * @param optId      F_OPTINFO表中 OPTID 字段
     * @param tagId      一般用于关联到业务主体[业务主体主键]
     * @param optMethod  记录系统日志的方法名
     * @param optContent 记录操作内容
     * @param oldValue   更新操作时，更新前数据
     */
    void log(String usercode, String optId, String tagId, String optMethod, String optContent, String oldValue);

    void log(String usercode, String tagId, String optMethod, String optContent, String oldValue);

    void log(String usercode, String tagId, String optContent, String oldValue);

    void log(String usercode, String tagId, String optContent);

    /**
     * 记录系统日志
     *
     * @param optLog
     */
    void log(OptLog optLog);

    List<OptLog> listOptLog(Map<String, Object> filterMap, PageDesc pageDesc);

    List<OptLog> listOptLog(Map<String, Object> filterMap);

}
