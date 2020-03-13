package com.centit.dde.controller;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.services.TaskExchangeManager;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.datapacket.po.DataPacket;
import com.centit.product.datapacket.service.DataPacketService;
import com.centit.product.datapacket.utils.DataPacketUtil;
import com.centit.product.datapacket.vo.DataPacketSchema;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringEscapeUtils;
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
public class TaskExchangeController extends BaseController{

    private static final Log log = LogFactory.getLog(TaskExchangeController.class);

    @Autowired
    private TaskExchangeManager taskExchangeManager;
    @Autowired
    private DataPacketService dataPacketService;
    @PostMapping
    @ApiOperation(value = "新增任务")
    @WrapUpResponseBody
    public void createTaskExchange(TaskExchange taskExchange){
        taskExchange.setExchangeDescJson(StringEscapeUtils.unescapeHtml4(taskExchange.getExchangeDescJson()));
        taskExchangeManager.createTaskExchange(taskExchange);
    }

    @PutMapping(value = "/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @ApiOperation(value = "更新任务")
    @WrapUpResponseBody
    public void updateTaskExchange(@PathVariable String taskId, @RequestBody TaskExchange taskExchange){
        taskExchange.setTaskId(taskId);
        taskExchange.setExchangeDescJson(StringEscapeUtils.unescapeHtml4(taskExchange.getExchangeDescJson()));
        if(taskExchange.getTaskCron()==null) taskExchange.setTaskCron("");
        taskExchangeManager.updateTaskExchange(taskExchange);
    }

    @DeleteMapping(value = "/{taskId}")
    @ApiImplicitParam(name = "taskId", value = "任务ID")
    @ApiOperation(value = "删除任务")
    @WrapUpResponseBody
    public void delTaskExchange(@PathVariable String taskId){
       taskExchangeManager.delTaskExchangeById(taskId);
    }

    @GetMapping
    @ApiOperation(value = "查询所有任务")
    @WrapUpResponseBody
    public PageQueryResult<TaskExchange> listTaskExchange(PageDesc pageDesc){
        List<TaskExchange> taskExchanges = taskExchangeManager.listTaskExchange(new HashMap<String, Object>(), pageDesc);
        return PageQueryResult.createResult(taskExchanges,pageDesc);
    }


    @GetMapping(value = "/{taskId}")
    @ApiOperation(value = "查询一个任务")
    @WrapUpResponseBody
    public TaskExchange getTaskExchange(@PathVariable String taskId){
        TaskExchange taskExchange = taskExchangeManager.getTaskExchange(taskId);
        return taskExchange;
    }
    @ApiOperation(value = "获取任务的数据包模式")
    @GetMapping(value = "/schema/{taskId}")
    @WrapUpResponseBody
    public DataPacketSchema getDataPacketSchema(@PathVariable String taskId){
        TaskExchange taskExchange = taskExchangeManager.getTaskExchange(taskId);
        DataPacket dataPacket = dataPacketService.getDataPacket(taskExchange.getPacketId());
        DataPacketSchema schema = DataPacketSchema.valueOf(dataPacket);
        if(dataPacket!=null) {
            JSONObject obj = dataPacket.getDataOptDesc();
            if (obj != null) {
                schema= DataPacketUtil.calcDataPacketSchema(schema, obj);
            }
            obj = taskExchange.getExchangeDesc();
            if (obj != null) {
                schema= DataPacketUtil.calcDataPacketSchema(schema, obj);
            }
        }
        return schema;
    }

    @ApiOperation(value = "编辑数据交换数据处理描述信息")
    @PutMapping(value = "/opt/{taskId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String taskId, @RequestBody String exchangeOptJson){
        taskExchangeManager.updateExchangeOptJson(taskId, exchangeOptJson);
    }
}
