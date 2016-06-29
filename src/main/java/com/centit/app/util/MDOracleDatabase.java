package com.centit.app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.centit.support.database.config.DBConfig;
import com.centit.support.database.metadata.OracleDatabase;


public class MDOracleDatabase extends OracleDatabase implements MDDatabase {

    private final static String sqlGETTABLES = "select table_name from user_tables ";

    private DBConfig dbconfig;

    @Override
    public void setDBConfig(DBConfig dbc) {
        // TODO Auto-generated method stub
        super.setDBConfig(dbc);
        dbconfig = dbc;
    }

    @Override
    public List<String> getAllTableCode() {
        List<String> tabNames = new ArrayList<String>();
        try {
            Connection conn = dbconfig.getConn();
            //        tab.setSchema( dbc.getDbSchema().toUpperCase());
            // get columns
            PreparedStatement pStmt = conn.prepareStatement(sqlGETTABLES);
            //           pStmt.setString(1, tabName);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {

                tabNames.add(rs.getString("TABLE_NAME"));
            }
            rs.close();
            pStmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return tabNames;


    }

}
