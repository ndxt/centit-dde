package com.centit.dde.dataset;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.fileserver.common.FileBaseInfo;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileIOOpt;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileDataSet extends DataSet {

    public static String FILE_DATA_SET_DEFAULT_NAME = "fileInfo";
    public FileDataSet() {
        this.dataSetName = FileDataSet.FILE_DATA_SET_DEFAULT_NAME;
        this.data = null;// Collections.emptyList();
    }

    public FileDataSet(String fileName, int fileSize, Object fileData) {
        this.dataSetName = FileDataSet.FILE_DATA_SET_DEFAULT_NAME;
        this.setFileContent(fileName, fileSize, fileData);
    }

    @JSONField(serialize = false)
    @Override
    public String getDataSetType() {
        return "R";
    }

    @JSONField(serialize = false)
    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void setData(Object dataobj) {
        if(dataobj instanceof Map) {
            this.data = dataobj;
        }
    }

    public void setFileInfo(FileBaseInfo fileInfo) {
        JSONObject obj = (JSONObject)JSON.toJSON(fileInfo);
        setFileInfo(obj);
    }

    public void setFileInfo(Map<String, Object> fileInfo) {
        if(this.data == null || !(this.data instanceof Map)){
            this.data = fileInfo;
        } else {
            this.data = CollectionsOpt.unionTwoMap(
                fileInfo , (Map<String, Object>) this.data);
        }
    }

    public void setFileContent(String fileName, int fileSize, Object fileData){
        if(this.data == null || !(this.data instanceof Map)){
            this.data = new HashMap<>(20);
        }
        ((Map<String, Object>)this.data).put(ConstantValue.FILE_NAME, fileName);
        if(fileSize > 0) {
            ((Map<String, Object>) this.data).put(ConstantValue.FILE_SIZE, fileSize);
        }
        ((Map<String, Object>)this.data).put(ConstantValue.FILE_CONTENT, fileData);
    }

    @JSONField(serialize = false)
    public String getFileName(){
        if(this.data==null){
            return null;
        }
        return StringBaseOpt.castObjectToString(
            ((Map<String, Object>)this.data).get(ConstantValue.FILE_NAME));
    }

    @JSONField(serialize = false)
    public int getFileSize(){
        if(this.data==null){
            return 0;
        }
        return NumberBaseOpt.castObjectToInteger(
            ((Map<String, Object>)this.data).get(ConstantValue.FILE_SIZE),0);
    }

    @JSONField(serialize = false)
    public InputStream getFileInputStream(){
        if(this.data==null){
            return null;
        }
        Object fileObj = ((Map<String, Object>)this.data).get(ConstantValue.FILE_CONTENT);
        return FileIOOpt.castObjectToInputStream(fileObj);
    }

}
