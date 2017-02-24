package com.centit.dde.po;

import java.sql.Connection;

import javax.persistence.Transient;

import com.centit.dde.util.ConnPool;
import com.centit.support.database.DataSourceDescription;
import com.centit.support.database.DbcpConnect;
import com.centit.framework.staticsystem.po.DatabaseInfo;

public class MapInfoDBConn {
    @Transient
    private DataSourceDescription leftDBC;
    @Transient
    private DataSourceDescription rightDBC;

    public MapInfoDBConn() {

    }

    public void loadDBConfig(DatabaseInfo sourceDatabaseInfo,
                             DatabaseInfo desDatabaseInfo) {
        try {
            leftDBC = new DataSourceDescription();
            rightDBC = new DataSourceDescription();
            leftDBC.setConnUrl(sourceDatabaseInfo.getDatabaseUrl());
            leftDBC.setUsername(sourceDatabaseInfo.getUsername());
            leftDBC.setPassword(sourceDatabaseInfo.getClearPassword());

            rightDBC.setConnUrl(desDatabaseInfo.getDatabaseUrl());
            rightDBC.setUsername(desDatabaseInfo.getUsername());
            rightDBC.setPassword(desDatabaseInfo.getClearPassword());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connectDB() throws Exception {
    }

    public void disConnectDB(Connection conn) throws Exception {
        ConnPool.closeConn(conn);
    }

    public DbcpConnect getLeftDBConn() throws Exception {
        return ConnPool.getConn(leftDBC);
    }

    public DbcpConnect getRightDBConn() throws Exception {
        return ConnPool.getConn(rightDBC);
    }

    public DataSourceDescription getLeftDBC() {
        return leftDBC;
    }

    public void setLeftDBC(DataSourceDescription leftDBC) {
        this.leftDBC = leftDBC;
    }

    public DataSourceDescription getRightDBC() {
        return rightDBC;
    }

    public void setRightDBC(DataSourceDescription rightDBC) {
        this.rightDBC = rightDBC;
    }


}
