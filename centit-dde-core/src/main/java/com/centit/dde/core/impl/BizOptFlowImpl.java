package com.centit.dde.core.impl;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.*;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.dao.TaskLogDao;
import com.centit.dde.po.*;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.CloneUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.vo.CycleVo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.DataCheckRuleService;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.*;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @Value("${os.file.base.dir:./file_home/export}")
    private String path;

    @Autowired
    private SourceInfoDao sourceInfoDao;


    @Autowired
    private MetaDataService metaDataService;

    @Autowired
    private TaskDetailLogDao taskDetailLogDao;

    @Autowired
    private DataPacketDraftDao dataPacketCopyDao;

    @Autowired
    private TaskLogDao taskLogDao;

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
    private FileStore fileStore;

    private Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }

    @PostConstruct
    public void init() {
        //allOperations.put(ConstantValue.SCHEDULER, (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runStart(bizModel1, bizOptJson1));//模块调度
        allOperations.put("start", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runStart(bizModel1, bizOptJson1));//起始节点
        allOperations.put("postData", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runRequestBody(bizModel, bizOptJson));//获取post数据
        allOperations.put("postFile", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runRequestFile(bizModel, bizOptJson));//获取post文件
        allOperations.put("map", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runMap(bizModel1, bizOptJson1));
        allOperations.put("filter", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runFilter(bizModel1, bizOptJson1));
        allOperations.put("append", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runAppend(bizModel1, bizOptJson1));
        allOperations.put("stat", (bizModel2, bizOptJson2, dataOptContext) -> BuiltInOperation.runStat(bizModel2, bizOptJson2));
        allOperations.put("analyse", (bizModel3, bizOptJson3, dataOptContext) -> BuiltInOperation.runAnalyse(bizModel3, bizOptJson3));
        allOperations.put("cross", (bizModel2, bizOptJson2, dataOptContext) -> BuiltInOperation.runCross(bizModel2, bizOptJson2));
        //allOperations.put("compare", BuiltInOperation::runCompare);
        allOperations.put("sort", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runSort(bizModel1, bizOptJson1));
        allOperations.put("join", (bizModel1, bizOptJson1, dataOptContext) -> BuiltInOperation.runJoin(bizModel1, bizOptJson1));
        allOperations.put("union", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runUnion(bizModel, bizOptJson));
        //allOperations.put("filterExt", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runFilterExt(bizModel, bizOptJson));
        allOperations.put("check", new CheckRuleBizOperation());
        allOperations.put("static", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runStaticData(bizModel, bizOptJson));
        allOperations.put("http", new HttpBizOperation(sourceInfoDao));
        allOperations.put("clear", (bizModel, bizOptJson, dataOptContext) -> BuiltInOperation.runClear(bizModel, bizOptJson));
        allOperations.put("js", new JSBizOperation());
        allOperations.put("persistence", new PersistenceBizOperation(path, sourceInfoDao, metaDataService));
        allOperations.put("database", new DbBizOperation(sourceInfoDao, queryDataScopeFilter));
        allOperations.put("excel", new ExcelBizOperation());
        allOperations.put("csv", new CsvBizOperation());
        allOperations.put("json", new JsonBizOperation());
        allOperations.put("sqlS", new RunSqlsBizOperation(sourceInfoDao));//批量执行SQL
        allOperations.put("SSD", new ReportBizOperation(fileStore));
        allOperations.put(ConstantValue.GENERATE_CSV, new GenerateCsvFileBizOperation());
        allOperations.put(ConstantValue.GENERATE_JSON, new GenerateJsonFileBizOperation());
        allOperations.put(ConstantValue.GENERAT_EXCEL, new GenerateExcelFileBizeOperation(fileStore));
        allOperations.put(ConstantValue.DEFINE_JSON_DATA, new DefineJsonDataBizOperation());
        allOperations.put(ConstantValue.FILE_UPLOAD, new FileUploadBizOperation(fileStore));
        allOperations.put(ConstantValue.FILE_DOWNLOAD, new FileDownloadBizOperation(fileStore));
        //为了兼容历史数据保留着  现已拆分为元数据查询  和  元数据更新 2个组件
        allOperations.put(ConstantValue.METADATA_OPERATION,
            new MetadataBizOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_QUERY,
            new MetadataQueryBizOperation(metaObjectService, queryDataScopeFilter, metaDataCache));
        allOperations.put(ConstantValue.METADATA_OPERATION_UPDATE, new MetadataUpdateBizOperation(metaObjectService));
        allOperations.put(ConstantValue.COMPARE_SOURCE, new ObjectCompareBizOperation());
        allOperations.put(ConstantValue.SESSION_DATA, new GetSessionDataBizOperation());
        allOperations.put(ConstantValue.COMMIT_TRANSACTION, new TransactionCommitOperation(sourceInfoDao));
        allOperations.put(ConstantValue.INTERSECT_DATASET, new IntersectDataSetBizOperation());
        allOperations.put(ConstantValue.MINUS_DATASET, new MinusDataSetBizOperation());
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    @Override
    public DataOptResult run(DataPacketInterface dataPacket, DataOptContext dataOptContext) throws Exception {
        //LOGID name
        BizModel bizModel = new BizModel(dataOptContext.getLogId());

        bizModel.setCallStackData(dataOptContext.getCallStackData());
        DataOptStep dataOptStep = new DataOptStep(dataPacket.getDataOptDescJson());

        dataOptContext.setNeedRollback(dataPacket.getNeedRollback());
        dataOptContext.setOptId(dataPacket.getOptId());
        dataOptContext.setLogLevel(dataPacket.getLogLevel());
        try {
            runModule(bizModel, dataOptStep, dataOptContext);
            AbstractSourceConnectThreadHolder.commitAndRelease();
        } catch (Exception e) {
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
            //加个错误日志到数据库中
        }
        return bizModel.getOptResult();
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
            returnResult(bizModel, dataOptStep);
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
        String debugId = StringBaseOpt.castObjectToString(dataOptContext.getDebugId());
        if (StringUtils.isNotBlank(debugId) && debugId.equals(stepJson.getString("id"))) {
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
        if (ConstantValue.TRUE.equals(dataOptContext.getNeedRollback())) {
            //如果API不能允许报错，报错就中断
            if (bizModel.getOptResult().getLastError().getCode() == ResponseData.ERROR_OPERATION) {
                returnResult(bizModel, dataOptStep);
                dataOptStep.setEndStep();
                AbstractSourceConnectThreadHolder.rollbackAndRelease();
            }
        }
    }

    private void returnResult(BizModel bizModel, DataOptStep dataOptStep) {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson = stepJson.getJSONObject("properties");
        String type = BuiltInOperation.getJsonFieldString(stepJson, "resultOptions", RETURN_RESULT_ALL_DATASET);
        String dataSetId;
        if (bizModel != null && bizModel.getOptResult().hasErrors() || RETURN_RESULT_STATE.equals(type)) {
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            return;//bizModel.getOptResult().getLastError();
        }
        if (RETURN_RESULT_DATASET.equals(type) || RETURN_RESULT_ORIGIN.equals(type)) {
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet = bizModel.fetchDataSetByName(dataSetId);
            if (dataSet == null) {
                bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
            } else {
                bizModel.getOptResult().setResultObject(dataSet.getData());
                if (RETURN_RESULT_DATASET.equals(type)) {
                    Map<String, Object> mapFirstRow = dataSet.getFirstRow();
                    boolean isFile = mapFirstRow != null;
                    if (isFile) {
                        Object fileData = mapFirstRow.get(ConstantValue.FILE_CONTENT);
                        if (fileData instanceof OutputStream || fileData instanceof InputStream) {
                            String fileName = StringBaseOpt.castObjectToString(mapFirstRow.get(ConstantValue.FILE_NAME));
                            bizModel.getOptResult().setResultFile(fileName, fileData);
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
        //返回异常信息
        if (RETURN_RESULT_ERROR.equals(type)) {
            String code = stepJson.getString("code");
            String message = stepJson.getString("message");
            dataSetId = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet = bizModel.fetchDataSetByName(dataSetId);
            ResponseSingleData response = new ResponseSingleData();
            response.setCode(NumberBaseOpt.castObjectToInteger(code, 1));
            response.setMessage(message);
            if (dataSet != null) {
                response.setData(dataSet.getData());
            }
            bizModel.getOptResult().addLastStepResult("return", response);
            bizModel.getOptResult().setResultType(DataOptResult.RETURN_CODE_AND_MESSAGE);
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
                if (ConstantValue.DATA_COPY.equals(cycleVo.getAssignType())) {
                    //clone 复制对象数据
                    bizModel.putDataSet(cycleVo.getId(), CloneUtils.clone(new DataSet(value)));
                } else {
                    //引用对象数据
                    bizModel.putDataSet(cycleVo.getId(), new DataSet(value));
                }
            }
            //设置下个节点信息
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
                    break;
                }
                if (ConstantValue.CYCLE_JUMP_OUT.equals(stepType)) {
                    String breakType = step.getString("endType");
                    endCycle = ConstantValue.CYCLE_JUMP_BREAK.equals(breakType);
                    break;
                }
                //执行节点操作，并设置该节点的下个节点信息
                runStep(bizModel, dataOptStep, dataOptContext);
            }
            if (endCycle) {
                break;
            }
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                iter = (Integer) iter + cycleVo.getRangeStep();
            }
        }
        dataOptStep.seekToCycleEnd(cycleVo.getId());
    }

    private void runOneStepOpt(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) {
        int logLevel = dataOptContext.getLogLevel();
        TaskDetailLog detailLog = null;
        if ((ConstantValue.LOGLEVEL_CHECK_DEBUG & logLevel) != 0) {
            detailLog = writeLog(dataOptStep, dataOptContext);
        }
        JSONObject bizOptJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        try {
            BizOperation opt = allOperations.get(bizOptJson.getString("type"));
            ResponseData responseData = opt.runOpt(bizModel, bizOptJson, dataOptContext);
            //dataOptContext.setReturnResult(responseData);
            bizModel.getOptResult().addLastStepResult(bizOptJson.getString("id"), responseData);
            if (responseData.getCode() != ResponseData.RESULT_OK) {
                throw new ObjectException(responseData, responseData.getCode(), responseData.getMessage());
            }
            Map<String, Object> jsonObject = CollectionsOpt.objectToMap(responseData.getData());
            if (detailLog != null && dataOptContext.getLogId() != null && (ConstantValue.LOGLEVEL_CHECK_DEBUG & logLevel) != 0) {
                detailLog.setSuccessPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("success")));
                detailLog.setErrorPieces(NumberBaseOpt.castObjectToInteger(jsonObject.get("error")));
                detailLog.setLogInfo("ok");
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.updateObject(detailLog);
            }
        } catch (Exception e) {
            if (ConstantValue.LOGLEVEL_TYPE_ERROR == logLevel) {
                TaskLog taskLog = dataOptContext.getTaskLog();
                //主日志只记录一次
                if (taskLog != null && StringUtils.isEmpty(taskLog.getLogId())) {
                    taskLog.setRunEndTime(new Date());
                    taskLog.setOtherMessage("error");
                    taskLogDao.saveNewObject(taskLog);
                    dataOptContext.setLogId(taskLog.getLogId());
                }
            }
            if (detailLog == null) {
                detailLog = writeLog(dataOptStep, dataOptContext);
            }
            if (e instanceof ObjectException) {
                ObjectException objectException=(ObjectException)e;
                bizModel.getOptResult().addLastStepResult(bizOptJson.getString("id"), (ResponseData) (objectException.getObjectData()));
                detailLog.setLogInfo(e.getMessage());
            } else {
                String errMsg = ObjectException.extortExceptionMessage(e, 8);
                ResponseData responseData = ResponseData.makeErrorMessageWithData(errMsg, ResponseData.ERROR_OPERATION,
                    e.getMessage());
                bizModel.getOptResult().addLastStepResult(bizOptJson.getString("id"), responseData);
                detailLog.setLogInfo(errMsg);
            }
            //dataOptContext.setReturnResult(responseData);


            detailLog.setRunEndTime(new Date());
            taskDetailLogDao.updateObject(detailLog);
        }
    }

    private TaskDetailLog writeLog(DataOptStep dataOptStep, DataOptContext dataOptContext) {
        TaskDetailLog detailLog = new TaskDetailLog();
        if (dataOptContext.getLogId() == null) {
            return detailLog;
        }
        JSONObject bizOptJson = dataOptStep.getCurrentStep();
        bizOptJson = bizOptJson.getJSONObject("properties");
        String sOptType = bizOptJson.getString("type");
        String processName = bizOptJson.getString("processName");
        if (StringBaseOpt.isNvl(processName)) {
            processName = bizOptJson.getString("nodeName");
        }
        String logType = sOptType + ":" + processName;
        detailLog.setRunBeginTime(new Date());
        detailLog.setLogId(dataOptContext.getLogId());
        detailLog.setSuccessPieces(0);
        detailLog.setErrorPieces(0);
        detailLog.setLogType(logType);
        detailLog.setLogInfo("start");
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(
            StringBaseOpt.fillZeroForString(StringBaseOpt.castObjectToString(dataOptStep.getStepNo(), "0"), 6));
        taskDetailLogDao.saveNewObject(detailLog);
        return detailLog;
    }


    /**
     * 模块调度
     */
    private void moduleSchedule(BizModel bizModel, DataOptStep dataOptStep, DataOptContext dataOptContext) throws Exception {
        String taskLog = dataOptContext.getLogId();
        JSONObject stepJson = dataOptStep.getCurrentStep().getJSONObject("properties");

        Map<String, Object> queryParams = CollectionsOpt.cloneHashMap(
            CollectionsOpt.objectToMap(bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG)));

        queryParams.putAll(BuiltInOperation.jsonArrayToMap(stepJson.getJSONArray("config"), "paramName", "paramDefaultValue"));
        queryParams.entrySet().forEach(entry -> {
            String key = entry.getKey();
            Object calculateValue = VariableFormula.calculate(String.valueOf(queryParams.get(key)),
                new BizModelJSONTransform(bizModel));
            if (calculateValue != null) {
                queryParams.put(key, calculateValue);
            }
        });
        String packetId = stepJson.getString("packetName");
        Integer logLevel = dataOptContext.getLogLevel();
        DataPacketInterface dataPacketInterface;
        if (ConstantValue.RUN_TYPE_COPY.equals(dataOptContext.getRunType())) {
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
        moduleOptContext.setLogId(taskLog);
        moduleOptContext.setTaskLog(dataOptContext.getTaskLog());
        moduleOptContext.setOptId(dataOptContext.getOptId());
        moduleOptContext.setNeedRollback(dataOptContext.getNeedRollback());
        moduleOptContext.setStackData(ConstantValue.MODULE_CALL_TAG, queryParams);
        Object bizData = run(dataPacketInterface, moduleOptContext);

        if (bizData != null) {
            if (bizData instanceof ResponseData) {
                bizModel.putDataSet(stepJson.getString("id"), new DataSet(((ResponseSingleData) bizData).getData()));
            } else {
                bizModel.putDataSet(stepJson.getString("id"), new DataSet(bizData));
            }
        }
    }
}
