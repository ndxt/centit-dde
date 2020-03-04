package com.centit.dde.services;

import com.centit.dde.po.TaskExchange;
import com.centit.support.database.utils.PageDesc;

import java.util.List;
import java.util.Map;

public interface TaskExchangeManager {

    TaskExchange getTaskExchange(String taskId);

    List<TaskExchange> listTaskExchange(Map<String, Object> param, PageDesc pageDesc);

    void createTaskExchange(TaskExchange taskExchange);

    void updateTaskExchange(TaskExchange taskExchange);

    void delTaskExchangeById(String taskId);

    void updateExchangeOptJson(String taskId, String exchangeOptJson);
}
