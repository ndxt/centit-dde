package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.*;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.ip.po.DatabaseInfo;
import com.centit.framework.ip.service.IntegrationEnvironment;
import com.centit.support.common.ObjectException;
import com.centit.support.database.utils.DataSourceDescription;

import java.io.IOException;
import java.util.Map;


/**
 * @author zhf
 */
public class ExcelBizOperation implements BizOperation {
    private FileStore fileStore;
    public ExcelBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }
    @Override
    public JSONObject runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "nodeName", bizModel.getModelName());
        String filePath = BuiltInOperation.getJsonFieldString(bizOptJson, "sql", "");
        ExcelDataSet excelDataSet = new ExcelDataSet();
        try {
            excelDataSet.setFilePath(
                fileStore.getFile(filePath).getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDataSet dataSet = excelDataSet.load(null);
        bizModel.putDataSet(sourDsName, dataSet);
        return BuiltInOperation.getJsonObject(dataSet.getRowCount());
    }
}
