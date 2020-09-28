package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.aync.service.ExchangeService;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataSetColumnDesc;
import com.centit.dde.po.DataSetDefine;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.services.DataSetDefineService;
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
import com.centit.support.database.utils.PageDesc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Api(value = "数据包", tags = "数据包")
@RestController
@RequestMapping(value = "packet")
public class DataPacketController extends BaseController {

    private  FileStore fileStore;

    @Autowired(required = false)
    public void setFileStore(FileStore fileStore) {
        this.fileStore = fileStore;
    }

    private final DataPacketService dataPacketService;
    private final ExchangeService exchangeService;

    private final DataSetDefineService dataSetDefineService;

    private final IntegrationEnvironment integrationEnvironment;


    public DataPacketController( DataPacketService dataPacketService, DataSetDefineService dataSetDefineService, IntegrationEnvironment integrationEnvironment, ExchangeService exchangeService) {
        this.dataPacketService = dataPacketService;
        this.dataSetDefineService = dataSetDefineService;
        this.integrationEnvironment = integrationEnvironment;
        this.exchangeService = exchangeService;
    }

    @ApiOperation(value = "新增数据包")
    @PostMapping
    @WrapUpResponseBody
    public void createDataPacket(@RequestBody DataPacket dataPacket, HttpServletRequest request) {
        dataPacket.setRecorder(WebOptUtils.getCurrentUserCode(request));
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
        BizModel bizModel;
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

    private BizModel getBizModel(String dataSets, BizModel bizModel) {
        if (StringUtils.isNotBlank(dataSets)) {
            String[] dss = dataSets.split(",");
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
    public SimpleDataSet fetchDbQueryData(@PathVariable String queryId, HttpServletRequest request) {

        Map<String, Object> params = collectRequestParameters(request);

        DataSetDefine query = dataSetDefineService.getDbQuery(queryId);
        DataPacket dataPacket = dataPacketService.getDataPacket(query.getPacketId());

        BizModel bizModel = dataPacketService.fetchDataPacketData(dataPacket.getPacketId(), params);

        return (SimpleDataSet) bizModel.fetchDataSetByName(queryId);
    }



    @GetMapping(value = "/exist/{applicationId}/{interfaceName}")
    @ApiOperation(value = "接口名称是否已存在")
    @WrapUpResponseBody
    public Boolean isExist(@PathVariable String applicationId, @PathVariable String interfaceName) {
        Map<String, Object> params = new HashMap<>(10);
        params.put("interfaceName", interfaceName);
        params.put("applicationId", applicationId);
        List<DataPacket> list = dataPacketService.listDataPacket(params, new PageDesc());
        return list.size() > 0;
    }


}
