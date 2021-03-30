package com.centit.dde.controller;

import com.centit.dde.po.DataPacketCopy;
import com.centit.dde.services.DataPacketCopyService;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Api(value = "COPY数据包", tags = "COPY数据包")
@RestController
@RequestMapping(value = "packet")
public class DataPacketCopyController extends BaseController {

    private FileStore fileStore;

    @Autowired(required = false)
    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    private final DataPacketCopyService dataPacketCopyService;



    public DataPacketCopyController(DataPacketCopyService dataPacketCopyService) {
        this.dataPacketCopyService = dataPacketCopyService;
    }

    @ApiOperation(value = "新增数据包")
    @PostMapping
    @WrapUpResponseBody
    public void createDataPacket(@RequestBody DataPacketCopy dataPacketCopy, HttpServletRequest request) {
        dataPacketCopy.setRecorder(WebOptUtils.getCurrentUserCode(request));
        dataPacketCopy.setDataOptDescJson(dataPacketCopy.getDataOptDescJson());
        //将发布状态改成1，每次编辑后都需要重新发布
        dataPacketCopy.setState("1");
        dataPacketCopyService.createDataPacket(dataPacketCopy);
    }

    @ApiOperation(value = "编辑数据包")
    @PutMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacket(@PathVariable String packetId, @RequestBody DataPacketCopy dataPacket) {
        dataPacket.setPacketId(packetId);
        dataPacket.setDataOptDescJson(dataPacket.getDataOptDescJson());
        dataPacket.setState("1");
        dataPacketCopyService.updateDataPacket(dataPacket);
    }

    @ApiOperation(value = "数据包发布")
    @PutMapping(value = "release/{packetId}")
    @WrapUpResponseBody
    public void releaseDataPacket(@PathVariable String packetId) {
        DataPacketCopy dataPacketCopy = dataPacketCopyService.getDataPacket(packetId);
        //修改发布状态
        dataPacketCopy.setState("2");
        dataPacketCopyService.releaseDataPacket(dataPacketCopy);
    }

    @ApiOperation(value = "编辑数据包数据处理描述信息")
    @PutMapping(value = "/opt/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String packetId, @RequestBody String dataOptDescJson) {
        dataPacketCopyService.updateDataPacketOptJson(packetId, dataOptDescJson);
    }

    @ApiOperation(value = "删除数据包")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void deleteDataPacket(@PathVariable String packetId) {
        dataPacketCopyService.deleteDataPacket(packetId);
    }

    @ApiOperation(value = "查询数据包")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<DataPacketCopy> listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        List<DataPacketCopy> list = dataPacketCopyService.listDataPacket(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个数据包")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacketCopy getDataPacket(@PathVariable String packetId) {
        return dataPacketCopyService.getDataPacket(packetId);
    }

    @GetMapping(value = "/exist/{applicationId}/{interfaceName}")
    @ApiOperation(value = "接口名称是否已存在")
    @WrapUpResponseBody
    public Boolean isExist(@PathVariable String applicationId, @PathVariable String interfaceName) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("interfaceName", interfaceName);
        params.put("applicationId", applicationId);
        List<DataPacketCopy> list = dataPacketCopyService.listDataPacket(params, new PageDesc());
        return list.size() > 0;
    }


}