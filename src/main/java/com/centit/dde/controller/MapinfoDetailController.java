package com.centit.dde.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.po.MapinfoDetailId;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.MapinfoDetailManager;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.common.ResponseData;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.dao.PageDesc;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.support.compiler.Lexer;
import com.sun.istack.Nullable;

@Controller
@RequestMapping("/mapinfodetail")
public class MapinfoDetailController extends BaseController {
    private static final Log log = LogFactory.getLog(MapinfoDetailController.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    @Resource
    @Nullable
    private MapinfoDetailManager mapinfoDetailMag;
    @Resource
    @Nullable
    private ExchangeMapinfoManager exchangeMapinfoManager;
    @Resource
    @Nullable
    private DatabaseInfoManager databaseInfoManager;
    private String[] sourceColumnName;
    private String[] SourceColumnSentence;
    private String[] SourceColumnType;
    private String[] GoalColumnName;
    private String[] GoalColumnType;
    private String[] GoalisPk;
    private String[] GoalisNullable;
    private String[] GoalFieldDefault;
    private String s_mapinfoId;
    private String s_columnNo;
    private String result;
    private String databaseName;
    private String noInitfirst;
    private String isRepeat;
    private String mapinfoDesc;
    private String recordOperate;
    private String mapinfoName;




    @RequestMapping(value="/showMapinfoDetail",method = {RequestMethod.GET})
    public void showMapinfoDetail(PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        String soueceTableName = null;
        String goalTableName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
//        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        Map<String, Object> searchColumn = convertSearchColumn(request);

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));

        exchangeMapinfo = exchangeMapinfoManager.getObjectById(exchangeMapinfo.getMapinfoId());
        //TODO 这个地方逻辑混乱，需要重新整理 codefan@sina.com
        if (exchangeMapinfo == null){
//          return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
            return;
        }

        if (exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }
        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        }

        if (searchColumn.get("method") != null && searchColumn.get("method").equals("save")) {
            if (exchangeMapinfo == null) {
                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, exchangeMapinfo.getSourceDatabaseName(), exchangeMapinfo.getDestDatabaseName(), String.valueOf(searchColumn.get("mapinfoId")), String.valueOf(searchColumn.get("type")));
            } else if (StringUtils.hasText(exchangeMapinfo.getQuerySql())) {
                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(), soueceTableName, goalTableName, exchangeMapinfo.getSourceDatabaseName(), exchangeMapinfo.getDestDatabaseName(), String.valueOf(searchColumn.get("mapinfoId")), String.valueOf(searchColumn.get("type")));
            }

        }
        if (searchColumn.get("method") != null && searchColumn.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) searchColumn.get("querySql"))) {
                this.updateSourceColumnSentence((String) searchColumn.get("querySql"), (String) searchColumn.get("querySqlsource"), String.valueOf(searchColumn.get("mapinfoId")), soueceTableName, goalTableName);
            }
        }

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());
        
       /* sourceTableStruct = */
        //从数据库表中读取表结构
//        sourceTableStruct = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
        JSONArray sts = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
        if (!sts.isEmpty()) {
            //初始化表结构
            sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(sourceDatabaseInfo, soueceTableName);
        }

        JSONArray sts1 = mapinfoDetailMag.getGoalTableStructFromDatabase(Long.valueOf(s_mapinfoId));

        if (!sts1.isEmpty()) {
            //初始化表结构
            goalTableStruct = mapinfoDetailMag.getGoalTableStruct(goalDatabaseInfo, goalTableName);
        }

        List TableStruct = new ArrayList();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }

        ResponseData resData = new ResponseData();
        if (((String) searchColumn.get("type")).equals("initcopy4")) {
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
        if (exchangeMapinfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapinfo.getMapinfoName());
            resData.addResponseData("isRepeat", exchangeMapinfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            resData.addResponseData("recordOperate", exchangeMapinfo.getRecordOperate());
        }
//            resetPageParam(paramMap);
        if (((String) searchColumn.get("type")).equals("initinner")) {
//            return "mapInfo4Details";页面跳转
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else if (((String) searchColumn.get("type")).equals("initcopy")) {
//            return "copyMapinfoDetail";
            JsonResultUtils.writeResponseDataAsJson(resData, response);
            return;
        } else if (((String) searchColumn.get("type")).equals("initcopy4")) {
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
    public void addAndsaveMapinfoDatails(PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        String soueceTableName = null;
        String goalTableName = null;
        String sourcedatabaseName = null;
        String goaldatabaseName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
//        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        Map<String, Object> searchColumn = convertSearchColumn(request);
//        List<ExchangeMapinfo> objList = exchangeMapinfoManager.listObjects(searchColumn, pageDesc);

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        if ((String) searchColumn.get("mapinfoId") != null) {
            exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));
            exchangeMapinfo = exchangeMapinfoManager.getObjectById(exchangeMapinfo.getMapinfoId());
        }

        if (exchangeMapinfo != null && exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }

        if ((String) searchColumn.get("sourcedatabaseName") != null) {
            sourcedatabaseName = (String) searchColumn.get("sourcedatabaseName");
        }
        if ((String) searchColumn.get("goaldatabaseName") != null) {
            goaldatabaseName = (String) searchColumn.get("goaldatabaseName");
        }

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        }

        if (searchColumn.get("method") != null && searchColumn.get("method").equals("save")) {
            if (exchangeMapinfo == null) {
                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(searchColumn.get("mapinfoId")), String.valueOf(searchColumn.get("type")));
            } else if (StringUtils.hasText(exchangeMapinfo.getQuerySql())) {
                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(), soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(searchColumn.get("mapinfoId")), String.valueOf(searchColumn.get("type")));
            }
        }
        if (searchColumn.get("method") != null && searchColumn.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) searchColumn.get("querySql"))) {
                this.updateSourceColumnSentence((String) searchColumn.get("querySql"), (String) searchColumn.get("querySqlsource"), String.valueOf(searchColumn.get("mapinfoId")), soueceTableName, goalTableName);
            }
        }
        DatabaseInfo sourceDatabaseInfo = null;
        DatabaseInfo goalDatabaseInfo = null;

        if (sourcedatabaseName != null) {
            sourceDatabaseInfo = databaseInfoManager.getObjectById(sourcedatabaseName);
        }
        if (goaldatabaseName != null) {
            goalDatabaseInfo = databaseInfoManager.gestObjectById(goaldatabaseName);
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getSourceDatabaseName() != null) {
            sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestDatabaseName() != null) {
            goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());
        }

        if (s_mapinfoId != null) {
            if (sourceDatabaseInfo != null && StringUtils.hasText(soueceTableName) && (((String) searchColumn.get("type")).equals("reinitsource") || mapinfoDetailMag.getSourceTableStructFromDatabase(
                            Long.valueOf(s_mapinfoId)).size() == 0)) {
                // 初始化表结构
                sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(
                        sourceDatabaseInfo, soueceTableName);
            } else if (mapinfoDetailMag.getSourceTableStructFromDatabase(
                    Long.valueOf(s_mapinfoId)).size() != 0) {
                // 从数据库表中读取表结构
                sourceTableStruct = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
            }
            if (goalDatabaseInfo != null
                    && StringUtils.hasText(goalTableName)
                    && (((String) searchColumn.get("type")).equals("reinitgoal") || mapinfoDetailMag
                    .getSourceTableStructFromDatabase(
                            Long.valueOf(s_mapinfoId)).size() == 0)) {
                // 初始化表结构
                goalTableStruct = mapinfoDetailMag.getGoalTableStruct(
                        goalDatabaseInfo, goalTableName);
            } else if (mapinfoDetailMag.getGoalTableStructFromDatabase(
                    Long.valueOf(s_mapinfoId)).size() != 0) {
                // 从数据库表中读取表结构
                goalTableStruct = mapinfoDetailMag
                        .getGoalTableStructFromDatabase(Long
                                .valueOf(s_mapinfoId));
            }
        }

        List TableStruct = new ArrayList();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        ResponseData resData = new ResponseData();
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (((String) searchColumn.get("type")).equals("initfirst")) {
                resData.addResponseData("s_mapinfoId",mapinfoDetailMag.getMapinfoId());
//                ServletActionContext.getContext().put("s_mapinfoId",
//                        mapinfoDetailMag.getMapinfoId());
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
        if (exchangeMapinfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapinfo.getMapinfoName());
            resData.addResponseData("isRepeat", exchangeMapinfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            resData.addResponseData("recordOperate", exchangeMapinfo.getRecordOperate());
            
//            ServletActionContext.getContext().put("mapinfoName", exchangeMapinfo.getMapinfoName());
//            ServletActionContext.getContext().put("isRepeat", exchangeMapinfo.getIsRepeat());
//            ServletActionContext.getContext().put("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
//            /*ServletActionContext.getContext().put("tableOperate", exchangeMapinfo.getTableOperate());*/
//            ServletActionContext.getContext().put("recordOperate", exchangeMapinfo.getRecordOperate());
        }
//        resetPageParam(paramMap);
        if (((String) searchColumn.get("type")).equals("initinner")) {
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
    public void copyAddAndsaveMapinfoDatails(PageDesc pageDesc,HttpServletRequest request,HttpServletResponse response) {
        String soueceTableName = null;
        String goalTableName = null;
        String sourcedatabaseName = null;
        String goaldatabaseName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
//        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        Map<String, Object> searchColumn = convertSearchColumn(request);

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        if ((String) searchColumn.get("mapinfoId") != null) {
            exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));
            exchangeMapinfo = exchangeMapinfoManager.getObjectById(exchangeMapinfo.getMapinfoId());
        }

        if (exchangeMapinfo != null && exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }

        if ((String) searchColumn.get("sourcedatabaseName") != null) {
            sourcedatabaseName = (String) searchColumn.get("sourcedatabaseName");
        }
        if ((String) searchColumn.get("goaldatabaseName") != null) {
            goaldatabaseName = (String) searchColumn.get("goaldatabaseName");
        }

//        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
//        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
//        List<Map<String, String>> length = new ArrayList<Map<String, String>>();
        List<Object> sourceTableStruct = null;
        List<Object> goalTableStruct = null;
        List<Object> length = null;

        if ((String) searchColumn.get("soueceTableName") != null) {
            soueceTableName = (String) searchColumn.get("soueceTableName");
        }
        if ((String) searchColumn.get("goalTableName") != null) {
            goalTableName = (String) searchColumn.get("goalTableName");
        }

//        if(filterMap.get("method")!=null&&filterMap.get("method").equals("save")){
//            if(exchangeMapinfo==null){
        this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(searchColumn.get("mapinfoId")), String.valueOf(searchColumn.get("type")));
//            }else if(StringUtils.hasText(exchangeMapinfo.getQuerySql())){
//                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(),soueceTableName,goalTableName,sourcedatabaseName,goaldatabaseName,String.valueOf(filterMap.get("mapinfoId")),String.valueOf(filterMap.get("type")));                
//            }
//        }       
        if (searchColumn.get("method") != null && searchColumn.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) searchColumn.get("querySql"))) {
                this.updateSourceColumnSentence((String) searchColumn.get("querySql"), (String) searchColumn.get("querySqlsource"), String.valueOf(searchColumn.get("mapinfoId")), soueceTableName, goalTableName);
            }
        }
        DatabaseInfo sourceDatabaseInfo = null;
        DatabaseInfo goalDatabaseInfo = null;

        if (sourcedatabaseName != null) {
            sourceDatabaseInfo = databaseInfoManager.getObjectById(sourcedatabaseName);
        }
        if (goaldatabaseName != null) {
            goalDatabaseInfo = databaseInfoManager.getObjectById(goaldatabaseName);
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getSourceDatabaseName() != null) {
            sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestDatabaseName() != null) {
            goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());
        }

        if (s_mapinfoId != null) {

            if (sourceDatabaseInfo != null
                    && StringUtils.hasText(soueceTableName)
                    && (((String) searchColumn.get("type")).equals("reinitsource") || mapinfoDetailMag
                    .getSourceTableStructFromDatabase(
                            Long.valueOf(s_mapinfoId)).size() == 0)) {
                // 初始化表结构
                sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(
                        sourceDatabaseInfo, soueceTableName);
            } else if (mapinfoDetailMag.getSourceTableStructFromDatabase(
                    Long.valueOf(s_mapinfoId)).size() != 0) {
                // 从数据库表中读取表结构
                sourceTableStruct = mapinfoDetailMag
                        .getSourceTableStructFromDatabase(Long
                                .valueOf(s_mapinfoId));
            }
            if (goalDatabaseInfo != null
                    && StringUtils.hasText(goalTableName)
                    && (((String) searchColumn.get("type")).equals("reinitgoal") || mapinfoDetailMag
                    .getSourceTableStructFromDatabase(
                            Long.valueOf(s_mapinfoId)).size() == 0)) {
                // 初始化表结构
                goalTableStruct = mapinfoDetailMag.getGoalTableStruct(
                        goalDatabaseInfo, goalTableName);
            } else if (mapinfoDetailMag.getGoalTableStructFromDatabase(
                    Long.valueOf(s_mapinfoId)).size() != 0) {
                // 从数据库表中读取表结构
                goalTableStruct = mapinfoDetailMag
                        .getGoalTableStructFromDatabase(Long
                                .valueOf(s_mapinfoId));
            }
        }

        List TableStruct = new ArrayList();
        TableStruct.add(sourceTableStruct);
        TableStruct.add(goalTableStruct);
        ResponseData resData = new ResponseData();
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (((String) searchColumn.get("type")).equals("initfirst")) {
                resData.addResponseData("s_mapinfoId",mapinfoDetailMag.getMapinfoId());
//                ServletActionContext.getContext().put("s_mapinfoId",
//                        mapinfoDetailMag.getMapinfoId());
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
        resData.addResponseData("PAGE_DESC", pageDesc);
        resData.addResponseData("PAGE_DESC", pageDesc);
        resData.addResponseData("PAGE_DESC", pageDesc);
        resData.addResponseData("PAGE_DESC", pageDesc);
        resData.addResponseData("PAGE_DESC", pageDesc);
//        ServletActionContext.getContext().put("SOURCETABLESTRUCT", sourceTableStruct);
//        ServletActionContext.getContext().put("GOALTABLESTRUCT", goalTableStruct);
//        ServletActionContext.getContext().put("TABLESTRUCT", TableStruct);
//        ServletActionContext.getContext().put("LENGTH", length);
//        ServletActionContext.getContext().put("s_soueceTableName", soueceTableName);
//        ServletActionContext.getContext().put("s_goalTableName", goalTableName);
//        ServletActionContext.getContext().put("s_sourceDatabaseName", sourcedatabaseName);
//        ServletActionContext.getContext().put("s_goalDatabaseName", goaldatabaseName);
        if (exchangeMapinfo != null) {
            resData.addResponseData("mapinfoName", exchangeMapinfo.getMapinfoName());
            resData.addResponseData("isRepeat", exchangeMapinfo.getIsRepeat());
            resData.addResponseData("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            resData.addResponseData("recordOperate", exchangeMapinfo.getRecordOperate());
//            ServletActionContext.getContext().put("mapinfoName", exchangeMapinfo.getMapinfoName());
//            ServletActionContext.getContext().put("isRepeat", exchangeMapinfo.getIsRepeat());
//            ServletActionContext.getContext().put("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
//            /*ServletActionContext.getContext().put("tableOperate", exchangeMapinfo.getTableOperate());*/
//            ServletActionContext.getContext().put("recordOperate", exchangeMapinfo.getRecordOperate());
        }
//        resetPageParam(paramMap);
//        addAndsaveMapinfoDatails();
//        return addAndsaveMapinfoDatails();
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/defSourceData",method = {RequestMethod.GET})
    public void defSourceData(HttpServletRequest request, HttpServletResponse response) {
//        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));
        exchangeMapinfo = exchangeMapinfoManager.getObjectById(exchangeMapinfo.getMapinfoId());

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());

        List<String> tables = mapinfoDetailMag.getTables(sourceDatabaseInfo, sourceDatabaseInfo.getDatabaseType());
//        ServletActionContext.getContext().put("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
//        ServletActionContext.getContext().put("GOALURL", goalDatabaseInfo.getDatabaseUrl());
//        ServletActionContext.getContext().put("SQL", exchangeMapinfo.getQuerySql());
//        ServletActionContext.getContext().put("sourceDatabaseName", (String) filterMap.get("sourceDatabaseName"));
//        //ServletActionContext.getContext().put("TABLES", tables);
//        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());
//        return "defSourceData";
        ResponseData resData = new ResponseData();
        resData.addResponseData("OBJLIST", sourceDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("SQL", exchangeMapinfo.getQuerySql());
        resData.addResponseData("sourceDatabaseName", (String) searchColumn.get("sourceDatabaseName"));
        resData.addResponseData("DATABASE", databaseInfoManager.listDatabase());
        JsonResultUtils.writeResponseDataAsJson(resData, response);
s    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value="/defSourceData_add",method = {RequestMethod.GET})
    public void defSourceData_add(HttpServletRequest request,HttpServletResponse response) {
        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);
//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(s_mapinfoId));
        ResponseData resData = new ResponseData();
        exchangeMapinfo = exchangeMapinfoManager.getObjectById(exchangeMapinfo.getMapinfoId());
        if (exchangeMapinfo != null) {
            resData.addResponseData("SQL", exchangeMapinfo.getQuerySql());
//            ServletActionContext.getContext().put("SQL", exchangeMapinfo.getQuerySql());
        }
        resData.addResponseData("DATABASE", databaseInfoManager.listDatabase());
//        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());

        //database下面所有 的表
        if (searchColumn.containsKey("sourcedatabaseName") && StringUtils.hasText((String) searchColumn.get("sourcedatabaseName"))) {
            DatabaseInfo databaseInfo = databaseInfoManager.getObjectById((String) searchColumn.get("sourcedatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());

            resData.addResponseData("tables", tables);
//            request.setAttribute("tables", tables);
        }

//        return "defSourceData_add";
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/defGoalData_add",method = {RequestMethod.GET})
    public void defGoalData_add(HttpServletRequest request,HttpServletResponse response) {
        ResponseData resData = new ResponseData();
        resData.addResponseData("DATABASE", databaseInfoManager.listDatabase());
//        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());

        //database下面所有 的表
//        Map<Object, Object> paramMap = request.getParameterMap();
//        resetPageParam(paramMap);

//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        if (searchColumn.containsKey("goaldatabaseName") && StringUtils.hasText((String) searchColumn.get("goaldatabaseName"))) {
            DatabaseInfo databaseInfo = databaseInfoManager.getObjectById((String) searchColumn.get("goaldatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());

            resData.addResponseData("tables", tables);
//            request.setAttribute("tables", tables);
        }

//        return "defGoalData_add";
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

    @RequestMapping(value="/database",method = {RequestMethod.GET})
    public void database(HttpServletRequest request,HttpServletResponse response) {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo = databaseInfoManager.getObjectById(databaseName);
        List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(tables);
        result = jsonArray.toString();
        JsonResultUtils.writeSingleDataJson(result, response);
//        return "tables";
    }

    @RequestMapping(value="/defDestData",method = {RequestMethod.GET})
    public void defDestData(HttpServletRequest request,HttpServletResponse response) {
//        Map<Object, Object> paramMap = request.getParameterMap();
////        resetPageParam(paramMap);
//
//        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        Map<String, Object> searchColumn = convertSearchColumn(request);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(searchColumn.get("mapinfoId"))));

        exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());

        ResponseData resData = new ResponseData();
        List<String> tables = mapinfoDetailMag.getTables(goalDatabaseInfo, goalDatabaseInfo.getDatabaseType());
        resData.addResponseData("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
        resData.addResponseData("goalDatabaseName", (String) searchColumn.get("goalDatabaseName"));
        resData.addResponseData("DATABASE", databaseInfoManager.listDatabase());
        
        /*ServletActionContext.getContext().put("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("goalDatabaseName", (String) filterMap.get("goalDatabaseName"));
        //ServletActionContext.getContext().put("TABLES", tables);
        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());*/
//        return "defDestData";
        JsonResultUtils.writeResponseDataAsJson(resData, response);
    }

//    页面 跳转
//    @RequestMapping(value="/defDestData_add")
//    public String defDestData_add() {
//
//        return "defSourceData_add";
//    }
    

    @RequestMapping(value="/saveAndsaveMapinfoDetails",method = {RequestMethod.PUT})
    private void saveAndsaveMapinfoDetails(String sourceSql, String soueceTableName, String goalTableName, String sourcedatabaseName, String goaldatabaseName, String mapInfoId, String type) {
        String sql = null;
        if (sourceColumnName != null && SourceColumnSentence != null) {
            sql = this.createSql(sourceSql, sourceColumnName, SourceColumnSentence,
                    soueceTableName);
        }

        mapinfoDetailMag.deleteMapinfoDetails(Long.valueOf(mapInfoId));
        //新增时直接保存无问题，新增时再新增字段会直接调用更新方法，报错
        if (type.equals("save") || null == mapinfoDetailMag.getObjectById(Long.valueOf(mapInfoId))) {
            exchangeMapinfoManager.deleteObjectById(Long.valueOf(mapInfoId));
            ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
            exchangeMapinfo.setMapinfoId(Long.valueOf(mapInfoId));
            exchangeMapinfo.setSourceDatabaseName(sourcedatabaseName);
            exchangeMapinfo.setSourceTablename(soueceTableName);
            exchangeMapinfo.setMapinfoName(mapinfoName);
            exchangeMapinfo.setIsRepeat(isRepeat);
            exchangeMapinfo.setMapinfoDesc(mapinfoDesc);
            /*exchangeMapinfo.setTableOperate(tableOperate);*/
            exchangeMapinfo.setRecordOperate(recordOperate);
            exchangeMapinfo.setQuerySql(sql);
            exchangeMapinfo.setDestDatabaseName(goaldatabaseName);
            exchangeMapinfo.setDestTablename(goalTableName);
            exchangeMapinfoManager.saveObject(exchangeMapinfo);
        } else {
            mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(mapInfoId), soueceTableName, goalTableName, transferWord(sql));
        }


        int length = sourceColumnName.length > GoalColumnName.length ? sourceColumnName.length : GoalColumnName.length;
        int j = Math.abs(sourceColumnName.length - GoalColumnName.length);

        if (sourceColumnName.length > GoalColumnName.length) {
            for (int k = length - j; k < length; k++) {
                GoalColumnName = Arrays.copyOf(GoalColumnName, GoalColumnName.length + j);
                GoalColumnType = Arrays.copyOf(GoalColumnType, GoalColumnType.length + j);
                GoalisNullable = Arrays.copyOf(GoalisNullable, GoalisNullable.length + j);
                GoalisPk = Arrays.copyOf(GoalisPk, GoalisPk.length + j);
                GoalFieldDefault = Arrays.copyOf(GoalFieldDefault, GoalFieldDefault.length + j);
            }
        } else if (sourceColumnName.length < GoalColumnName.length) {
            for (int k = length - j; k < length; k++) {
                sourceColumnName = Arrays.copyOf(sourceColumnName, sourceColumnName.length + j);
                SourceColumnType = Arrays.copyOf(SourceColumnType, SourceColumnType.length + j);
                SourceColumnSentence = Arrays.copyOf(SourceColumnSentence, SourceColumnSentence.length + j);
            }

        }

        for (int i = 0; i < length; i++) {
            if (SourceColumnSentence[i] == null || sourceColumnName == null || SourceColumnType == null) {
                continue;
            }
            MapinfoDetail mapinfoDetail = new MapinfoDetail();
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setMapinfoId(Long.valueOf(mapInfoId));
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

            mapinfoDetailMag.saveMapinfoDetails(mapinfoDetail);
        }
    }

    @RequestMapping(value="/saveMapinfoDetails" ,method = {RequestMethod.PUT} )
    private void saveMapinfoDetails(String sourceSql, String soueceTableName, String goalTableName) {
        String sql = null;
        if (sourceColumnName != null && SourceColumnSentence != null && sourceSql != null) {
            sql = this.createSql(sourceSql, sourceColumnName, SourceColumnSentence,
                    soueceTableName);
        }
        mapinfoDetailMag.deleteMapinfoDetails(Long.valueOf(s_mapinfoId));
        mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(s_mapinfoId), soueceTableName, goalTableName, transferWord(sql));
        int length = sourceColumnName.length > GoalColumnName.length ? sourceColumnName.length : GoalColumnName.length;
        int j = Math.abs(sourceColumnName.length - GoalColumnName.length);

        if (sourceColumnName.length > GoalColumnName.length) {
            for (int k = length - j; k < length; k++) {
                GoalColumnName = Arrays.copyOf(GoalColumnName, GoalColumnName.length + j);
                GoalColumnType = Arrays.copyOf(GoalColumnType, GoalColumnType.length + j);
                GoalisNullable = Arrays.copyOf(GoalisNullable, GoalisNullable.length + j);
                GoalisPk = Arrays.copyOf(GoalisPk, GoalisPk.length + j);
                GoalFieldDefault = Arrays.copyOf(GoalFieldDefault, GoalFieldDefault.length + j);
            }
        } else if (sourceColumnName.length < GoalColumnName.length) {
            for (int k = length - j; k < length; k++) {
                sourceColumnName = Arrays.copyOf(sourceColumnName, sourceColumnName.length + j);
                SourceColumnType = Arrays.copyOf(SourceColumnType, SourceColumnType.length + j);
                SourceColumnSentence = Arrays.copyOf(SourceColumnSentence, SourceColumnSentence.length + j);
            }

        }

        for (int i = 0; i < length; i++) {
            MapinfoDetail mapinfoDetail = new MapinfoDetail();
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setMapinfoId(Long.valueOf(s_mapinfoId));
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
    }

    @RequestMapping(value="/transferWord")
    private String transferWord(String word) {
        String wordTemp = word;
        if (word.contains("''")) {

            return wordTemp;
        } else if (word.contains("'")) {
            wordTemp = word.replace("'", "''");
        }
        return wordTemp;
    }

    @RequestMapping(value="/updateSourceColumnSentence")
    private void updateSourceColumnSentence(String sql, String sourceSql, String mapinfoId, String soueceTableName, String goalTableName) {
        Map<String, Object> structs = this.analysisSql(sql);
        mapinfoDetailMag.updateSourceColumnSentence(structs, mapinfoId);
        mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(mapinfoId), soueceTableName, goalTableName, transferWord(sql));
    }

    @RequestMapping(value="/createSql")
    private static String createSql(String sourceSql, String[] SourceColumnName, String[] SourceColumnSentence, String soueceTableName) {
        List<String> sqlPieces = new ArrayList<String>();
        if (sourceSql != null) {
            sqlPieces = SQLUtils.splitSqlByFields(sourceSql);
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

    @RequestMapping(value="/analysisSql")
    private static Map<String, Object> analysisSql(String sql) {
        int nPos = 1;
        List<String> sStrs = new ArrayList<String>();
        List<String> sFieldDescs = new ArrayList<String>();
        List<Object> sStrsAndsFieldDescs = new ArrayList<Object>();
        Map<String, Object> structs = new HashMap<String, Object>();

        List<String> sqlPieces = SQLUtils.splitSqlByFields(sql);
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
            while ((nLeftBracket > 0)
                    || ((!",".equalsIgnoreCase(sWord)) && (!"from"
                    .equalsIgnoreCase(sWord)))
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
//    @RequestMapping(value="/add")
//    public String add() {
//        return "add";
//    }

//    @RequestMapping(value="/add_add")
//    public String add_add() {
//        return "add_add";
//    }

    @RequestMapping(value="/edit" ,method = {RequestMethod.GET})
    public void edit(HttpServletRequest request,HttpServletResponse response) {
        try {
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setColumnNo(Long.valueOf(s_columnNo));
            cid.setMapinfoId(Long.valueOf(s_mapinfoId));
            object = mapinfoDetailMag.getObjectById(cid);
//            return EDIT;
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
        
    }

    @RequestMapping(value="/edit_add" ,method = {RequestMethod.GET})
    public void edit_add(HttpServletRequest request,HttpServletResponse response) {
        try {
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setColumnNo(Long.valueOf(s_columnNo));
            cid.setMapinfoId(Long.valueOf(s_mapinfoId));
            object = mapinfoDetailMag.getObjectById(cid);
//            return "edit_add";
            JsonResultUtils.writeSingleDataJson(object, response);
        } catch (Exception e) {
            e.printStackTrace();
//            return ERROR;
            JsonResultUtils.writeErrorMessageJson("error", response);
        }
    }

    @RequestMapping(value="/delete" ,method = {RequestMethod.DELETE})
    public void delete(HttpServletRequest request,HttpServletResponse response) {

//        return "delete";

        JsonResultUtils.writeSuccessJson(response);
    }
}
