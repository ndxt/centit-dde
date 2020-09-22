package com.centit.dde.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.aync.service.ExchangeService;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.DataSetDefineService;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "Http触发任务响应", tags = "Http触发任务响应")
@RestController
@RequestMapping(value = "httpTask")
public class HttpTaskController {

    private final DataPacketService dataPacketService;
    private final ExchangeService exchangeService;

    public HttpTaskController(DataPacketService dataPacketService, ExchangeService exchangeService) {
        this.dataPacketService = dataPacketService;
        this.exchangeService = exchangeService;
    }

    @ApiOperation(value = "响应post方法")
    @PutMapping(value = "/do/{packetId}")
    @WrapUpResponseBody
    public BizModel doHttpTask(@PathVariable String packetId, @RequestBody String jsonString) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        for(DataSetDefine dataSetDefine:dataPacket.getDataSetDefines()){
            if("requestBody".equals(dataSetDefine.getQueryName())){
                dataSetDefine.setQuerySQL(jsonString);
                break;
            }
        }
        return dataPacketService.fetchDataPacketData(packetId, null);
    }

    @GetMapping(value = "/run/{packetId}")
    @ApiOperation(value = "立即执行任务")
    @WrapUpResponseBody
    public void runTaskExchange(@PathVariable String packetId) {
        exchangeService.runTask(packetId);
    }

    @GetMapping(value = "/{applicationId}/{interfaceName}/{sourceName}")
    @ApiOperation(value = "获取数据")
    @WrapUpResponseBody
    public DataSet getData(@PathVariable String applicationId, @PathVariable String interfaceName,
                           @PathVariable String sourceName, HttpServletRequest request) {
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        Map<String, Object> params2 = new HashMap<>(10);
        params2.put("interfaceName", interfaceName);
        params2.put("applicationId", applicationId);
        List<DataPacket> list = dataPacketService.listDataPacket(params2, new PageDesc());
        if (list.size() > 0) {
            DataPacket taskExchange = list.get(0);
            BizModel bizModel = dataPacketService.fetchDataPacketData(taskExchange.getPacketId(), params, taskExchange.getDataOptDescJson());
            String sourceCode = getSourceCode(sourceName, taskExchange.getDataOptDescJson());
            if (StringUtils.isNotBlank(sourceCode)) {
                return bizModel.fetchDataSetByName(sourceCode);
            } else {
                return bizModel.getMainDataSet();
            }
        }
        return null;
    }

    private String getSourceCode(String sourceName, JSONObject bizOptJson) {
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
