package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.LoginUserPermissionCheck;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Api(value = "已发布API网关接口管理", tags = "已发布API网关接口管理")
@RestController
@RequestMapping(value = "packet")
public class DataPacketController extends BaseController {
    private final DataPacketService dataPacketService;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    public DataPacketController(DataPacketService dataPacketService) {
        this.dataPacketService = dataPacketService;
    }

    @ApiOperation(value = "查询API网关")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<DataPacket> listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        List<DataPacket> list = dataPacketService.listDataPacket(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "工作流查询API网关")
    @GetMapping(value = "/workflow/{optId}")
    @WrapUpResponseBody
    public List<Map<String,String>> listDataPacket(@PathVariable String optId, HttpServletRequest request) {
        return dataPacketService.listDataPacket(optId, WebOptUtils.getCurrentTopUnit(request));
    }


    @ApiOperation(value = "查询单个API网关")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacket getDataPacket(@PathVariable String packetId) {
        return dataPacketService.getDataPacket(packetId);
    }


    @ApiOperation(value = "更改api日志模式")
    @ApiImplicitParam(
        name = "jsonObject",
        value = "更改api日志级别参数：{logLevel:\"logLevel\", packetIds:[\"packetId\"],osId:\"osId\"};"
    )
    @PutMapping(value = "/chgLogLevel")
    @WrapUpResponseBody
    public void chgLogLevel(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JSONArray packetIds = jsonObject.getJSONArray("packetIds");
        String osId = jsonObject.getString("osId");
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, osId, request);
        String logLevel = jsonObject.getString("logLevel");
        int lv = DataPacket.mapLogLevel(logLevel);
        if(lv<1){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "日志等级参数不正确！");
        }

        List<String> apis = StringBaseOpt.objectToStringList(packetIds);
        dataPacketService.updatePackedLogLevel(lv, apis);
    }

    @ApiOperation(value = "更改应用所有api日志模式")
    @ApiImplicitParam(
        name = "jsonObject",
        value = "更改应用所有日志级别参数：{logLevel:\"logLevel\", osId:\"osId\"}"
    )
    @PutMapping(value = "/chgOSLogLevel")
    @WrapUpResponseBody
    public void chgOSLogLevel(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String osId = jsonObject.getString("osId");
        String logLevel = jsonObject.getString("logLevel");
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, osId, request);
        int lv = DataPacket.mapLogLevel(logLevel);
        if(lv<1){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "日志等级参数不正确！");
        }
        dataPacketService.updateApplicationLogLevel(lv, osId);
    }
}
