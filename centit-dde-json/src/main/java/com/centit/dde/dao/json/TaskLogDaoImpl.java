package com.centit.dde.dao.json;

import com.alibaba.fastjson2.JSON;
import com.centit.dde.adapter.dao.TaskLogDao;
import com.centit.dde.adapter.po.TaskLog;
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
@Repository(value = "ddeTaskLogDao")
public class TaskLogDaoImpl implements TaskLogDao {

    private final static Logger logger = LoggerFactory.getLogger(TaskLogDaoImpl.class);

    @Override
    public void saveNewObject(TaskLog taskLog) {
        logger.info(JSON.toJSONString(taskLog));
    }

    @Override
    public int updateObject(TaskLog taskLog) {
        return 1;
    }

    @Override
    public int saveObjectReferences(TaskLog taskLog) {
        return 1;
    }

    @Override
    public TaskLog getObjectById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public int deleteObjectForceById(Object id) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public List<TaskLog> listObjectsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {
        throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
            "该方法在当前版本下没有实现，请联系研发人员!");
    }
}
