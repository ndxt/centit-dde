package com.centit.dde.controller;

import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "数据包", tags = "数据包")
@RestController
@RequestMapping(value = "dataPacket")
public class DataPacketController extends BaseController {

    @Autowired
    DataPacketService dataPacketService;

    public DataPacketController(DataPacketService dataPacketService) {
        this.dataPacketService=dataPacketService;
    }


    @ApiOperation(value = "查询数据包")
    @GetMapping()
    @WrapUpResponseBody
    public PageQueryResult<DataPacket> listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        List<DataPacket> list = dataPacketService.listDataPacket(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "删除数据包")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void deleteDataPacket(@PathVariable String packetId) {
        dataPacketService.deleteDataPacket(packetId);
    }
}
