package com.centit.dde.service;

import com.centit.core.service.BaseEntityManager;
import com.centit.dde.po.TaskDetailLog;

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
public interface TaskDetailLogManager extends BaseEntityManager<TaskDetailLog> {
    public Long getTaskDetailLogId();

    void flush();
}
