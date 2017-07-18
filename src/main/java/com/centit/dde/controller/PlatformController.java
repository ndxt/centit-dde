package com.centit.dde.controller;

import com.centit.dde.util.ConnPool;
import com.centit.dde.util.SQLUtils;
import com.centit.framework.core.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.framework.staticsystem.po.OsInfo;
import com.centit.framework.staticsystem.service.IntegrationEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/platform")
public class PlatformController extends BaseController {
    private static final Log log = LogFactory.getLog(ExchangeMapInfoController.class);

    @Resource
    protected IntegrationEnvironment integrationEnvironment;

    /**
     * 获取业务系统列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "/listOs", method = RequestMethod.GET)
    public void listOs(HttpServletRequest request, HttpServletResponse response) {
        List<OsInfo> dbList = integrationEnvironment.listOsInfos();
        JsonResultUtils.writeSingleDataJson(dbList, response);
    }

    /**
     * 获取数据库列表
     * @param request
     * @param response
     */
    @RequestMapping(value="/listDb" ,method = {RequestMethod.GET})
    public void listDb(HttpServletRequest request,HttpServletResponse response) {

        List<DatabaseInfo> dbList = integrationEnvironment.listDatabaseInfo();
        JsonResultUtils.writeSingleDataJson(dbList, response);
    }

    /**
     * 获取数据库中表
     * @param databaseCode
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/listTable/{databaseCode}" ,method = {RequestMethod.GET})
    public void listTable(@PathVariable String databaseCode, HttpServletResponse response) {
        List<Map<String, String>> tableList = new ArrayList();
        Connection connection = null;
        if(databaseCode != null && !"".equals(databaseCode)){
            try {
                DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
                if (databaseInfo != null) {
                    connection = ConnPool.getConn(databaseInfo);
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet tables = metaData.getTables(null, databaseInfo.getUsername().toUpperCase(), null, new String[]{"TABLE"});
                    while (tables.next()) {
                        Map map = new HashMap();
                        map.put("tableName", tables.getString("Table_NAME"));
                        tableList.add(map);
                    }
                }
            }catch(Exception e){
                log.error("获取数据库表格出错");
            }finally{
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("关闭连接出错");
                }
            }
        }
        JsonResultUtils.writeSingleDataJson(tableList, response);
    }

    private List<Map<String, String>> listField(String databaseCode, String tableName) {

        List<Map<String, String>> fieldList = new ArrayList<>();
        Connection connection = null;
        if(databaseCode != null && !"".equals(databaseCode)) {
            DatabaseInfo databaseInfo = integrationEnvironment.getDatabaseInfo(databaseCode);
            if (databaseInfo != null) {
                try {
                    connection = ConnPool.getConn(databaseInfo);
                    DatabaseMetaData metaData = connection.getMetaData();
                    ResultSet resultSet = metaData.getColumns(null, databaseInfo.getUsername().toUpperCase(), tableName, null);
                    while (resultSet.next()) {
                        Map<String, String> map = new HashMap<>();
                        map.put("fieldName", resultSet.getString("COLUMN_NAME"));
                        map.put("fieldType", resultSet.getString("TYPE_NAME"));
                        fieldList.add(map);
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        connection.close();
                    } catch (SQLException e) {

                    }
                }
            }
        }
        return fieldList;
    }

    /**
     * 生成SQL
     * @param databaseCode
     * @param tableName
     * @param response
     */
    @RequestMapping(value="/generateSQL/{databaseCode}/{tableName}", method = RequestMethod.GET)
    public void generateSQL(@PathVariable String databaseCode,
                            @PathVariable String tableName,
                            HttpServletResponse response) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        List<Map<String, String>> fields = listField(databaseCode, tableName);
        for(Map<String, String> field : fields) {
            sql.append(field.get("fieldName"));
            sql.append(",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" FROM ");
        sql.append(tableName);
        JsonResultUtils.writeSingleDataJson(sql, response);
    }

    @RequestMapping(value="/listFields/{sql}", method = RequestMethod.GET)
    public void list(@PathVariable String sql, HttpServletResponse response){
        JsonResultUtils.writeSingleDataJson(SQLUtils.splitSqlByFields(sql),response);
    }

}
