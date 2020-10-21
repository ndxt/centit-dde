package com.centit.dde.services.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.JSBizOperation;
import com.centit.dde.bizopt.PersistenceBizOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.dde.core.BizSupplier;
import com.centit.dde.dao.TaskDetailLogDao;
import com.centit.dde.po.TaskDetailLog;
import com.centit.dde.utils.BuiltInOperation;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.product.metadata.service.DatabaseRunTime;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.product.metadata.service.MetaObjectService;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.algorithm.UuidOpt;
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
    }

    @Override
    public void registerOperation(String key, BizOperation opt) {
        allOperations.put(key, opt);
    }

    /**
     * @return 返回真正运行的次数, 如果小于 0 表示報錯
     */
    @Override
    public BizModel run(BizSupplier supplier, JSONObject bizOptJson, String logId) {
        int n = 0;
        BizModel result = null;
        do {
            BizModel tempBM = supplier.get();
            if (tempBM == null || tempBM.isEmpty()) {
                break;
            }
            n++;
            result = apply(tempBM, bizOptJson, logId, n);
        } while (supplier.isBatchWise());
        return result;
    }

    protected void runOneStep(BizModel bizModel, JSONObject bizOptJson, String logId, int batchNum) {
        String sOptType = bizOptJson.getString("operation");
        BizOperation opt = allOperations.get(sOptType);
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
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum,"0"));
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
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum,"0"));
                taskDetailLogDao.saveNewObject(detailLog);
            }
            JSONObject jsonObject=opt.runOpt(bizModel, bizOptJson);
            if (logId != null) {
                detailLog.setSuccessPieces(jsonObject.getIntValue("success"));
                detailLog.setErrorPieces(jsonObject.getIntValue("error"));
                detailLog.setLogInfo(BuiltInOperation.getJsonFieldString(jsonObject,"info","ok"));
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
                detailLog.setTaskId(StringBaseOpt.castObjectToString(batchNum,"0"));
                detailLog.setRunEndTime(new Date());
                taskDetailLogDao.saveNewObject(detailLog);
            }
            throw new ObjectException(bizOptJson, ObjectException.extortExceptionMessage(e, 4));
        }
    }

    public BizModel apply(BizModel bizModel, JSONObject bizOptJson, String logId, int batchNum) {
        JSONArray optSteps = bizOptJson.getJSONArray("steps");
        if (optSteps == null || optSteps.isEmpty()) {
            return bizModel;
        }
        for (Object step : optSteps) {
            if (step instanceof JSONObject) {
                /*result =*/
                runOneStep(bizModel, (JSONObject) step, logId, batchNum);
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
    public BizModel debug(BizSupplier supplier, JSONObject bizOptJson) {
        BizModel bizModel = supplier.get();
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
