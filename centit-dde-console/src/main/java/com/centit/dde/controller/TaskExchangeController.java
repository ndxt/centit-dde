package com.centit.dde.controller;

import com.centit.dde.po.TaskExchange;
import com.centit.dde.service.TaskExchangeManager;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName TaskExchangeController
 * @Date 2019/3/20 16:23
 * @Version 1.0
 */
@RestController
@Api(value = "交换任务", tags = "交换任务")
@RequestMapping(value = "taskExchange")
public class TaskExchangeController {

    private static final Log log = LogFactory.getLog(TaskExchangeController.class);

    @Autowired
    private TaskExchangeManager taskExchangeManager;

    @PostMapping
    @WrapUpResponseBody
    public void createTaskExchange(TaskExchange taskExchange){
        taskExchangeManager.createTaskExchange(taskExchange);
    }

    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @PutMapping(value = "/{taskId}")
    @WrapUpResponseBody
    public void updateTaskExchange(@PathVariable String taskId, TaskExchange taskExchange){
        taskExchange.setTaskId(taskId);
        taskExchangeManager.updateTaskExchange(taskExchange);
    }

    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @PutMapping(value = "/{taskId}")
    @WrapUpResponseBody
    public void updateTaskExchange(@PathVariable String taskId){
       taskExchangeManager.delTaskExchangeById(taskId);
    }

    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<TaskExchange> listTaskExchange(PageDesc pageDesc){
        List<TaskExchange> taskExchanges = taskExchangeManager.listTaskExchange(new HashMap<String, Object>(), pageDesc);
        return PageQueryResult.createResult(taskExchanges,pageDesc);
    }

    @ApiOperation(value = "查询一个任务")
    @GetMapping(value = "/{taskId}")
    @WrapUpResponseBody
    public TaskExchange getTaskExchange(@PathVariable String taskId){
        TaskExchange taskExchange = taskExchangeManager.getTaskExchange(taskId);
        return taskExchange;
    }

}
