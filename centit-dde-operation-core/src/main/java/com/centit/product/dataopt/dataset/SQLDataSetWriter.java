package com.centit.product.dataopt.dataset;

import com.centit.product.dataopt.core.DataSet;
import com.centit.product.dataopt.core.DataSetWriter;
import com.centit.product.dataopt.utils.DBBatchUtils;
import com.centit.support.common.ObjectException;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;
import com.centit.support.database.utils.PersistenceException;
import com.centit.support.database.utils.TransactionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 *      数据库连接信息 DatabaseInfo
 *      对应的表信息 SimpleTableInfo
 */
public class SQLDataSetWriter implements DataSetWriter {
    public static String WRITER_ERROR_TAG = "rmdb_dataset_writer_result";
    private static final Logger logger = LoggerFactory.getLogger(SQLDataSetWriter.class);

    private DataSourceDescription dataSource;
    private TableInfo tableInfo;

    private Connection connection;
    /** true 数据集整体作为一个事务写入
     * false 数据集每一条作为一个事物写入
     * 默认为true 主要式为了效率考虑
     */
    private boolean saveAsWhole;

    public SQLDataSetWriter(){
        saveAsWhole = true;
        connection = null;
    }

    public SQLDataSetWriter(DataSourceDescription dataSource, TableInfo tableInfo){
        this.saveAsWhole = true;
        this.connection = null;
        this.tableInfo = tableInfo;
        this.dataSource = dataSource;
    }

    public SQLDataSetWriter(Connection connection, TableInfo tableInfo){
        this.saveAsWhole = true;
        this.connection = connection;
        this.tableInfo = tableInfo;
    }

    private void fetchConnect(){
        try {
            connection = DbcpConnectPools.getDbcpConnect(dataSource);
        } catch (SQLException e) {
            throw new ObjectException(PersistenceException.DATABASE_OPERATE_EXCEPTION, e);
        }
    }
    /**
     * 将 dataSet 数据集 持久化
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        if(this.saveAsWhole) {
            try {
                if (connection == null) {
                    TransactionHandler.executeInTransaction(dataSource,
                        (conn) -> DBBatchUtils.batchInsertObjects(conn,
                            tableInfo, dataSet.getData()));
                } else {
                    TransactionHandler.executeInTransaction(connection,
                        (conn) -> DBBatchUtils.batchInsertObjects(conn,
                            tableInfo, dataSet.getData()));
                }
                for(Map<String, Object> row : dataSet.getData()){
                    row.put(WRITER_ERROR_TAG,"ok");
                }
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                for(Map<String, Object> row : dataSet.getData()){
                    row.put(WRITER_ERROR_TAG,e.getMessage());
                }
            }
        } else { // 这部分也可以 直接运行sql语句 而不是用 GeneralJsonObjectDao 方式来提高效率
            boolean createConn = false;
            if (connection == null) {
                fetchConnect();
                createConn = true;
            }

            for(Map<String, Object> row : dataSet.getData()){
                try {
                    TransactionHandler.executeInTransaction(connection,
                        (conn) ->
                            GeneralJsonObjectDao.createJsonObjectDao(connection, tableInfo)
                                .saveNewObject(row));
                    row.put(WRITER_ERROR_TAG,"ok");
                } catch (SQLException e) {
                    row.put(WRITER_ERROR_TAG,e.getMessage());
                }
            }

            if(createConn){
                DbcpConnectPools.closeConnect(connection);
            }
        }
    }

    /**
     * 默认和 save 等效
     * 对于数据库类型的持久化来说可以有差别，比如合并，以避免主键冲突
     *
     * @param dataSet 数据集
     */
    @Override
    public void merge(DataSet dataSet) {

        if(this.saveAsWhole) {
            try {
                if (connection == null) {
                    TransactionHandler.executeInTransaction(dataSource,
                        (conn) -> DBBatchUtils.batchMergeObjects(conn,
                            tableInfo, dataSet.getData()));
                } else {
                    TransactionHandler.executeInTransaction(connection,
                        (conn) -> DBBatchUtils.batchMergeObjects(conn,
                            tableInfo, dataSet.getData()));
                }
                for(Map<String, Object> row : dataSet.getData()){
                    row.put(WRITER_ERROR_TAG, "ok");
                }
            } catch (SQLException e) {
                logger.error(e.getLocalizedMessage());
                for(Map<String, Object> row : dataSet.getData()){
                    row.put(WRITER_ERROR_TAG, e.getMessage());
                }
            }
        } else {
            boolean createConn = false;
            if (connection == null) {
                fetchConnect();
                createConn = true;
            }

            for(Map<String, Object> row : dataSet.getData()){
                try {
                    TransactionHandler.executeInTransaction(connection,
                        (conn) ->{
                            try {
                                return GeneralJsonObjectDao.createJsonObjectDao(connection, tableInfo)
                                    .mergeObject(row);
                            } catch (IOException e) {
                                throw new ObjectException(PersistenceException.DATABASE_OPERATE_EXCEPTION, e);
                            }
                        });
                    row.put(WRITER_ERROR_TAG, "ok");
                } catch (SQLException | ObjectException e) {
                    row.put(WRITER_ERROR_TAG, e.getMessage());
                }
            }

            if(createConn){
                DbcpConnectPools.closeConnect(connection);
            }
        }
    }

    public void setDataSource(DataSourceDescription dataSource) {
        this.dataSource = dataSource;
    }

    public void setTableInfo(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setSaveAsWhole(boolean saveAsWhole) {
        this.saveAsWhole = saveAsWhole;
    }
}
