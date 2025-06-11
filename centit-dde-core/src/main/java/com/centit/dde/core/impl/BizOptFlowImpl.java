package com.centit.dde.core.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.*;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.*;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.*;
import com.centit.dde.vo.CycleVo;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.framework.session.CentitSessionRepo;
import com.centit.product.metadata.service.*;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.product.oa.service.WorkDayManager;
import com.centit.search.service.ESServerConfig;
import com.centit.search.utils.ImagePdfTextExtractor;
import com.centit.support.algorithm.*;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.file.FileIOOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.workflow.service.FlowEngine;
import com.centit.workflow.service.FlowManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 业务流
 *
 * @author codefan@sina.com
 */
@Service
public class BizOptFlowImpl implements BizOptFlow {

    @Value("${app.home:./}")
    private String appHome;

    protected static final Logger logger = LoggerFactory.getLogger(BizOptFlowImpl.class);
    public static final String RETURN_RESULT_ALL_DATASET = "1";
    public static final String RETURN_RESULT_STATE = "2";
    public static final String RETURN_RESULT_DATASET = "3";
    //RETURN_DATA_AS_RAW
    public static final String RETURN_RESULT_ORIGIN = "4";
    public static final String RETURN_RESULT_ERROR = "5";
    public static final String RETURN_RESULT_FILE = "6";
    public static final String RETURN_RESULT_INNERDATA = "7";

    @Autowired
    private SourceInfoMetadata sourceInfoMetadata;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private DataPacketDraftDao dataPacketDraftDao;

    @Autowired
    private DataPacketDao dataPacketDao;

    @Autowired(required = false)
    private MetaObjectService metaObjectService;

    @Autowired(required = false)
    private DataScopePowerManager queryDataScopeFilter;

    @Autowired
    private MetaDataCache metaDataCache;

    @Autowired
    private DataCheckRuleService dataCheckRuleService;

    @Autowired(required = false)
    private FileInfoOpt fileInfoOpt;

    @Autowired
    @NotNull
    private OperationLogWriter operationLogWriter;

    @Autowired(required = false)
    private OptFlowNoInfoManager optFlowNoInfoManager;

    @Autowired
    private FlowEngine flowEngine;

    @Autowired
    private FlowManager flowManager;

    @Autowired
    private PlatformEnvironment platformEnvironment;

    @Autowired(required = false)
    private CentitSessionRepo centitSessionRepo;
    @Autowired
    private WorkDayManager workDayManager;
    @Autowired
    ESServerConfig esServerConfig;

    @Autowired(required = false)
    ImagePdfTextExtractor.OcrServerHost ocrServerHost;

    private Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }

    @PostConstruct
    public void init() {
        //allOperations.put(ConstantValue.SCHEDULER, (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runStart(bizModel1, bizOptJson1));//模块调度
        allOperations.put("start", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runStart());

        //allOperations.put(ConstantValue.SESSION_DATA, new SessionDataOperation());
        allOperations.put("postData", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runRequestBody(bizModel, bizOptJson));
        allOperations.put("postFile", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runRequestFile(bizModel, bizOptJson));

        allOperations.put("map", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runMap(bizModel, bizOptJson));
        allOperations.put("filter", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runFilter(bizModel, bizOptJson));
        allOperations.put("append", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runAppend(bizModel, bizOptJson));
        allOperations.put("stat", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runStat(bizModel, bizOptJson));
        allOperations.put("analyse", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runAnalyse(bizModel, bizOptJson));
        allOperations.put("cross", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runCross(bizModel, bizOptJson));
        allOperations.put("sort", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runSort(bizModel, bizOptJson));
        allOperations.put("sortAsTree", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runSortAsTree(bizModel, bizOptJson));
        allOperations.put("toTree", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runToTree(bizModel, bizOptJson));
        allOperations.put("join", new JoinDataOperation());
        allOperations.put("union", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runUnion(bizModel, bizOptJson));
        allOperations.put("check", new CheckRuleOperation(dataCheckRuleService));
        allOperations.put("static", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runStaticData(bizModel, bizOptJson));
        allOperations.put("http", new HttpServiceOperation(sourceInfoMetadata));
        allOperations.put("clear", new ClearDataOperation());
        allOperations.put("js", new InnerScriptOperation());
        allOperations.put("persistence", new PersistenceDBOperation(/*path,*/ sourceInfoMetadata, metaDataService));
        allOperations.put("database", new QuerySqlOperation(sourceInfoMetadata, queryDataScopeFilter));
        allOperations.put("excel", new ReadExcelOperation());
        allOperations.put("csv", new ReadCsvOperation());
        allOperations.put("json", new ReadObjectFileOperation());
        allOperations.put("sqlS", new RunSqlOperation(sourceInfoMetadata));
        allOperations.put("SSD", new DocReportOperation(fileInfoOpt));
        allOperations.put("optflow", new OptflowSerialNumberOperation(optFlowNoInfoManager));
        allOperations.put("unit", new UnitFilterOperation(/*platformEnvironment*/));
        allOperations.put("user", new UserFilterOperation(platformEnvironment));
        allOperations.put("session", new SessionDataOperation(this.platformEnvironment, this.centitSessionRepo));
        allOperations.put("redisRead", new RedisReadOperation(sourceInfoMetadata));
        allOperations.put("redisWrite", new RedisWriteOperation(sourceInfoMetadata));
        allOperations.put("logWrite", new OptLogOperation());
        allOperations.put("logRead", new OptLogQueryOperation(operationLogWriter));
        allOperations.put("qrCode", new QrCodeOperation(fileInfoOpt));
        allOperations.put("dictionary", new DataDictionaryOperation());
        allOperations.put("desensitize", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runDesensitize(bizModel, bizOptJson));
        allOperations.put("workday", new WorkDayOperation(workDayManager));
        allOperations.put(ConstantValue.ENCRYPT, new EncryptOperation());
        allOperations.put(ConstantValue.DECIPHER, new DecipherOperation());
        allOperations.put(ConstantValue.GENERATE_CSV, new WriteCsvOperation());
        allOperations.put(ConstantValue.GENERATE_JSON, new WriteObjectFileOperation());
        allOperations.put(ConstantValue.GENERAT_EXCEL, new WriteExcelOperation(fileInfoOpt));
        allOperations.put(ConstantValue.DEFINE_JSON_DATA, new DefineJsonDataOperation());
        allOperations.put(ConstantValue.FILE_UPLOAD, new FileUploadOperation(fileInfoOpt));
        allOperations.put(ConstantValue.FILE_DOWNLOAD, new FileDownloadOperation(fileInfoOpt));
        //为了兼容历史数据保留着  现已拆分为元数据查询  和  元数据更新 2个组件
        allOperations.put(ConstantValue.METADATA_OPERATION,
            new MetadataOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_QUERY,
            new MetadataQueryOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_UPDATE, new MetadataUpdateOperation(metaObjectService, queryDataScopeFilter));
        allOperations.put(ConstantValue.COMPARE_SOURCE, new ObjectCompareOperation());

        allOperations.put(ConstantValue.COMMIT_TRANSACTION, new TransactionCommitOperation(sourceInfoMetadata));
        allOperations.put(ConstantValue.INTERSECT_DATASET, new IntersectDataSetOperation());
        allOperations.put(ConstantValue.MINUS_DATASET, new MinusDataSetOperation());
        allOperations.put(ConstantValue.ASSIGNMENT, new AssignmentOperation());

        //注册查询操作类
        allOperations.put(ConstantValue.CREATE_WORKFLOW, new CreateWorkFlowOperation(flowEngine));
        //注册插入操作类
        allOperations.put(ConstantValue.SUBMIT_WORKFLOW, new SubmitWorkFlowOperation(flowEngine));
        allOperations.put(ConstantValue.FLOW_INSTANCE_TEAM_VAR, new ManageFlowTeamAndVarOperation(flowEngine));
        //注册删除节点
        allOperations.put(ConstantValue.DELETE_WORKFLOW, new DeleteWorkFlowOperation(flowManager));

        allOperations.put(ConstantValue.MANAGER_WORKFLOW, new ManagerWorkFlowOperation(flowManager, flowEngine));
        //注册查询待办节点
        allOperations.put(ConstantValue.USER_TASK_WORKFLOW, new WorkFlowUserTaskOperation(flowEngine));
        allOperations.put(ConstantValue.INST_NODES_WORKFLOW, new WorkFlowInstNodesOperation(flowManager));

        allOperations.put(ConstantValue.WF_TASK_MANAGER, new WorkFlowTaskManagerOperation(flowManager));

        //注册FTP下载组件
        allOperations.put(ConstantValue.FTP_FILE_DOWNLOAD, new FtpDownloadOperation(sourceInfoMetadata));
        //注册FTP上传组件
        allOperations.put(ConstantValue.FTP_FILE_UPLOAD, new FtpUploadOperation(sourceInfoMetadata));

        //注册查询操作类
        allOperations.put(ConstantValue.ELASTICSEARCH_QUERY,
            new EsQueryBizOperation(esServerConfig, sourceInfoMetadata));
        //注册插入操作类
        allOperations.put(ConstantValue.ELASTICSEARCH_WRITE,
            new EsWriteBizOperation(esServerConfig, sourceInfoMetadata, ocrServerHost));

        allOperations.put(ConstantValue.DOCUMENT_TO_PDF, new DocToPdfOperation()); //"docToPdf"
        allOperations.put(ConstantValue.ADD_WATER_MARK, new AddWaterMarkOperation(fileInfoOpt)); //"waterMark"
        allOperations.put("sqliteIn", new SqliteImportOperation(this.appHome));
        allOperations.put("sqliteOut", new SqliteExportOperation(this.appHome));
        allOperations.put("signature", new SignatureVerifyOperation());
        //拼接文件、图片
        allOperations.put("mergeFile", new MergeFileOperation());
        //压缩和解压
        allOperations.put("zip", new ZipUnzipOperation(this.appHome));

        //--------添加 文件获取函数，用于 excel中的图片加载
        DataSetOptUtil.extendFuncs.put("loadFile", (a) -> {
            try {
                InputStream is = fileInfoOpt.loadFileStream(StringBaseOpt.castObjectToString(a[0]));
                return FileIOOpt.readBytesFromInputStream(is);
            } catch (IOException e) {
                return null;
            }
        });
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    private DataOptResult runInner(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception {
        //LOGID name
        BizModel bizModel = new BizModel(dataOptContext.getLogId());
        bizModel.setCallStackData(dataOptContext.getCallStackData());
        DataOptStep dataOptStep = dataPacket.attainDataOptStep();
        dataOptContext.setNeedRollback(dataPacket.getNeedRollback());
        dataOptContext.setOptId(dataPacket.getOptId());
        dataOptContext.setLogLevel(dataPacket.getLogLevel());
        dataOptContext.setPacketId(dataPacket.getPacketId());
        dataOptContext.setOsId(dataPacket.getOsId());
        runModule(bizModel, dataOptStep, dataOptContext);
        return bizModel.getOptResult();
    }

    @Override
    public DataOptResult run(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception {
        try {
            DataOptResult result = runInner(dataPacket, dataOptContext);
            AbstractSourceConnectThreadHolder.commitAndRelease();
            return result;
        } catch (Exception exception) {
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
            throw exception;
        }
    }

    private void runModule(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        dataOptStep.setStartStep();
        while (true) {
            if (dataOptStep.isEndStep()) {
                return;
            }
            runStep(bizModel, dataOptStep, dataOptContext);
        }
    }

    private void runStep(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        String stepType = stepJson.getString("type");
        //返回节点
        if (ConstantValue.RESULTS.equals(stepType)) {
            //bizModel.getOptResult().setResultObject(returnResult(bizModel, dataOptStep));
            returnResult(bizModel, dataOptStep, dataOptContext);
            //debug 时把返回结果也记录到日志中
            if ((ConstantValue.LOGLEVEL_CHECK_DEBUG & dataOptContext.getLogLevel()) != 0) {
                CallApiLogDetail detailLog = BizOptUtils.createLogDetail(stepJson, dataOptContext);
                detailLog.setLogInfo(bizModel.getOptResult().toResponseData().toJSONString());
            }
            dataOptStep.setEndStep();
            return;
        }
        if (ConstantValue.BRANCH.equals(stepType)) {
            calcBatchStep(bizModel, dataOptStep, dataOptContext);
            return;
        }
        // 模块调用
        if (ConstantValue.SCHEDULER.equals(stepType)) {
            // 先将第一个api的请求数据暂存起来，等模块调度结束后需要恢复回去，
            // 这样做的目的是为了解决另第二个api的参数不能影响到第一个api的参数 参数的作用域不同
            Map<String, Object> stackData = bizModel.dumpStackData();
            moduleSchedule(bizModel, dataOptStep, dataOptContext);
            bizModel.restoreStackData(stackData);
        } else if (ConstantValue.CYCLE.equals(stepType)) {
            //当节点为“结束循环”时，将对应的循环节点信息set到json中
            runCycle(bizModel, dataOptStep, dataOptContext);
        } else {
            runOneStepOpt(bizModel, dataOptStep, dataOptContext);
        }

        if (dataOptStep.isEndStep()) {
            return;
        }

        //断点调试，指定节点数据返回； 除了通过 断点判断 还可以通过 step 判断（计步骤）；
        if (ConstantValue.RUN_TYPE_DEBUG.equals(dataOptContext.getRunType())) {
            boolean isBreak = dataOptContext.getBreakStepNo() != -1 ?
                dataOptStep.getStepNo() >= dataOptContext.getBreakStepNo() :
                StringUtils.equals(dataOptContext.getDebugId(), stepJson.getString("id"));

            if (isBreak) {
                String currentNodeId = stepJson.getString("id");
                dataOptStep.getCurrentStep().getJSONObject("properties").put("resultOptions", "1");
                String source = stepJson.getString("source");
                //设置返回节点  内部方法会通过这个source 来判断返回具体的某个节点 这个只能重置为当前ID 下面再重置回去
                dataOptStep.getCurrentStep().getJSONObject("properties").put("source", stepJson.getString("id"));
                //恢复原始JSON数据，否则后面更新的时候会将原本的数据替换为当前节点id
                dataOptStep.getCurrentStep().getJSONObject("properties").put("source", source);
                JSONObject bizData = new JSONObject();
                JSONObject dump = new JSONObject();
                dump.put("allNodeData", bizModel.getBizData());
                dump.put("stackData", bizModel.dumpStackData());
                //添加计步器
                bizData.put("stepNo", dataOptStep.getStepNo());
                //添加当前节点数据
                DataSet currentNodeData = bizModel.getDataSet(currentNodeId);
                if (currentNodeData == null) {
                    currentNodeData = new DataSet(currentNodeId,
                        CollectionsOpt.createHashMap("message",
                            dataOptContext.getI18nMessage("dde.705.no_data_was_generated")));
                }
                bizData.put("currentNodeData", currentNodeData);
                bizData.put("dump", dump);
                //添加断点调试的返回结果
                bizModel.getOptResult().setResultObject(bizData);
                dataOptStep.setEndStep();
                return;
            }
        }

        dataOptStep.setNextStep();
        dataOptContext.plusStepNo();
        //stepNo ++
        //如果API不能允许报错，报错就中断
        if (ConstantValue.TRUE.equals(dataOptContext.getNeedRollback()) && bizModel.getOptResult().hasErrors()) {
            // bizModel.getOptResult().getLastError().getCode() == ResponseData.ERROR_OPERATION) {
            //报错 不能返回数据 returnResult(bizModel, dataOptStep);
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            //bizModel.getOptResult().setResultObject(bizModel.getOptResult().getLastError());
            dataOptStep.setEndStep();
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
        }
    }

    // 返回节点
    private void returnResult(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) {
        JSONObject stepJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        String type = BuiltInOperation.getJsonFieldString(stepJson, "resultOptions", RETURN_RESULT_ALL_DATASET);
        String dataSetId;
        //仅仅返回状态
        if (!bizModel.getOptResult().hasErrors() && RETURN_RESULT_STATE.equals(type)) {
            bizModel.getOptResult().setResultObject(ResponseData.makeSuccessResponse());
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            return;//bizModel.getOptResult().getLastError();
        }

        if (RETURN_RESULT_DATASET.equals(type) || RETURN_RESULT_ORIGIN.equals(type)) {
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            //返回session数据
            if (ConstantValue.SESSION_DATA_TAG.equals(dataSetId)) {
                CentitUserDetails userDetails = dataOptContext.getCurrentUserDetail();
                JSONObject sessionData;
                if (userDetails == null) {
                    sessionData = new JSONObject();
                } else {
                    sessionData = userDetails.toJsonWithoutSensitive();
                }
                sessionData.put(SecurityContextUtils.SecurityContextTokenName, dataOptContext.getSessionId());
                bizModel.getOptResult().setResultObject(sessionData);
            } else {
                DataSet dataSet = bizModel.getDataSet(dataSetId);
                if (dataSet == null) {
                    bizModel.getOptResult().setStepResponse(
                        BuiltInOperation.getJsonFieldString(stepJson, "id", "endNode"),
                        ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                            dataOptContext.getI18nMessage("dde.604.data_source_not_found2", dataSetId)));
                    //添加错误日志
                    CallApiLogDetail detailLog = BizOptUtils.createLogDetail(stepJson, dataOptContext);
                    detailLog.setLogInfo(dataOptContext.getI18nMessage("dde.604.data_source_not_found2", dataSetId));
                    return;
                }

                Map<String, Object> mapFirstRow = dataSet.getFirstRow();
                if (RETURN_RESULT_DATASET.equals(type) && !mapFirstRow.isEmpty()) {
                    Object fileData = mapFirstRow.get(ConstantValue.FILE_CONTENT);
                    if (fileData instanceof OutputStream || fileData instanceof InputStream || fileData instanceof byte[]) {
                        bizModel.getOptResult().setResultFile(mapFirstRow);
                        return;
                    }
                }
                bizModel.getOptResult().setResultObject(dataSet.getData());
            }
            //这段代码是为了兼容以前的文件类型返回值，后面应该不需要了
            if (RETURN_RESULT_DATASET.equals(type)) {
                bizModel.getOptResult().setResultType(DataOptResult.RETURN_OPT_DATA);
            } else {
                bizModel.getOptResult().setResultType(DataOptResult.RETURN_DATA_AS_RAW);
            }
            return;
        }

        if (RETURN_RESULT_FILE.equals(type)) {
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "fileDataSet", "");
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            if (dataSet == null) {
                CallApiLogDetail detailLog = BizOptUtils.createLogDetail(stepJson, dataOptContext);
                detailLog.setLogInfo(dataOptContext.getI18nMessage("dde.604.data_source_not_found2", dataSetId));
                throw new ObjectException(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                    dataOptContext.getI18nMessage("dde.604.file_dataset_not_found", dataSetId));
            }
            FileDataSet fileInfo = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, stepJson, false);
            bizModel.getOptResult().setResultFile(fileInfo.getFirstRow());
            return;
        }
        //返回异常信息
        if (RETURN_RESULT_ERROR.equals(type)) {
            String code = stepJson.getString("code");
            String message = stepJson.getString("message");
            ResponseSingleData response = new ResponseSingleData();
            response.setCode(NumberBaseOpt.castObjectToInteger(code, 500));
            String reMessage = StringBaseOpt.objectToString(
                JSONTransformer.transformer(message, new BizModelJSONTransform(bizModel)));
            response.setMessage(StringUtils.isNotBlank(reMessage) ? reMessage : message);
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            if (dataSet != null) {
                response.setData(dataSet.getData());
            }

            if (response.getCode() >= 400 && (ConstantValue.LOGLEVEL_CHECK_DEBUG & dataOptContext.getLogLevel()) != 0) {
                CallApiLogDetail detailLog = BizOptUtils.createLogDetail(stepJson, dataOptContext);
                detailLog.setLogInfo(response.toJSONString());
            }

            bizModel.getOptResult().setStepResponse(
                BuiltInOperation.getJsonFieldString(stepJson, "id", "endNode"),
                response);

            bizModel.getOptResult().setResultObject(response);// .addLastStepResult("return", response);
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            return;
        }
        //返回内部数据，用于调试
        if (RETURN_RESULT_INNERDATA.equals(type)) {
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_DATA_AS_RAW);
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("bizData", bizModel.getBizData());
            returnData.put("stackData", bizModel.getStackData());
            bizModel.getOptResult().setResultObject(returnData);
            return;
        }

        Map<String, DataSet> bizData = bizModel.getBizData();
        if (bizData != null) {
            Map<String, Object> returnData = new HashMap<>();
            for (Map.Entry<String, DataSet> dataSetEntry : bizData.entrySet()) {
                DataSet dataSet = dataSetEntry.getValue();
                if (dataSet != null) {
                    returnData.put(dataSetEntry.getKey(), dataSet.getData());
                }
            }
            bizModel.getOptResult().setResultObject(returnData);
        }
        bizModel.getOptResult().setResultType(DataOptResult.RETURN_OPT_DATA);
    }

    private void calcBatchStep(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
        String stepId = stepJson.getString("id");
        List<JSONObject> linksJson = dataOptStep.getNextLinks(stepId);
        if (linksJson == null || linksJson.isEmpty()) {
            logger.error("当前分支节点(" + stepId + ")没有后续节点，请检查对应的业务逻辑图。");
        }
        boolean found = false;
        String stepTag = null;
        for (JSONObject jsonObject : linksJson) {
            String expression = jsonObject.getString("expression");
            if (StringUtils.isNotBlank(expression) && !ConstantValue.ELSE.equalsIgnoreCase(expression)) {
                if (!found || StringUtils.compare(stepTag, jsonObject.getString("step"), false) > 0) {
                    Object calculate = VariableFormula.calculate(expression, new BizModelJSONTransform(bizModel), DataSetOptUtil.extendFuncs);
                    if (BooleanBaseOpt.castObjectToBoolean(calculate, false)) {
                        stepJson = dataOptStep.getOptStep(jsonObject.getString("targetId"));
                        dataOptStep.setCurrentStep(stepJson);
                        stepTag = jsonObject.getString("step");
                        found = true;
                    }
                }
            }
        }
        if (found) {
            return;
        }
        for (JSONObject jsonObject : linksJson) {
            if (ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                stepJson = dataOptStep.getOptStep(jsonObject.getString("targetId"));
                dataOptStep.setCurrentStep(stepJson);
                return;
            }
        }
        // logger.error("当前分支节点("+stepId+")的" + linksJson.size() + "个分支中没有符合业务逻辑的分支、也没有else分支。");
        // dataOptStep.setEndStep();
        throw new ObjectException(stepJson,
            ResponseData.ERROR_POSTCONDITION_FAILED, //705
            dataOptContext.getI18nMessage("dde.705.no_follow_up_node", stepId));
    }

    private Integer getRangeSet(String expression, BizModelJSONTransform varTrains, Integer defaultValue) {
        if (StringUtils.isBlank(expression)) {
            return defaultValue;
        }
        if (StringRegularOpt.isNumber(expression)) {
            return NumberBaseOpt.castObjectToInteger(expression);
        }
        return NumberBaseOpt.castObjectToInteger(
            VariableFormula.calculate(expression, varTrains), defaultValue);
    }

    private void runCycle(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
        CycleVo cycleVo = stepJson.toJavaObject(CycleVo.class);
        //循环节点的下个节点信息
        JSONObject startNode = dataOptStep.getNextStep(cycleVo.getId());
        Object iter = null;
        List<Object> expendTree;
        //提取出需要操作的数据
        if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType())) {
            // 计算 range 的设置值
            BizModelJSONTransform varTrains = new BizModelJSONTransform(bizModel, stepJson);
            cycleVo.setIntRangeBegin(getRangeSet(cycleVo.getRangeBegin(), varTrains, 0));
            cycleVo.setIntRangeStep(getRangeSet(cycleVo.getRangeStep(), varTrains, 1));
            cycleVo.setIntRangeEnd(getRangeSet(cycleVo.getRangeEnd(), varTrains, 0));

            iter = cycleVo.getIntRangeBegin();
        } else {
            DataSet refObject = bizModel.getDataSet(cycleVo.getSource());
            Collection<?> searchData = null;
            if (refObject != null) {
                if (StringUtils.isNotBlank(cycleVo.getSubsetFieldName())) {
                    Object obj = ReflectionOpt.attainExpressionValue(refObject.getData(), cycleVo.getSubsetFieldName());
                    if (obj != null) {
                        searchData = CollectionsOpt.objectToList(obj);
                    }
                } else {
                    searchData = refObject.getDataAsList();
                }
            }
            //CYCLE_TYPE_TRAVERSE_TREE = traverseTree; breadthFirst｜depthFirst ；childrenField ：children
            if (searchData != null) {
                if (ConstantValue.CYCLE_TYPE_TRAVERSE_TREE.equals(cycleVo.getCycleType())) {
                    boolean breadthFirst = "breadthFirst".equals(stepJson.getString("traverseType"));
                    String childrenField = stepJson.getString("childrenField");
                    if (StringUtils.isBlank(childrenField)) {
                        iter = searchData.iterator();
                    } else if (!searchData.isEmpty()) {
                        if (breadthFirst) { //广度优先遍历
                            expendTree = CollectionsOpt.breadthFirstTraverseForest(searchData, childrenField);
                        } else { // 深度优先遍历
                            expendTree = CollectionsOpt.depthFirstTraverseForest(searchData, childrenField);
                        }
                        iter = expendTree.iterator();
                    }
                } else {
                    iter = searchData.iterator();
                }
            }
        }
        JSONObject cycleEndNode = null;

        while (iter != null) {
            //rang循环
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                boolean isRangeEnd = (cycleVo.getIntRangeStep() > 0 && (Integer) iter >= cycleVo.getIntRangeEnd()) ||
                    ((cycleVo.getIntRangeStep() < 0 && (Integer) iter <= cycleVo.getIntRangeEnd()));
                if (isRangeEnd) {
                    break;
                }
                bizModel.putDataSet(cycleVo.getId(), new DataSet(iter));
            } else {
                //foreach循环 和 search tree
                if (!((Iterator<Object>) iter).hasNext()) {
                    break;
                }
                Object value = ((Iterator<Object>) iter).next();
                bizModel.putDataSet(cycleVo.getId(), new DataSet(value));
            }
            //新的一轮循环，设置起始节点
            dataOptStep.setCurrentStep(startNode);
            boolean endCycle = false;
            while (true) {
                //获取节点信息
                if (dataOptStep.isEndStep()) {
                    break;
                }
                JSONObject step = dataOptStep.getCurrentStep();
                String stepType = step.getString("type");
                if (ConstantValue.CYCLE_FINISH.equals(stepType)) {
                    // 这儿记录循环结束节点
                    cycleEndNode = step;
                    break; // 一个循环结束（内循环）， 继续下一个循环
                }
                if (ConstantValue.CYCLE_JUMP_OUT.equals(stepType)) {
                    // break or continue
                    JSONObject jumpStepJson = step.getJSONObject("properties");
                    String breakType = jumpStepJson.getString("endType");
                    endCycle = ConstantValue.CYCLE_JUMP_BREAK.equals(breakType);
                    break; // break 内循环 继续循环
                }
                //执行节点操作，并设置该节点的下个节点信息
                runStep(bizModel, dataOptStep, dataOptContext);
            }
            if (endCycle || dataOptStep.isEndStep()) { // break; dataOptStep.isEndStep() debug模式会有这样的情况
                break;
            }
            //获取下一个迭代数据，继续循环
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                iter = (Integer) iter + cycleVo.getIntRangeStep();
            }
        }
        if (!dataOptStep.isEndStep()) {
            if (cycleEndNode != null) {
                dataOptStep.setCurrentStep(cycleEndNode);
            } else {
                dataOptStep.seekToCycleEnd(cycleVo.getId());
            }
        }
    }

    private void runOneStepOpt(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        Date runBeginTime = new Date();// 获取当期时间

        JSONObject bizOptJson = dataOptStep.getCurrentStep().getJSONObject("properties");

        String optType = bizOptJson.getString("type");
        BizOperation opt = allOperations.get(optType);
        if (opt == null) {
            throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT,
                dataOptContext.getI18nMessage("dde.613.component_not_found", optType));
        }
        ResponseData responseData;
        try {
            responseData = opt.runOpt(bizModel, bizOptJson, dataOptContext);
        } catch (Exception runOptException) {
            //记录异常日志信息
            CallApiLogDetail detailLog = BizOptUtils.createLogDetail(bizOptJson, dataOptContext);
            String errMsg = ObjectException.extortExceptionMessage(runOptException);
            detailLog.setLogInfo(errMsg);
            detailLog.setRunBeginTime(runBeginTime);

            boolean exceptionAsError = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("exceptionAsError"), true);
            if (exceptionAsError) {
                if (runOptException instanceof ObjectException) {
                    ObjectException objectException = (ObjectException) runOptException;
                    responseData = ResponseData.makeErrorMessageWithData(objectException.getObjectData(),
                        objectException.getExceptionCode(), objectException.getMessage());
                } else {
                    responseData = ResponseData.makeErrorMessageWithData(
                        CollectionsOpt.createHashMap("exception", runOptException.getClass().getName(),
                            "trace", ObjectException.extortExceptionMessage(runOptException)),
                        ResponseData.ERROR_OPERATION,
                        runOptException.getMessage());
                }
            } else {
                //添加 异常处理流程
                throw runOptException;
            }
        }
        int logLevel = dataOptContext.getLogLevel();
        // 记录日志
        if ((ConstantValue.LOGLEVEL_CHECK_DEBUG & logLevel) != 0) {
            CallApiLogDetail detailLog = BizOptUtils.createLogDetail(bizOptJson, dataOptContext);
            Map<String, Object> jsonObject = CollectionsOpt.objectToMap(responseData.getData());
            if (jsonObject != null) {
                detailLog.setSuccessPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("success"), 0));
                detailLog.setErrorPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("error"), 0));
            }

            if ("start".equals(optType)) {
                detailLog.setLogInfo(JSON.toJSONString(dataOptContext.getContextData()));
            } else {
                DataSet dataSet;
                if ("append".equals(optType) || "desensitize".equals(optType)) { // 派生 和 脱敏
                    dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
                } else if (ConstantValue.ASSIGNMENT.equals(optType)) { // 赋值
                    dataSet = bizModel.getDataSet(bizOptJson.getString("target"));
                } else {
                    dataSet = bizModel.getDataSet(bizOptJson.getString("id"));
                }
                if (dataSet != null) {
                    detailLog.setLogInfo(dataSet.toDebugString());
                } else {
                    detailLog.setLogInfo(responseData.toJSONString());
                }
            }
            detailLog.setRunBeginTime(runBeginTime);
        }
        //正确返回也要设置运行结果
        bizModel.getOptResult().setStepResponse(bizOptJson.getString("id"), responseData);
    }

    /**
     * 模块调度
     */
    private void moduleSchedule(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {

        JSONObject stepJson = dataOptStep.getCurrentStep().getJSONObject("properties");

        Object callParams = DataSetOptUtil.getCallModuleParams(bizModel, stepJson, ConstantValue.REQUEST_PARAMS_TAG);

        String packetId = stepJson.getString("packetName");
        Integer logLevel = dataOptContext.getLogLevel();
        DataPacketInterface dataPacketInterface;
        if (ConstantValue.RUN_TYPE_DEBUG.equals(dataOptContext.getRunType())) {
            DataPacketDraft dataPacketDraft = dataPacketDraftDao.getObjectWithReferences(packetId);
            //设置子api流程的日志级别和父api流程一样，方便查看日志信息
            dataPacketDraft.setLogLevel(logLevel);
            dataPacketInterface = dataPacketDraft;
        } else {
            DataPacket dataPacket = dataPacketDao.getObjectWithReferences(packetId);
            dataPacket.setLogLevel(logLevel);
            dataPacketInterface = dataPacket;
        }
        String taskType = dataPacketInterface.getTaskType();
        if (ConstantValue.TASK_TYPE_TIME.equals(taskType) || ConstantValue.TASK_TYPE_MSG.equals(taskType)) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED,
                dataOptContext.getI18nMessage("dde.405.cant_request_schedule_task"));
        }
        if (dataPacketInterface.getIsDisable() != null && dataPacketInterface.getIsDisable()) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED,
                dataOptContext.getI18nMessage("dde.405.api_is_disable", packetId));
        }
        DataOptContext moduleOptContext = new DataOptContext(dataOptContext.getMessageSource(), dataOptContext.getLocale());
        moduleOptContext.setCallApiLog(dataOptContext.getCallApiLog());
        moduleOptContext.setOptId(dataOptContext.getOptId());
        moduleOptContext.setNeedRollback(dataOptContext.getNeedRollback());
        moduleOptContext.setRunType(dataOptContext.getRunType());
        //添加原始调用参数
        moduleOptContext.setStackData(ConstantValue.MODULE_CALL_TAG, callParams);
        //添加调用环境的上下文
        moduleOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
        moduleOptContext.setStackData(ConstantValue.REQUEST_HEADERS_TAG, bizModel.getStackData(ConstantValue.REQUEST_HEADERS_TAG));
        moduleOptContext.setStackData(ConstantValue.REQUEST_COOKIES_TAG, bizModel.getStackData(ConstantValue.REQUEST_COOKIES_TAG));
        moduleOptContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, bizModel.getStackData(ConstantValue.APPLICATION_INFO_TAG));
        moduleOptContext.setStackData(ConstantValue.SESSION_DATA_TAG, bizModel.fetchCurrentUserDetail());
        moduleOptContext.setTopUnit(dataOptContext.getTopUnit());

        DataOptResult result = runInner(dataPacketInterface, moduleOptContext);
        Object resultData = result.getResultData();
        bizModel.putDataSet(stepJson.getString("id"), DataSet.toDataSet(resultData));

        if (resultData instanceof ResponseData) {
            ResponseData responseData = (ResponseData) resultData;
            if (responseData.getCode() != ResponseData.RESULT_OK && responseData.getCode() != ResponseData.HTTP_OK) {
                bizModel.getOptResult().setStepResponse(
                    stepJson.getString("id"), responseData);
                return;
            }
        }

        bizModel.getOptResult().setStepResponse(
            stepJson.getString("id"), BuiltInOperation.createResponseSuccessData(1));
    }
}
