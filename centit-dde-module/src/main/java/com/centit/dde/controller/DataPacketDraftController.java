package com.centit.dde.controller;

import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhf
 */
@Api(value = "未发布API网关接口管理", tags = "未发布API网关接口管理")
@RestController
@RequestMapping(value = "packetDraft")
public class DataPacketDraftController extends BaseController {


    private final DataPacketDraftService dataPacketDraftService;

    @Autowired
    WorkGroupManager workGroupManager;


    public DataPacketDraftController(DataPacketDraftService dataPacketDraftService) {
        this.dataPacketDraftService = dataPacketDraftService;
    }

    @ApiOperation(value = "新增API网关")
    @PostMapping
    @WrapUpResponseBody
    public DataPacketDraft createDataPacket(@RequestBody DataPacketDraft dataPacketDraft, HttpServletRequest request) {
        if (!workGroupManager.loginUserIsExistWorkGroup(dataPacketDraft.getOsId())){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        dataPacketDraft.setRecorder(WebOptUtils.getCurrentUserCode(request));
        dataPacketDraft.setDataOptDescJson(dataPacketDraft.getDataOptDescJson());
        dataPacketDraftService.createDataPacket(dataPacketDraft);
        return dataPacketDraft;
    }

    @ApiOperation(value = "编辑API网关")
    @PutMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacket(@PathVariable String packetId, @RequestBody DataPacketDraft dataPacket) {
        if (!workGroupManager.loginUserIsExistWorkGroup(dataPacket.getOsId())){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        dataPacket.setPacketId(packetId);
        dataPacket.setDataOptDescJson(dataPacket.getDataOptDescJson());
        dataPacketDraftService.updateDataPacket(dataPacket);
    }

    @ApiOperation(value = "API网关发布")
    @PutMapping(value = "publish/{packetId}")
    @WrapUpResponseBody
    public void publishDataPacket(@PathVariable String packetId) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (!workGroupManager.loginUserIsExistWorkGroup(dataPacketDraft.getOsId())){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        dataPacketDraftService.updateDataPacket(dataPacketDraft);
        dataPacketDraftService.publishDataPacket(dataPacketDraft);
    }

    @ApiOperation(value = "编辑API网关数据处理描述信息")
    @PutMapping(value = "/opt/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String packetId, @RequestBody String dataOptDescJson) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (!workGroupManager.loginUserIsExistWorkGroup(dataPacketDraft.getOsId())){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        dataPacketDraftService.updateDataPacketOptJson(packetId, dataOptDescJson);
    }

    @ApiOperation(value = "删除API网关")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void deleteDataPacket(@PathVariable String packetId) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (!workGroupManager.loginUserIsExistWorkGroup(dataPacketDraft.getOsId())){
            throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您未登录或没有权限！");
        }
        dataPacketDraftService.deleteDataPacket(packetId);
    }

    @ApiOperation(value = "查询API网关")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<DataPacketDraft> listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        List<DataPacketDraft> list = dataPacketDraftService.listDataPacket(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个API网关")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacketDraft getDataPacket(@PathVariable String packetId) {
        return dataPacketDraftService.getDataPacket(packetId);
    }

}
