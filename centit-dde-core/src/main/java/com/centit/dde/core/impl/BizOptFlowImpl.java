package com.centit.dde.core.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.*;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.po.DataPacketInterface;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.vo.CycleVo;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.framework.model.adapter.OperationLogWriter;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.product.oa.service.OptFlowNoInfoManager;
import com.centit.support.algorithm.*;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
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

    public static final String RETURN_RESULT_ALL_DATASET = "1";
    public static final String RETURN_RESULT_STATE = "2";
    public static final String RETURN_RESULT_DATASET = "3";
    //RETURN_DATA_AS_RAW
    public static final String RETURN_RESULT_ORIGIN = "4";
    public static final String RETURN_RESULT_ERROR = "5";
    public static final String RETURN_RESULT_FILE = "6";
    public static final String RETURN_RESULT_INNERDATA = "7";

    @Value("${os.file.base.dir:./file_home/export}")
    private String path;

    @Autowired
    private SourceInfoDao sourceInfoDao;

    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private DataPacketDraftDao dataPacketCopyDao;

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

    @Autowired
    private OptFlowNoInfoManager optFlowNoInfoManager;

    private Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }

    @PostConstruct
    public void init() {
        //allOperations.put(ConstantValue.SCHEDULER, (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runStart(bizModel1, bizOptJson1));//模块调度
        allOperations.put("start", (bizModel, bizOptJson, dataOptContext)-> BuiltInOperation.runStart());

        allOperations.put(ConstantValue.SESSION_DATA, new SessionDataOperation());
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
        allOperations.put("join", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runJoin(bizModel, bizOptJson));
        allOperations.put("union", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runUnion(bizModel, bizOptJson));
        allOperations.put("check", new CheckRuleOperation(dataCheckRuleService));
        allOperations.put("static", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runStaticData(bizModel, bizOptJson));
        allOperations.put("http", new HttpServiceOperation(sourceInfoDao));
        allOperations.put("clear", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runClear(bizModel, bizOptJson));
        allOperations.put("js", new InnerJSOperation());
        allOperations.put("persistence", new PersistenceDBOperation(/*path,*/ sourceInfoDao, metaDataService));
        allOperations.put("database", new QuerySqlOperation(sourceInfoDao, queryDataScopeFilter));
        allOperations.put("excel", new ReadExcelOperation());
        allOperations.put("csv", new ReadCsvOperation());
        allOperations.put("json", new ReadJsonFileOperation());
        allOperations.put("sqlS", new RunSqlOperation(sourceInfoDao));
        allOperations.put("SSD", new DocReportOperation(fileInfoOpt));
        allOperations.put("optflow", new OptflowSerialNumberOperation(optFlowNoInfoManager));
        allOperations.put("unit", new UnitFilterOperation());
        allOperations.put("user", new UserFilterOperation());

        allOperations.put("redisRead", new RedisReadOperation(sourceInfoDao));
        allOperations.put("redisWrite", new RedisWriteOperation(sourceInfoDao));
        allOperations.put("logWrite", new OptLogOperation());
        allOperations.put("logRead", new OptLogQueryOperation(operationLogWriter));

        allOperations.put(ConstantValue.ENCRYPT, new EncryptOperation());
        allOperations.put(ConstantValue.DECIPHER, new DecipherOperation());
        allOperations.put(ConstantValue.GENERATE_CSV, new WriteCsvOperation());
        allOperations.put(ConstantValue.GENERATE_JSON, new WriteJsonFileOperation());
        allOperations.put(ConstantValue.GENERAT_EXCEL, new WriteExcelOperation(fileInfoOpt));
        allOperations.put(ConstantValue.DEFINE_JSON_DATA, new DefineJsonDataOperation());
        allOperations.put(ConstantValue.FILE_UPLOAD, new FileUploadOperation(fileInfoOpt));
        allOperations.put(ConstantValue.FILE_DOWNLOAD, new FileDownloadOperation(fileInfoOpt));
        //为了兼容历史数据保留着  现已拆分为元数据查询  和  元数据更新 2个组件
        allOperations.put(ConstantValue.METADATA_OPERATION,
            new MetadataOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_QUERY,
            new MetadataQueryOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_UPDATE, new MetadataUpdateOperation(metaObjectService));
        allOperations.put(ConstantValue.COMPARE_SOURCE, new ObjectCompareOperation());

        allOperations.put(ConstantValue.COMMIT_TRANSACTION, new TransactionCommitOperation(sourceInfoDao));
        allOperations.put(ConstantValue.INTERSECT_DATASET, new IntersectDataSetOperation());
        allOperations.put(ConstantValue.MINUS_DATASET, new MinusDataSetOperation());
        allOperations.put(ConstantValue.ASSIGNMENT, new AssignmentOperation());
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    private DataOptResult runInner(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception {
        //LOGID name
        BizModel bizModel = new BizModel(dataOptContext.getLogId());
        bizModel.setCallStackData(dataOptContext.getCallStackData());
        DataOptStep dataOptStep = dataPacket.getDataOptStep();
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
        if (ConstantValue.RESULTS.equals(stepType)) {
            //bizModel.getOptResult().setResultObject(returnResult(bizModel, dataOptStep));
            returnResult(bizModel, dataOptStep, dataOptContext);
            //debug 时把返回结果也记录到日志中
            if ((ConstantValue.LOGLEVEL_CHECK_DEBUG & dataOptContext.getLogLevel()) != 0) {
                TaskDetailLog detailLog = createLogDetail(dataOptStep, dataOptContext);
                detailLog.setLogInfo(bizModel.getOptResult().toResponseData().toJSONString());
            }
            dataOptStep.setEndStep();
            return;
        }
        if (ConstantValue.BRANCH.equals(stepType)) {
            setBatchStep(bizModel, dataOptStep);
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

        //断点调试，指定节点数据返回
        String debugId = dataOptContext.getDebugId();
        if (StringUtils.equals(debugId, stepJson.getString("id"))) {
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
            bizData.put("currentNodeData", bizModel.getDataSet(debugId));
            bizData.put("dump", dump);
            //添加断点调试的返回结果
            bizModel.getOptResult().setResultObject(bizData);
            dataOptStep.setEndStep();
            return;
        }

        dataOptStep.setNextStep();
        dataOptContext.plusStepNo();
        //stepNo ++

        if (ConstantValue.TRUE.equals(dataOptContext.getNeedRollback())) {
            //如果API不能允许报错，报错就中断
            if (bizModel.getOptResult().hasErrors()){// bizModel.getOptResult().getLastError().getCode() == ResponseData.ERROR_OPERATION) {
                //报错 不能返回数据 returnResult(bizModel, dataOptStep);
                bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
                //bizModel.getOptResult().setResultObject(bizModel.getOptResult().getLastError());
                dataOptStep.setEndStep();
                AbstractSourceConnectThreadHolder.rollbackAndRelease();
            }
        }
    }

    private void returnResult(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
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
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            if (dataSet == null) {
                bizModel.getOptResult().setStepResponse(
                    BuiltInOperation.getJsonFieldString(stepJson, "id", "endNode"), ResponseData.makeErrorMessage(
                    ResponseData.ERROR_OPERATION, "获取数据集："+ dataSetId +"出错！" ));
                //添加错误日志
                TaskDetailLog detailLog = createLogDetail(dataOptStep, dataOptContext);
                detailLog.setLogInfo("获取数据集："+ dataSetId +"出错！");
            } else {
                bizModel.getOptResult().setResultObject(dataSet.getData());
                //这段代码是为了兼容以前的文件类型返回值，后面应该不需要了
                if (RETURN_RESULT_DATASET.equals(type)) {
                    Map<String, Object> mapFirstRow = dataSet.getFirstRow();
                    if (!mapFirstRow.isEmpty()) {
                        Object fileData = mapFirstRow.get(ConstantValue.FILE_CONTENT);
                        if (fileData instanceof OutputStream || fileData instanceof InputStream) {
                            bizModel.getOptResult().setResultFile(mapFirstRow);
                            return;
                        }
                    }
                    bizModel.getOptResult().setResultType(DataOptResult.RETURN_OPT_DATA);
                } else {
                    bizModel.getOptResult().setResultType(DataOptResult.RETURN_DATA_AS_RAW);
                }
            }
            return;
        }

        if (RETURN_RESULT_FILE.equals(type)) {
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(dataSet, stepJson);
            bizModel.getOptResult().setResultFile(fileInfo.getFirstRow());
            return;
        }
        //返回异常信息
        if (RETURN_RESULT_ERROR.equals(type)) {
            String code = stepJson.getString("code");
            String message = stepJson.getString("message");
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet = bizModel.getDataSet(dataSetId);
            ResponseSingleData response = new ResponseSingleData();
            response.setCode(NumberBaseOpt.castObjectToInteger(code, 1));
            String reMessage = StringBaseOpt.objectToString(
                    JSONTransformer.transformer(message, new BizModelJSONTransform(bizModel)));
            response.setMessage(StringUtils.isNotBlank(reMessage) ? reMessage : message);
            if (dataSet != null) {
                response.setData(dataSet.getData());
            }
            bizModel.getOptResult().setResultObject(response);// .addLastStepResult("return", response);
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            return;
        }
        //返回内部数据，用于调试
        if (RETURN_RESULT_INNERDATA.equals(type)) {
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_DATA_AS_RAW);
            Map<String, Object> returnData = new HashMap<>();
            returnData.put("bizData",bizModel.getBizData());
            returnData.put("stackData",bizModel.getStackData());
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

    private void setBatchStep(BizModel bizModel, DataOptStep dataOptStep) {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
        String stepId = stepJson.getString("id");
        List<JSONObject> linksJson = dataOptStep.getNextLinks(stepId);
        for (JSONObject jsonObject : linksJson) {
            if (!ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                String expression = jsonObject.getString("expression");
                Object calculate = VariableFormula.calculate(expression, new BizModelJSONTransform(bizModel), DataSetOptUtil.extendFuncs);
                if (BooleanBaseOpt.castObjectToBoolean(calculate, false)) {
                    stepJson = dataOptStep.getOptStep(jsonObject.getString("targetId"));
                    dataOptStep.setCurrentStep(stepJson);
                    return;
                }
            }
        }
        for (JSONObject jsonObject : linksJson) {
            if (ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                stepJson = dataOptStep.getOptStep(jsonObject.getString("targetId"));
                dataOptStep.setCurrentStep(stepJson);
                return;
            }
        }
        dataOptStep.setEndStep();
    }

    private void runCycle(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
        CycleVo cycleVo = stepJson.toJavaObject(CycleVo.class);
        //循环节点的下个节点信息
        JSONObject startNode = dataOptStep.getNextStep(cycleVo.getId());
        Object iter = null;
        //提取出需要操作的数据
        if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType())) {
            iter = cycleVo.getRangeBegin();
        } else {
            DataSet refObject = bizModel.getDataSet(cycleVo.getSource());
            if (refObject != null) {
                if (StringUtils.isNotBlank(cycleVo.getSubsetFieldName())) {
                    Object obj = ReflectionOpt.attainExpressionValue(refObject.getData(), cycleVo.getSubsetFieldName());
                    if (obj != null) {
                        iter = CollectionsOpt.objectToList(obj).iterator();
                    }
                } else {
                    iter = refObject.getDataAsList().iterator();
                }
            }
        }
        while (iter != null) {
            //rang循环
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                boolean isRangeEnd = (cycleVo.getRangeStep() > 0 && (Integer) iter >= cycleVo.getRangeEnd()) ||
                    ((cycleVo.getRangeStep() < 0 && (Integer) iter <= cycleVo.getRangeEnd()));
                if (isRangeEnd) {
                    break;
                }
                bizModel.putDataSet(cycleVo.getId(), new DataSet(iter));
            } else {
                //foreach循环
                if (!((Iterator<Object>) iter).hasNext()) {
                    break;
                }
                Object value = ((Iterator<Object>) iter).next();
                //TODO 这个地方有问题
                //if (ConstantValue.DATA_COPY.equals(cycleVo.getAssignType())) {
                    //clone 复制对象数据
                    //bizModel.putDataSet(cycleVo.getId(), new DataSet(value));
                //} else {
                    //引用对象数据
                    bizModel.putDataSet(cycleVo.getId(), new DataSet(value));
                //}
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
                    break; // break 内循环 继续循环
                }
                if (ConstantValue.CYCLE_JUMP_OUT.equals(stepType)) {
                    String breakType = step.getString("endType");
                    endCycle = ConstantValue.CYCLE_JUMP_BREAK.equals(breakType);
                    break; // break 内循环 继续循环
                }
                //执行节点操作，并设置该节点的下个节点信息
                runStep(bizModel, dataOptStep, dataOptContext);
            }
            if (endCycle) {
                break;
            }
            //获取下一个迭代数据，继续循环
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                iter = (Integer) iter + cycleVo.getRangeStep();
            }
        }
        dataOptStep.seekToCycleEnd(cycleVo.getId());
    }

    private void runOneStepOpt(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) {
        int logLevel = dataOptContext.getLogLevel();
        Date runBeginTime = new Date();// 获取当期时间

        JSONObject bizOptJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        try {
            BizOperation opt = allOperations.get(bizOptJson.getString("type"));

            ResponseData responseData = opt.runOpt(bizModel, bizOptJson, dataOptContext);
            /*if (responseData.getCode() != ResponseData.RESULT_OK) {
                throw new ObjectException(responseData, responseData.getCode(), responseData.getMessage());
            }*/
            if ((ConstantValue.LOGLEVEL_CHECK_DEBUG & logLevel) != 0) {
                TaskDetailLog detailLog = createLogDetail(dataOptStep, dataOptContext);
                Map<String, Object> jsonObject = CollectionsOpt.objectToMap(responseData.getData());
                detailLog.setSuccessPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("success"), 0));
                detailLog.setErrorPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("error"), 0));
                DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("id"));
                if(dataSet !=null) {
                    detailLog.setLogInfo(dataSet.toJSONString());
                } else {
                    detailLog.setLogInfo(responseData.toJSONString());
                }
                detailLog.setRunBeginTime(runBeginTime);
            }
            //正确返回也要设置运行结果
            bizModel.getOptResult().setStepResponse(bizOptJson.getString("id"), responseData);

        } catch (Exception e) {
            TaskDetailLog detailLog = createLogDetail(dataOptStep, dataOptContext);
            String errMsg = ObjectException.extortExceptionMessage(e);
            detailLog.setLogInfo(errMsg);
            detailLog.setRunBeginTime(runBeginTime);

            ResponseData responseData = ResponseData.makeErrorMessageWithData(errMsg, ResponseData.ERROR_OPERATION,
                e.getMessage());
            bizModel.getOptResult().setStepResponse(bizOptJson.getString("id"), responseData);
        }
    }

    private TaskDetailLog createLogDetail(DataOptStep dataOptStep, DataOptContext dataOptContext) {
        TaskDetailLog detailLog = new TaskDetailLog();

        JSONObject bizOptJson = dataOptStep.getCurrentStep();
        bizOptJson = bizOptJson.getJSONObject("properties");
        String sOptType = bizOptJson.getString("type");
        String processName = bizOptJson.getString("processName");
        if (StringBaseOpt.isNvl(processName)) {
            processName = bizOptJson.getString("nodeName");
        }
        String logType = sOptType + ":" + processName;
        detailLog.setRunBeginTime(new Date());
        detailLog.setOptNodeId(bizOptJson.getString("id"));
        detailLog.setLogType(logType);
        detailLog.setStepNo(dataOptContext.getStepNo());
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(
            StringBaseOpt.fillZeroForString(StringBaseOpt.castObjectToString(dataOptStep.getStepNo(), "0"), 6));
        dataOptContext.getTaskLog().addDetailLog(detailLog);
        return detailLog;
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
            DataPacketDraft dataPacketDraft = dataPacketCopyDao.getObjectWithReferences(packetId);
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
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED, "定时任务或消息触发不支持请求，该类型任务会自动触发！");
        }
        if (dataPacketInterface.getIsDisable() != null && dataPacketInterface.getIsDisable()) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED, "API接口已被禁用，请先恢复！");
        }
        DataOptContext moduleOptContext = new DataOptContext();
        moduleOptContext.setTaskLog(dataOptContext.getTaskLog());
        moduleOptContext.setOptId(dataOptContext.getOptId());
        moduleOptContext.setNeedRollback(dataOptContext.getNeedRollback());
        moduleOptContext.setStackData(ConstantValue.MODULE_CALL_TAG, callParams);
        //添加原始调用参数
        moduleOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
        //添加调用环境的上下文
        moduleOptContext.setStackData(ConstantValue.SESSION_DATA_TAG,
                     bizModel.getStackData(ConstantValue.SESSION_DATA_TAG));

        DataOptResult result = runInner(dataPacketInterface, moduleOptContext);

        bizModel.putDataSet(stepJson.getString("id"), DataSet.toDataSet(result.getResultData()));
        if(result.hasErrors()){
            bizModel.getOptResult().setStepResponse(
                stepJson.getString("id"), result.makeErrorResponse());
        } else {
            bizModel.getOptResult().setStepResponse(
                stepJson.getString("id"), BuiltInOperation.createResponseSuccessData(1));
        }
    }
}
