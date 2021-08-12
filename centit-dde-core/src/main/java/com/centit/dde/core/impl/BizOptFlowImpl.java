package com.centit.dde.core.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.*;
import com.centit.dde.dao.DataPacketCopyDao;
import com.centit.dde.dao.DataPacketDao;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.DataPacket;
import com.centit.dde.po.DataPacketCopy;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.CloneUtils;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.CycleVo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 业务流
 *
 * @author codefan@sina.com
 */
@Service
public class BizOptFlowImpl implements BizOptFlow {
    private static final Logger logger = LoggerFactory.getLogger(BizOptFlowImpl.class);
    @Value("${os.file.base.dir:./file_home/export}")
    private String path;
    @Autowired
    private SourceInfoDao sourceInfoDao;
    @Autowired
    private MetaDataService metaDataService;
    @Autowired
    private TaskDetailLogDao taskDetailLogDao;
    @Autowired
    private DataPacketCopyDao dataPacketCopyDao;
    @Autowired
    private DataPacketDao dataPacketDao;
    @Autowired(required = false)
    private MetaObjectService metaObjectService;
    @Autowired(required = false)
    private FileStore fileStore;
    private Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }

    @PostConstruct
    public void init() {
        allOperations.put("sche", BuiltInOperation::runStart);
        allOperations.put("Getpostdata", BuiltInOperation::runRequestBody);
        allOperations.put("Getpostfile", BuiltInOperation::runRequestFile);
        allOperations.put("map", BuiltInOperation::runMap);
        allOperations.put("filter", BuiltInOperation::runFilter);
        allOperations.put("append", BuiltInOperation::runAppend);
        allOperations.put("start", BuiltInOperation::runStat);
        allOperations.put("analyse", BuiltInOperation::runAnalyse);
        allOperations.put("cross", BuiltInOperation::runCross);
        allOperations.put("compare", BuiltInOperation::runCompare);
        allOperations.put("sort", BuiltInOperation::runSort);
        allOperations.put("join", BuiltInOperation::runJoin);
        allOperations.put("union", BuiltInOperation::runUnion);
        allOperations.put("filterExt", BuiltInOperation::runFilterExt);
        allOperations.put("check", BuiltInOperation::runCheckData);
        allOperations.put("static", BuiltInOperation::runStaticData);
        HttpBizOperation httpBizOperation = new HttpBizOperation(sourceInfoDao);
        allOperations.put("htts", httpBizOperation);
        allOperations.put("clear", BuiltInOperation::runClear);
        JSBizOperation jsBizOperation = new JSBizOperation(metaObjectService);
        allOperations.put("js", jsBizOperation);
        PersistenceBizOperation databaseOperation = new PersistenceBizOperation(
            path, sourceInfoDao, metaDataService);
        allOperations.put("persistence", databaseOperation);
        DbBizOperation dbBizOperation = new DbBizOperation(sourceInfoDao);
        allOperations.put("database", dbBizOperation);
        ExcelBizOperation excelBizOperation = new ExcelBizOperation();
        allOperations.put("excel", excelBizOperation);
        CsvBizOperation csvBizOperation = new CsvBizOperation();
        allOperations.put("csv", csvBizOperation);
        JsonBizOperation jsonBizOperation = new JsonBizOperation();
        allOperations.put("json", jsonBizOperation);
        RunSqlsBizOperation runsqlsbizoperation = new RunSqlsBizOperation(sourceInfoDao);
        allOperations.put("sqlS", runsqlsbizoperation);
        ReportBizOperation reportBizOperation = new ReportBizOperation(fileStore);
        allOperations.put("SSD", reportBizOperation);
        allOperations.put(ConstantValue.GENERATECSV, new GenerateCsvBizOperation());
        allOperations.put(ConstantValue.GENERATEJSON, new GenerateJsonBizOperation());
        allOperations.put(ConstantValue.RETURN_JSON, GenerateJsonBizOperation::returnJson);
        allOperations.put(ConstantValue.FILEUPLOADS, new FileUploadBizOperation(fileStore));
        allOperations.put(ConstantValue.GENERATEXCEL, new GenerateExcelBizeOperation());
        allOperations.put(ConstantValue.FILEDOWNLOAD, new FileDownloadBizOperation(fileStore));
        allOperations.put(ConstantValue.WRITE_DB, new WriteDbBizOperation(metaObjectService));
        allOperations.put(ConstantValue.ASSIGNMENT, new AssignmentBizOperation());
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    @Override
    public Object run(Object dataPacket, String logId, Map<String, Object> queryParams) throws Exception {
        JSONObject jsonObject = null;
        String needRollBack = ConstantValue.FALSE;
        if (dataPacket instanceof DataPacket) {
            jsonObject = ((DataPacket) dataPacket).getDataOptDescJson();
            needRollBack = ((DataPacket) dataPacket).getNeedRollback();
        } else if (dataPacket instanceof DataPacketCopy) {
            jsonObject = ((DataPacketCopy) dataPacket).getDataOptDescJson();
            needRollBack = ((DataPacketCopy) dataPacket).getNeedRollback();
        }
        SimpleBizModel bizModel = new SimpleBizModel(logId);
        bizModel.setModelTag(queryParams);
        DataOptStepWithBizModel dataOptStepWithBizmodel = new DataOptStepWithBizModel(jsonObject, needRollBack, bizModel);
        try {
            runModule(dataOptStepWithBizmodel);
            AbstractSourceConnectThreadHolder.commitAndRelease();
        } catch (Exception e) {
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
        }
        return dataOptStepWithBizmodel.getPreResult();
    }

    private void runModule(DataOptStepWithBizModel dataOptStepWithBizmodel) throws Exception {
        dataOptStepWithBizmodel.setCurrentStep(dataOptStepWithBizmodel.getStartStep());
        while (true) {
            JSONObject step = dataOptStepWithBizmodel.getCurrentStep();
            if (step == null) {
                return;
            }
            runStep(dataOptStepWithBizmodel);
        }
    }

    private void runStep(DataOptStepWithBizModel dataOptStepWithBizmodel) throws Exception {
        JSONObject stepJson = dataOptStepWithBizmodel.getCurrentStep();
        String stepType = stepJson.getString("type");
        SimpleBizModel bizModel = dataOptStepWithBizmodel.getBizModel();
        if (ConstantValue.RESULTS.equals(stepType)) {
            Object returnResult = returnResult(dataOptStepWithBizmodel);
            dataOptStepWithBizmodel.setPreResult(returnResult);
            dataOptStepWithBizmodel.setCurrentStep(null);
            return;
        }
        if (ConstantValue.BRANCH.equals(stepType)) {
            stepJson = getBatchStep(dataOptStepWithBizmodel);
            dataOptStepWithBizmodel.setCurrentStep(stepJson);
            return;
        }
        // 模块调用
        if (ConstantValue.SCHEDULER.equals(stepType)) {
            Map<String, Object> queryParams = CollectionsOpt.cloneHashMap(dataOptStepWithBizmodel.getQueryParams());
            queryParams.putAll(BuiltInOperation.jsonArrayToMap(stepJson.getJSONArray("config"),
                "paramName", "paramDefaultValue"));
            for (String key : queryParams.keySet()) {
                Object calculateValue = VariableFormula.calculate(String.valueOf(queryParams.get(key)), new BizModelJSONTransform(bizModel));
                if (calculateValue != null) {
                    queryParams.put(key, calculateValue);
                }
            }
            JSONObject dataOptJson;
            if (ConstantValue.RUN_TYPE_COPY.equals(dataOptStepWithBizmodel.getRunType())) {
                DataPacketCopy dataPacket = dataPacketCopyDao
                    .getObjectWithReferences(stepJson.getString("packetName"));
                dataOptJson = dataPacket.getDataOptDescJson();
            } else {
                DataPacket dataPacket = dataPacketDao
                    .getObjectWithReferences(stepJson.getString("packetName"));
                dataOptJson = dataPacket.getDataOptDescJson();
            }
            DataOptStepWithBizModel subModuleDataOptStepWithBizModel = new DataOptStepWithBizModel(dataOptJson, dataOptStepWithBizmodel.getNeedRollback(), bizModel);
            runModule(subModuleDataOptStepWithBizModel);
            dataOptStepWithBizmodel.setPreResult(subModuleDataOptStepWithBizModel.getPreResult());
            if (subModuleDataOptStepWithBizModel.getPreResult() instanceof DataSet) {
                bizModel.putDataSet(stepJson.getString("id"), (DataSet) subModuleDataOptStepWithBizModel.getPreResult());
            }
        } else if (ConstantValue.CYCLE.equals(stepType)) {
            //当节点为“结束循环”时，将对应的循环节点信息set到json中
            runCycle(dataOptStepWithBizmodel);
        } else {
            runOneStepOpt(dataOptStepWithBizmodel);
        }
        dataOptStepWithBizmodel.setNextStep();
        if (ConstantValue.TRUE.equals(dataOptStepWithBizmodel.getNeedRollback())) {
            if (dataOptStepWithBizmodel.getPreResult() instanceof ResponseData) {
                if (((ResponseData) dataOptStepWithBizmodel.getPreResult()).getCode() == ResponseData.ERROR_OPERATION) {
                    dataOptStepWithBizmodel.setCurrentStep(null);
                    AbstractSourceConnectThreadHolder.rollbackAndRelease();
                }
            }
        }
    }

    private Object returnResult(DataOptStepWithBizModel dataOptStepWithBizmodel) throws Exception {
        JSONObject stepJson = dataOptStepWithBizmodel.getCurrentStep();
        SimpleBizModel bizModel = dataOptStepWithBizmodel.getBizModel();
        String type = BuiltInOperation.getJsonFieldString(stepJson, "resultOptions", "1");
        String path;
        bizModel.getModelTag().remove("requestFile");
        bizModel.getModelTag().remove("requestBody");
        if (bizModel.getResponseMapData().getCode() != ResponseData.RESULT_OK) {
            return bizModel.getResponseMapData();
        }
        switch (type) {
            case "2":
                return bizModel.getResponseMapData();
            case "3":
                path = BuiltInOperation.getJsonFieldString(stepJson, "source", "");
                return bizModel.fetchDataSetByName(path);
            case "4":
                path = BuiltInOperation.getJsonFieldString(stepJson, "textarea", "D");
                return JSONTransformer.transformer(
                    JSON.parse(path), new BizModelJSONTransform(bizModel));
            case "5":
                path = BuiltInOperation.getJsonFieldString(stepJson, "kname", "D");
                return bizModel.fetchDataSetByName(path);
            case "6":
                return BuiltInOperation.returnExcel(bizModel, stepJson);
            default:
                return bizModel;
        }
    }

    private JSONObject getBatchStep(DataOptStepWithBizModel dataOptStepWithBizmodel) {
        JSONObject stepJson = dataOptStepWithBizmodel.getCurrentStep();
        String stepId = stepJson.getString("id");
        List<JSONObject> linksJson = dataOptStepWithBizmodel.getNextLinks(stepId);
        for (JSONObject jsonObject : linksJson) {
            if (!ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                if (BooleanBaseOpt.castObjectToBoolean(
                    VariableFormula.calculate(jsonObject.getString("expression"),
                        new BizModelJSONTransform(dataOptStepWithBizmodel.getBizModel())), false)) {
                    return dataOptStepWithBizmodel.getOptStep(jsonObject.getString("targetId"));
                }
            }
        }
        for (JSONObject jsonObject : linksJson) {
            if (ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                return dataOptStepWithBizmodel.getOptStep(jsonObject.getString("targetId"));
            }
        }
        return null;
    }

    private void runCycle(DataOptStepWithBizModel dataOptStepWithBizmodel) throws Exception {
        JSONObject stepJson = dataOptStepWithBizmodel.getCurrentStep();
        SimpleBizModel bizModel = dataOptStepWithBizmodel.getBizModel();
        CycleVo cycleVo = stepJson.toJavaObject(CycleVo.class);
        //循环节点的下个节点信息
        JSONObject startNode = dataOptStepWithBizmodel.getNextStep(cycleVo.getId());
        JSONObject endNode = null;
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
            dataOptStepWithBizmodel.setCurrentStep(startNode);
            boolean endCycle = false;
            while (true) {
                //获取节点信息
                JSONObject step = dataOptStepWithBizmodel.getCurrentStep();
                if (step == null) {
                    break;
                }
                String stepType = step.getString("type");
                if (ConstantValue.CYCLE_FINISH.equals(stepType)) {
                    endNode = step;
                    break;
                }
                if (ConstantValue.CYCLE_JUMP_OUT.equals(stepType)) {
                    String breakType = step.getString("endType");
                    endCycle = ConstantValue.CYCLE_JUMP_BREAK.equals(breakType);
                    break;
                }
                //执行节点操作，并设置该节点的下个节点信息
                runStep(dataOptStepWithBizmodel);
            }
            if (endCycle) {
                break;
            }
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType()) && iter instanceof Integer) {
                iter = (Integer) iter + cycleVo.getRangeStep();
            }
        }
        if (endNode == null) {
            endNode = dataOptStepWithBizmodel.seekToCycleEnd(cycleVo.getId());
        }
        dataOptStepWithBizmodel.setCurrentStep(endNode);
    }

    private void runOneStepOpt(DataOptStepWithBizModel dataOptStepWithBizmodel) {
        long startTime = System.currentTimeMillis();
        TaskDetailLog detailLog = writeLog(dataOptStepWithBizmodel);
        SimpleBizModel bizModel = dataOptStepWithBizmodel.getBizModel();
        JSONObject bizOptJson = dataOptStepWithBizmodel.getCurrentStep();
        try {
            BizOperation opt = allOperations.get(bizOptJson.getString("type"));
            ResponseData responseData = opt.runOpt(bizModel, bizOptJson);
            dataOptStepWithBizmodel.setPreResult(responseData);
            if (responseData.getCode() != ResponseData.RESULT_OK) {
                throw new ObjectException(responseData, responseData.getCode(), responseData.getMessage());
            }
            JSONObject jsonObject = JSONObject.parseObject(responseData.getData().toString());
            if (dataOptStepWithBizmodel.getLogId() != null) {
                detailLog.setSuccessPieces(jsonObject.getIntValue("success"));
                detailLog.setErrorPieces(jsonObject.getIntValue("error"));
                detailLog.setLogInfo(BuiltInOperation.getJsonFieldString(jsonObject, "info", "ok"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.updateObject(detailLog);
            }
            long expendTime = System.currentTimeMillis() - startTime;
            logger.info(String.format("节点：%s，运行耗时:%s ms", bizOptJson.getString("SetsName"), expendTime));
        } catch (Exception e) {
            logger.error(String.format("节点：%s，运行异常:%s", bizOptJson.getString("SetsName"), e.getMessage()));
            String errMsg = ObjectException.extortExceptionMessage(e, 8);
            ResponseData responseData = ResponseData.makeErrorMessageWithData(e, ResponseData.ERROR_OPERATION,
                errMsg);
            dataOptStepWithBizmodel.setPreResult(responseData);
            bizModel.addResponseMapData(bizOptJson.getString("id"), responseData);
            detailLog.setLogInfo(errMsg);
            detailLog.setRunEndTime(new Date());
            taskDetailLogDao.updateObject(detailLog);
        }
    }

    private TaskDetailLog writeLog(DataOptStepWithBizModel dataOptStepWithBizModel) {
        TaskDetailLog detailLog = new TaskDetailLog();
        if (dataOptStepWithBizModel.getLogId() == null) {
            return detailLog;
        }
        JSONObject bizOptJson = dataOptStepWithBizModel.getCurrentStep();
        String sOptType = bizOptJson.getString("type");
        String processName = bizOptJson.getString("processName");
        if (StringBaseOpt.isNvl(processName)) {
            processName = bizOptJson.getString("nodeName");
        }
        String logType = sOptType + ":" + processName;
        detailLog.setRunBeginTime(new Date());
        detailLog.setLogId(dataOptStepWithBizModel.getLogId());
        detailLog.setSuccessPieces(0);
        detailLog.setErrorPieces(0);
        detailLog.setLogType(logType);
        detailLog.setLogInfo("start");
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(StringBaseOpt.fillZeroForString(StringBaseOpt.castObjectToString(dataOptStepWithBizModel.getStepNo(), "0"), 6));
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
