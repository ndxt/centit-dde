package com.centit.dde.services;


import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

/**
 * create by scaffold
 * <p/>
 * TaskLog,TaskDetailLog,TaskErrorData 的关系。
 * tasklog：任务日志
 * 每个交换任务的一次运行 会在tasklog中插入一条记录
 * 每个导入任务的会在tasklog中插入一条记录
 * 每个webService接收一条数据会插入一条记录，由于WebService不是定时任务，所以taskID为固定的值 2
 * TaskDetailLog ： 任务日志明细
 * 每个交换任务的一个对应关系会在     TaskDetailLog 插入一条记录
 * 每个导入任务的一个表的操作 会在 TaskDetailLog 插入一条记录
 * 每个webService接收数据的一个操作 会在 TaskDetailLog 插入一条记录
 * <p/>
 * 人工运行或者测试对用关系，或则导出操作是 也会插入一条记录，这是因为没有对应的taskID 所已经taskID的默认值社会自为1
 * TaskErrorData ：操作错误是数据信息
 * 每个交换操作错误时的数据记录
 * 导入数据操作时的数据记录
 * webService接收数据操作时的数据记录
 *
 * @author codefan@hotmail.com
 */
public interface TaskDetailLogManager {

    TaskDetailLog getTaskDetailLog(String logDetailId);

    List<TaskDetailLog> listTaskDetailLog(Map<String, Object> param, PageDesc pageDesc);

    void createTaskDetailLog(TaskDetailLog detailLog);

    void updateTaskDetailLog(TaskDetailLog detailLog);

    void delTaskDetailLog(String logDetailId);
    //删除指定时间之前的数据
    int delTaskDetailLog(DelTaskLogParameter delTaskLogParameter);
}
