package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.services.TaskLogManager;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @Autowired
    public TaskLogController(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    @DeleteMapping(value = "/{logId}")
    @ApiOperation(value = "删除日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public void delTaskLog(@PathVariable String logId){
        taskLogManager.deleteTaskLogById(logId);
    }

    @GetMapping
    @ApiOperation(value = "查询所有日志")
    @WrapUpResponseBody
    public PageQueryResult<Map<String, Object>> listTaskLog(PageDesc pageDesc, HttpServletRequest request){
        Map<String, Object> parameters = collectRequestParameters(request);
        List<Map<String, Object>> callApiLogs = taskLogManager.listTaskLog(parameters, pageDesc);
        return PageQueryResult.createResult(callApiLogs, pageDesc);
    }

    @GetMapping(value = "/{logId}")
    @ApiOperation(value = "查询单个日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public CallApiLog getTaskLog(@PathVariable String logId){
        return taskLogManager.getLogWithDetail(logId);
    }

    @GetMapping(value = "/details/{logId}")
    @ApiOperation(value = "查询单个日志")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public List<CallApiLogDetail> getTaskLogDetails(@PathVariable String logId){
        return taskLogManager.listLogDetails(logId);
    }

    @GetMapping(value = "/callByHour")
    @ApiOperation(value = "日志统计")
    @WrapUpResponseBody
    public JSONArray statApiCallSumByHour(String taskId, String statType){

        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statApiCallSumByHour(taskId,
                DatetimeOpt.addMonths(currentDate,-1),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statApiCallSumByHour(taskId,
                DatetimeOpt.addDays(currentDate,-7),
                currentDate );
        }
        // day
        return taskLogManager.statApiCallSumByHour(taskId,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );

    }

}
