package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileSystemOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ZipUnzipOperation implements BizOperation {

    private String tempHome;

    public ZipUnzipOperation(String appHome) {
        if (appHome.endsWith("/") || appHome.endsWith("\\")) {
            this.tempHome = appHome + "temp";
        } else {
            this.tempHome = appHome + File.separatorChar + "temp";
        }
        FileSystemOpt.createDirect(this.tempHome);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        // zip / unzip
        String optType = BuiltInOperation.getJsonFieldString(bizOptJson, "optType", "zip");
        //暂时不支持 加密算法
        //String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if("zip".equals(optType)){
            List<FileDataSet> files = FileDataSetOptUtil.fetchFiles(dataSet, bizOptJson);
            if(files.isEmpty()){
                throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "文件数据获取失败");
            }
            String fileNameDesc = BuiltInOperation.getJsonFieldString(bizOptJson, ConstantValue.FILE_NAME, "");
            BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel, dataSet.getData());
            String fileName = null;
            if(StringUtils.isNotBlank(fileNameDesc)){
                fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
            }
            if(StringUtils.isBlank(fileName)) {
                fileName = DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(),"yyyyMMDD_HHmm") + ".zip";
            }

            FileDataSet fileDataSet = FileDataSetOptUtil.zipFileDatasetList(fileName, files);
            bizModel.putDataSet(targetDsName, fileDataSet);
            return BuiltInOperation.createResponseSuccessData(1);
        } else { // unzip
            FileDataSet zipDataset = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, true);
            List<FileDataSet> fileDataSets = FileDataSetOptUtil.unzipFileDatasetList(tempHome, zipDataset);
            List<Object> filesData = new ArrayList<>();
            for(FileDataSet file : fileDataSets){
                filesData.add(file.getData());
            }
            bizModel.putDataSet(targetDsName,new DataSet(filesData));
            return BuiltInOperation.createResponseSuccessData(fileDataSets.size());
        }
    }
}
