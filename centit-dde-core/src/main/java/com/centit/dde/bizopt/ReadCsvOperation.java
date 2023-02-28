package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;

import java.io.InputStream;


/**
 * @author zhf
 */
public class ReadCsvOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        FileDataSet fileInfo = DataSetOptUtil.attainFileDataset(dataSet, bizOptJson);
        InputStream inputStream = fileInfo.getFileInputStream();
        if (inputStream != null) {
            CsvDataSet csvDataSet = new CsvDataSet();
            csvDataSet.setInputStream(inputStream);
            DataSet simpleDataSet = csvDataSet.load(bizOptJson);
            bizModel.putDataSet(targetDsName, simpleDataSet);
            return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
        } else {
            return BuiltInOperation.createResponseData(0, 1, ResponseData.ERROR_OPERATION,
                bizOptJson.getString("SetsName") + "：读取CSV文件异常，不支持的流类型转换！");
        }
    }
}
