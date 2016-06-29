package com.centit.test.datafile;

import com.centit.support.database.config.DirectConnDB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Created by sx on 2014/12/11.
 */
public class TestJdbc {

    static Connection getConnection() throws Exception {
        DirectConnDB db = new DirectConnDB("jdbc:sqlserver://192.168.132.76:1433;databaseName=BSDFW2", "sqlser", "abc#A123");


        return db.getConn();
    }

    static void getTables() throws Exception {
        Connection connection = getConnection();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE", "VIEW"});
        while (tables.next()) {
            System.out.println(tables.getString("TABLE_NAME"));
        }

    }

    public static void main(String[] args) throws Exception {
        getTables();
//        getConnection();
    }


}
