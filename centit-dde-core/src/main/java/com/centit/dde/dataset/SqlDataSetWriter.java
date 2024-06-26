package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSON;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.DataSetWriter;
import com.centit.dde.utils.DBBatchUtils;
import com.centit.product.metadata.api.ISourceInfo;
import com.centit.product.metadata.po.MetaTable;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.support.common.ObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据库数据集 读取和写入类
 * 需要设置的参数有：
 * 数据库连接信息 DatabaseInfo
 * 对应的表信息 SimpleTableInfo
 *
 * @author zhf
 */
public class SqlDataSetWriter implements DataSetWriter {
    private static final Logger logger = LoggerFactory.getLogger(SqlDataSetWriter.class);

    private ISourceInfo dataSource;
    private MetaTable tableInfo;
    private Map fieldsMap;
    private int successNums;
    private int errorNums;
    private StringBuilder operateMessage;
    private String writeTag;
    private String writeMsg;

    private BizModel bizModel;

    public void setFieldsMap(Map fieldsMap) {
        this.fieldsMap = fieldsMap;
    }

    private Connection connection;
    /**
     * true 数据集整体作为一个事务写入
     * false 数据集每一条作为一个事物写入
     * 默认为true 主要式为了效率考虑
     */
    private boolean saveAsWhole;

    public SqlDataSetWriter(final BizModel bizModel, ISourceInfo dataSource, MetaTable tableInfo,
                            String writeTag, String writeMsg) {
        this.bizModel = bizModel;
        this.saveAsWhole = true;
        this.connection = null;
        this.tableInfo = tableInfo;
        this.dataSource = dataSource;
        this.writeMsg = writeMsg;
        this.writeTag = writeTag;
        this.operateMessage = new StringBuilder();
    }

    private void fetchConnect() {
        try {
            connection = AbstractSourceConnectThreadHolder.fetchConnect(dataSource);
        } catch (Exception e) {
            throw new ObjectException(ObjectException.DATABASE_OPERATE_EXCEPTION, e);
        }
    }

    /**
     * 将 dataSet 数据集 持久化
     *
     * @param dataSet 数据集
     */
    @Override
    public void save(DataSet dataSet) {
        if (connection == null) {
            fetchConnect();
        }
        if (this.saveAsWhole) {
            try {
                DBBatchUtils.batchInsertObjects(bizModel, connection,
                    tableInfo, dataSet.getDataAsList(), fieldsMap);
                successNums = 0;
                errorNums = 0;
                for (Map<String, Object> row : dataSet.getDataAsList()) {
                    dealResultMsg(row);
                    successNums++;
                }
            } catch (SQLException e) {
                dealWholeException(dataSet, e);
            }
        } else {
            /* 这部分也可以 直接运行sql语句 而不是用 GeneralJsonObjectDao 方式来提高效率 */
            successNums = 0;
            errorNums = 0;

            for (Map<String, Object> row : dataSet.getDataAsList()) {
                try {
                    int iResult = DBBatchUtils.insertObject(bizModel, connection,
                        tableInfo, row, fieldsMap);
                    connection.commit();
                    if (iResult > 0) {
                        dealResultMsg(row);
                        successNums++;
                    } else {
                        row.put(writeTag,false);
                        row.put(writeMsg, "执行无结果" + iResult);
                        errorNums++;
                    }
                } catch (SQLException e) {
                    row.put(writeTag,false);
                    row.put(writeMsg, e.getMessage());
                    errorNums++;
                    operateMessage.append(e.getMessage()).append("\r\n")
                        .append(JSON.toJSONString(row)).append("\r\n");
                }
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
        if (connection == null) {
            fetchConnect();
        }
        if (this.saveAsWhole) {
            try {
                DBBatchUtils.batchMergeObjects(bizModel, connection,
                    tableInfo, dataSet.getDataAsList(), fieldsMap);
                successNums = 0;
                errorNums = 0;
                for (Map<String, Object> row : dataSet.getDataAsList()) {
                    dealResultMsg(row);
                    successNums++;
                }
            } catch (SQLException e) {
                dealWholeException(dataSet, e);
            }
        } else {
            successNums = 0;
            errorNums = 0;
            for (Map<String, Object> row : dataSet.getDataAsList()) {
                try {
                    int iResult = DBBatchUtils.mergeObject(bizModel, connection,
                        tableInfo, row, fieldsMap);
                    connection.commit();
                    if (iResult > 0) {
                        dealResultMsg(row);
                        successNums++;
                    } else {
                        row.put(writeTag,false);
                        row.put(writeMsg, "执行无结果" + iResult);
                        errorNums++;
                    }
                } catch (SQLException | ObjectException e) {
                    row.put(writeTag,false);
                    row.put(writeMsg, e.getMessage());
                    errorNums++;

                    operateMessage.append(e.getMessage()).append("\r\n")
                        .append(JSON.toJSONString(row)).append("\r\n");

                }
            }

        }
    }

    private void dealResultMsg(Map<String, Object> row) {
        row.put(writeTag, true);
        row.put(writeMsg,"ok");
    }

    private void dealWholeException(DataSet dataSet, SQLException e) {
        successNums = 0;
        errorNums = 0;
        operateMessage.append(e.getMessage()).append("\r\n");
        for (Map<String, Object> row : dataSet.getDataAsList()) {
            row.put(writeTag,false);
            row.put(writeMsg, e.getMessage());
            errorNums++;
        }
    }

    public void setSaveAsWhole(boolean saveAsWhole) {
        this.saveAsWhole = saveAsWhole;
    }

    public int getSuccessNums() {
        return successNums;
    }

    public int getErrorNums() {
        return errorNums;
    }

    public String getOperateMessage() {
        if(operateMessage.length()<1)
            return "ok";
        return operateMessage.toString();
    }
}
