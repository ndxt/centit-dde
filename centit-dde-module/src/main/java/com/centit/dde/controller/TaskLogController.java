package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.services.TaskLogManager;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(TaskLogController.class);

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
    @ApiOperation(value = "查询单个日志明细")
    @ApiImplicitParam(name = "logId", value = "日志ID")
    @WrapUpResponseBody
    public List<CallApiLogDetail> getTaskLogDetails(@PathVariable String logId){
        return taskLogManager.listLogDetails(logId);
    }

    @GetMapping(value = "/statByTask")
    @ApiOperation(value = "日志统计,单条API日志")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "API接口ID"),
        @ApiImplicitParam(name = "statType", value = "统计类型：month/week/day")})
    @WrapUpResponseBody
    public JSONArray statApiCallSumByTask(String taskId, String statType){
        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statApiCallSumByTask(taskId,
                DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statApiCallSumByTask(taskId,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate );
        }
        // day
        return taskLogManager.statApiCallSumByTask(taskId,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );

    }

    @GetMapping(value = "/statByOs")
    @ApiOperation(value = "日志统计，一个应用下的日志")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "osId", value = "应用ID，applicationId/osId"),
        @ApiImplicitParam(name = "statType", value = "统计类型：month/week/day")})
    @WrapUpResponseBody
    public JSONArray statApiCallSumByApplication(String osId, String statType){
        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statApiCallSumByApplication(osId,
                DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statApiCallSumByApplication(osId,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate );
        }
        // day
        return taskLogManager.statApiCallSumByApplication(osId,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );

    }

    @GetMapping(value = "/statByTopUnit")
    @ApiOperation(value = "日志统计，一个租户下的日志")
    @ApiImplicitParam(name = "statType", value = "统计类型：month/week/day")
    @WrapUpResponseBody
    public JSONArray statApiCallSumByTopUnit(String statType, HttpServletRequest request){
        Date currentDate = DatetimeOpt.currentUtilDate();
        String topUnit = WebOptUtils.getCurrentTopUnit(request);
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statApiCallSumByTopUnit(topUnit,
                DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statApiCallSumByTopUnit(topUnit,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate );
        }
        // day
        return taskLogManager.statApiCallSumByTopUnit(topUnit,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );

    }

    @GetMapping(value = "/apiEfficiency")
    @ApiOperation(value = "日志统计，根据optId统计接口响应时间和成功率")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "taskId", value = "接口ID optCode"),
        @ApiImplicitParam(name = "startDate", value = "开始时间"),
        @ApiImplicitParam(name = "endDate", value = "结束时间")})
    @WrapUpResponseBody
    public JSONObject statApiEfficiency(String taskId,String statType){
        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")) {
            return taskLogManager.statApiEfficiency(taskId, DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate);
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statApiEfficiency(taskId,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate);
        }
        return taskLogManager.statApiEfficiency(taskId,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate);
    }

    @GetMapping(value = "/osStatInfo")
    @ApiOperation(value = "应用下面的统计信息")
    @ApiImplicitParam(name = "osId", value = "应用ID，applicationId/osId")
    @WrapUpResponseBody
    public JSONObject statApplication(String osId){
        return taskLogManager.statApplicationInfo(osId);
    }

    @GetMapping(value = "/topActive")
    @ApiOperation(value = "应用下面最活跃的应用")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "osId", value = "应用ID，applicationId/osId"),
        @ApiImplicitParam(name = "topSize", value = ">=5, 根据空间选择， &topSize=15"),
        @ApiImplicitParam(name = "statType", value = "统计类型：month/week/day")})
    @WrapUpResponseBody
    public JSONArray statTopActive(String osId, Integer topSize,  String statType){
        if(topSize == null || topSize < 5) topSize = 5;
        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statTopActive(osId, topSize,
                DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statTopActive(osId, topSize,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate );
        }
        // day
        return taskLogManager.statTopActive(osId, topSize,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );
    }

    @GetMapping(value = "/topFailed")
    @ApiOperation(value = "应用下面失败的应用")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "osId", value = "应用ID，applicationId/osId"),
        @ApiImplicitParam(name = "topSize", value = ">=5, 根据空间选择， &topSize=15"),
        @ApiImplicitParam(name = "statType", value = "统计类型：month/week/day")})
    @WrapUpResponseBody
    public JSONArray statTopFailed(String osId, Integer topSize, String statType){
        if(topSize == null || topSize < 5) topSize = 5;
        Date currentDate = DatetimeOpt.currentUtilDate();
        if(StringUtils.equalsAnyIgnoreCase(statType , "month")){
            return taskLogManager.statTopFailed(osId, topSize,
                DatetimeOpt.truncateToDay(DatetimeOpt.addMonths(currentDate,-1)),
                currentDate );
        }
        if(StringUtils.equalsAnyIgnoreCase(statType , "week")){
            return taskLogManager.statTopFailed(osId, topSize,
                DatetimeOpt.truncateToDay(DatetimeOpt.addDays(currentDate,-7)),
                currentDate );
        }
        // day
        return taskLogManager.statTopFailed(osId, topSize,
            DatetimeOpt.addDays(currentDate,-1),
            currentDate );
    }

}
