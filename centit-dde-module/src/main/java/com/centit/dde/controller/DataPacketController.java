package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataSetColumnDesc;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.po.TaskLog;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.DataSetDefineService;
import com.centit.dde.services.TaskLogManager;
import com.centit.dde.utils.DataPacketUtil;
import com.centit.dde.vo.DataPacketSchema;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.core.dao.PageQueryResult;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.SimpleBizModel;
import com.centit.product.dataopt.core.SimpleDataSet;
import com.centit.product.dataopt.dataset.CsvDataSet;
import com.centit.product.dataopt.dataset.ExcelDataSet;
import com.centit.product.dataopt.dataset.SQLDataSetReader;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Api(value = "数据包", tags = "数据包")
@RestController
@RequestMapping(value = "packet")
public class DataPacketController extends BaseController {

    @Autowired(required = false)
    private FileStore fileStore;

    @Autowired
    private DataPacketService dataPacketService;

    @Autowired
    private DataSetDefineService dataSetDefineService;

    @Autowired
    private IntegrationEnvironment integrationEnvironment;
    @Autowired
    private TaskLogManager taskLogManager;
    @Autowired
    private TaskRun taskRun;

    @ApiOperation(value = "新增数据包")
    @PostMapping
    @WrapUpResponseBody
    public void createDataPacket(DataPacket dataPacket, HttpServletRequest request) {
        String userCode = WebOptUtils.getCurrentUserCode(request);
        dataPacket.setRecorder(userCode);
        dataPacket.setDataOptDescJson(dataPacket.getDataOptDescJson());
        dataPacketService.createDataPacket(dataPacket);
    }

    @ApiOperation(value = "编辑数据包")
    @PutMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacket(@PathVariable String packetId, @RequestBody DataPacket dataPacket) {
        dataPacket.setPacketId(packetId);
        dataPacket.setDataOptDescJson(dataPacket.getDataOptDescJson());
        for (DataSetDefine setDefine : dataPacket.getDataSetDefines()) {
            for (DataSetColumnDesc columnDesc : setDefine.getColumns()) {
                columnDesc.setPacketId(dataPacket.getPacketId());
                columnDesc.setQueryId(setDefine.getQueryId());
            }
        }
        dataPacketService.updateDataPacket(dataPacket);
    }

    @ApiOperation(value = "编辑数据包数据处理描述信息")
    @PutMapping(value = "/opt/{packetId}")
    @WrapUpResponseBody
    public void updateDataPacketOpt(@PathVariable String packetId, @RequestBody String dataOptDescJson) {
        dataPacketService.updateDataPacketOptJson(packetId, dataOptDescJson);
    }

    @ApiOperation(value = "删除数据包")
    @DeleteMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public void deleteDataPacket(@PathVariable String packetId) {
        dataPacketService.deleteDataPacket(packetId);
    }

    @ApiOperation(value = "获取数据包初始模式（不包括数据预处理）")
    @GetMapping(value = "/originschema/{packetId}")
    @WrapUpResponseBody
    public DataPacketSchema getDataPacketOriginSchema(@PathVariable String packetId) {
        return DataPacketSchema.valueOf(dataPacketService.getDataPacket(packetId));
    }

    @ApiOperation(value = "获取数据包模式")
    @GetMapping(value = "/schema/{packetId}")
    @WrapUpResponseBody
    public DataPacketSchema getDataPacketSchema(@PathVariable String packetId) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        DataPacketSchema schema = DataPacketSchema.valueOf(dataPacket);
        if (dataPacket != null) {
            JSONObject obj = dataPacket.getDataOptDescJson();
            if (obj != null) {
                return DataPacketUtil.calcDataPacketSchema(schema, obj);
            }
        }
        return schema;
    }

    @ApiOperation(value = "根据额外的操作步骤获取数据包模式")
    @PostMapping(value = "/extendschema/{packetId}")
    @WrapUpResponseBody
    public DataPacketSchema getDataPacketSchemaWithOpt(@PathVariable String packetId, @RequestBody String optsteps) {
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        DataPacketSchema schema = DataPacketSchema.valueOf(dataPacket);
        if (dataPacket != null) {
            JSONObject obj = JSON.parseObject(optsteps);
            if (obj != null) {
                return DataPacketUtil.calcDataPacketSchema(schema, obj);
            }
        }
        return schema;
    }

    @ApiOperation(value = "查询数据包")
    @GetMapping
    @WrapUpResponseBody
    public PageQueryResult<DataPacket> listDataPacket(HttpServletRequest request, PageDesc pageDesc) {
        List<DataPacket> list = dataPacketService.listDataPacket(BaseController.collectRequestParameters(request), pageDesc);
        return PageQueryResult.createResult(list, pageDesc);
    }

    @ApiOperation(value = "查询单个数据包")
    @GetMapping(value = "/{packetId}")
    @WrapUpResponseBody
    public DataPacket getDataPacket(@PathVariable String packetId) {
        return dataPacketService.getDataPacket(packetId);
    }

    @ApiOperation(value = "获取数据包数据")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "packetId", value = "数据包ID",
        required = true, paramType = "path", dataType = "String"
    ), @ApiImplicitParam(
        name = "datasets", value = "需要返回的数据集名称，用逗号隔开，如果为空返回全部"
    )})
    @GetMapping(value = "/packet/{packetId}")
    @WrapUpResponseBody
    public BizModel fetchDataPacketData(@PathVariable String packetId, String datasets,
                                        HttpServletRequest request) {
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        BizModel bizModel = dataPacketService.fetchDataPacketData(packetId, params);
        BizModel dup = getBizModel(datasets, bizModel);
        if (dup != null) {
            return dup;
        }
        return bizModel;
    }

    @ApiOperation(value = "获取数据包数据并对数据进行业务处理")
    @PutMapping(value = "/dataopts/{packetId}/{datasets}")
    @WrapUpResponseBody
    public BizModel fetchDataPacketDataWithOpt(@PathVariable String packetId,
                                               @PathVariable String datasets,
                                               @RequestBody JSONObject optsteps,
                                               HttpServletRequest request) {
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        BizModel bizModel = null;
        //optsteps =StringEscapeUtils.unescapeHtml4(optsteps);
        if (optsteps.isEmpty()) {
            bizModel = dataPacketService.fetchDataPacketData(packetId, params);
        } else {
            bizModel = dataPacketService.fetchDataPacketData(packetId, params, optsteps);
        }
        BizModel dup = getBizModel(datasets, bizModel);
        if (dup != null) {
            return dup;
        }
        return bizModel;
    }

    private BizModel getBizModel(String datasets, BizModel bizModel) {
        if (StringUtils.isNotBlank(datasets)) {
            String[] dss = datasets.split(",");
            SimpleBizModel dup = new SimpleBizModel(bizModel.getModelName());
            dup.setModelTag(bizModel.getModelTag());
            Map<String, DataSet> dataMap = new HashMap<>(dss.length + 1);
            for (String dsn : dss) {
                DataSet ds = bizModel.fetchDataSetByName(dsn);
                if (ds != null) {
                    dataMap.put(dsn, ds);
                }
            }
            dup.setBizData(dataMap);
            return dup;
        }
        return null;
    }

    @ApiOperation(value = "获取数据库查询数据")
    @ApiImplicitParam(name = "queryId", value = "数据查询ID", required = true,
        paramType = "path", dataType = "String")
    @GetMapping(value = "/dbquery/{queryId}")
    @WrapUpResponseBody
    public SimpleDataSet fetchDBQueryData(@PathVariable String queryId, HttpServletRequest request) {

        Map<String, Object> params = collectRequestParameters(request);

        DataSetDefine query = dataSetDefineService.getDbQuery(queryId);
        DataPacket dataPacket = dataPacketService.getDataPacket(query.getPacketId());
        Map<String, Object> modelTag = dataPacket.getPacketParamsValue();
        switch (query.getSetType()) {
            case "D":
//                if (WebOptUtils.getCurrentUserInfo(request) == null) {
//                    throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN,
//                        "用户没有登录或者超时，请重新登录！");
//                }
                params.put("currentUser", WebOptUtils.getCurrentUserInfo(request));
                params.put("currentUnitCode", WebOptUtils.getCurrentUnitCode(request));
                SQLDataSetReader sqlDSR = new SQLDataSetReader();
                sqlDSR.setDataSource(DataSourceDescription.valueOf(
                    integrationEnvironment.getDatabaseInfo(query.getDatabaseCode())));
                sqlDSR.setSqlSen(query.getQuerySQL());
                if (params != null) {
                    modelTag.putAll(params);
                }
                SimpleDataSet simpleDataSet = sqlDSR.load(modelTag);
                simpleDataSet.setDataSetName(query.getQueryName());
                return simpleDataSet;
            case "E":
                ExcelDataSet excelDataSet = new ExcelDataSet();
                try {
                    excelDataSet.setFilePath(fileStore.getFile(query.getQuerySQL()).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return excelDataSet.load(params);
            case "C":
                CsvDataSet csvDataSet = new CsvDataSet();
                try {
                    csvDataSet.setFilePath(fileStore.getFile(query.getQuerySQL()).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return csvDataSet.load(params);
            default:
                throw new IllegalStateException("Unexpected value: " + query.getSetType());
        }
    }

    @GetMapping(value = "/run/{packetId}")
    @ApiOperation(value = "立即执行任务")
    @WrapUpResponseBody
    public Callable<BizModel> runTaskExchange(@PathVariable String packetId) {
        Callable<BizModel> bizModelCallable = () -> {
            DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
            TaskLog taskLog = new TaskLog();
            taskLog.setTaskId(packetId);
            taskLog.setRunBeginTime(new Date());
            taskLog.setRunType(dataPacket.getPacketName());
            taskLogManager.createTaskLog(taskLog);
            return taskRun.runTask(taskLog.getLogId());
        };
        return bizModelCallable;
    }

    @GetMapping(value = "/exist/{applicationId}/{interfaceName}")
    @ApiOperation(value = "接口名称是否已存在")
    @WrapUpResponseBody
    public Boolean isExist(@PathVariable String applicationId, @PathVariable String interfaceName) {
        Map<String, Object> params = new HashMap<>();
        params.put("interfaceName", interfaceName);
        params.put("applicationId", applicationId);
        List<DataPacket> list = dataPacketService.listDataPacket(params, new PageDesc());
        if (list.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @GetMapping(value = "/{applicationId}/{interfaceName}/{sourceName}")
    @ApiOperation(value = "获取数据")
    @WrapUpResponseBody
    public DataSet getData(@PathVariable String applicationId, @PathVariable String interfaceName,
                           @PathVariable String sourceName, HttpServletRequest request) {
        Map<String, Object> params = BaseController.collectRequestParameters(request);
        Map<String, Object> params2 = new HashMap<>();
        params2.put("interfaceName", interfaceName);
        params2.put("applicationId", applicationId);
        List<DataPacket> list = dataPacketService.listDataPacket(params2, new PageDesc());
        if (list.size() > 0) {
            DataPacket taskExchange = list.get(0);
            BizModel bizModel = dataPacketService.fetchDataPacketData(taskExchange.getPacketId(), params, taskExchange.getDataOptDescJson());
            String sourceCode = getSourceCode(sourceName, taskExchange.getDataOptDescJson());
            if (StringUtils.isNotBlank(sourceCode)) {
                return bizModel.fetchDataSetByName(sourceCode);
            } else {
                return bizModel.getMainDataSet();
            }
        }
        return null;
    }

    private String getSourceCode(String sourceName, JSONObject bizOptJson) {
        JSONArray jsonArray = bizOptJson.getJSONArray("steps");
        for (Object step : jsonArray) {
            if (step instanceof JSONObject) {
                if ("interface".equals(((JSONObject) step).getString("operation"))) {
                    Object mapInfo = ((JSONObject) step).get("fieldsMap");
                    if (mapInfo instanceof Map) {
                        return (String) ((Map) mapInfo).get(sourceName);
                    }
                } else {
                    return "";
                }
            }
        }
        return "";
    }
}
