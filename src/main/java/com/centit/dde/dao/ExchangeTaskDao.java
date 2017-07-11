package com.centit.dde.dao;

import com.centit.dde.po.ExchangeTask;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.hibernate.dao.BaseDaoImpl;
import com.centit.framework.hibernate.dao.DatabaseOptUtils;
import com.centit.framework.staticsystem.po.DatabaseInfo;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnectPools;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Repository
public class ExchangeTaskDao extends BaseDaoImpl<ExchangeTask,Long> {
    public static final Log log = LogFactory.getLog(ExchangeTaskDao.class);
    @Override
    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<>();

            filterField.put("taskId", CodeBook.EQUAL_HQL_ID);


            filterField.put("taskName", CodeBook.LIKE_HQL_ID);
            filterField.put("taskNameEq", "taskName = ?");

            filterField.put("taskType", CodeBook.LIKE_HQL_ID);

            filterField.put("taskCron", CodeBook.LIKE_HQL_ID);

            filterField.put("taskDesc", CodeBook.LIKE_HQL_ID);

            filterField.put("lastRunTime", CodeBook.LIKE_HQL_ID);

            filterField.put("nextRunTime", CodeBook.LIKE_HQL_ID);

            filterField.put("isvalid", CodeBook.EQUAL_HQL_ID);

            filterField.put("createTime", CodeBook.LIKE_HQL_ID);

            filterField.put("created", CodeBook.LIKE_HQL_ID);

            filterField.put(CodeBook.ORDER_BY_HQL_ID, "createTime desc");

            filterField.put("taskType", CodeBook.EQUAL_HQL_ID);
        }
        return filterField;
    }

    /*public List<Object> getSqlValues(String sql){
        return this.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql).list();
    }
    */
    public List<List<Object>> getSqlValues(DatabaseInfo databaseInfo, String sql) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;

        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(databaseInfo.getDatabaseCode());
        dbc.setConnUrl(databaseInfo.getDatabaseUrl());
        dbc.setUsername(databaseInfo.getUsername());
        dbc.setPassword(databaseInfo.getClearPassword());    
        
        List<List<Object>> datas = new ArrayList<List<Object>>();

        try {
            conn = DbcpConnectPools.getDbcpConnect(dbc);
            pstmt = conn.prepareStatement(sql);
            rsmd = pstmt.getMetaData();
            rs = pstmt.executeQuery();
            int columnCount = rsmd.getColumnCount();

            // 循环结果集
            while (rs.next()) {
                List<Object> dataTemp = new ArrayList<Object>();
                for (int i = 1; i <= columnCount; i++) {
                    dataTemp.add(rs.getObject(i));
                }
                datas.add(dataTemp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    public List<Object> insertDatas(DatabaseInfo databaseInfo, String sql, List<List<Object>> datas) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String errorMessage = "成功添加记录";
        int correctCount = 0;
        List<Object> logs = new ArrayList<Object>();
        List<Object> logTemp = new ArrayList<Object>();

        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(databaseInfo.getDatabaseCode());
        dbc.setConnUrl(databaseInfo.getDatabaseUrl());
        dbc.setUsername(databaseInfo.getUsername());
        dbc.setPassword(databaseInfo.getClearPassword());
        
        try {
            conn = DbcpConnectPools.getDbcpConnect(dbc);
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        logs.add(0, new Date());
        for (int i = 0; i < datas.size(); i++) {
            List<Object> log = new ArrayList<Object>();
            try {
                List<Object> data = datas.get(i);
                for (int j = 1; j <= data.size(); j++) {
                    pstmt.setObject(j, data.get(j - 1));
                }
                pstmt.executeUpdate();
                correctCount++;

            } catch (SQLException e) {
                errorMessage = e.getMessage();
                log.add(0, errorMessage);
                log.add(1, datas.get(i));
            }
            logTemp.add(log);
        }
        logs.add(1, new Date());
        logs.add(2, correctCount);
        logs.add(3, datas.size() - correctCount);
        logs.add(4, logTemp);
        try {
            if (pstmt != null)
                pstmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public String getMapinfoName(Long mapinfoId) {
        String hql = "select t.mapinfoName from ExchangeMapInfo t where t.mapinfoId=" + mapinfoId;
        Object o = DatabaseOptUtils.getSingleObjectByHql(this,hql);
        if (o == null)
            return "";
        return o.toString();

    }

    public Long getNewTaskId() {
        return DatabaseOptUtils.getNextLongSequence(this,"D_TASK_ID");
    }


}
