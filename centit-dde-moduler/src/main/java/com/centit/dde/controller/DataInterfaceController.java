package com.centit.dde.controller;

import com.centit.dde.po.TaskExchange;
import com.centit.dde.services.TaskExchangeManager;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(value = "datatransfer")
@Api(value = "数据传输接口", tags = "数据传输接口")
public class DataInterfaceController extends BaseController {

    @Autowired
    private TaskExchangeManager taskExchangeManager;

    @PostMapping(value = "/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @ApiOperation(value = "上传数据")
    @WrapUpResponseBody
    public void uploadTaskData(@PathVariable String taskId,
                                   @RequestBody String dataJson){
        //TODO 1 将dataJson保存到临时文件
        // 2创建任务，调用centit-dde-datamoving 程序执行数据倒入
    }

    @GetMapping(value = "/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @ApiOperation(value = "检验任务")
    @WrapUpResponseBody
    public boolean checkTaskInfo(@PathVariable String taskId){
        TaskExchange taskExchange = taskExchangeManager.getTaskExchange(taskId);
        return taskExchange!=null;
    }
}
