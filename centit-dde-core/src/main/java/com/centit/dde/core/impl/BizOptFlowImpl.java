package com.centit.dde.core.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.*;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
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
        JSONObject dataOptDescJson = bizOptJson.getJSONObject("dataOptDescJson");
        JSONArray node = dataOptDescJson.getJSONArray("nodeList");
        JSONArray link = dataOptDescJson.getJSONArray("linkList");
        String startId = "";
        for (Object o : node) {
            if (((JSONObject) o).get("source").equals("obtain")) {
                startId = ((JSONObject) o).getString("id");
            }
        }
        BizModel bizModel = apply(node, link, startId, startId, null, logId, 0, queryParams);
        if (bizModel.isEmpty()) {
            TaskDetailLog detailLog = new TaskDetailLog();
            detailLog.setRunBeginTime(new Date());
            detailLog.setLogId(logId);
            detailLog.setSuccessPieces(0);
            detailLog.setErrorPieces(0);
            detailLog.setLogType("emptyBizModel");
            detailLog.setLogInfo("ok");
            detailLog.setRunEndTime(new Date());
            detailLog.setTaskId(StringBaseOpt.castObjectToString(0, "0"));
            taskDetailLogDao.saveNewObject(detailLog);
        }
        return bizModel;
    }

    protected void runOneStep(BizModel bizModel, JSONObject bizOptJson, String logId, int batchNum, Map<String, Object> queryParams) {
        String sOptType = bizOptJson.getString("operation");
        BizOperation opt = allOperations.get(sOptType);
        if (opt instanceof DBBizOperation) {
            ((DBBizOperation) opt).setQueryParams(queryParams);
        }
        if (opt == null) {
            if (logId != null) {
                TaskDetailLog detailLog = new TaskDetailLog();
                detailLog.setRunBeginTime(new Date());
                detailLog.setLogId(logId);
                detailLog.setSuccessPieces(0);
                detailLog.setErrorPieces(0);
                detailLog.setLogType("error");
                detailLog.setLogInfo("找不到对应的操作：" + sOptType);
                detailLog.setRunEndTime(new Date());
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum, "0"));
                taskDetailLogDao.saveNewObject(detailLog);
            }
            throw new ObjectException(bizOptJson, "找不到对应的操作：" + sOptType);
        }
        try {
            TaskDetailLog detailLog = new TaskDetailLog();
            if (logId != null) {
                String processName = bizOptJson.getString("processName");
                detailLog.setRunBeginTime(new Date());
                detailLog.setLogId(logId);
                detailLog.setLogType(sOptType + ":" + processName);
                detailLog.setSuccessPieces(0);
                detailLog.setErrorPieces(0);
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum, "0"));
                taskDetailLogDao.saveNewObject(detailLog);
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
                TaskDetailLog detailLog = new TaskDetailLog();
                detailLog.setRunBeginTime(new Date());
                detailLog.setLogId(logId);
                detailLog.setSuccessPieces(0);
                detailLog.setErrorPieces(0);
                detailLog.setLogType("error");
                detailLog.setLogInfo(ObjectException.extortExceptionMessage(e, 4));
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum, "0"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.saveNewObject(detailLog);
            }
            throw new ObjectException(bizOptJson, ObjectException.extortExceptionMessage(e, 4));
        }
    }

    public BizModel apply(JSONArray node, JSONArray link, String startId, String firstId, BizModel bizModel, String logId, int batchNum, Map<String, Object> queryParams) {
        if (StringBaseOpt.isNvl(startId)) {
            return bizModel;
        }
        int i = 0;
        if (startId.equals(firstId)) {
            batchNum++;
            i = 0;
        }
        for (Object edge : link) {
            String start = BuiltInOperation.getJsonFieldString((JSONObject) edge, "sourceId", "");
            String end = BuiltInOperation.getJsonFieldString((JSONObject) edge, "targetId", "");
            if (start.equals(startId)) {
                for (Object step : node) {
                    if (((JSONObject) step).get("id").equals(startId)) {
                        String num = StringBaseOpt.castObjectToString(batchNum).concat(StringBaseOpt.castObjectToString(i++));
                        runOneStep(bizModel, (JSONObject) step, logId, NumberBaseOpt.parseInteger(num), queryParams);
                    }
                }
                if (!"".equals(end)) {
                    apply(node, link, end, firstId, bizModel, logId, batchNum, queryParams);
                }
            }
        }
        return bizModel;
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
