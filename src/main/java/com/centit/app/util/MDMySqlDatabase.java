package com.centit.app.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.centit.support.database.config.DBConfig;
import com.centit.support.database.metadata.Database;
import com.centit.support.database.metadata.TableField;
import com.centit.support.database.metadata.TableMetadata;


public class MDMySqlDatabase implements MDDatabase, Database {
    private final static String sqlGETTABLES = " show tables";

    private final static String sqlGetTabColumns =
            "show columns from ";


    private String sDBSchema;
    private DBConfig dbc;

    @Override
    public void setDBConfig(DBConfig dbc) {
        this.dbc = dbc;
    }

    public String getDBSchema() {
        return sDBSchema;
    }

    public void setDBSchema(String schema) {
        sDBSchema = schema;
    }

    public TableMetadata getTableMetadata(String tabName) {
        TableMetadata tab = new TableMetadata(tabName);

        try {
            Connection conn = dbc.getConn();
            tab.setSchema(dbc.getDbSchema().toUpperCase());
            // get columns
            String sql = sqlGetTabColumns + tabName;
            PreparedStatement pStmt = conn.prepareStatement(sql);

            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                // a.name, c.name AS typename, a.length , a.xprec, a.xscale, isnullable
                TableField field = new TableField();
                field.setColumn(rs.getString("field"));
                field.setDBType(rs.getString("type"));
                //  field.setMaxLength(rs.getInt("length"));
                //   field.setPrecision(rs.getInt("xprec"));
                //  field.setScale(rs.getInt("xscale"));
                field.setNullEnable(rs.getString("NULL"));
                field.mapToMetadata();

                tab.getColumns().add(field);
                if (rs.getString("key") != null && rs.getString("key").trim().equals("PRI")) {
                    tab.getPkColumns().add(rs.getString("field"));
                }

            }
            rs.close();
            pStmt.close();


            //conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }

    @Override
    public List<String> getAllTableCode() {
        List<String> tabNames = new ArrayList<String>();
        try {
            Connection conn = dbc.getConn();
            //        tab.setSchema( dbc.getDbSchema().toUpperCase());
            // get columns
            PreparedStatement pStmt = conn.prepareStatement(sqlGETTABLES);
            //           pStmt.setString(1, tabName);
            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                tabNames.add(rs.getString(1));
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
