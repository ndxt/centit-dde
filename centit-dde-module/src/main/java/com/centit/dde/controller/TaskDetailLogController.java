package com.centit.dde.controller;

import com.centit.dde.adapter.po.TaskDetailLog;
import com.centit.dde.services.TaskDetailLogManager;
import com.centit.framework.core.controller.BaseController;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * @author zhf
 */
@RestController
@RequestMapping(value = "taskDetailLog")
@Api(value = "任务明细日志", tags = "任务明细日志")
public class TaskDetailLogController extends BaseController {
    private static final Log log = LogFactory.getLog(TaskLogController.class);

    private final TaskDetailLogManager taskDetailLogManager;
    @Autowired
    public TaskDetailLogController(TaskDetailLogManager taskDetailLogManager) {
        this.taskDetailLogManager = taskDetailLogManager;
    }

    @PostMapping
    @ApiOperation(value = "新增明细日志")
    @WrapUpResponseBody
    public void createTaskDetailLog(TaskDetailLog detailLog){
        taskDetailLogManager.createTaskDetailLog(detailLog);
    }

    @PutMapping(value = "/{logDetailId}")
    @ApiOperation(value = "编辑明细日志")
    @ApiImplicitParam(name = "logDetailId", value = "明细日志编号")
    @WrapUpResponseBody
    public void updateTaskDetailLog(@PathVariable String logDetailId, TaskDetailLog detailLog){
        detailLog.setLogId(logDetailId);
        taskDetailLogManager.updateTaskDetailLog(detailLog);
    }

    @DeleteMapping(value = "/{logDetailId}")
    @ApiOperation(value = "删除明细日志")
    @ApiImplicitParam(name = "logDetailId", value = "明细日志编号")
    @WrapUpResponseBody
    public void delTaskDetailLog(@PathVariable String logDetailId){
        taskDetailLogManager.delTaskDetailLog(logDetailId);
    }

    @GetMapping
    @ApiOperation(value = "查询所有明细日志")
    @WrapUpResponseBody
    public PageQueryResult<TaskDetailLog> listTaskDetailLog(PageDesc pageDesc,HttpServletRequest request){
        Map<String, Object> parameters = collectRequestParameters(request);
        List<TaskDetailLog> taskDetailLogs = taskDetailLogManager.listTaskDetailLog(parameters, pageDesc);
        return PageQueryResult.createResult(taskDetailLogs,pageDesc);
    }

    @GetMapping(value = "/{logDetailId}")
    @ApiOperation(value = "查询单个明细日志")
    @ApiImplicitParam(name = "logDetailId", value = "明细日志编号")
    @WrapUpResponseBody
    public TaskDetailLog getTaskDetailLog(@PathVariable String logDetailId){
        return taskDetailLogManager.getTaskDetailLog(logDetailId);
    }
}
