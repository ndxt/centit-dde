package com.centit.dde.util;

import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.support.database.utils.DBType;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnPool {

    public static DataSource getDataSource(DatabaseInfo dbInfo) throws SQLException {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbInfo.getDatabaseCode());
        dbc.setConnUrl(dbInfo.getDatabaseUrl());
        dbc.setUsername(dbInfo.getUsername());
        dbc.setPassword(dbInfo.getClearPassword());
        return null;//DbcpConnectPools.getDataSource(dbc);
    }

    public static Connection getConn(DatabaseInfo dbInfo) throws SQLException {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbInfo.getDatabaseCode());
        dbc.setConnUrl(dbInfo.getDatabaseUrl());
        dbc.setUsername(dbInfo.getUsername());
        //dbc.setPassword(dbInfo.getClearPassword());
        dbc.setPassword(dbInfo.getPassword());
        return DbcpConnectPools.getDbcpConnect(dbc);
    }
    
    
    public static Connection getConn(DataSourceDescription dbInfo) throws SQLException {
     
        return DbcpConnectPools.getDbcpConnect(dbInfo);
    }
    

    public static boolean testConn(DatabaseInfo dbinfo){
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbinfo.getDatabaseCode());
        dbc.setConnUrl(dbinfo.getDatabaseUrl());
        dbc.setUsername(dbinfo.getUsername());
        //dbc.setPassword(dbinfo.getClearPassword());
        dbc.setPassword(dbinfo.getPassword());
        return DbcpConnectPools.testDataSource(dbc);
    }
    

    public static synchronized void closeConn(Connection conn) {
        try {
            if (null != conn && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            
        }
    }

    public static synchronized void closeAll() {
        DbcpConnectPools.shutdownDataSource();
    }


    public static String getValidationQuery(String dbUrl) {
        DBType dbType = DBType.mapDBType(dbUrl);
        if (DBType.Oracle == dbType) {
            return "select 1 from dual";
        } else if (DBType.SqlServer == dbType || DBType.MySql == dbType) {
            return "select 1";
        }
        return "";
    }
}
