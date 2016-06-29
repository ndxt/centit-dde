package com.centit.dde.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.SQLUtils;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.ExchangeMapinfo;
import com.centit.dde.po.MapinfoDetail;
import com.centit.dde.po.MapinfoDetailId;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.dde.service.ExchangeMapinfoManager;
import com.centit.dde.service.MapinfoDetailManager;
import com.centit.support.compiler.Lexer;


public class MapinfoDetailAction extends BaseEntityDwzAction<MapinfoDetail> {
    private static final Log log = LogFactory.getLog(MapinfoDetailAction.class);

    //private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private MapinfoDetailManager mapinfoDetailMag;
    private ExchangeMapinfoManager exchangeMapinfoManager;
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

    public String getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getMapinfoDesc() {
        return mapinfoDesc;
    }

    public void setMapinfoDesc(String mapinfoDesc) {
        this.mapinfoDesc = mapinfoDesc;
    }


    public String getRecordOperate() {
        return recordOperate;
    }

    public void setRecordOperate(String recordOperate) {
        this.recordOperate = recordOperate;
    }

    public String getMapinfoName() {
        return mapinfoName;
    }

    public void setMapinfoName(String mapinfoName) {
        this.mapinfoName = mapinfoName;
    }

    public String getNoInitfirst() {
        return noInitfirst;
    }

    public void setNoInitfirst(String noInitfirst) {
        this.noInitfirst = noInitfirst;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String[] getSourceColumnSentence() {
        return SourceColumnSentence;
    }

    public void setSourceColumnSentence(String[] sourceColumnSentence) {
        SourceColumnSentence = sourceColumnSentence;
    }

    public String[] getGoalFieldDefault() {
        return GoalFieldDefault;
    }

    public void setGoalFieldDefault(String[] goalFieldDefault) {
        GoalFieldDefault = goalFieldDefault;
    }

    public String getS_columnNo() {
        return s_columnNo;
    }

    public void setS_columnNo(String s_columnNo) {
        this.s_columnNo = s_columnNo;
    }

    public String getS_mapinfoId() {
        return s_mapinfoId;
    }

    public void setS_mapinfoId(String s_mapinfoId) {
        this.s_mapinfoId = s_mapinfoId;
    }

    public MapinfoDetailManager getMapinfoDetailMag() {
        return mapinfoDetailMag;
    }

    public void setMapinfoDetailMag(MapinfoDetailManager mapinfoDetailMag) {
        this.mapinfoDetailMag = mapinfoDetailMag;
    }

    public String[] getSourceColumnType() {
        return SourceColumnType;
    }

    public void setSourceColumnType(String[] sourceColumnType) {
        SourceColumnType = sourceColumnType;
    }

    public String[] getGoalColumnName() {
        return GoalColumnName;
    }

    public void setGoalColumnName(String[] goalColumnName) {
        GoalColumnName = goalColumnName;
    }

    public String[] getGoalColumnType() {
        return GoalColumnType;
    }

    public void setGoalColumnType(String[] goalColumnType) {
        GoalColumnType = goalColumnType;
    }

    public String[] getGoalisPk() {
        return GoalisPk;
    }

    public void setGoalisPk(String[] goalisPk) {
        GoalisPk = goalisPk;
    }

    public String[] getGoalisNullable() {
        return GoalisNullable;
    }

    public void setGoalisNullable(String[] goalisNullable) {
        GoalisNullable = goalisNullable;
    }

    public String[] getSourceColumnName() {
        return sourceColumnName;
    }

    public void setSourceColumnName(String[] sourceColumnName) {
        this.sourceColumnName = sourceColumnName;
    }

    public DatabaseInfoManager getDatabaseInfoManager() {
        return databaseInfoManager;
    }

    public void setDatabaseInfoManager(DatabaseInfoManager databaseInfoManager) {
        this.databaseInfoManager = databaseInfoManager;
    }

    public ExchangeMapinfoManager getExchangeMapinfoManager() {
        return exchangeMapinfoManager;
    }

    public void setExchangeMapinfoManager(
            ExchangeMapinfoManager exchangeMapinfoManager) {
        this.exchangeMapinfoManager = exchangeMapinfoManager;
    }

    public void setMapinfoDetailManager(MapinfoDetailManager basemgr) {
        mapinfoDetailMag = basemgr;
        this.setBaseEntityManager(mapinfoDetailMag);
    }

    public String showMapinfoDetail() {
        String soueceTableName = null;
        String goalTableName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(filterMap.get("mapinfoId"))));

        exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);
        //TODO 这个地方逻辑混乱，需要重新整理 codefan@sina.com
        if (exchangeMapinfo == null)
            return ERROR;

        if (exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }
        if ((String) filterMap.get("soueceTableName") != null) {
            soueceTableName = (String) filterMap.get("soueceTableName");
        }
        if ((String) filterMap.get("goalTableName") != null) {
            goalTableName = (String) filterMap.get("goalTableName");
        }

        if (filterMap.get("method") != null && filterMap.get("method").equals("save")) {
            if (exchangeMapinfo == null) {
                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, exchangeMapinfo.getSourceDatabaseName(), exchangeMapinfo.getDestDatabaseName(), String.valueOf(filterMap.get("mapinfoId")), String.valueOf(filterMap.get("type")));
            } else if (StringUtils.hasText(exchangeMapinfo.getQuerySql())) {
                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(), soueceTableName, goalTableName, exchangeMapinfo.getSourceDatabaseName(), exchangeMapinfo.getDestDatabaseName(), String.valueOf(filterMap.get("mapinfoId")), String.valueOf(filterMap.get("type")));
            }

        }
        if (filterMap.get("method") != null && filterMap.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) filterMap.get("querySql"))) {
                this.updateSourceColumnSentence((String) filterMap.get("querySql"), (String) filterMap.get("querySqlsource"), String.valueOf(filterMap.get("mapinfoId")), soueceTableName, goalTableName);
            }
        }

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());
        
       /* sourceTableStruct = */
        //从数据库表中读取表结构
        sourceTableStruct = mapinfoDetailMag.getSourceTableStructFromDatabase(Long.valueOf(s_mapinfoId));
        if (sourceTableStruct.size() < 1 && sourceDatabaseInfo != null && StringUtils.hasText(soueceTableName) && (((String) filterMap.get("type")).equals("reinitsource"))) {
            //初始化表结构
            sourceTableStruct = mapinfoDetailMag.getSourceTableStruct(sourceDatabaseInfo, soueceTableName);
        }

        goalTableStruct = mapinfoDetailMag.getGoalTableStructFromDatabase(Long.valueOf(s_mapinfoId));

        if (goalTableStruct.size() < 1 && goalDatabaseInfo != null && StringUtils.hasText(goalTableName) && (((String) filterMap.get("type")).equals("reinitgoal"))) {
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

        if (((String) filterMap.get("type")).equals("initcopy4")) {
            ServletActionContext.getContext().put("h_mapinfoId",
                    mapinfoDetailMag.getMapinfoId());
        }
        ServletActionContext.getContext().put("SOURCETABLESTRUCT", sourceTableStruct);
        ServletActionContext.getContext().put("GOALTABLESTRUCT", goalTableStruct);
        ServletActionContext.getContext().put("TABLESTRUCT", TableStruct);
        ServletActionContext.getContext().put("LENGTH", length);
        ServletActionContext.getContext().put("s_soueceTableName", soueceTableName);
        if (sourceDatabaseInfo != null && sourceDatabaseInfo.getDatabaseName() != null && StringUtils.hasText(sourceDatabaseInfo.getDatabaseName())) {
            ServletActionContext.getContext().put("s_sourceDatabaseName", sourceDatabaseInfo.getDatabaseName());
        }
        ServletActionContext.getContext().put("s_goalTableName", goalTableName);
        if (goalDatabaseInfo != null && StringUtils.hasText(goalDatabaseInfo.getDatabaseName())) {
            ServletActionContext.getContext().put("s_goalDatabaseName", goalDatabaseInfo.getDatabaseName());
        }
        if (exchangeMapinfo != null) {
            ServletActionContext.getContext().put("mapinfoName", exchangeMapinfo.getMapinfoName());
            ServletActionContext.getContext().put("isRepeat", exchangeMapinfo.getIsRepeat());
            ServletActionContext.getContext().put("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            /*ServletActionContext.getContext().put("tableOperate", exchangeMapinfo.getTableOperate());*/
            ServletActionContext.getContext().put("recordOperate", exchangeMapinfo.getRecordOperate());
        }
        resetPageParam(paramMap);
        if (((String) filterMap.get("type")).equals("initinner")) {
            return "mapInfo4Details";
        } else if (((String) filterMap.get("type")).equals("initcopy")) {
            return "copyMapinfoDetail";
        } else if (((String) filterMap.get("type")).equals("initcopy4")) {
            return "copyMapInfo4Details";
        } else {
            return "showMapinfoDetail";
        }
    }

    public String addAndsaveMapinfoDatails() {
        String soueceTableName = null;
        String goalTableName = null;
        String sourcedatabaseName = null;
        String goaldatabaseName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        if ((String) filterMap.get("mapinfoId") != null) {
            exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(filterMap.get("mapinfoId"))));
            exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);
        }

        if (exchangeMapinfo != null && exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }

        if ((String) filterMap.get("sourcedatabaseName") != null) {
            sourcedatabaseName = (String) filterMap.get("sourcedatabaseName");
        }
        if ((String) filterMap.get("goaldatabaseName") != null) {
            goaldatabaseName = (String) filterMap.get("goaldatabaseName");
        }

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

        if ((String) filterMap.get("soueceTableName") != null) {
            soueceTableName = (String) filterMap.get("soueceTableName");
        }
        if ((String) filterMap.get("goalTableName") != null) {
            goalTableName = (String) filterMap.get("goalTableName");
        }

        if (filterMap.get("method") != null && filterMap.get("method").equals("save")) {
            if (exchangeMapinfo == null) {
                this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(filterMap.get("mapinfoId")), String.valueOf(filterMap.get("type")));
            } else if (StringUtils.hasText(exchangeMapinfo.getQuerySql())) {
                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(), soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(filterMap.get("mapinfoId")), String.valueOf(filterMap.get("type")));
            }
        }
        if (filterMap.get("method") != null && filterMap.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) filterMap.get("querySql"))) {
                this.updateSourceColumnSentence((String) filterMap.get("querySql"), (String) filterMap.get("querySqlsource"), String.valueOf(filterMap.get("mapinfoId")), soueceTableName, goalTableName);
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
                    && (((String) filterMap.get("type")).equals("reinitsource") || mapinfoDetailMag
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
                    && (((String) filterMap.get("type")).equals("reinitgoal") || mapinfoDetailMag
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
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (((String) filterMap.get("type")).equals("initfirst")) {
                ServletActionContext.getContext().put("s_mapinfoId",
                        mapinfoDetailMag.getMapinfoId());
            }
        }
        ServletActionContext.getContext().put("SOURCETABLESTRUCT", sourceTableStruct);
        ServletActionContext.getContext().put("GOALTABLESTRUCT", goalTableStruct);
        ServletActionContext.getContext().put("TABLESTRUCT", TableStruct);
        ServletActionContext.getContext().put("LENGTH", length);
        ServletActionContext.getContext().put("s_soueceTableName", soueceTableName);
        ServletActionContext.getContext().put("s_goalTableName", goalTableName);
        ServletActionContext.getContext().put("s_sourcedatabaseName", sourcedatabaseName);
        ServletActionContext.getContext().put("s_goaldatabaseName", goaldatabaseName);
        if (exchangeMapinfo != null) {
            ServletActionContext.getContext().put("mapinfoName", exchangeMapinfo.getMapinfoName());
            ServletActionContext.getContext().put("isRepeat", exchangeMapinfo.getIsRepeat());
            ServletActionContext.getContext().put("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            /*ServletActionContext.getContext().put("tableOperate", exchangeMapinfo.getTableOperate());*/
            ServletActionContext.getContext().put("recordOperate", exchangeMapinfo.getRecordOperate());
        }
        resetPageParam(paramMap);
        if (((String) filterMap.get("type")).equals("initinner")) {
            return "mapInfo4Details_add";
        } else {
            return "addAndsaveMapinfoDatails";
        }
    }

    public String copyAddAndsaveMapinfoDatails() {
        String soueceTableName = null;
        String goalTableName = null;
        String sourcedatabaseName = null;
        String goaldatabaseName = null;
        Map<Object, Object> paramMap = request.getParameterMap();

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);

        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        if ((String) filterMap.get("mapinfoId") != null) {
            exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(filterMap.get("mapinfoId"))));
            exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);
        }

        if (exchangeMapinfo != null && exchangeMapinfo.getSourceTablename() != null) {
            soueceTableName = exchangeMapinfo.getSourceTablename();
        }
        if (exchangeMapinfo != null && exchangeMapinfo.getDestTablename() != null) {
            goalTableName = exchangeMapinfo.getDestTablename();
        }

        if ((String) filterMap.get("sourcedatabaseName") != null) {
            sourcedatabaseName = (String) filterMap.get("sourcedatabaseName");
        }
        if ((String) filterMap.get("goaldatabaseName") != null) {
            goaldatabaseName = (String) filterMap.get("goaldatabaseName");
        }

        List<Map<String, String>> sourceTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> goalTableStruct = new ArrayList<Map<String, String>>();
        List<Map<String, String>> length = new ArrayList<Map<String, String>>();

        if ((String) filterMap.get("soueceTableName") != null) {
            soueceTableName = (String) filterMap.get("soueceTableName");
        }
        if ((String) filterMap.get("goalTableName") != null) {
            goalTableName = (String) filterMap.get("goalTableName");
        }

//        if(filterMap.get("method")!=null&&filterMap.get("method").equals("save")){
//            if(exchangeMapinfo==null){
        this.saveAndsaveMapinfoDetails(null, soueceTableName, goalTableName, sourcedatabaseName, goaldatabaseName, String.valueOf(filterMap.get("mapinfoId")), String.valueOf(filterMap.get("type")));
//            }else if(StringUtils.hasText(exchangeMapinfo.getQuerySql())){
//                this.saveAndsaveMapinfoDetails(exchangeMapinfo.getQuerySql(),soueceTableName,goalTableName,sourcedatabaseName,goaldatabaseName,String.valueOf(filterMap.get("mapinfoId")),String.valueOf(filterMap.get("type")));                
//            }
//        }       
        if (filterMap.get("method") != null && filterMap.get("method").equals("updateSourceColumnSentence")) {
            if (StringUtils.hasText((String) filterMap.get("querySql"))) {
                this.updateSourceColumnSentence((String) filterMap.get("querySql"), (String) filterMap.get("querySqlsource"), String.valueOf(filterMap.get("mapinfoId")), soueceTableName, goalTableName);
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
                    && (((String) filterMap.get("type")).equals("reinitsource") || mapinfoDetailMag
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
                    && (((String) filterMap.get("type")).equals("reinitgoal") || mapinfoDetailMag
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
        if (sourceTableStruct.size() >= goalTableStruct.size()) {
            length.addAll(sourceTableStruct);
        } else {
            length.addAll(goalTableStruct);
        }
        if (noInitfirst == null || !noInitfirst.equals("true")) {
            if (((String) filterMap.get("type")).equals("initfirst")) {
                ServletActionContext.getContext().put("s_mapinfoId",
                        mapinfoDetailMag.getMapinfoId());
            }
        }
        ServletActionContext.getContext().put("SOURCETABLESTRUCT", sourceTableStruct);
        ServletActionContext.getContext().put("GOALTABLESTRUCT", goalTableStruct);
        ServletActionContext.getContext().put("TABLESTRUCT", TableStruct);
        ServletActionContext.getContext().put("LENGTH", length);
        ServletActionContext.getContext().put("s_soueceTableName", soueceTableName);
        ServletActionContext.getContext().put("s_goalTableName", goalTableName);
        ServletActionContext.getContext().put("s_sourceDatabaseName", sourcedatabaseName);
        ServletActionContext.getContext().put("s_goalDatabaseName", goaldatabaseName);
        if (exchangeMapinfo != null) {
            ServletActionContext.getContext().put("mapinfoName", exchangeMapinfo.getMapinfoName());
            ServletActionContext.getContext().put("isRepeat", exchangeMapinfo.getIsRepeat());
            ServletActionContext.getContext().put("mapinfoDesc", exchangeMapinfo.getMapinfoDesc());
            /*ServletActionContext.getContext().put("tableOperate", exchangeMapinfo.getTableOperate());*/
            ServletActionContext.getContext().put("recordOperate", exchangeMapinfo.getRecordOperate());
        }
        resetPageParam(paramMap);
        return addAndsaveMapinfoDatails();
    }

    public String defSourceData() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(filterMap.get("mapinfoId"))));
        exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());

        List<String> tables = mapinfoDetailMag.getTables(sourceDatabaseInfo, sourceDatabaseInfo.getDatabaseType());
        ServletActionContext.getContext().put("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("SQL", exchangeMapinfo.getQuerySql());
        ServletActionContext.getContext().put("sourceDatabaseName", (String) filterMap.get("sourceDatabaseName"));
        //ServletActionContext.getContext().put("TABLES", tables);
        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());
        return "defSourceData";
    }

    @SuppressWarnings("unchecked")
    public String defSourceData_add() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(s_mapinfoId));
        exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);
        if (exchangeMapinfo != null) {
            ServletActionContext.getContext().put("SQL", exchangeMapinfo.getQuerySql());
        }
        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());

        //database下面所有 的表
        if (filterMap.containsKey("sourcedatabaseName") && StringUtils.hasText((String) filterMap.get("sourcedatabaseName"))) {
            DatabaseInfo databaseInfo = databaseInfoManager.getObjectById((String) filterMap.get("sourcedatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());

            request.setAttribute("tables", tables);
        }

        return "defSourceData_add";
    }

    public String defGoalData_add() {
        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());


        //database下面所有 的表
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        if (filterMap.containsKey("goaldatabaseName") && StringUtils.hasText((String) filterMap.get("goaldatabaseName"))) {
            DatabaseInfo databaseInfo = databaseInfoManager.getObjectById((String) filterMap.get("goaldatabaseName"));
            List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());

            request.setAttribute("tables", tables);
        }

        return "defGoalData_add";
    }

    public String database() {
        DatabaseInfo databaseInfo = new DatabaseInfo();
        databaseInfo = databaseInfoManager.getObjectById(databaseName);
        List<Object> tables = mapinfoDetailMag.getTable(databaseInfo, databaseInfo.getDatabaseType());
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(tables);
        result = jsonArray.toString();
        return "tables";
    }

    public String defDestData() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        ExchangeMapinfo exchangeMapinfo = new ExchangeMapinfo();
        exchangeMapinfo.setMapinfoId(Long.parseLong(String.valueOf(filterMap.get("mapinfoId"))));

        exchangeMapinfo = exchangeMapinfoManager.getObject(exchangeMapinfo);

        DatabaseInfo sourceDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getSourceDatabaseName());
        DatabaseInfo goalDatabaseInfo = databaseInfoManager.getObjectById(exchangeMapinfo.getDestDatabaseName());

        List<String> tables = mapinfoDetailMag.getTables(goalDatabaseInfo, goalDatabaseInfo.getDatabaseType());
        ServletActionContext.getContext().put("GOALURL", goalDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("SOURCEURL", sourceDatabaseInfo.getDatabaseUrl());
        ServletActionContext.getContext().put("goalDatabaseName", (String) filterMap.get("goalDatabaseName"));
        //ServletActionContext.getContext().put("TABLES", tables);
        ServletActionContext.getContext().put("DATABASE", databaseInfoManager.listDatabase());
        return "defDestData";
    }

    public String defDestData_add() {

        return "defSourceData_add";
    }

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

    private String transferWord(String word) {
        String wordTemp = word;
        if (word.contains("''")) {

            return wordTemp;
        } else if (word.contains("'")) {
            wordTemp = word.replace("'", "''");
        }
        return wordTemp;
    }

    private void updateSourceColumnSentence(String sql, String sourceSql, String mapinfoId, String soueceTableName, String goalTableName) {
        Map<String, Object> structs = this.analysisSql(sql);
        mapinfoDetailMag.updateSourceColumnSentence(structs, mapinfoId);
        mapinfoDetailMag.updateExchangeMapinfo(Long.valueOf(mapinfoId), soueceTableName, goalTableName, transferWord(sql));
    }

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

    public String add() {
        return "add";
    }

    public String add_add() {
        return "add_add";
    }

    public String edit() {
        try {
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setColumnNo(Long.valueOf(s_columnNo));
            cid.setMapinfoId(Long.valueOf(s_mapinfoId));
            object = mapinfoDetailMag.getObjectById(cid);
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit_add() {
        try {
            MapinfoDetailId cid = new MapinfoDetailId();
            cid.setColumnNo(Long.valueOf(s_columnNo));
            cid.setMapinfoId(Long.valueOf(s_mapinfoId));
            object = mapinfoDetailMag.getObjectById(cid);
            return "edit_add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {

        return "delete";
    }
}
