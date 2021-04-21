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
import com.centit.dde.transaction.AbstractSourceConnectThreadHolder;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.CycleVo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.dao.SourceInfoDao;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
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
        allOperations.put("start", BuiltInOperation::runStart);
        allOperations.put("sche", BuiltInOperation::runStart);
        allOperations.put("resBody", BuiltInOperation::runRequestBody);
        allOperations.put("resFile", BuiltInOperation::runRequestFile);
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
        ExcelBizOperation excelBizOperation = new ExcelBizOperation(fileStore);
        allOperations.put("excel", excelBizOperation);
        CsvBizOperation csvBizOperation = new CsvBizOperation(fileStore);
        allOperations.put("csv", csvBizOperation);
        JsonBizOperation jsonBizOperation = new JsonBizOperation(fileStore);
        allOperations.put("json", jsonBizOperation);
        RunSqlsBizOperation runsqlsbizoperation = new RunSqlsBizOperation(sourceInfoDao);
        allOperations.put("sqlS", runsqlsbizoperation);
        ReportBizOperation reportBizOperation = new ReportBizOperation(fileStore);
        allOperations.put("SSD", reportBizOperation);
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    /**
     * 获取分支
     */
    private JSONObject getBatchStep(BizModel bizModel, DataOptDescJson dataOptDescJson, String stepId, Object preResult) {
        List<JSONObject> linksJson = dataOptDescJson.getNextLinks(stepId);
        for (JSONObject jsonObject : linksJson) {
            if(!ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                if (BooleanBaseOpt.castObjectToBoolean(
                    VariableFormula.calculate(jsonObject.getString("expression"),
                        new BizModelJSONTransform(bizModel)), false)) {
                    return dataOptDescJson.getOptStep(jsonObject.getString("targetId"));
                }
            }
        }
        for (JSONObject jsonObject : linksJson) {
            if (ConstantValue.ELSE.equalsIgnoreCase(jsonObject.getString("expression"))) {
                return dataOptDescJson.getOptStep(jsonObject.getString("targetId"));
            }
        }
        return null;
    }

    private Object returnResult(BizModel bizModel, JSONObject stepJson) throws Exception {
        String type = BuiltInOperation.getJsonFieldString(stepJson, "resultOptions", "1");
        String path;
        bizModel.getModelTag().remove("requestFile");
        bizModel.getModelTag().remove("requestBody");
        switch (type) {
            case "2":
                return "ok";
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
                return BuiltInOperation.returnExcel(bizModel,stepJson);
            default:
                return bizModel;
        }
    }

    public Object runCycle(DataOptDescJson dataOptDescJson, String logId, BizModel bizModel,
                           Object preResult) throws Exception {
        JSONObject stepJson = dataOptDescJson.getCurrentStep();
        CycleVo cycleVo = stepJson.toJavaObject(CycleVo.class);
        JSONObject startNode = dataOptDescJson.getNextStep(cycleVo.getId());
        JSONObject endNode = null;
        Object iter = null;
        //Iterator<Object> rangeObject;
        if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType())) {
            iter = cycleVo.getRangeBegin();
        } else {
            DataSet refObject = bizModel.getDataSet(cycleVo.getSource());
            if(refObject!= null) {
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

        while(iter != null) {
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType())) {
                if((cycleVo.getRangeStep()>0 && (Integer)iter >= cycleVo.getRangeEnd()) ||
                    ((cycleVo.getRangeStep()<0 && (Integer)iter <= cycleVo.getRangeEnd()))){
                    break;
                }
                bizModel.putDataSet(cycleVo.getId(),new SimpleDataSet(iter));
            } else {
                if(!((Iterator<Object>)iter).hasNext()){
                    break;
                }
                Object value = ((Iterator<Object>) iter).next();
                if("1".equals(cycleVo.getAssignType())){
                    //clone
                    bizModel.putDataSet(cycleVo.getId(), new SimpleDataSet(value));
                } else{
                    bizModel.putDataSet(cycleVo.getId(), new SimpleDataSet(value));
                }
            }

            dataOptDescJson.setCurrentStep(startNode);
            boolean endCycle = false;
            while(true){
                JSONObject step = dataOptDescJson.getCurrentStep();
                if(step == null){
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

                preResult = runStep(dataOptDescJson, logId, bizModel, preResult);
            }
            if(endCycle){
                break;
            }
            if (ConstantValue.CYCLE_TYPE_RANGE.equals(cycleVo.getCycleType())) {
                iter = (Integer) iter + cycleVo.getRangeStep();
            }
        }

        if(endNode == null){
            endNode = dataOptDescJson.seekToCycleEnd(cycleVo.getId());
        }

        dataOptDescJson.setCurrentStep(endNode);
        return preResult;
    }

    public Object runStep(DataOptDescJson dataOptDescJson, String logId, BizModel bizModel,
                          Object preResult) throws Exception {
        JSONObject stepJson = dataOptDescJson.getCurrentStep();
        String stepId = stepJson.getString("id");
        String stepType = stepJson.getString("type");
        if (ConstantValue.RESULTS.equals(stepType)) {
            preResult = returnResult(bizModel, stepJson);
            dataOptDescJson.setCurrentStep(null);
            return preResult;
        }
        if (ConstantValue.BRANCH.equals(stepType)) {
            stepJson = getBatchStep(bizModel, dataOptDescJson, stepId, preResult);
            dataOptDescJson.setCurrentStep(stepJson);
            return preResult;
        }
        // 模块调用
        if (ConstantValue.SCHEDULER.equals(stepType)) {
            JSONObject moduleOptJson;

            Map<String, Object> queryParams = CollectionsOpt.cloneHashMap(bizModel.getModelTag());
            queryParams.putAll(BuiltInOperation.jsonArrayToMap(stepJson.getJSONArray("config"),
                "paramName", "paramDefaultValue"));
            if (ConstantValue.RUN_TYPE_DEBUG.equals(dataOptDescJson.getRunType())){
                DataPacketCopy dataPacket = dataPacketCopyDao
                    .getObjectWithReferences(stepJson.getString("packetName"));
                moduleOptJson = dataPacket.getDataOptDescJson();
            }else {
                DataPacket dataPacket = dataPacketDao
                    .getObjectWithReferences(stepJson.getString("packetName"));
                moduleOptJson = dataPacket.getDataOptDescJson();
            }
            preResult = runModule(moduleOptJson, logId, queryParams);
        } else if (ConstantValue.CYCLE.equals(stepType)){
            //当节点为“结束循环”时，将对应的循环节点信息set到json中
            preResult = runCycle(dataOptDescJson, logId, bizModel, preResult);
        } else {
            preResult = runOneStepOpt(dataOptDescJson, bizModel, stepJson, logId);
        }
        stepJson = dataOptDescJson.getCurrentStep();
        if(stepJson!=null) {
            dataOptDescJson.setCurrentStep(
                dataOptDescJson.getNextStep(stepJson.getString("id")));
        }
        return preResult;
    }

    private Object runModule(JSONObject bizOptJson, String logId, Map<String, Object> queryParams)
          throws Exception {
        SimpleBizModel bizModel = new SimpleBizModel(logId);
        bizModel.setModelTag(queryParams);
        DataOptDescJson dataOptDescJson = new DataOptDescJson(bizOptJson);
        String runType = (String)bizModel.getModelTag().get("runType");
        if (ConstantValue.RUN_TYPE_DEBUG.equals(runType)){
            dataOptDescJson.setRunType(ConstantValue.RUN_TYPE_DEBUG);
        }
        Object preResult = ResponseData.successResponse;
        dataOptDescJson.setCurrentStep(dataOptDescJson.getStartStep());
        while(true){
            JSONObject step = dataOptDescJson.getCurrentStep();
            if(step == null){
                return preResult;
            }
            preResult = runStep(dataOptDescJson, logId, bizModel, preResult);
        }
    }

    @Override
    public Object run(JSONObject bizOptJson, String logId, Map<String, Object> queryParams) throws Exception {
        Object responseData = ResponseData.successResponse;
        try {
            responseData = runModule(bizOptJson, logId, queryParams);
            AbstractSourceConnectThreadHolder.commitAndRelease();
        } catch (Exception e) {
            AbstractSourceConnectThreadHolder.rollbackAndRelease();
        }
        return responseData;
    }

    private TaskDetailLog writeLog(String logId, String logType, int step, String logInfo) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setLogId(logId);
        detailLog.setSuccessPieces(0);
        detailLog.setErrorPieces(0);
        detailLog.setLogType(logType);
        detailLog.setLogInfo(logInfo);
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(StringBaseOpt.fillZeroForString(StringBaseOpt.castObjectToString(step, "0"), 6));
        taskDetailLogDao.saveNewObject(detailLog);
        return detailLog;
    }

    /**
     * 单步运行
     */
    public ResponseData runOneStepOpt(DataOptDescJson dataOptDescJson,
                                      BizModel bizModel, JSONObject bizOptJson, String logId) {
        String sOptType = bizOptJson.getString("type");
        BizOperation opt = allOperations.get(sOptType);
        if (opt == null) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_OPERATION,
                "找不到对应的操作：" + sOptType);
        }
        try {
            TaskDetailLog detailLog = new TaskDetailLog();
            if (logId != null) {
                String processName = bizOptJson.getString("processName");
                if (StringBaseOpt.isNvl(processName)) {
                    processName = bizOptJson.getString("nodeName");
                }
                detailLog = writeLog(logId, sOptType + ":" + processName,
                    dataOptDescJson.getStepNo(), "");
            }
            ResponseData responseData = opt.runOpt(bizModel, bizOptJson);
            JSONObject jsonObject = JSONObject.parseObject(responseData.getData().toString());
            if (logId != null) {
                detailLog.setSuccessPieces(jsonObject.getIntValue("success"));
                detailLog.setErrorPieces(jsonObject.getIntValue("error"));
                detailLog.setLogInfo(BuiltInOperation.getJsonFieldString(jsonObject, "info", "ok"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.updateObject(detailLog);
            }
            return responseData;
        } catch (Exception e) {
            return ResponseData.makeErrorMessage(ResponseData.ERROR_OPERATION, e.getMessage());
        }
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
}
