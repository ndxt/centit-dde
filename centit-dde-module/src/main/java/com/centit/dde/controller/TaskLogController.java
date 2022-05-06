package com.centit.dde.controller;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.TaskDetailLogManager;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.vo.StatisticsParameter;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@RestController
@RequestMapping(value = "taskLog")
@Api(value = "任务日志", tags = "任务日志")
public class TaskLogController extends BaseController {

    private static final Log log = LogFactory.getLog(TaskLogController.class);

    private final TaskLogManager taskLogManager;

    private final TaskDetailLogManager taskDetailLogManager;

    @Autowired
    public TaskLogController(TaskLogManager taskLogManager,TaskDetailLogManager taskDetailLogManager) {
        this.taskLogManager = taskLogManager;
        this.taskDetailLogManager=taskDetailLogManager;
    }

    @PostMapping
    @ApiOperation(value = "新增日志")
    @WrapUpResponseBody
    public void createTaskLog(TaskLog taskLog){
        taskLogManager.createTaskLog(taskLog);
    }

    @PutMapping(value = "/{logId}")
    @ApiOperation(value = "编辑日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public void updateTaskExchange(@PathVariable String logId, TaskLog taskLog){
        taskLog.setLogId(logId);
        taskLogManager.updateTaskLog(taskLog);
    }

    @DeleteMapping(value = "/{logId}")
    @ApiOperation(value = "删除日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public void delTaskExchange(@PathVariable String logId){
        taskLogManager.deleteTaskLogById(logId);
    }

    @GetMapping
    @ApiOperation(value = "查询所有日志")
    @WrapUpResponseBody
    public PageQueryResult<TaskLog> listTaskExchange(PageDesc pageDesc, HttpServletRequest request){
        Map<String, Object> parameters = collectRequestParameters(request);
        List<TaskLog> taskLogs = taskLogManager.listTaskLog(parameters, pageDesc);
        return PageQueryResult.createResult(taskLogs,pageDesc);
    }

    @GetMapping(value = "/{logId}")
    @ApiOperation(value = "查询单个日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public TaskLog getTaskLog(@PathVariable String logId){
        return taskLogManager.getLog(logId);
    }

    @PostMapping(value = "/statistics")
    @ApiOperation(value = "日志统计")
    @WrapUpResponseBody
    public Map<String, Object> getLogStatisticsInfo(@RequestBody StatisticsParameter parameter){
        return taskLogManager.getLogStatisticsInfo(parameter);
    }


    @DeleteMapping(value = "/{runBeginTime}/{isError}")
    @ApiOperation(value = "删除某个时间段之前的日志")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "runBeginTime",value = "指定开始时间"),
        @ApiImplicitParam(name = "isError",value = "是否包含错误信息"),
    })
    @WrapUpResponseBody
    public JSONObject delLogInfoAndLogDetail(@PathVariable String runBeginTime, @PathVariable Boolean isError){
        int taskLogCount = taskLogManager.deleteTaskLog(runBeginTime, isError);
        int taskDetailLogCount = taskDetailLogManager.delTaskDetailLog(runBeginTime, isError);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskLogCount",taskLogCount);
        jsonObject.put("taskDetailLogCount",taskDetailLogCount);
        return jsonObject;
    }

}
