package com.centit.dde.dao.json;

import com.centit.dde.adapter.dao.TaskDetailLogDao;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.support.database.utils.PageDesc;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("taskDetailLogDao")
public class TaskDetailLogDaoImpl implements TaskDetailLogDao {


    @Override
    public void saveNewObject(TaskDetailLog taskDetailLog) {

    }

    @Override
    public int updateObject(TaskDetailLog taskDetailLog) {
        return 0;
    }

    @Override
    public int saveObjectReferences(TaskDetailLog taskDetailLog) {
        return 0;
    }

    @Override
    public TaskDetailLog getObjectById(Object id) {
        return null;
    }

    @Override
    public int deleteObjectById(Object id) {
        return 0;
    }

    @Override
    public List<TaskDetailLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        return null;
    }

    @Override
    public int delTaskDetailLog(String packetId, Date runBeginTime, boolean isError) {
        return 0;
    }
}
