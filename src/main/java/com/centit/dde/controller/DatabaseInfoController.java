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
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.security.model.CentitUserDetails;
import com.centit.support.json.JsonPropertyUtils;
import com.centit.support.security.DESSecurityUtils;

@Controller
@RequestMapping("/sys/databaseinfo")
public class DatabaseInfoController extends BaseController {
	
	@Resource
    private DatabaseInfoManager databaseInfoMag;

	@RequestMapping(method = RequestMethod.GET)
	public void list( PageDesc pageDesc, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        String[] field=null;
        List<DatabaseInfo> listObjects = null;
        if (null == pageDesc) {
            listObjects = databaseInfoMag.listObjects(searchColumn);
        } else {
            listObjects = databaseInfoMag.listObjects(searchColumn, pageDesc);
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
    public void saveDatabaseInfo(@Valid DatabaseInfo databaseinfo,HttpServletRequest request, HttpServletResponse response) {
    	
    	DatabaseInfo temp=databaseInfoMag.getObjectById(databaseInfoMag.getNextKey());
    	if (temp!=null){
    		JsonResultUtils.writeErrorMessageJson("该数据库标识已存在", response);
            return;
        }
    	CentitUserDetails userInfo = super.getLoginUser(request);
      	databaseinfo.setPassword(DESSecurityUtils.encryptAndBase64(databaseinfo.getPassword(),DatabaseInfo.DESKEY));
    	databaseinfo.setCreated(userInfo.getUserCode());
        databaseinfo.setCreateTime(new Date());
        databaseInfoMag.mergeObject(databaseinfo);


        databaseinfo.setPassword(DESSecurityUtils.decryptBase64String(databaseinfo.getPassword(),DatabaseInfo.DESKEY));
        if (databaseInfoMag.connectionTest(databaseinfo)) {
        	JsonResultUtils.writeBlankJson(response);
        } else {
        	JsonResultUtils.writeErrorMessageJson("数据库连接测试失败", response);
        }     
        
    }
    @RequestMapping(value = "/{databaseCode}", method = {RequestMethod.PUT})
    public void updateDatabaseInfo(@PathVariable String databaseCode, 
    		@Valid DatabaseInfo databaseinfo,HttpServletResponse response) {
    	DatabaseInfo temp=databaseInfoMag.getObjectById(databaseCode);
    	if (!databaseinfo.getPassword().equals(temp.getPassword())){
    		databaseinfo.setPassword(DESSecurityUtils.encryptAndBase64(databaseinfo.getPassword(),DatabaseInfo.DESKEY));
    	}
        databaseInfoMag.mergeObject(databaseinfo);


        databaseinfo.setPassword(DESSecurityUtils.decryptBase64String(databaseinfo.getPassword(),DatabaseInfo.DESKEY));
        
        //连接是否成功不影响保存
        if (databaseInfoMag.connectionTest(databaseinfo)) {
        	JsonResultUtils.writeBlankJson(response);
        } else {
        	JsonResultUtils.writeErrorMessageJson("数据库连接测试失败", response);
        }
        
    }
    @RequestMapping(value = "/{databaseCode}", method = {RequestMethod.GET})
    public void getDatabaseInhfo(@PathVariable String databaseCode, HttpServletResponse response) {
    	DatabaseInfo databaseInfo = databaseInfoMag.getObjectById(databaseCode);

        JsonResultUtils.writeSingleDataJson(databaseInfo, response,
                JsonPropertyUtils.getExcludePropPreFilter(DatabaseInfo.class, "databaseInfo"));
    }
    @RequestMapping(value = "/{databaseCode}", method = {RequestMethod.DELETE})
    public void deleteOsInfo(@PathVariable String databaseCode, HttpServletResponse response) {
    	databaseInfoMag.deleteObjectById(databaseCode);

    	JsonResultUtils.writeBlankJson(response);
    }

   
    
}
