package com.centit.dde.transfer;

import java.sql.Connection;

import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.util.ConnPool;
import com.centit.support.algorithm.StringBaseOpt;

public class MapInfoDBConn {
    private DirectConnDB leftDBC;
    private DirectConnDB rightDBC;

    public MapInfoDBConn() {

    }

    public void loadDBConfig(DatabaseInfo sourceDatabaseInfo,
                             DatabaseInfo desDatabaseInfo) {
        try {
            leftDBC = new DirectConnDB();
            rightDBC = new DirectConnDB();
            leftDBC.praiseConnUrl(sourceDatabaseInfo.getDatabaseUrl());
            leftDBC.setUser(sourceDatabaseInfo.getUsername());
            leftDBC.setPassword(StringBaseOpt
                    .decryptBase64Des(sourceDatabaseInfo.getPassword()));

            rightDBC.praiseConnUrl(desDatabaseInfo.getDatabaseUrl());
            rightDBC.setUser(desDatabaseInfo.getUsername());
            rightDBC.setPassword(StringBaseOpt.decryptBase64Des(desDatabaseInfo
                    .getPassword()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectDB() throws Exception {
    }

    public void disConnectDB(Connection conn) throws Exception {
        ConnPool.closeConn(conn);
    }

    public Connection getLeftDBConn() throws Exception {
        return ConnPool.getConn(leftDBC);
    }

    public Connection getRightDBConn() throws Exception {
        return ConnPool.getConn(rightDBC);
    }

    public DirectConnDB getLeftDBC() {
        return leftDBC;
    }

    public void setLeftDBC(DirectConnDB leftDBC) {
        this.leftDBC = leftDBC;
    }

    public DirectConnDB getRightDBC() {
        return rightDBC;
    }

    public void setRightDBC(DirectConnDB rightDBC) {
        this.rightDBC = rightDBC;
    }


}
