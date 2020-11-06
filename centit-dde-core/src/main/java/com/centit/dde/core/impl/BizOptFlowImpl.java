package com.centit.dde.core.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.DataOptDescJson;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务流
 */
@Service
public class BizOptFlowImpl implements BizOptFlow {
    @Value("${os.file.base.dir:./file_home/export}")
    private String path;

    @Autowired
    private IntegrationEnvironment integrationEnvironment;

    @Autowired
    private MetaDataService metaDataService;
    @Autowired
    private TaskDetailLogDao taskDetailLogDao;
    @Autowired(required = false)
    private MetaObjectService metaObjectService;

    @Autowired(required = false)
    private DatabaseRunTime databaseRunTime;
    @Autowired(required = false)
    private FileStore fileStore;

    public Map<String, BizOperation> allOperations;

    public BizOptFlowImpl() {
        allOperations = new HashMap<>(50);
    }


    @PostConstruct
    public void init() {
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
        allOperations.put("http", BuiltInOperation::runHttpData);

        JSBizOperation jsBizOperation = new JSBizOperation(metaObjectService,
            databaseRunTime);
        allOperations.put("js", jsBizOperation);

        PersistenceBizOperation databaseOperation = new PersistenceBizOperation(
            path, integrationEnvironment, metaDataService);
        allOperations.put("persistence", databaseOperation);
        DBBizOperation dbBizOperation = new DBBizOperation(integrationEnvironment);
        allOperations.put("obtain-database", dbBizOperation);
        ExcelBizOperation excelBizOperation = new ExcelBizOperation(fileStore);
        allOperations.put("obtain-excel", excelBizOperation);
        CsvBizOperation csvBizOperation = new CsvBizOperation(fileStore);
        allOperations.put("obtain-csv", csvBizOperation);
        JsonBizOperation jsonBizOperation = new JsonBizOperation(fileStore);
        allOperations.put("obtain-json", jsonBizOperation);
        HttpBizOperation httpBizOperation = new HttpBizOperation();
        allOperations.put("obtain-http", httpBizOperation);
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    /**
     * @return 返回真正运行的次数, 如果小于 0 表示報錯
     */
    @Override
    public BizModel run(JSONObject bizOptJson, String logId, Map<String, Object> queryParams) {
        DataOptDescJson dataOptDescJson = new DataOptDescJson(bizOptJson.getJSONObject("dataOptDescJson"));
        String startId = dataOptDescJson.getStartId();
        if (StringBaseOpt.isNvl(startId)) {
            writeLog(0, 0, logId, "error", "没有start节点", 0);
            return null;
        }
        BizModel bizModel = null;
        int i = 0;
        while (!StringBaseOpt.isNvl(startId)) {
            JSONObject stepJson = dataOptDescJson.getOptStep(startId);
            if ("branch".equals(stepJson.getString("type"))) {
                List<JSONObject> linksJson = dataOptDescJson.getNextLinks(startId);
                for (int j = 0; j < linksJson.size(); j++) {
                    JSONObject jsonObject = linksJson.get(j);
                    if (BooleanBaseOpt.castObjectToBoolean(
                        VariableFormula.calculate(jsonObject.getString("rule"), queryParams), false)) {
                        startId = dataOptDescJson.getNextId(startId, j);
                        break;
                    }
                }
            } else {
                runOneStep(bizModel, stepJson, logId, i++, queryParams);
                startId = dataOptDescJson.getNextId(startId, 0);
            }
        }
        if (bizModel.isEmpty()) {
            writeLog(0, 0, logId, "emptyBizModel", "ok", 0);
        }
        return bizModel;
    }

    private TaskDetailLog writeLog(int success, int error, String logId, String logType, String logInfo, int taskId) {
        TaskDetailLog detailLog = new TaskDetailLog();
        detailLog.setRunBeginTime(new Date());
        detailLog.setLogId(logId);
        detailLog.setSuccessPieces(success);
        detailLog.setErrorPieces(error);
        detailLog.setLogType(logType);
        detailLog.setLogInfo(logInfo);
        detailLog.setRunEndTime(new Date());
        detailLog.setTaskId(StringBaseOpt.castObjectToString(taskId, "0"));
        taskDetailLogDao.saveNewObject(detailLog);
        return detailLog;
    }

    protected void runOneStep(BizModel bizModel, JSONObject bizOptJson, String logId, int batchNum, Map<String, Object> queryParams) {
        String sOptType = bizOptJson.getString("operation");
        BizOperation opt = allOperations.get(sOptType);
        if (opt instanceof DBBizOperation) {
            ((DBBizOperation) opt).setQueryParams(queryParams);
        }
        if (opt == null) {
            if (logId != null) {
                writeLog(0, 0, logId, "error", "找不到对应的操作：" + sOptType, batchNum);
            }
            throw new ObjectException(bizOptJson, "找不到对应的操作：" + sOptType);
        }
        try {
            TaskDetailLog detailLog = new TaskDetailLog();
            if (logId != null) {
                String processName = bizOptJson.getString("processName");
                detailLog = writeLog(0, 0, logId, sOptType + ":" + processName, "", batchNum);
            }
            JSONObject jsonObject = opt.runOpt(bizModel, bizOptJson);
            if (logId != null) {
                detailLog.setSuccessPieces(jsonObject.getIntValue("success"));
                detailLog.setErrorPieces(jsonObject.getIntValue("error"));
                detailLog.setLogInfo(BuiltInOperation.getJsonFieldString(jsonObject, "info", "ok"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.updateObject(detailLog);
            }
        } catch (Exception e) {
            if (logId != null) {
                writeLog(0, 0, logId, "error", ObjectException.extortExceptionMessage(e, 4), batchNum);
            }
            throw new ObjectException(bizOptJson, ObjectException.extortExceptionMessage(e, 4));
        }
    }

    protected void debugOneStep(BizModel bizModel, JSONObject bizOptJson) {
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
                    debugOneStep(bizModel, (JSONObject) step);
                }
            }
        }
        return bizModel;
    }
}
