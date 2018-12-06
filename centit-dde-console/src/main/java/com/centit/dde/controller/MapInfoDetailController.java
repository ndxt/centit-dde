package com.centit.dde.controller;

import com.centit.dde.po.ExchangeMapInfo;
import com.centit.dde.po.MapInfoDetail;
import com.centit.dde.service.ExchangeMapInfoManager;
import com.centit.dde.service.MapInfoDetailManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseMapData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.compiler.Lexer;
import com.centit.support.database.utils.PageDesc;
import com.centit.support.database.utils.QueryUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Controller
@RequestMapping("/mapinfodetail")
public class MapInfoDetailController extends BaseController {
    private static final Log log = LogFactory.getLog(MapInfoDetailController.class);

    private static final long serialVersionUID = 1L;
    @Resource
    private MapInfoDetailManager mapinfoDetailMag;

    @Resource
    private ExchangeMapInfoManager exchangeMapInfoManager;

    @Resource
    private IntegrationEnvironment integrationEnvironment;

    @RequestMapping(value="/showMapinfoDetail",method = {RequestMethod.GET})
    public void showMapinfoDetail(String s_mapinfoId,PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        String soueceTableName = null;
        String goalTableName = null;
        Map<String, Object> searchColumn = convertSearchColumn(request);

        List<Map<String, String>> sourceTableStruct = new ArrayList<>();
        List<Map<String, String>> goalTableStruct = new ArrayList<>();
        List<Map<String, String>> length = new ArrayList<>();

        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        exchangeMapInfo.setMapInfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));

        exchangeMapInfo = exchangeMapInfoManager.getObjectById(exchangeMapInfo.getMapInfoId());
        //TODO 这个地方逻辑混乱，需要重新整理 codefan@sina.com
        if (exchangeMapInfo == null){
//          return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
            return;
        }
        if (exchangeMapInfo.getSourceTableName() != null) {
            soueceTableName = exchangeMapInfo.getSourceTableName();
        }
        if (exchangeMapInfo.getDestTableName() != null) {
            goalTableName = exchangeMapInfo.getDestTableName();
        }
        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        }

        String type= (String) searchColumn.get("type");
        String mapinfoId= (String) searchColumn.get("mapinfoId");
        String method = (String)searchColumn.get("method");
        String querySql = (String) searchColumn.get("querySql");
        String querySqlsource = (String) searchColumn.get("querySqlsource");
        
        if (method!= null && method.equals("save")) {
            if (exchangeMapInfo == null) {
//                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, exchangeMapInfo.getSourceDatabaseName(),
//                        exchangeMapInfo.getDestDatabaseName(),mapinfoId,type);
            } else if (StringUtils.hasText(exchangeMapInfo.getQuerySql())) {
//                this.saveAndsaveMapinfoDetails(exchangeMapInfo.getQuerySql(), soueceTableName, goalTableName,
//                        exchangeMapInfo.getSourceDatabaseName(), exchangeMapInfo.getDestDatabaseName(),mapinfoId,type);
            }

        }
        if (method!= null && method.equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText(querySql)) {
                this.updateSourceColumnSentence(querySql, querySqlsource, String.valueOf(mapinfoId), soueceTableName, goalTableName);
            }
        }

        DatabaseInfo sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());
        
       /* sourceTableStruct = */
        //从数据库表中读取表结构
//        sourceTableStruct = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
        List<Map<String, String>> sts = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
        if (!sts.isEmpty()) {
            //初始化表结构
            sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(sourceDatabaseInfo, soueceTableName);
        }

        List<Map<String, String>> sts1 = mapinfoDetailMag.getGoalTableStructFromDatabase(Long.valueOf(s_mapinfoId));

        if (!sts1.isEmpty()) {
            //初始化表结构
            goalTableStruct = mapinfoDetailMag.getGoalTableStruct(goalDatabaseInfo, goalTableName);
        }

        List<List<Map<String,String>>> TableStruct = new ArrayList();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }

        ResponseMapData resData = new ResponseMapData();
        if (type.equals("initcopy4")) {
            resData.addResponseData("h_mapinfoId",mapinfoDetailMag.getMapinfoId());
        }
        resData.addResponseData("SOURCETABLESTRUCT", sourceTableStruct);
        resData.addResponseData("GOALTABLESTRUCT", goalTableStruct);
        resData.addResponseData("TABLESTRUCT", TableStruct);
        resData.addResponseData("LENGTH", length);
        resData.addResponseData("s_soueceTableName", soueceTableName);
        if (sourceDatabaseInfo != null && sourceDatabaseInfo.getDatabaseName() != null && StringUtils.hasText(sourceDatabaseInfo.getDatabaseName())) {
            resData.addResponseData("s_sourceDatabaseName", sourceDatabaseInfo.getDatabaseName());
        }
        resData.addResponseData("s_goalTableName", goalTableName);
        if (goalDatabaseInfo != null && StringUtils.hasText(goalDatabaseInfo.getDatabaseName())) {
            resData.addResponseData("s_goalDatabaseName", goalDatabaseInfo.getDatabaseName());
        }
        if (exchangeMapInfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapInfo.getMapInfoName());
            resData.addResponseData("isRepeat", exchangeMapInfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapInfo.getMapInfoDesc());
            resData.addResponseData("recordOperate", exchangeMapInfo.getRecordOperate());
        }
        if (type.equals("initinner")) {
//            return "mapInfo4Details";页面跳转
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else if (type.equals("initcopy")) {
//            return "copyMapinfoDetail";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else if (type.equals("initcopy4")) {
//            return "copyMapInfo4Details";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else {
//            return "showMapinfoDetail";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        }
    }

    @RequestMapping(value="/addAndsaveMapinfoDatails",method = {RequestMethod.GET})
    public void addAndsaveMapinfoDatails(String noInitfirst,String s_mapinfoId,PageDesc pageDesc, HttpServletRequest request,HttpServletResponse response) {
        String soueceTableName = null;
        String goalTableName = null;
        String sourcedatabaseName = null;
        String goaldatabaseName = null;
        Map<String, Object> searchColumn = convertSearchColumn(request);

        String type= (String) searchColumn.get("type");
        String mapinfoId= (String) searchColumn.get("mapinfoId");
        String method = (String)searchColumn.get("method");
        String querySql = (String) searchColumn.get("querySql");
        String querySqlsource = (String) searchColumn.get("querySqlsource");
        
        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        if (mapinfoId != null) {
            exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.parseLong(mapinfoId));
        }

        if (exchangeMapInfo != null && exchangeMapInfo.getSourceTableName() != null) {
            soueceTableName = exchangeMapInfo.getSourceTableName();
        }
        if (exchangeMapInfo != null && exchangeMapInfo.getDestTableName() != null) {
            goalTableName = exchangeMapInfo.getDestTableName();
        }

        if ((String) searchColumn.get("sourcedatabaseName") != null) {
            sourcedatabaseName = (String) searchColumn.get("sourcedatabaseName");
        }
        if ((String) searchColumn.get("goaldatabaseName") != null) {
            goaldatabaseName = (String) searchColumn.get("goaldatabaseName");
        }
        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        }

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();


        
        
        if (method!= null &&method.equals("save")) {
            if (exchangeMapInfo == null) {
//                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, mapinfoId, type);
            } else if (StringUtils.hasText(exchangeMapInfo.getQuerySql())) {
//                this.saveAndsaveMapinfoDetails(exchangeMapInfo.getQuerySql(), soueceTableName, goalTableName, sourcedatabaseName,goaldatabaseName,mapinfoId, type);
            }
        }
        if (method!= null &&method.equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText(querySql)) {
                this.updateSourceColumnSentence(querySql, querySqlsource,mapinfoId, soueceTableName, goalTableName);
            }
        }
        DatabaseInfo sourceDatabaseInfo = null;
        DatabaseInfo goalDatabaseInfo = null;

        if (sourcedatabaseName != null) {
            sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(sourcedatabaseName);
        }
        if (goaldatabaseName != null) {
            goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(goaldatabaseName);
        }
        if (exchangeMapInfo != null && exchangeMapInfo.getSourceDatabaseName() != null) {
            sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        }
        if (exchangeMapInfo != null && exchangeMapInfo.getDestDatabaseName() != null) {
            goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());
        }

        if (s_mapinfoId != null) {
            List<Map<String, String>> stableStrut = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
            List<Map<String, String>> gtableStrut = mapinfoDetailMag.getGoalTableStructFromDatabase(Long.valueOf(s_mapinfoId));
            if (sourceDatabaseInfo != null  && StringUtils.hasText(soueceTableName) && (type.equals("reinitsource")|| stableStrut.size() == 0)) {
                // 初始化表结构
                sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(sourceDatabaseInfo, soueceTableName);
            } else if (stableStrut.size() != 0) {
                // 从数据库表中读取表结构
                sourceTableStruct = stableStrut;
            }
            if (goalDatabaseInfo != null && StringUtils.hasText(goalTableName) && (type.equals("reinitgoal") || stableStrut.size() == 0)) {
                // 初始化表结构
                goalTableStruct = mapinfoDetailMag.getGoalTableStruct(goalDatabaseInfo, goalTableName);
            } else if (gtableStrut.size() != 0) {
                // 从数据库表中读取表结构
                goalTableStruct = gtableStrut;
            }
        }

        List<List<Map<String,String>>> TableStruct = new ArrayList();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        ResponseMapData resData = new ResponseMapData();
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (type.equals("initfirst")) {
                resData.addResponseData("s_mapinfoId",mapinfoDetailMag.getMapinfoId());
            }
        }
        resData.addResponseData("SOURCETABLESTRUCT", sourceTableStruct);
        resData.addResponseData("GOALTABLESTRUCT", goalTableStruct);
        resData.addResponseData("TABLESTRUCT", TableStruct);
        resData.addResponseData("LENGTH", length);
        resData.addResponseData("s_soueceTableName", soueceTableName);
        resData.addResponseData("s_goalTableName", goalTableName);
        resData.addResponseData("s_sourcedatabaseName", sourcedatabaseName);
        resData.addResponseData("s_goaldatabaseName", goaldatabaseName);
        if (exchangeMapInfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapInfo.getMapInfoName());
            resData.addResponseData("isRepeat", exchangeMapInfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapInfo.getMapInfoDesc());
            resData.addResponseData("recordOperate", exchangeMapInfo.getRecordOperate());
        }
        if (type.equals("initinner")) {
//            return "mapInfo4Details_add";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else {
//            return "addAndsaveMapinfoDatails";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        }
    }
        
        
        
    @RequestMapping(value="/copyAddAndsaveMapinfoDatails",method = {RequestMethod.GET})
    public void copyAddAndsaveMapinfoDatails(String noInitfirst,String s_mapinfoId,PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        
        Map<String, Object> searchColumn = convertSearchColumn(request);
        String type= (String) searchColumn.get("type");
        String mapinfoId= (String) searchColumn.get("mapinfoId");
        String method = (String)searchColumn.get("method");
        String querySql = (String) searchColumn.get("querySql");
        String querySqlsource = (String) searchColumn.get("querySqlsource");
        
        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        if (mapinfoId!= null) {
            exchangeMapInfo = exchangeMapInfoManager.getObjectById(Long.parseLong(mapinfoId));
        }
        String soueceTableName = null;
        String goalTableName = null;
        if (exchangeMapInfo != null ) {
            soueceTableName = exchangeMapInfo.getSourceTableName();
        }
        if (exchangeMapInfo != null ) {
            goalTableName = exchangeMapInfo.getDestTableName();
        }
        String sourcedatabaseName = (String) searchColumn.get("sourcedatabaseName");
        String goaldatabaseName = (String) searchColumn.get("goaldatabaseName");
        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        } 
        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

//        this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, mapinfoId, type);
        if (method!= null && method.equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText(querySql)) {
                this.updateSourceColumnSentence(querySql, querySqlsource,mapinfoId, soueceTableName, goalTableName);
            }
        }
        DatabaseInfo sourceDatabaseInfo = null;
        DatabaseInfo goalDatabaseInfo = null;

        if (sourcedatabaseName != null) {
            sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(sourcedatabaseName);
        }
        if (goaldatabaseName != null) {
            goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(goaldatabaseName);
        }
        if (exchangeMapInfo != null && exchangeMapInfo.getSourceDatabaseName() != null) {
            sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        }
        if (exchangeMapInfo != null && exchangeMapInfo.getDestDatabaseName() != null) {
            goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());
        }

        if (s_mapinfoId != null) {
            List<Map<String, String>> stableStrut = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
            List<Map<String, String>> gtableStrut = mapinfoDetailMag.getGoalTableStructFromDatabase(Long.valueOf(s_mapinfoId));
            if (sourceDatabaseInfo != null
                    && StringUtils.hasText(soueceTableName)
                    && (type.equals("reinitsource") || stableStrut.size() == 0)) {
                // 初始化表结构
                List<Map<String, String>> test = mapinfoDetailMag.getSourceTableStruct(sourceDatabaseInfo, soueceTableName);
                sourceTableStruct = test;
            } else if (gtableStrut.size() != 0) {
                // 从数据库表中读取表结构
                sourceTableStruct = gtableStrut;
            }
            if (goalDatabaseInfo != null
                    && StringUtils.hasText(goalTableName)
                    && (type.equals("reinitgoal") || stableStrut.size() == 0)) {
                // 初始化表结构
                List<Map<String, String>> test = mapinfoDetailMag.getGoalTableStruct(goalDatabaseInfo, goalTableName);
                goalTableStruct = test;
            } else if (gtableStrut.size() != 0) {
                // 从数据库表中读取表结构
                goalTableStruct = gtableStrut;
            }
        }

        List TableStruct = new ArrayList<List<Object>>();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        ResponseMapData resData = new ResponseMapData();
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (type.equals("initfirst")) {
                resData.addResponseData("s_mapinfoId",mapinfoDetailMag.getMapinfoId());
            }
        }
        resData.addResponseData("SOURCETABLESTRUCT", sourceTableStruct);
        resData.addResponseData("GOALTABLESTRUCT", goalTableStruct);
        resData.addResponseData("TABLESTRUCT", TableStruct);
        resData.addResponseData("LENGTH", length);
        resData.addResponseData("s_soueceTableName", soueceTableName);
        resData.addResponseData("s_goalTableName", goalTableName);
        resData.addResponseData("s_sourceDatabaseName", sourcedatabaseName);
        resData.addResponseData("s_goalDatabaseName", goaldatabaseName);
        resData.addResponseData("PAGE_DESC", pageDesc);
        if (exchangeMapInfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapInfo.getMapInfoName());
            resData.addResponseData("isRepeat", exchangeMapInfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapInfo.getMapInfoDesc());
            resData.addResponseData("recordOperate", exchangeMapInfo.getRecordOperate());
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/defSourceData",method = {RequestMethod.GET})
    public void defSourceData(String s_mapinfoId,HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        exchangeMapInfo.setMapInfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));
        exchangeMapInfo = exchangeMapInfoManager.getObjectById(exchangeMapInfo.getMapInfoId());

        DatabaseInfo sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());

        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("OBJLIST", sourceDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("SQL", exchangeMapInfo.getQuerySql());
        resData.addResponseData("sourceDatabaseName", (String) searchColumn.get("sourceDatabaseName"));
        resData.addResponseData("DATABASE",integrationEnvironment.listDatabaseInfo());
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/defSourceData_add",method = {RequestMethod.GET})
    public void defSourceData_add(String s_mapinfoId,HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        exchangeMapInfo.setMapInfoId(Long.parseLong(s_mapinfoId));
        ResponseMapData resData = new ResponseMapData();
        exchangeMapInfo = exchangeMapInfoManager.getObjectById(exchangeMapInfo.getMapInfoId());
        if (exchangeMapInfo != null) {
            resData.addResponseData("SQL", exchangeMapInfo.getQuerySql());
        }
        resData.addResponseData("DATABASE", integrationEnvironment.listDatabaseInfo());

        //database下面所有 的表
        if (searchColumn.containsKey("sourcedatabaseName") && StringUtils.hasText((String) searchColumn.get("sourcedatabaseName"))) {
            DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo((String) searchColumn.get("sourcedatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo);
            resData.addResponseData("tables", tables);
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/defGoalData_add",method = {RequestMethod.GET})
    public void defGoalData_add(HttpServletRequest request,HttpServletResponse response) {
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("DATABASE", integrationEnvironment.listDatabaseInfo());
        Map<String, Object> searchColumn = convertSearchColumn(request);
        if (searchColumn.containsKey("goaldatabaseName") && StringUtils.hasText((String) searchColumn.get("goaldatabaseName"))) {
            DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo((String) searchColumn.get("goaldatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo);
            resData.addResponseData("tables", tables);
        }
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/database",method = {RequestMethod.GET})
    public void database(String databaseName,HttpServletRequest request,HttpServletResponse response) {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo = integrationEnvironment.getDatabaseInfo(databaseName);
        List<Object> tables = mapinfoDetailMag.getTable(databaseInfo);
        JsonResultUtils.writeSingleDataJson(tables, response);
    }

    @RequestMapping(value="/defDestData",method = {RequestMethod.GET})
    public void defDestData(HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
        exchangeMapInfo.setMapInfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));
        exchangeMapInfo = exchangeMapInfoManager.getObjectById(exchangeMapInfo.getMapInfoId());
        DatabaseInfo sourceDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = integrationEnvironment.getDatabaseInfo(exchangeMapInfo.getDestDatabaseName());
        ResponseMapData resData = new ResponseMapData();
        resData.addResponseData("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("goalDatabaseName", (String) searchColumn.get("goalDatabaseName"));
        resData.addResponseData("DATABASE", integrationEnvironment.listDatabaseInfo());
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }
    

    private void saveAndsaveMapinfoDetails(String sourceSql, String[] SourceColumnSentence,String[] sourceColumnName,
            String soueceTableName, String goalTableName, String sourcedatabaseName, 
            String mapinfoName,String isRepeat,String mapinfoDesc,String recordOperate,
            String goaldatabaseName, Long mapInfoId, String type,String[] GoalColumnName,
            String[] GoalColumnType,String[] GoalisNullable,String[] GoalisPk,
            String[] SourceColumnType,String[] GoalFieldDefault) {
        String sql = null;
        if (sourceColumnName != null && SourceColumnSentence != null) {
            sql = createSql(sourceSql, sourceColumnName, SourceColumnSentence,soueceTableName);
        }

        mapinfoDetailMag.deleteMapinfoDetails(mapInfoId);
        //新增时直接保存无问题，新增时再新增字段会直接调用更新方法，报错
        if (type.equals("save") || null == mapinfoDetailMag.getObjectByProperty("mapInfoId",Long.valueOf(mapInfoId))) {
            exchangeMapInfoManager.deleteObjectById(mapInfoId);
            ExchangeMapInfo exchangeMapInfo = new ExchangeMapInfo();
            exchangeMapInfo.setMapInfoId(mapInfoId);
            exchangeMapInfo.setSourceDatabaseName(sourcedatabaseName);
            exchangeMapInfo.setSourceTableName(soueceTableName);
            exchangeMapInfo.setMapInfoName(mapinfoName);
            exchangeMapInfo.setIsRepeat(isRepeat);
            exchangeMapInfo.setMapInfoDesc(mapinfoDesc);
            exchangeMapInfo.setRecordOperate(recordOperate);
            exchangeMapInfo.setQuerySql(sql);
            exchangeMapInfo.setDestDatabaseName(goaldatabaseName);
            exchangeMapInfo.setDestTableName(goalTableName);
            exchangeMapInfoManager.saveNewObject(exchangeMapInfo);
        } else {
            mapinfoDetailMag.updateExchangeMapinfo(mapInfoId, soueceTableName, goalTableName, transferWord(sql));
        }
        int s = sourceColumnName.length;
        int G = GoalColumnName.length;

        int length = s > G ? s : G;
        int j = Math.abs(s - G);

        if (s > G) {
            for (int k = length - j; k < length; k++) {
                GoalColumnName = Arrays.copyOf(GoalColumnName, G + j);
                GoalColumnType = Arrays.copyOf(GoalColumnType, GoalColumnType.length + j);
                GoalisNullable = Arrays.copyOf(GoalisNullable, GoalisNullable.length + j);
                GoalisPk = Arrays.copyOf(GoalisPk, GoalisPk.length + j);
                GoalFieldDefault = Arrays.copyOf(GoalFieldDefault, GoalFieldDefault.length + j);
            }
        } else if (s < G) {
            for (int k = length - j; k < length; k++) {
                sourceColumnName = Arrays.copyOf(sourceColumnName, s + j);
                SourceColumnType = Arrays.copyOf(SourceColumnType, SourceColumnType.length + j);
                SourceColumnSentence = Arrays.copyOf(SourceColumnSentence, SourceColumnSentence.length + j);
            }

        }

        for (int i = 0; i < length; i++) {
            if (SourceColumnSentence[i] == null || sourceColumnName == null || SourceColumnType == null) {
                continue;
            }
            MapInfoDetail mapInfoDetail = new MapInfoDetail();
            mapInfoDetail.setMapInfoId(mapInfoId);
            mapInfoDetail.setColumnNo(Long.valueOf(i + 1));
            if (StringUtils.hasText(SourceColumnSentence[i])) {
                mapInfoDetail.setSourceFieldName(SourceColumnSentence[i].split(" ")[SourceColumnSentence[i].split(" ").length - 1]);
            } else {
                mapInfoDetail.setSourceFieldName(sourceColumnName[i]);
            }
            mapInfoDetail.setSourceFieldType(SourceColumnType[i]);
            if (StringUtils.hasText(SourceColumnSentence[i])) {
                mapInfoDetail.setSourceFieldSentence(SourceColumnSentence[i]);
            } else {
                mapInfoDetail.setSourceFieldSentence(sourceColumnName[i]);
            }
            mapInfoDetail.setDestFieldName(GoalColumnName[i]);
            mapInfoDetail.setDestFieldType(GoalColumnType[i]);
            mapInfoDetail.setIsPk(GoalisPk[i]);
            mapInfoDetail.setIsNull(GoalisNullable[i]);
            mapInfoDetail.setDestFieldDefault(GoalFieldDefault[i]);

            mapinfoDetailMag.saveMapinfoDetails(mapInfoDetail);
        }
    }

/*    private void saveMapinfoDetails(String s_mapinfoId,String sourceSql, String soueceTableName, 
            String goalTableName,String[] sourceColumnName,String[] GoalColumnName,String[] SourceColumnSentence,String[] SourceColumnType,
            String[] GoalColumnType,String[] GoalisNullable,String[] GoalisPk,String[] GoalFieldDefault) {
        String sql = null;
        if (sourceColumnName != null && SourceColumnSentence != null && sourceSql != null) {
            sql = this.createSql(sourceSql, sourceColumnName, SourceColumnSentence,soueceTableName);
        }
        mapinfoDetailMag.deleteMapinfoDetails(Long.valueOf(s_mapinfoId));
        mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(s_mapinfoId), soueceTableName, goalTableName, transferWord(sql));
        int s = sourceColumnName.length;
        int G = GoalColumnName.length;
        
        int length = s > G ? s : G;
        int j = Math.abs(s - G);

        if (s > G) {
            for (int k = length - j; k < length; k++) {
                GoalColumnName = Arrays.copyOf(GoalColumnName, G + j);
                GoalColumnType = Arrays.copyOf(GoalColumnType, GoalColumnType.length + j);
                GoalisNullable = Arrays.copyOf(GoalisNullable, GoalisNullable.length + j);
                GoalisPk = Arrays.copyOf(GoalisPk, GoalisPk.length + j);
                GoalFieldDefault = Arrays.copyOf(GoalFieldDefault, GoalFieldDefault.length + j);
            }
        } else if (s < G) {
            for (int k = length - j; k < length; k++) {
                sourceColumnName = Arrays.copyOf(sourceColumnName, s + j);
                SourceColumnType = Arrays.copyOf(SourceColumnType, SourceColumnType.length + j);
                SourceColumnSentence = Arrays.copyOf(SourceColumnSentence, SourceColumnSentence.length + j);
            }

        }

        for (int i = 0; i < length; i++) {
            MapInfoDetail mapinfoDetail = new MapInfoDetail();
            MapInfoDetailId cid = new MapInfoDetailId();
            cid.setMapInfoId(Long.valueOf(s_mapinfoId));
            cid.setColumnNo(Long.valueOf(i + 1));
            mapinfoDetail.setCid(cid);
            if (StringUtils.hasText(SourceColumnSentence[i])) {
                mapinfoDetail.setSourceFieldName(SourceColumnSentence[i].split(" ")[SourceColumnSentence[i].split(" ").length - 1]);
            } else {
                mapinfoDetail.setSourceFieldName(sourceColumnName[i]);
            }
            mapinfoDetail.setSourceFieldType(SourceColumnType[i]);
            if (StringUtils.hasText(SourceColumnSentence[i])) {
                mapinfoDetail.setSourceFieldSentence(SourceColumnSentence[i]);
            } else {
                mapinfoDetail.setSourceFieldSentence(sourceColumnName[i]);
            }
            mapinfoDetail.setDestFieldName(GoalColumnName[i]);
            mapinfoDetail.setDestFieldType(GoalColumnType[i]);
            mapinfoDetail.setIsPk(GoalisPk[i]);
            mapinfoDetail.setIsNull(GoalisNullable[i]);
            mapinfoDetail.setDestFieldDefault(GoalFieldDefault[i]);
            mapinfoDetailMag.saveObject(mapinfoDetail);
        }
    }*/

    private static String transferWord(String word) {
        String wordTemp = word;
        if (word.contains("''")) {

            return wordTemp;
        } else if (word.contains("'")) {
            wordTemp = word.replace("'", "''");
        }
        return wordTemp;
    }

    private void updateSourceColumnSentence(String sql, String sourceSql, String mapinfoId, String soueceTableName, String goalTableName) {
        Map<String, Object> structs = analysisSql(sql);
        mapinfoDetailMag.updateSourceColumnSentence(structs, mapinfoId);
        mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(mapinfoId), soueceTableName, goalTableName, transferWord(sql));
    }

    private static String createSql(String sourceSql, String[] SourceColumnName, String[] SourceColumnSentence, String soueceTableName) {
        List<String> sqlPieces = new ArrayList<String>();
        if (sourceSql != null) {
            sqlPieces = QueryUtils.splitSqlByFields(sourceSql);
        }

        StringBuffer sql = new StringBuffer();
        if (sourceSql != null) {
            sql.append(sqlPieces.get(0).trim() + " ");
            if (sqlPieces.size() == 4) {
                sql.append(sqlPieces.get(3).trim() + " ");
            }
        } else {
            sql.append("select ");
        }

        for (int i = 0; i < SourceColumnSentence.length; i++) {
            if (StringUtils.hasText(SourceColumnSentence[i])) {
                if (i < SourceColumnSentence.length - 1 && StringUtils.hasText(SourceColumnSentence[i + 1])) {
                    sql.append(SourceColumnSentence[i] + ",");
                } else {
                    sql.append(SourceColumnSentence[i]);
                }
            } else if (StringUtils.hasText(SourceColumnName[i])) {
                sql.append(SourceColumnName[i] + "");
                if (StringUtils.hasText(SourceColumnSentence[i]) && null != SourceColumnSentence[i]) {
                    sql.append(" as ");
                    sql.append(SourceColumnSentence[i]);
                }
                if (i != SourceColumnName.length - 1 && StringUtils.hasText(SourceColumnName[i + 1])) {
                    sql.append(",");
                }
            }
        }

        if (sourceSql != null) {
            sql.append(" from " + sqlPieces.get(2).trim());
        } else {
            sql.append(" from " + soueceTableName);
        }
        return sql.toString();
    }

    private static Map<String, Object> analysisSql(String sql) {
        int nPos = 1;
        List<String> sStrs = new ArrayList<String>();
        List<String> sFieldDescs = new ArrayList<String>();
        List<Object> sStrsAndsFieldDescs = new ArrayList<Object>();
        Map<String, Object> structs = new HashMap<String, Object>();

        List<String> sqlPieces = QueryUtils.splitSqlByFields(sql);
        if (sqlPieces.get(0).contains("with") && sqlPieces.get(0).contains("select")) {
            sql = "select " + sqlPieces.get(1);
        }
        Lexer lex = new Lexer();
        lex.setFormula(sql);
        String sWord = lex.getAWord(false);
        int nLeftBracket = 0;
        if (!"select".equalsIgnoreCase(sWord))
            return structs;
        nPos = lex.getCurrPos();
        int nFieldBegin = nPos;
        sWord = lex.getAWord(false);
        while ((!"".equalsIgnoreCase(sWord)) && !"from".equalsIgnoreCase(sWord)) {

            String sField = sWord;
            nPos = lex.getCurrPos();
            int nFieldEnd = nPos;

            if ("(".equalsIgnoreCase(sWord))
                nLeftBracket++;
            else if (")".equalsIgnoreCase(sWord))
                nLeftBracket--;

            sWord = lex.getAWord(false);
            while ((nLeftBracket > 0)|| ((!",".equalsIgnoreCase(sWord)) && (!"from".equalsIgnoreCase(sWord)))
                    && (!"".equalsIgnoreCase(sWord))) {

                if ((nLeftBracket == 0) && (!"end".equalsIgnoreCase(sWord)))
                    sField = sWord;
                else
                    sField = sField + ' ' + sWord;
                nPos = lex.getCurrPos();
                nFieldEnd = nPos;
                if (sWord.equals("("))
                    nLeftBracket++;
                else if (sWord.equals(")"))
                    nLeftBracket--;
                if (nLeftBracket < 0)
                    return structs;
                sWord = lex.getAWord(false);
            }
            sStrs.add(sField);
            sFieldDescs.add(sql.substring(nFieldBegin, nFieldEnd).trim());
            nPos = lex.getCurrPos();
            nFieldBegin = nPos;
            if (sWord.equals(","))
                sWord = lex.getAWord(false);
        }
        sStrsAndsFieldDescs.add(0, sStrs);
        sStrsAndsFieldDescs.add(1, sFieldDescs);
        structs.put("sStrsAndsFieldDescs", sStrsAndsFieldDescs);
        return structs;
    }

    @RequestMapping(value="/edit" ,method = {RequestMethod.GET})
    public void edit(String s_columnNo,String s_mapinfoId,
            HttpServletRequest request,HttpServletResponse response) {
        try {
            MapInfoDetail object = mapinfoDetailMag.getObjectById(new MapInfoDetail(Long.valueOf(s_mapinfoId),Long.valueOf(s_columnNo)));
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
        
    }

    @RequestMapping(value="/edit_add" ,method = {RequestMethod.GET})
    public void edit_add(String s_columnNo,String s_mapinfoId,
            HttpServletRequest request,HttpServletResponse response) {
        try {
            MapInfoDetail object = mapinfoDetailMag.getObjectById(new MapInfoDetail(Long.valueOf(s_mapinfoId),Long.valueOf(s_columnNo)));
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/delete" ,method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {
        JsonResultUtils.writeSuccessJson(response);
    }
}
