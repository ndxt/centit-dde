package com.centit.dde.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.centit.product.dataopt.core.DataSetReader;
import com.centit.product.dataopt.core.SimpleDataSet;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.database.jsonmaptable.GeneralJsonObjectDao;
import com.centit.support.database.metadata.TableInfo;
import com.centit.support.database.utils.DataSourceDescription;
import com.centit.support.database.utils.DbcpConnectPools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 *      数据库连接信息 DatabaseInfo
 *      对应的表信息 SimpleTableInfo
 */
public class KeyValueTableReader implements DataSetReader {

    private static final Logger logger = LoggerFactory.getLogger(KeyValueTableReader.class);
    private DataSourceDescription dataSource;
    /**
     * KeyValueTable 的表明
     */
    private TableInfo tableInfo;

    private Connection connection;
    /**
     * 读取 dataSet 数据集
     * @param params 模块的自定义参数
     * @return dataSet 数据集
     */
    @Override
    @SuppressWarnings("unchecked")
    public SimpleDataSet load(final Map<String, Object> params) {
        Connection conn = connection;
        boolean createConnect = false;
        try {
            if(conn == null){
                conn = DbcpConnectPools.getDbcpConnect(dataSource);
                createConnect = true;
            }

            GeneralJsonObjectDao dao = GeneralJsonObjectDao.createJsonObjectDao(conn, tableInfo);
            JSONArray ja = dao.listObjectsByProperties(params);
            if(ja==null){
                return null;
            }
            List<Map<String, Object>> keyValueData = new ArrayList<>(ja.size()+1);
            for(Object obj : ja){
                if(obj instanceof Map){
                    Object value = ((Map) obj).get("objectJson");
                    if(value!=null) {
                        if (value instanceof Map) {
                            keyValueData.add((Map) value);
                        } else {
                            Object json = JSON.parse(StringBaseOpt.castObjectToString(value));
                            if (json instanceof Map) {
                                keyValueData.add((Map) json);
                            }
                        }
                    }
                }
            }

            SimpleDataSet dataSet = new SimpleDataSet();
            dataSet.setData(keyValueData);
            return dataSet;
        } catch (SQLException | IOException e) {
            logger.error(e.getLocalizedMessage());
            throw new ObjectException(e.getLocalizedMessage());
        } finally {
            if(createConnect){
                DbcpConnectPools.closeConnect(conn);
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
}
