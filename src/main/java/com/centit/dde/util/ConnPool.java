package com.centit.dde.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.centit.dde.po.DatabaseInfo;
import com.centit.support.database.DBType;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnect;
import com.centit.support.database.DbcpConnectPools;

public class ConnPool {
  
    public static DbcpConnect getConn(DatabaseInfo dbInfo) throws SQLException {
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbInfo.getDatabaseCode());
        dbc.setConnUrl(dbInfo.getDatabaseUrl());
        dbc.setUsername(dbInfo.getUsername());
        dbc.setPassword(dbInfo.getClearPassword());        
        return DbcpConnectPools.getDbcpConnect(dbc);
    }
    
    
    public static DbcpConnect getConn(DataSourceDescription dbInfo) throws SQLException {
     
        return DbcpConnectPools.getDbcpConnect(dbInfo);
    }
    

    public static boolean testConn(DatabaseInfo dbinfo){
        DataSourceDescription dbc = new DataSourceDescription();
        dbc.setDatabaseCode(dbinfo.getDatabaseCode());
        dbc.setConnUrl(dbinfo.getDatabaseUrl());
        dbc.setUsername(dbinfo.getUsername());
        dbc.setPassword(dbinfo.getClearPassword());        
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
