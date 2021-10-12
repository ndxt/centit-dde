package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.fileserver.common.FileInfo;
import com.centit.fileserver.common.FileStoreContext;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传节点
 *
 */
public class FileUploadBizOperation implements BizOperation {

    FileStoreContext fileStoreContext;

    public FileUploadBizOperation( FileStoreContext fileStoreContext) {
        this.fileStoreContext = fileStoreContext;
    }

    public FileUploadBizOperation() {
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileNameField= (String) bizModel.getModelTag().get("fileName");
        if(fileNameField==null) {
            fileNameField = BuiltInOperation.getJsonFieldString(bizOptJson, "fileName", String.valueOf(System.currentTimeMillis()));
        }
        String fileDataField=BuiltInOperation.getJsonFieldString(bizOptJson,"fileupexpression",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500,
                bizOptJson.getString("SetsName")+"：文件上传失败，请选择数据集！");
        }
        if (dataSet!=null && StringUtils.isNotBlank(fileNameField)&&StringUtils.isNotBlank(fileDataField)){
            Map<String, String> mapInfo = new HashMap<>();
            mapInfo.put(fileDataField, fileDataField);
            mapInfo.put(fileNameField, fileNameField);
            dataSet = DataSetOptUtil.mapDateSetByFormula(dataSet, mapInfo.entrySet());
        }
        List<String> fileIds = new ArrayList<>();
        List<Map<String, Object>> dataSetDataAsList = dataSet.getDataAsList();
        for (Map<String, Object> dataMap : dataSetDataAsList) {
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(dataMap.get(fileNameField)==null?fileNameField:String.valueOf(dataMap.get(fileNameField)));
            String fileId;
            Object object = StringUtils.isNotBlank(fileDataField)?dataMap.get(fileDataField):dataSet.getFirstRow().get("fileContent");
            if (object instanceof byte[]){
                fileId = fileStoreContext.saveFile(fileInfo, 0,new ByteArrayInputStream((byte[])object));
            }else if (object instanceof InputStream){
                fileId = fileStoreContext.saveFile(fileInfo, 0,(InputStream) object);
            }else {
                return  BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：上传文件失败，不支持的流类型转换！");
            }
            fileIds.add(fileId);
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(fileIds));
        return BuiltInOperation.getResponseSuccessData(dataSet.getSize());
    }
}
