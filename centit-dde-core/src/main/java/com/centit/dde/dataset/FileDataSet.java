package com.centit.dde.dataset;

import com.alibaba.fastjson.annotation.JSONField;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.file.FileIOOpt;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileDataSet extends DataSet {

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
            if(this.data == null || !(this.data instanceof Map)){
                this.data = dataobj;
            } else {
                this.data = CollectionsOpt.unionTwoMap(
                    (Map<String, Object>)dataobj , (Map<String, Object>) this.data);
            }
        }
    }

    public void setFileInfo(String fileName, int fileSize, Object fileData){
        if(data == null || !(data instanceof Map)){
            data = new HashMap<>(20);
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
