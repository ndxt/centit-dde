package com.centit.dde.controller;

import com.alibaba.fastjson2.JSON;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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
    public List<Map<String,String>> listDataPacket(@PathVariable String optId) {
        return dataPacketService.listDataPacket(optId);
    }


    @ApiOperation(value = "查询单个API网关")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacket getDataPacket(@PathVariable String packetId) {
        return dataPacketService.getDataPacket(packetId);
    }


    @ApiOperation(value = "更改api日志模式")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "logLevel", type = "path", value = "日志等级"),
        @ApiImplicitParam(name = "packetIds", type="body", value="接口id或者id数组", dataTypeClass = String.class)})
    @PutMapping(value = "/chgLogLevel/{logLevel}")
    @WrapUpResponseBody
    public void chgLogLevel(@PathVariable String logLevel, @RequestBody String packetIds) {
        int lv = DataPacket.mapLogLevel(logLevel);
        if(lv<1){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "日志等级参数不正确！");
        }
        Object apiIds = JSON.parse(packetIds);
        String[] apis = StringBaseOpt.objectToStringArray(apiIds);
        dataPacketService.updatePackedLogLevel(lv, apis);
    }

    @ApiOperation(value = "更改模块所有api日志模式")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "logLevel", type = "path", value = "日志等级"),
        @ApiImplicitParam(name = "moduleId", type="body", value="模块id", dataTypeClass = String.class)})
    @PutMapping(value = "/chgModuleLevel/{logLevel}")
    @WrapUpResponseBody
    public void chgModuleLogLevel(@PathVariable String logLevel, @RequestBody String moduleId) {
        int lv = DataPacket.mapLogLevel(logLevel);
        if(lv<1){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "日志等级参数不正确！");
        }
        dataPacketService.updateModuleLogLevel(lv, moduleId);
    }

    @ApiOperation(value = "更改应用所有api日志模式")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "logLevel", type = "path", value = "日志等级"),
        @ApiImplicitParam(name = "osId", type="body", value="应用id，OS_ID", dataTypeClass = String.class)})
    @PutMapping(value = "/chgModuleLevel/{logLevel}")
    @WrapUpResponseBody
    public void chgOSLogLevel(@PathVariable String logLevel, @RequestBody String osId) {
        int lv = DataPacket.mapLogLevel(logLevel);
        if(lv<1){
            throw new ObjectException(ObjectException.DATA_VALIDATE_ERROR, "日志等级参数不正确！");
        }
        dataPacketService.updateApplicationLogLevel(lv, osId);
    }
}
