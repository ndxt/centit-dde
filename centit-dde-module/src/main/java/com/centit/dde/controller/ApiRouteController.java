package com.centit.dde.controller;

import com.centit.dde.routemeta.RouteMetadataService;
import com.centit.framework.common.JsonResultUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "api路由", tags = "api路由")
@RestController
@RequestMapping(value = "api")
public class ApiRouteController extends DoApiController {

    @Autowired
    private RouteMetadataService routeMetadataService;

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    @ApiOperation(value = "执行get方法")
    public void doGetApi(HttpServletRequest request, HttpServletResponse response) {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "GET");

        JsonResultUtils.writeSingleDataJson(apiInfo, response);
    }


}
