package com.centit.dde.controller;

import com.centit.dde.po.DataPacketTemplate;
import com.centit.dde.services.DataPacketTemplateService;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "API模板管理")
@RestController
@RequestMapping("/template/")
public class DataPacketTemplateController extends BaseController {
    @Autowired
    DataPacketTemplateService dataPacketTemplateService;

    @ApiOperation(value = "新增API模板")
    @PutMapping
    @WrapUpResponseBody
    public DataPacketTemplate createDataPacketTemplate(@RequestBody DataPacketTemplate dataPacketTemplate){
        dataPacketTemplateService.createDataPacketTemplate(dataPacketTemplate);
        return dataPacketTemplate;
    }

    @ApiOperation(value = "修改API模板")
    @PostMapping
    @WrapUpResponseBody
    public  void updateDataPacketTemplate(@RequestBody DataPacketTemplate dataPacketTemplate){
        dataPacketTemplateService.updateDataPacketTemplate(dataPacketTemplate);
    }

    @ApiOperation(value = "修改API模板")
    @PostMapping("{templateId}")
    @WrapUpResponseBody
    public void updateDataPacketTemplateContent(@PathVariable String templateId, @RequestBody String content){
        dataPacketTemplateService.updateDataPacketTemplateContent(templateId,content);
    }

    @ApiOperation(value = "删除API模板")
    @DeleteMapping("{templateId}")
    @WrapUpResponseBody
    public void deleteDataPacketTemplate(@PathVariable String templateId){
        dataPacketTemplateService.deleteDataPacketTemplate(templateId);
    }

    @ApiOperation(value = "获取API模板列表")
    @GetMapping
    @WrapUpResponseBody
    public List<DataPacketTemplate> listDataPacketTemplate(Map<String, Object> params, PageDesc pageDesc){
        return dataPacketTemplateService.listDataPacketTemplate(params,pageDesc);
    }
}

