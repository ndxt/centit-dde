package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.TaskExchange;
import com.centit.dde.services.TaskExchangeManager;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.datapacket.po.DataPacket;
import com.centit.product.datapacket.service.DataPacketService;
import com.centit.product.datapacket.utils.DataPacketUtil;
import com.centit.product.datapacket.vo.DataPacketSchema;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "datatransfer")
@Api(value = "数据传输接口", tags = "数据传输接口")
public class DataInterfaceController extends BaseController {

    @Autowired
    private TaskExchangeManager taskExchangeManager;
    @Autowired
    private DataPacketService dataPacketService;
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
    @ApiOperation(value = "获取数据集名称集合")
    @PostMapping(value = "/allschema/{taskId}")
    @WrapUpResponseBody
    public DataPacketSchema getDataPacketSchemaWithOpt(@PathVariable String taskId) {
        TaskExchange taskExchange = taskExchangeManager.getTaskExchange(taskId);
        DataPacket dataPacket = dataPacketService.getDataPacket(taskExchange.getPacketId());
        DataPacketSchema schema = DataPacketSchema.valueOf(dataPacket);
        if (taskExchange.getExchangeDesc() != null) {
            return DataPacketUtil.calcDataPacketSchema(schema,taskExchange.getExchangeDesc());
        }
        return schema;
    }

    @GetMapping(value = "/{applicationId}/{interfaceName}/{sourceName}")
    @ApiOperation(value = "获取数据")
    @WrapUpResponseBody
    public DataSet getData(@PathVariable String applicationId, @PathVariable String interfaceName,
                           @PathVariable String sourceName, HttpServletRequest request){
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        Map<String, Object> params2 = new HashMap<>();
        params2.put("interfaceName",interfaceName);
        params2.put("applicationId",applicationId);
        List<TaskExchange> list = taskExchangeManager.listTaskExchange(params2,new PageDesc());
        if (list.size()>0){
            TaskExchange taskExchange=list.get(0);
            BizModel bizModel=dataPacketService.fetchDataPacketData(taskExchange.getPacketId(), params, taskExchange.getExchangeDescJson());
            String sourceCode= getSourceCode(sourceName,taskExchange.getExchangeDesc());
            if(StringUtils.isNotBlank(sourceCode)) {
                return bizModel.fetchDataSetByName(sourceCode);
            }else{
                return bizModel.getMainDataSet();
            }
        }
        return null;
    }
    private String getSourceCode(String sourceName, JSONObject bizOptJson){
        JSONArray jsonArray = bizOptJson.getJSONArray("steps");
        for (Object step : jsonArray) {
            if (step instanceof JSONObject) {
                if ("interface".equals(((JSONObject) step).getString("operation"))) {
                    Object mapInfo = ((JSONObject) step).get("fieldsMap");
                    if (mapInfo instanceof Map) {
                        return (String) ((Map) mapInfo).get(sourceName);
                    }
                } else {
                    return "";
                }
            }
        }
        return "";
    }
}
