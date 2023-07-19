package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSON;
import com.centit.dde.adapter.dao.TaskDetailLogDao;
import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("taskDetailLogDao")
public class TaskDetailLogDaoImpl implements TaskDetailLogDao {

    private final static Logger logger = LoggerFactory.getLogger(TaskDetailLogDaoImpl.class);

    @Override
    public void saveNewObject(TaskDetailLog taskDetailLog) {
        logger.info(JSON.toJSONString(taskDetailLog));
    }

    @Override
    public int updateObject(TaskDetailLog taskDetailLog) {
        return 1;
    }

    @Override
    public int saveObjectReferences(TaskDetailLog taskDetailLog) {
        return 1;
    }

    @Override
    public TaskDetailLog getObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public int deleteObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public List<TaskDetailLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public int delTaskDetailLog(String packetId, Date runBeginTime, boolean isError) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }
}
