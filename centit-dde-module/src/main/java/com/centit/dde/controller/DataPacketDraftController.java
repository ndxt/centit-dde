package com.centit.dde.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.DataPacketTemplateService;
import com.centit.dde.utils.HttpParames;
import com.centit.dde.utils.LoginUserPermissionCheck;
import com.centit.dde.utils.MetaDataParames;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.filter.RequestThreadLocal;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.product.adapter.api.WorkGroupManager;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private WorkGroupManager workGroupManager;

    @Autowired
    private DataPacketTemplateService dataPacketTemplateService;

    @Autowired
    private DataPacketService dataPacketService;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    public DataPacketDraftController(DataPacketDraftService dataPacketDraftService) {
        this.dataPacketDraftService = dataPacketDraftService;
    }

    @ApiOperation(value = "新增API网关")
    @PostMapping
    @WrapUpResponseBody
    public DataPacketDraft createDataPacket(@RequestBody DataPacketDraft dataPacketDraft, HttpServletRequest request) {
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        dataPacketDraft.setRecorder(WebOptUtils.getCurrentUserCode(request));
        dataPacketDraft.setDataOptDescJson(dataPacketDraft.getDataOptDescJson());
        dataPacketDraftService.createDataPacket(dataPacketDraft);
        return dataPacketDraft;
    }

    @ApiOperation(value = "新增HTTP调用类型API")
    @PostMapping("/http-type")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public DataPacketDraft createHttpTypeApi(@RequestBody HttpParames httpParames) {
        DataPacketDraft dataPacketDraft = new DataPacketDraft();
        dataPacketDraft.setOsId(httpParames.getOsId());
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        dataPacketDraft.setRecorder(loginUser);
        JSONObject dataPacketTemplate = dataPacketTemplateService.getDataPacketTemplateByType(8);
        JSONObject content = dataPacketTemplate.getJSONObject("content");
        String taskType = content.getString("taskType");
        JSONArray nodes = content.getJSONArray("nodes");
        for (Object node : nodes) {
            JSONObject nodeData = (JSONObject) node;
            JSONObject properties = nodeData.getJSONObject("properties");
            if ("htts".equals(properties.getString("type"))){
                if (StringUtils.isNotBlank(httpParames.getRequestBody())){
                    properties.put("querySQL",httpParames.getRequestBody());
                }
                if (httpParames.getParamesList()!=null&&httpParames.getParamesList().length>0){
                    properties.put("parameterList",httpParames.getParamesList());
                }
                properties.put("httpUrl",httpParames.getMethodName());
                properties.put("loginService",httpParames.getLoginUrlCode());
                properties.put("requestMode",httpParames.getMethodType());
                properties.put("databaseId",httpParames.getHttpUrlCode());
            }
        }
        dataPacketDraft.setBufferFreshPeriod(-1);
        dataPacketDraft.setIsValid(true);
        dataPacketDraft.setTaskType(taskType);
        dataPacketDraft.setOptId(httpParames.getOptId());
        dataPacketDraft.setOsId(httpParames.getOsId());
        dataPacketDraft.setPacketName(httpParames.getPacketName());
        dataPacketDraft.setPacketDesc(httpParames.getPacketName());
        dataPacketDraft.setDataOptDescJson(content);
        dataPacketDraftService.createDataPacket(dataPacketDraft);
        return dataPacketDraft;
    }


    @ApiOperation(value = "新增元数据类型API")
    @PostMapping("/metadata/api")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public List<DataPacketDraft> createMetaDataApi(@RequestBody MetaDataParames metaDataOrHttpParams) {
        DataPacketDraft dataPacket = new DataPacketDraft();
        dataPacket.setOsId(metaDataOrHttpParams.getOsId());
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacket);
        String loginUser = WebOptUtils.getCurrentUserCode(RequestThreadLocal.getLocalThreadWrapperRequest());
        if (StringBaseOpt.isNvl(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(RequestThreadLocal.getLocalThreadWrapperRequest(), "userCode");
        }
        List<DataPacketDraft> dataPacketDraftList = new ArrayList<>();
        Integer[] createType = metaDataOrHttpParams.getCreateType();
        for (Integer type : createType) {
            DataPacketDraft dataPacketDraft = createDataPacket(metaDataOrHttpParams, type);
            dataPacketDraft.setRecorder(loginUser);
            dataPacketDraftService.createDataPacket(dataPacketDraft);
            dataPacketDraftList.add(dataPacketDraft);
        }
        return dataPacketDraftList;
    }

    private DataPacketDraft createDataPacket(MetaDataParames metaDataOrHttpParams, Integer type){
        DataPacketDraft dataPacketDraft = new DataPacketDraft();
        dataPacketDraft.setBufferFreshPeriod(-1);
        dataPacketDraft.setIsValid(true);
        dataPacketDraft.setOptId(metaDataOrHttpParams.getOptId());
        dataPacketDraft.setOsId(metaDataOrHttpParams.getOsId());
        JSONObject dataPacketTemplate = dataPacketTemplateService.getDataPacketTemplateByType(type);
        String tableName=metaDataOrHttpParams.getTableName();
        String packetTemplateName = dataPacketTemplate.getString("packetTemplateName");
        String replace = packetTemplateName.replace("{name}", tableName);
        dataPacketDraft.setTaskType(dataPacketTemplate.getString("taskType"));
        dataPacketDraft.setPacketName(replace);
        dataPacketDraft.setPacketDesc(replace);
        String dataBaseCode = metaDataOrHttpParams.getDatabaseCode();
        String tableId = metaDataOrHttpParams.getTableId();
        JSONObject content = dataPacketTemplate.getJSONObject("content");
        JSONArray nodes = content.getJSONArray("nodes");
        for (Object node : nodes) {
            JSONObject nodeData = (JSONObject) node;
            JSONObject properties = nodeData.getJSONObject("properties");
            if ("metadata".equals(properties.getString("type"))){
                properties.put("tableLabelName",tableId);
                properties.put("templateType",type==6||type==7?1:type);
                properties.put("databaseName",dataBaseCode);
            }
        }
        dataPacketDraft.setDataOptDescJson(content);
        return  dataPacketDraft;
    }


    @ApiOperation(value = "编辑API网关")
    @PutMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacket(@PathVariable String packetId, @RequestBody DataPacketDraft dataPacketDraft) {
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        dataPacketDraft.setPacketId(packetId);
        dataPacketDraft.setDataOptDescJson(dataPacketDraft.getDataOptDescJson());
        dataPacketDraftService.updateDataPacket(dataPacketDraft);
    }

    @ApiOperation(value = "API网关发布")
    @PutMapping(value = "publish/{packetId}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void publishDataPacket(@PathVariable String packetId) throws ParseException {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(new Date());
        dataPacketDraft.setPublishDate(simpleDateFormat.parse(dateStr));
        dataPacketDraftService.updateDataPacket(dataPacketDraft);
        dataPacketDraftService.publishDataPacket(dataPacketDraft);
    }

    @ApiOperation(value = "编辑API网关数据处理描述信息")
    @PutMapping(value = "/opt/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String packetId, @RequestBody String dataOptDescJson) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        dataPacketDraftService.updateDataPacketOptJson(packetId, dataOptDescJson);
    }

    @ApiOperation(value = "删除API网关")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataPacket(@PathVariable String packetId) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        LoginUserPermissionCheck.loginUserPermissionCheck(workGroupManager,dataPacketDraft);
        platformEnvironment.deleteOptDefAndRolepowerByOptCode(dataPacketDraft.getOptCode());
        dataPacketService.deleteDataPacket(packetId);
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
