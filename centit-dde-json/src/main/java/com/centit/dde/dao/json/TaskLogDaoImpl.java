package com.centit.dde.dao.json;

import com.centit.dde.adapter.dao.TaskLogDao;
import com.centit.dde.adapter.po.TaskLog;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository(value = "ddeTaskLogDao")
public class TaskLogDaoImpl implements TaskLogDao {

    @Override
    public void saveNewObject(TaskLog taskLog) {

    }

    @Override
    public int updateObject(TaskLog taskLog) {
        return 0;
    }

    @Override
    public int saveObjectReferences(TaskLog taskLog) {
        return 0;
    }

    @Override
    public TaskLog getObjectById(Object id) {
        return null;
    }

    @Override
    public int deleteObjectForceById(Object id) {
        return 0;
    }

    @Override
    public List<TaskLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        return null;
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
        return null;
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {
        return 0;
    }
}
