package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.DataPacket;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.DataPacketUtil;
import com.centit.dde.vo.DataPacketSchema;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
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


}
