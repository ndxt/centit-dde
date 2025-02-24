package com.centit.dde.controller;

import com.centit.dde.routemeta.RouteMetadataService;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.support.common.ObjectException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(value = "api路由", tags = "api路由")
@RestController
@RequestMapping(value = "api")
public class ApiRouteController extends DoApiController {

    @Autowired
    private RouteMetadataService routeMetadataService;

    @GetMapping(value = "/**")
    @ApiOperation(value = "执行get方法")
    public void doGetApi(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "GET");
        if(apiInfo == null){
            throw new ObjectException(ResponseData.HTTP_NOT_FOUND, "未找到对应的api接口, GET:"+request.getRequestURI());
        }
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_GET, request, response);
    }

    @PostMapping(value = "/**")
    @ApiOperation(value = "发布：立即执行任务POST")
    public void runPostTaskExchange(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "POST");
        if(apiInfo == null){
            throw new ObjectException(ResponseData.HTTP_NOT_FOUND, "未找到对应的api接口, POST:"+request.getRequestURI());
        }
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_POST, request, response);
    }

    @PutMapping(value = "/**")
    @ApiOperation(value = "发布：立即执行任务PUT")
    public void runPutTaskExchange(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "PUT");
        if(apiInfo == null){
            throw new ObjectException(ResponseData.HTTP_NOT_FOUND, "未找到对应的api接口, PUT:"+request.getRequestURI());
        }
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_PUT, request, response);
    }

    @DeleteMapping(value = "/**")
    @ApiOperation(value = "发布：立即执行任务DELETE")
    public void runDelTaskExchange(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "DELETE");
        if(apiInfo == null){
            throw new ObjectException(ResponseData.HTTP_NOT_FOUND, "未找到对应的api接口, DELETE:"+request.getRequestURI());
        }
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_DELETE, request, response);
    }

}
