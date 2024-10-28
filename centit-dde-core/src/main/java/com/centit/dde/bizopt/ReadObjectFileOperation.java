package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;
import com.centit.support.xml.XMLObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;


/**
 * @author zhf
 */
public class ReadObjectFileOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileType = bizOptJson.getString("fileType");
        if(StringUtils.isBlank(fileType)){
            fileType = "json";
        }
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        FileDataSet fileInfo = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, true);
        InputStream inputStream = fileInfo.getFileInputStream();
        if (inputStream != null) {
            DataSet simpleDataSet;
            if("xml".equalsIgnoreCase(fileType)) {
                simpleDataSet = new DataSet(JSON.parse(FileIOOpt.readStringFromInputStream(inputStream)));
            } else {
                String objString = FileIOOpt.readStringFromInputStream(inputStream);
                Object obj = StringUtils.isBlank(objString)? null : XMLObject.xmlStringToObject(objString);
                simpleDataSet = new DataSet(obj);
            }
            bizModel.putDataSet(targetDsName, simpleDataSet);
            return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
        } else {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found2", sourDsName));
        }
    }
}
