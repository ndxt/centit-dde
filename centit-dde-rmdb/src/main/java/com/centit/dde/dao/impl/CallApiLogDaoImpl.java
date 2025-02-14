package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.CallApiLogDao;
import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.adapter.po.CallApiLogDetail;
import com.centit.dde.adapter.po.CallApiLogDetails;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository(value = "callApiLogDao")
public class CallApiLogDaoImpl implements CallApiLogDao {

    private final static Logger logger = LoggerFactory.getLogger(CallApiLogDaoImpl.class);

    private final ESIndexer callApiLogIndexer;
    private final ESSearcher callApiLogSearcher;
    private final ESIndexer callApiLogDetailIndexer;
    private final ESSearcher callApiLogDetailSearcher;

    private final ESServerConfig esServerConfig;

    @Autowired
    public CallApiLogDaoImpl(@Autowired ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
        this.callApiLogIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, CallApiLog.class);
        this.callApiLogSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, CallApiLog.class);
        this.callApiLogDetailIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, CallApiLogDetails.class);
        this.callApiLogDetailSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, CallApiLogDetails.class);
    }


    @Override
    public void saveLog(CallApiLog callApiLog) {
        callApiLogIndexer.saveNewDocument(callApiLog);
    }

    @Override
    public void saveLogDetils(CallApiLog callApiLog) {
        CallApiLogDetails details = new CallApiLogDetails();
        details.setLogId(callApiLog.getLogId());
        details.setTaskId(callApiLog.getTaskId());
        details.setRunBeginTime(callApiLog.getRunBeginTime());
        details.setDetailLogs(callApiLog.getDetailLogs());
        callApiLogDetailIndexer.saveNewDocument(details);
    }

    @Override
    public CallApiLog getLog(String logId) {
        JSONObject object = callApiLogSearcher.getDocumentById("logId", logId);
        if(object==null){
            return null;
        }
        return object.toJavaObject(CallApiLog.class);
    }
    @Override
    public List<CallApiLogDetail> listLogDetails(String logId) {
        JSONObject object = callApiLogDetailSearcher.getDocumentById("logId", logId);
        if(object==null){
            return null;
        }
        CallApiLogDetails details = object.toJavaObject(CallApiLogDetails.class);
        return details.getDetailLogs();
    }

    @Override
    public CallApiLog getLogWithDetail(String logId) {
        CallApiLog callLog = getLog(logId);
        if(callLog==null){
            return null;
        }
        callLog.setDetailLogs(listLogDetails(logId));
        return callLog;
    }

    @Override
    public void deleteLogById(String logId) {
        callApiLogIndexer.deleteDocument(logId);
    }

    @Override
    public void deleteLogDetailById(String logId) {
        callApiLogDetailIndexer.deleteDocument(logId);
    }

    @Override
    public List<Map<String, Object>> listLogsByProperties(Map<String, Object> param, PageDesc pageDesc) {
        Pair<Long, List<Map<String, Object>>> queryOut =
            callApiLogSearcher.search(param, null, pageDesc.getPageNo(), pageDesc.getPageSize());
        pageDesc.setTotalRows(queryOut.getLeft().intValue());
        return queryOut.getRight();
    }

    @Override
    public Map<String, Object> getLogStatisticsInfo(Map<String, Object> queryparameter) {
         return null;
    }

    @Override
    public int deleteTaskLog(String packetId, Date runBeginTime, boolean isError) {

        return 0;
    }

}
