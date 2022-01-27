package com.centit.dde.core.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.*;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.DataPacketDraftDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketDraft;
import com.centit.dde.po.DataPacketInterface;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.BizOptUtils;
import com.centit.dde.utils.CloneUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.CycleVo;
import com.centit.dde.vo.DataOptVo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.common.ResponseSingleData;
import com.centit.framework.core.service.DataScopePowerManager;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaDataCache;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(BizOptFlowImpl.class);
    public static final String RETURN_RESULT_STATE = "2";
    public static final String RETURN_RESULT_DATASET = "3";
    public static final String RETURN_RESULT_ORIGIN = "4";
    public static final String FILE_DOWNLOAD = "fileDownload";

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
    private DataPacketDao dataPacketDao;

    @Autowired(required = false)
    private MetaObjectService metaObjectService;

    @Autowired(required = false)
    private DataScopePowerManager queryDataScopeFilter;

    @Autowired
    private MetaDataCache metaDataCache;


    @Autowired(required = false)
    private FileStore fileStore;


    private Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }

    @PostConstruct
    public void init() {
        allOperations.put("schedule", BuiltInOperation::runStart);//模块调度
        allOperations.put("start", BuiltInOperation::runStart);//起始节点
        allOperations.put("postData", BuiltInOperation::runRequestBody);//获取post数据
        allOperations.put("postFile", BuiltInOperation::runRequestFile);//获取post文件
        allOperations.put("map", BuiltInOperation::runMap);
        allOperations.put("filter", BuiltInOperation::runFilter);
        allOperations.put("append", BuiltInOperation::runAppend);
        allOperations.put("stat", BuiltInOperation::runStat);
        allOperations.put("analyse", BuiltInOperation::runAnalyse);
        allOperations.put("cross", BuiltInOperation::runCross);
        allOperations.put("compare", BuiltInOperation::runCompare);
        allOperations.put("sort", BuiltInOperation::runSort);
        allOperations.put("join", BuiltInOperation::runJoin);
        allOperations.put("union", BuiltInOperation::runUnion);
        allOperations.put("filterExt", BuiltInOperation::runFilterExt);
        allOperations.put("check", BuiltInOperation::runCheckData);
        allOperations.put("static", BuiltInOperation::runStaticData);
        allOperations.put("htts", new HttpBizOperation(sourceInfoDao));
        allOperations.put("clear", BuiltInOperation::runClear);
        allOperations.put("js", new JSBizOperation(metaObjectService));
        allOperations.put("persistence", new PersistenceBizOperation(path, sourceInfoDao, metaDataService));
        allOperations.put("database", new DbBizOperation(sourceInfoDao));
        allOperations.put("excel", new ExcelBizOperation());
        allOperations.put("csv", new CsvBizOperation());
        allOperations.put("json", new JsonBizOperation());
        allOperations.put("sqlS", new RunSqlsBizOperation(sourceInfoDao));//批量执行SQL
        allOperations.put("SSD", new ReportBizOperation(fileStore));
        allOperations.put(ConstantValue.GENERATE_CSV_FILE , new GenerateCsvFileBizOperation());
        allOperations.put(ConstantValue.GENERATE_JSON_FILE, new GenerateJsonFileBizOperation());
        allOperations.put(ConstantValue.GENERAT_EXCEL_FILE , new GenerateExcelFileBizeOperation(fileStore));
        allOperations.put(ConstantValue.DEFINE_JSON_DATA, new DefineJsonDataBizOperation());
        allOperations.put(ConstantValue.FILE_UPLOAD, new FileUploadBizOperation(fileStore));
        allOperations.put(ConstantValue.FILE_DOWNLOAD, new FileDownloadBizOperation(fileStore));
        allOperations.put(ConstantValue.METADATA_OPERATION, new MetadataBizOperation(metaObjectService,queryDataScopeFilter,metaDataCache));
        allOperations.put(ConstantValue.ASSIGNMENT, new AssignmentBizOperation());
        allOperations.put(ConstantValue.COMPARE_SOURCE, new ObjectCompareBizOperation());
        allOperations.put(ConstantValue.SESSION_DATA, new GetSessionDataBizOperation());
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    @Override
    public Object run(DataPacketInterface dataPacket, String logId, Map<String, Object> queryParams,Map<String, Object> interimVariable) throws Exception {
        SimpleBizModel bizModel = new SimpleBizModel(logId);
        bizModel.setModelTag(queryParams);
        bizModel.setInterimVariable(interimVariable==null?new HashMap<>():interimVariable);
        //标签中默认的3个数据集  将数据set进去   需要在结束标签中移除这3个默认标签的数据  否则会显得返回结果数据很乱
        if (queryParams!=null && queryParams.size()>0){
            bizModel.putDataSet("getData",new SimpleDataSet(queryParams));
        }
        if (interimVariable.containsKey("requestBody")){
            String requestBody = (String) interimVariable.get("requestBody");
            DataSet destDs = BizOptUtils.castObjectToDataSet(requestBody.contains("[")?JSONObject.parseObject(requestBody,JSONArray.class):JSONObject.parseObject(requestBody));
            bizModel.putDataSet("postBodyData",destDs);
        }
        if (interimVariable.containsKey("requestFile")){
            bizModel.putDataSet("postFileData",new SimpleDataSet(interimVariable.get("requestFile")));
        }
        DataOptStep dataOptStep = new DataOptStep(dataPacket.getDataOptDescJson());
        DataOptVo dataOptVo = new DataOptVo(dataPacket.getNeedRollback(), bizModel);
        try {
            runModule(dataOptStep, dataOptVo);
            AbstractSourceConnectThreadHolder.commitAndRelease();
        } catch (Exception e) {
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
        }
        return dataOptVo.getPreResult();
    }

    private void runModule(DataOptStep dataOptStep, DataOptVo dataOptVo) throws Exception {
        dataOptStep.setStartStep();
        while (true) {
            if (dataOptStep.isEndStep()) {
                return;
            }
            runStep(dataOptStep, dataOptVo);
        }
    }

    private void runStep(DataOptStep dataOptStep, DataOptVo dataOptVo) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        String stepType = stepJson.getString("type");
        SimpleBizModel bizModel = dataOptVo.getBizModel();
        if (ConstantValue.RESULTS.equals(stepType)) {
            Object returnResult = returnResult(dataOptStep, dataOptVo);
            dataOptVo.setPreResult(returnResult);
            dataOptStep.setEndStep();
            return;
        }
        if (ConstantValue.BRANCH.equals(stepType)) {
            setBatchStep(dataOptStep, dataOptVo);
            return;
        }
        // 模块调用
        if (ConstantValue.SCHEDULER.equals(stepType)) {
            Map<String, Object> queryParams = CollectionsOpt.cloneHashMap(dataOptVo.getQueryParams());
            queryParams.putAll(BuiltInOperation.jsonArrayToMap(stepJson.getJSONArray("config"),
                "paramName", "paramDefaultValue"));
            for (String key : queryParams.keySet()) {
                Object calculateValue = VariableFormula.calculate(String.valueOf(queryParams.get(key)),
                    new BizModelJSONTransform(bizModel));
                if (calculateValue != null) {
                    queryParams.put(key, calculateValue);
                }
            }
            JSONObject dataOptJson;
            if (ConstantValue.RUN_TYPE_COPY.equals(dataOptVo.getRunType())) {
                DataPacketDraft dataPacket = dataPacketCopyDao.getObjectWithReferences(stepJson.getString("packetName"));
                dataOptJson = dataPacket.getDataOptDescJson();
            } else {
                DataPacket dataPacket = dataPacketDao.getObjectWithReferences(stepJson.getString("packetName"));
                dataOptJson = dataPacket.getDataOptDescJson();
            }
            DataOptStep subModuleDataOptStep = new DataOptStep(dataOptJson);
            DataOptVo subDataOptVo = new DataOptVo(dataOptVo.getNeedRollback(), bizModel);
            runModule(subModuleDataOptStep, subDataOptVo);
            dataOptVo.setPreResult(subDataOptVo.getPreResult());
            if (subDataOptVo.getPreResult() instanceof DataSet) {
                bizModel.putDataSet(stepJson.getString("id"), (DataSet) subDataOptVo.getPreResult());
            }
        } else if (ConstantValue.CYCLE.equals(stepType)) {
            //当节点为“结束循环”时，将对应的循环节点信息set到json中
            runCycle(dataOptStep, dataOptVo);
        } else {
            runOneStepOpt(dataOptStep, dataOptVo);
        }
        //断点调试，指定节点数据返回
        String debugId = (String)dataOptVo.getQueryParams().get("debugId");
        if (StringUtils.isNotBlank(debugId) && debugId.equals(stepJson.getString("id"))){
            dataOptStep.getCurrentStep().getJSONObject("properties").put("resultOptions","3");
            dataOptStep.getCurrentStep().getJSONObject("properties").put("source",stepJson.getString("id"));
            Object returnResult = returnResult(dataOptStep, dataOptVo);
            dataOptVo.setPreResult(returnResult);
            dataOptStep.setEndStep();
            return;
        }
        dataOptStep.setNextStep();
        if (ConstantValue.TRUE.equals(dataOptVo.getNeedRollback())) {
            if (dataOptVo.getPreResult() instanceof ResponseData) {
                if (((ResponseData) dataOptVo.getPreResult()).getCode() == ResponseData.ERROR_OPERATION) {
                    dataOptStep.setEndStep();
                    AbstractSourceConnectThreadHolder.rollbackAndRelease();
                }
            }
        }
    }

    private Object returnResult(DataOptStep dataOptStep, DataOptVo dataOptVo) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        SimpleBizModel bizModel = dataOptVo.getBizModel();
        //移除3个默认请求参数数据集数据  这个3个数据集保存的是请求的参数，这个不需要返回
        if(bizModel!=null){
            bizModel.removeDataSet("getData");
            bizModel.removeDataSet("postBodyData");
            bizModel.removeDataSet("postFileData");
        }
        stepJson=stepJson.getJSONObject("properties");
        String type = BuiltInOperation.getJsonFieldString(stepJson, "resultOptions", "1");
        String path;
        if (bizModel.getResponseMapData().getCode() != ResponseData.RESULT_OK || RETURN_RESULT_STATE.equals(type)) {
            ResponseMapData responseMapData=bizModel.getResponseMapData();
            ResponseSingleData responseSingleData= new ResponseSingleData(responseMapData.getCode(),responseMapData.getMessage());
            responseSingleData.setData(responseMapData);
            return responseSingleData;
        }
        if(RETURN_RESULT_DATASET.equals(type) || RETURN_RESULT_ORIGIN.equals(type)){
            path = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
            DataSet dataSet=bizModel.fetchDataSetByName(path);
            if(RETURN_RESULT_DATASET.equals(type)){
                Map<String, Object> mapFirstRow = dataSet.getFirstRow();
                ResponseSingleData responseSingleData= new ResponseSingleData();
                boolean isFile = mapFirstRow != null && mapFirstRow.containsKey(ConstantValue.FILE_CONTENT)
                    && (mapFirstRow.get(ConstantValue.FILE_CONTENT) instanceof OutputStream
                    || mapFirstRow.get(ConstantValue.FILE_CONTENT) instanceof InputStream);
                if (isFile) {
                    responseSingleData= new ResponseSingleData(FILE_DOWNLOAD);
                    responseSingleData.setData(dataSet);
                    return responseSingleData;
                }
                responseSingleData.setMessage("OK");
                responseSingleData.setData(dataSet.getData());
                return responseSingleData;
            }else{
                return dataSet;
            }
        }
        return ResponseData.makeResponseData(bizModel);
    }

    private void setBatchStep(DataOptStep dataOptStep, DataOptVo dataOptVo) {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson=stepJson.getJSONObject("properties");
        String stepId = stepJson.getString("id");
        List<JSONObject> linksJson = dataOptStep.getNextLinks(stepId);
        for (JSONObject jsonObject : linksJson) {
            if (!ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                if (BooleanBaseOpt.castObjectToBoolean(
                    VariableFormula.calculate(jsonObject.getString("expression"),
                        new BizModelJSONTransform(dataOptVo.getBizModel())), false)) {
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

    private void runCycle(DataOptStep dataOptStep, DataOptVo dataOptVo) throws Exception {
        JSONObject stepJson = dataOptStep.getCurrentStep();
        stepJson=stepJson.getJSONObject("properties");
        SimpleBizModel bizModel = dataOptVo.getBizModel();
        CycleVo cycleVo = stepJson.toJavaObject(CycleVo.class);
        //循环节点的下个节点信息
        JSONObject startNode = dataOptStep.getNextStep(cycleVo.getId());
        Object iter = null;
        //Iterator<Object> rangeObject;
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
                bizModel.putDataSet(cycleVo.getId(), new SimpleDataSet(iter));
            } else {
                //foreach循环
                if (!((Iterator<Object>) iter).hasNext()) {
                    break;
                }
                Object value = ((Iterator<Object>) iter).next();
                if (ConstantValue.DATA_COPY.equals(cycleVo.getAssignType())) {
                    //clone 复制对象数据
                    bizModel.putDataSet(cycleVo.getId(), CloneUtils.clone(new SimpleDataSet(value)));
                } else {
                    //引用对象数据
                    bizModel.putDataSet(cycleVo.getId(), new SimpleDataSet(value));
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
                runStep(dataOptStep, dataOptVo);
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

    private void runOneStepOpt(DataOptStep dataOptStep, DataOptVo dataOptVo) {
        TaskDetailLog detailLog = writeLog(dataOptStep, dataOptVo);
        SimpleBizModel bizModel = dataOptVo.getBizModel();
        JSONObject bizOptJson = dataOptStep.getCurrentStep().getJSONObject("properties");
        try {
            BizOperation opt = allOperations.get(bizOptJson.getString("type"));
            ResponseData responseData = opt.runOpt(bizModel, bizOptJson);
            dataOptVo.setPreResult(responseData);
            if (responseData.getCode() != ResponseData.RESULT_OK) {
                throw new ObjectException(responseData, responseData.getCode(), responseData.getMessage());
            }
            JSONObject jsonObject = JSONObject.parseObject(responseData.getData().toString());
            if (dataOptVo.getLogId() != null) {
                detailLog.setSuccessPieces(jsonObject.getIntValue("success"));
                detailLog.setErrorPieces(jsonObject.getIntValue("error"));
                detailLog.setLogInfo(BuiltInOperation.getJsonFieldString(jsonObject, "info", "ok"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.updateObject(detailLog);
            }
        } catch (Exception e) {
            String errMsg = ObjectException.extortExceptionMessage(e, 8);
            ResponseData responseData = ResponseData.makeErrorMessageWithData(e, ResponseData.ERROR_OPERATION,
                errMsg);
            dataOptVo.setPreResult(responseData);
            bizModel.addResponseMapData(bizOptJson.getString("id"), responseData);
            detailLog.setLogInfo(errMsg);
            detailLog.setRunEndTime(new Date());
            taskDetailLogDao.updateObject(detailLog);
        }
    }

    private TaskDetailLog writeLog(DataOptStep dataOptStep, DataOptVo dataOptVo) {
        TaskDetailLog detailLog = new TaskDetailLog();
        if (dataOptVo.getLogId() == null) {
            return detailLog;
        }
        JSONObject bizOptJson = dataOptStep.getCurrentStep();
        bizOptJson=bizOptJson.getJSONObject("properties");
        String sOptType = bizOptJson.getString("type");
        String processName = bizOptJson.getString("processName");
        if (StringBaseOpt.isNvl(processName)) {
            processName = bizOptJson.getString("nodeName");
        }
        String logType = sOptType + ":" + processName;
        detailLog.setRunBeginTime(new Date());
        detailLog.setLogId(dataOptVo.getLogId());
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

    @Override
    public BizModel debug(JSONObject bizOptJson) {
        BizModel bizModel = null;
        JSONArray optSteps = bizOptJson.getJSONArray("steps");
        if (optSteps != null) {
            for (Object step : optSteps) {
                if (step instanceof JSONObject) {
                    /*result =*/
                    debugOneStepOpt(bizModel, (JSONObject) step);
                }
            }
        }
        return bizModel;
    }

    private void debugOneStepOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sOptType = bizOptJson.getString("operation");
        BizOperation opt = allOperations.get(sOptType);
        if (opt != null) {
            //TODO 记录运行前日志
            opt.debugOpt(bizModel, bizOptJson);
            //TODO 记录运行后日志
        } else {
            //TODO 记录运行错误日志
        }
    }
}
