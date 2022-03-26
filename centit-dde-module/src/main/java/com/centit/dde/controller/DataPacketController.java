package com.centit.dde.controller;

import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
