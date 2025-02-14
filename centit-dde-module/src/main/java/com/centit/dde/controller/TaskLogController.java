package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.vo.DelTaskLogParameter;
import com.centit.dde.vo.StatisticsParameter;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
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
        return taskLogManager.getLog(logId);
    }

    @PostMapping(value = "/statistics")
    @ApiOperation(value = "日志统计")
    @WrapUpResponseBody
    public Map<String, Object> getLogStatisticsInfo(@RequestBody StatisticsParameter parameter){
        return taskLogManager.getLogStatisticsInfo(parameter);
    }


    @PostMapping(value = "delLogInfoAndLogDetail")
    @ApiOperation(value = "删除某个时间段之前的日志")
    @WrapUpResponseBody
    public ResponseData delLogInfoAndLogDetail(@RequestBody DelTaskLogParameter delTaskLogParameter){
        if (StringUtils.isBlank(delTaskLogParameter.getPacketId()))return ResponseData.makeErrorMessage("主键不能为空！");
        if (StringUtils.isBlank(delTaskLogParameter.getRunBeginTime()))return ResponseData.makeErrorMessage("删除时间不能为空！");
        int taskLogCount = taskLogManager.deleteTaskLog(delTaskLogParameter);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("taskLogCount",taskLogCount);
        return ResponseData.makeResponseData(jsonObject);
    }

}
