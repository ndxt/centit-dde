package com.centit.dde.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.dde.routemeta.RouteMetadataService;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.DataPacketTemplateService;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.LoginUserPermissionCheck;
import com.centit.dde.vo.HttpParameter;
import com.centit.dde.vo.MetaDataParameter;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.operationlog.RecordOperationLog;
import com.centit.product.metadata.po.MetaColumn;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.oa.team.utils.ResourceBaseController;
import com.centit.product.oa.team.utils.ResourceLock;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.UuidOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.security.SecurityOptUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhf
 */
@Api(value = "未发布API网关接口管理", tags = "未发布API网关接口管理")
@RestController
@RequestMapping(value = "packetDraft")
public class DataPacketDraftController extends ResourceBaseController {


    private final DataPacketDraftService dataPacketDraftService;

    /**
     * 使用注解RecordOperationLog保存日志时必须提供optId属性
     * @return 当前接口的id
     */
    public String getOptId (){
        return "apiDesgin";
    }

    @Autowired
    private RouteMetadataService routeMetadataService;

    @Autowired
    private DataPacketTemplateService dataPacketTemplateService;

    @Autowired
    private DataPacketService dataPacketService;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Autowired
    private MetaDataCache metaDataCache;

    public DataPacketDraftController(DataPacketDraftService dataPacketDraftService) {
        this.dataPacketDraftService = dataPacketDraftService;
    }

    @ApiOperation(value = "新增API网关")
    @PostMapping
    @WrapUpResponseBody
    public DataPacketDraft createDataPacket(@RequestBody DataPacketDraft dataPacketDraft, HttpServletRequest request) {
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        dataPacketDraft.setRecorder(WebOptUtils.getCurrentUserCode(request));
        dataPacketDraft.setDataOptDescJson(dataPacketDraft.getDataOptDescJson());
        dataPacketDraft.setLogLevel(ConstantValue.LOGLEVEL_TYPE_ERROR);
        dataPacketDraftService.createDataPacket(dataPacketDraft);
        return dataPacketDraft;
    }

    @ApiOperation(value = "新增HTTP调用类型API")
    @PostMapping("/http-type")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public DataPacketDraft createHttpTypeApi(@RequestBody HttpParameter httpParames, HttpServletRequest request) {
        DataPacketDraft dataPacketDraft = new DataPacketDraft();
        dataPacketDraft.setOsId(httpParames.getOsId());
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(request, "userCode");
        }
        dataPacketDraft.setRecorder(loginUser);
        JSONObject dataPacketTemplate = dataPacketTemplateService.getDataPacketTemplateByType(8);
        JSONObject content = dataPacketTemplate.getJSONObject("content");
        String taskType = content.getString("taskType");
        JSONArray nodes = content.getJSONArray("nodes");
        for (Object node : nodes) {
            JSONObject nodeData = (JSONObject) node;
            JSONObject properties = nodeData.getJSONObject("properties");
            if ("htts".equals(properties.getString("type"))) {
                if (StringUtils.isNotBlank(httpParames.getRequestBody())) {
                    properties.put("querySQL", httpParames.getRequestBody());
                }
                if (httpParames.getParamesList() != null && httpParames.getParamesList().length > 0) {
                    properties.put("parameterList", httpParames.getParamesList());
                }
                properties.put("httpUrl", httpParames.getMethodName());
                properties.put("loginService", httpParames.getLoginUrlCode());
                properties.put("requestMode", httpParames.getMethodType());
                properties.put("databaseId", httpParames.getHttpUrlCode());
            }
        }
        JSONObject schemaProps = dataPacketTemplate.getJSONObject("schemaProps");
        dataPacketDraft.setSchemaProps(schemaProps);
        dataPacketDraft.setBufferFreshPeriod(-1);
        dataPacketDraft.setIsValid(true);
        dataPacketDraft.setTaskType(taskType);
        dataPacketDraft.setOptId(httpParames.getOptId());
        dataPacketDraft.setOsId(httpParames.getOsId());
        dataPacketDraft.setLogLevel(ConstantValue.LOGLEVEL_TYPE_ERROR);
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
    public List<DataPacketDraft> createMetaDataApi(@RequestBody MetaDataParameter metaDataOrHttpParams,
                                                   HttpServletRequest request) {
        DataPacketDraft dataPacket = new DataPacketDraft();
        dataPacket.setOsId(metaDataOrHttpParams.getOsId());
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacket.getOsId(), request);
        String loginUser = WebOptUtils.getCurrentUserCode(request);
        if (StringUtils.isBlank(loginUser)) {
            loginUser = WebOptUtils.getRequestFirstOneParameter(request, "userCode");
        }
        List<DataPacketDraft> dataPacketDraftList = new ArrayList<>();
        Integer[] createType = metaDataOrHttpParams.getCreateType();
        for (Integer type : createType) {
            DataPacketDraft dataPacketDraft = createDataPacket(metaDataOrHttpParams, type);
            dataPacketDraft.setRecorder(loginUser);
            dataPacketDraftService.createDataPacket(dataPacketDraft);
            dataPacketDraft.setPublishDate(DatetimeOpt.truncateToSecond(DatetimeOpt.currentUtilDate()));
            dataPacketDraftService.publishDataPacket(dataPacketDraft);
            dataPacketDraftList.add(dataPacketDraft);
        }
        return dataPacketDraftList;
    }

    private DataPacketDraft createDataPacket(MetaDataParameter metaDataOrHttpParams, Integer type) {
        DataPacketDraft dataPacketDraft = new DataPacketDraft();
        dataPacketDraft.setBufferFreshPeriod(-1);
        dataPacketDraft.setIsValid(true);
        dataPacketDraft.setTemplateType(type);
        dataPacketDraft.setOptId(metaDataOrHttpParams.getOptId());
        dataPacketDraft.setOsId(metaDataOrHttpParams.getOsId());
        JSONObject dataPacketTemplate = dataPacketTemplateService.getDataPacketTemplateByType(type);
        String tableName = metaDataOrHttpParams.getTableName();
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
            String metadataType = properties.getString("type");
            if (StringUtils.startsWith(metadataType, "metadata")) {
                properties.put("tableId", tableId);
                properties.put("templateType", type == 6 || type == 7 ? 1 : type);
                properties.put("databaseName", dataBaseCode);
            } else if (StringUtils.equals(metadataType, "generateExcel")) {
                MetaTable metaTable = metaDataCache.getTableInfo(tableId);
                properties.put("fileName", "导出" +  metaTable.getTableLabelName() + ".xlsx");
                JSONArray configArray = new JSONArray();
                for(MetaColumn column : metaTable.getColumns()){
                    JSONObject col = new JSONObject();
                    col.put("columnName", column.getFieldLabelName());
                    if(StringUtils.isBlank(column.getReferenceType()) || "0".equals(column.getReferenceType())) {
                        col.put("expression", column.getPropertyName());
                    } else {
                        col.put("expression", column.getPropertyName()+"Desc");
                    }
                    configArray.add(col);
                }
                properties.put("config", configArray);
            }
        }

        JSONObject schemaProps = dataPacketTemplate.getJSONObject("schemaProps");
        dataPacketDraft.setSchemaProps(schemaProps);
        dataPacketDraft.setMetadataTableId(tableId);
        dataPacketDraft.setLogLevel(ConstantValue.LOGLEVEL_TYPE_ERROR);
        dataPacketDraft.setNeedRollback("T");
        dataPacketDraft.setDataOptDescJson(content);
        return dataPacketDraft;
    }

    @ApiOperation(value = "编辑API网关")
    @ApiImplicitParam(
        name = "packetId", value = "apiID号",
        required = true, paramType = "path", dataType = "String"
    )
    @PutMapping(value = "/{packetId}")
    @RecordOperationLog(content = "操作IP地址:{loginIp},用户{loginUser.userName}更新api",
        tag = "{arg0}", newValue = "无")
    @WrapUpResponseBody
    public void updateDataPacket(@PathVariable String packetId,
                                 @RequestBody String packetJsonStr, HttpServletRequest request){
        DataPacketDraft dataPacketDraft = JSONObject.parseObject(
            SecurityOptUtils.decodeSecurityString(packetJsonStr),
            DataPacketDraft.class);

        if (dataPacketDraft == null) {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "修改数据不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        dataPacketDraft.setRecorder(WebOptUtils.getCurrentUserCode(request));
        dataPacketDraft.setPacketId(packetId);
        dataPacketDraft.setDataOptDescJson(dataPacketDraft.getDataOptDescJson());
        dataPacketDraftService.updateDataPacket(WebOptUtils.getCurrentTopUnit(request), dataPacketDraft);
    }

    @ApiOperation(value = "API网关发布")
    @PutMapping(value = "publish/{packetId}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void publishDataPacket(@PathVariable String packetId, HttpServletRequest request){
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (dataPacketDraft == null) {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "发布数据不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        dataPacketDraftService.publishDataPacket(dataPacketDraft);
        routeMetadataService.rebuildMetadataTree(WebOptUtils.getCurrentTopUnit(request));
    }

    @ApiOperation(value = "API网关按应用批量发布")
    @PutMapping(value = "batchPublish/{osId}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void batchPublishByOsId(@PathVariable String osId, HttpServletRequest request) {
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, osId, request);
        dataPacketDraftService.batchPublishByOsId(osId);
        routeMetadataService.rebuildMetadataTree(WebOptUtils.getCurrentTopUnit(request));
    }

    @ApiOperation(value = "编辑API网关数据处理描述信息")
    @PutMapping(value = "/opt/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String packetId,
                                    @RequestBody String dataOptDescJson, HttpServletRequest request) {
        //检查资源
        ResourceLock.lockResource(packetId, WebOptUtils.getCurrentUserCode(request), request);

        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (dataPacketDraft == null) {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "修改数据不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        dataPacketDraftService.updateDataPacketOptJson(packetId, dataOptDescJson);
    }

    @ApiOperation(value = "物理删除API网关")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataPacket(@PathVariable String packetId, HttpServletRequest request) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (dataPacketDraft == null) {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "删除数据不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this,
            platformEnvironment, dataPacketDraft.getOsId(), request);
        dataPacketService.deleteDataPacket(packetId);
        dataPacketDraftService.deleteDataPacket(packetId);
        platformEnvironment.deleteOptMethod(dataPacketDraft.getOptCode());
    }

    @ApiOperation(value = "修改API可用状态(T:禁用，F:启用)")
    @PutMapping(value = "/{packetId}/{disableType}")
    @WrapUpResponseBody
    @Transactional(rollbackFor = Exception.class)
    public void updateDisableStatus(@PathVariable String packetId, @PathVariable String disableType, HttpServletRequest request) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        if (dataPacketDraft == null) {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "修改数据不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacketDraft.getOsId(), request);
        //启用  disableType 必须等于T 或者 F
        if ("F".equals(disableType) || "T".equals(disableType)) {
            DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
            if (dataPacket != null) {
                dataPacketService.updateDisableStatus(packetId, disableType);
            }
            dataPacketDraftService.updateDisableStatus(packetId, disableType);
        } else {
            throw new ObjectException(ResponseData.HTTP_PRECONDITION_FAILED, "非法传参，参数必须为T或F,传入的参数为：" + disableType);
        }
        //删除权限数据
        if(dataPacketDraft.getOptCode()!=null) {
            platformEnvironment.deleteOptMethod(dataPacketDraft.getOptCode());
        }
    }

    @ApiOperation(value = "查询API网关")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        JSONArray returnList = dataPacketDraftService.listDataPacketForList(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createJSONArrayResult(returnList, pageDesc,DataPacketDraft.class);
    }

    @ApiOperation(value = "查询单个API网关")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacketDraft getDataPacket(@PathVariable String packetId) {
        DataPacketDraft dataPacketDraft = dataPacketDraftService.getDataPacket(packetId);
        //判断api是否处于禁用状态   禁用状态直接返回空   底层接口无法修改才加该判断来实现
        if (dataPacketDraft != null && dataPacketDraft.getIsDisable()) {
            return null;
        }
        return dataPacketDraft;
    }


    @ApiOperation(value = "批量删除和清空回收站")
    @PostMapping("/batchDeleteByPacketIds")
    @ApiImplicitParam(
        name = "jsonObject",
        value = "批量删除-参数：{packetIds:[\"packetId\"],osId:\"osId\"};清空回收站-参数：{osId:\"osId\"}"
    )
    @WrapUpResponseBody
    public void batchDeleteByPacketIds(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        JSONArray packetIds = jsonObject.getJSONArray("packetIds");
        String osId = jsonObject.getString("osId");
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, osId, request);
        if (packetIds != null && packetIds.size() > 0) {
            String[] ids = packetIds.toArray(new String[packetIds.size()]);
            dataPacketDraftService.batchDeleteByPacketIds(ids);
            dataPacketService.batchDeleteByPacketIds(ids);
        } else if (StringUtils.isNotBlank(osId)) {
            dataPacketDraftService.clearTrashStand(osId);
            dataPacketService.clearTrashStand(osId);
        }
    }

    @ApiOperation(value = "API复制接口")
    @PostMapping("/ApiCopy")
    @ApiImplicitParam(
        name = "jsonObject",
        value = "API复制接口-参数：{\"packetId\":\"\",\"packetName\":\"\",\"optId\":\"\"}"
    )
    @WrapUpResponseBody
    public ResponseData ApiCopy(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String copyPacketId = jsonObject.getString("packetId");
        String packetName = jsonObject.getString("packetName");
        String optId = jsonObject.getString("optId");
        if (StringUtils.isBlank(copyPacketId) || StringUtils.isBlank(packetName) || StringUtils.isBlank(optId)) {
            return ResponseData.makeErrorMessage("缺少参数，请检查请求参数是否正确！");
        }
        DataPacketDraft dataPacket = dataPacketDraftService.getDataPacket(copyPacketId);
        if (dataPacket == null) {
            return ResponseData.makeErrorMessage("复制的API接口不存在！");
        }
        LoginUserPermissionCheck.loginUserPermissionCheck(this, platformEnvironment, dataPacket.getOsId(), request);
        String packetId = UuidOpt.getUuidAsString32();
        dataPacket.setPacketId(packetId);
        dataPacket.setPacketName(packetName);
        dataPacket.setOptId(optId);
        dataPacket.setPublishDate(null);
        dataPacket.setUpdateDate(DatetimeOpt.truncateToSecond(DatetimeOpt.currentUtilDate()));
        dataPacket.setOptCode(null);
        JSONObject schemaJson = dataPacket.getSchemaProps();
        if (schemaJson != null) {
            String schemaString = JSONObject.toJSONString(schemaJson);
            schemaString = StringUtils.replace(schemaString, copyPacketId, packetId);
            schemaJson = JSONObject.parseObject(schemaString);
            dataPacket.setSchemaProps(schemaJson);
        }
        dataPacketDraftService.createDataPacket(dataPacket);
        return ResponseData.successResponse;
    }
}
