package com.centit.dde.controller;

import com.centit.dde.routemeta.RouteMetadataService;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.security.CentitSecurityMetadata;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.support.common.ObjectException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Api(value = "api网关", tags = "api网关 - gateway")
@RestController
@RequestMapping(value = "gateway")
public class ApiRouteController extends DoApiController {

    @Autowired
    private RouteMetadataService routeMetadataService;

    private void judgePower(String packetId, HttpServletRequest request){
        List<ConfigAttribute> needRoles = CentitSecurityMetadata.getApiRoleList(packetId);
        if(needRoles==null || needRoles.isEmpty()) return ;
        CentitUserDetails ud = WebOptUtils.assertUserDetails(request);
        Collection<? extends GrantedAuthority> userRoles = ud.getAuthorities();
        if(userRoles!=null){
            Iterator<? extends GrantedAuthority> userRolesItr = userRoles.iterator();
            Iterator<ConfigAttribute> needRolesItr = needRoles.iterator();

            String needRole = needRolesItr.next().getAttribute();
            String userRole = userRolesItr.next().getAuthority();
            while(true){
                int n = needRole.compareTo(userRole);
                if(n==0)
                    return; // 匹配成功 完成认证
                if(SecurityContextUtils.ANONYMOUS_ROLE_CODE.equals(needRole)) return;
                if(n<0){
                    if(!needRolesItr.hasNext())
                        break;
                    needRole = needRolesItr.next().getAttribute();
                }else{
                    if(!userRolesItr.hasNext())
                        break;
                    userRole = userRolesItr.next().getAuthority();
                }
            }
        }

        StringBuilder errorMsgBuilder = new StringBuilder("no auth: ").append(packetId).append("; need role: ");
        boolean firstRole = true;
        for(ConfigAttribute ca : needRoles){
            if(firstRole){
                firstRole = false;
            } else {
                errorMsgBuilder.append(", ");
            }
            errorMsgBuilder.append(ca.getAttribute().substring(2));
        }
        errorMsgBuilder.append(".");
        throw new ObjectException(ResponseData.HTTP_FORBIDDEN, errorMsgBuilder.toString());
    }

    /**
     * url中的 第一个变量为topUnit 形如 /api/topUnit/模块/字模块/方法名
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException IO异常
     */
    @GetMapping(value = "/**")
    @ApiOperation(value = "执行get方法")
    public void doGetApi(HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        Pair<String, List<String>> apiInfo = routeMetadataService.mapUrlToPacketId(request.getRequestURI(),
            "GET");
        if(apiInfo == null){
            throw new ObjectException(ResponseData.HTTP_NOT_FOUND, "未找到对应的api接口, GET:"+request.getRequestURI());
        }
        judgePower(apiInfo.getLeft(), request);
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_GET,
            apiInfo.getRight(), request, response);
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
        judgePower(apiInfo.getLeft(), request);
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_POST,
            apiInfo.getRight(), request, response);
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
        judgePower(apiInfo.getLeft(), request);
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_PUT,
            apiInfo.getRight(), request, response);
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
        judgePower(apiInfo.getLeft(), request);
        returnObject(apiInfo.getLeft(), ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_DELETE,
            apiInfo.getRight(), request, response);
    }

}
