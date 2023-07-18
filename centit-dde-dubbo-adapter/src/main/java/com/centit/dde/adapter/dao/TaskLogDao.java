package com.centit.dde.adapter.dao;

import com.centit.dde.adapter.po.TaskLog;
import com.centit.support.database.utils.PageDesc;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface TaskLogDao {

    void saveNewObject(TaskLog taskLog);

    int updateObject(TaskLog taskLog);

    int saveObjectReferences(TaskLog taskLog);

    TaskLog getObjectById(Object id);

    int deleteObjectForceById(Object id);

    List<TaskLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc);

    Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter);

    int deleteTaskLog(String packetId, Date runBeginTime, boolean isError);
}
