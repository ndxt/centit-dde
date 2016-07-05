package com.centit.dde.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.OsInfoManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ObjectException;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.json.JsonPropertyUtils;

@Controller
@RequestMapping("/sys/osinfo")

public class OsInfoContoller extends  BaseController {

    @Resource
    private OsInfoManager osInfoMag;

    @RequestMapping(method = RequestMethod.GET)
    public void list( PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        String[] field=null;
        List<OsInfo> listObjects = null;
        if (null == pageDesc) {
            listObjects = osInfoMag.listObjects(searchColumn);
        } else {
            listObjects = osInfoMag.listObjects(searchColumn, pageDesc);
        }

        SimplePropertyPreFilter simplePropertyPreFilter = null;
        if (!ArrayUtils.isEmpty(field)) {
            simplePropertyPreFilter = new SimplePropertyPreFilter(OsInfo.class, field);
        }
        if (null == pageDesc) {
            JsonResultUtils.writeSingleDataJson(listObjects, response, simplePropertyPreFilter);
            return;
        }

        ResponseData resData = new ResponseData();
        resData.addResponseData(OBJLIST, listObjects);
        resData.addResponseData(PAGE_DESC, pageDesc);

        JsonResultUtils.writeResponseDataAsJson(resData, response, simplePropertyPreFilter);
    }
    @RequestMapping(method = {RequestMethod.POST})
    public void saveOsInfo(@Valid OsInfo osinfo,HttpServletRequest request, HttpServletResponse response) {
        if (osinfo != null) {
            
            osinfo.setLastUpdateTime(new Date());
        } else {
        	throw new ObjectException("对象不能为空");
            
        }
    	OsInfo temp=osInfoMag.getObjectById(osinfo.getOsId());
    	if (temp!=null){
    		JsonResultUtils.writeErrorMessageJson("该业务系统ID已存在", response);
            return;
        }
    	CentitUserDetails userInfo = super.getLoginUser(request);

        if (null == osinfo.getHasInterface()) {
            osinfo.setHasInterface("F");
        }
        osinfo.setCreated(userInfo.getUserCode());
        osinfo.setCreateTime(new Date());
        osInfoMag.mergeObject(osinfo);

        JsonResultUtils.writeBlankJson(response);
    }
    @RequestMapping(value = "/{osId}", method = {RequestMethod.PUT})
    public void updateOsInfo(@PathVariable String osId, 
    		@Valid OsInfo osinfo,HttpServletResponse response) {
      
        
        if (osinfo != null) {
            
            osinfo.setLastUpdateTime(new Date());
        } else {
        	throw new ObjectException("对象不能为空");
            
        }

        if (null == osinfo.getHasInterface()) {
            osinfo.setHasInterface("F");
        }

        osInfoMag.mergeObject(osinfo);

        JsonResultUtils.writeBlankJson(response);
    }
    @RequestMapping(value = "/{osId}", method = {RequestMethod.GET})
    public void getOsInhfo(@PathVariable String osId, HttpServletResponse response) {
        OsInfo osInfo = osInfoMag.getObjectById(osId);

        JsonResultUtils.writeSingleDataJson(osInfo, response,
                JsonPropertyUtils.getExcludePropPreFilter(OsInfo.class, "osInfo"));
    }
    @RequestMapping(value = "/{osId}", method = {RequestMethod.DELETE})
    public void deleteOsInfo(@PathVariable String osId, HttpServletResponse response) {
    	osInfoMag.deleteObjectById(osId);

    	JsonResultUtils.writeBlankJson(response);
    }
}
