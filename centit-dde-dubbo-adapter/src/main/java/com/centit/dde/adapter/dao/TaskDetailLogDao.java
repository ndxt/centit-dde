package com.centit.dde.adapter.dao;

import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.support.database.utils.PageDesc;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */

public interface TaskDetailLogDao {
    void saveNewObject(TaskDetailLog taskDetailLog);

    int updateObject(TaskDetailLog taskDetailLog);

    int saveObjectReferences(TaskDetailLog taskDetailLog);

    TaskDetailLog getObjectById(Object id);

    int deleteObjectById(Object id);

    List<TaskDetailLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc);

    int delTaskDetailLog(String packetId, Date runBeginTime, boolean isError);
}
