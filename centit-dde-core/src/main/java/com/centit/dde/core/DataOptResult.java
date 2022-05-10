package com.centit.dde.core;

import com.alibaba.fastjson.JSON;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ToResponseData;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.LeftRightPair;
import com.centit.support.common.ObjectException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataOptResult implements ToResponseData, Serializable {

    public static final int RETURN_CODE_AND_MESSAGE = 1;
    public static final int  RETURN_OPT_DATA = 0;
    public static final int  RETURN_DATA_AS_RAW = 2;
    public static final int  RETURN_FILE_STREAM = 3;

    /**
     * 返回结果类型
     * 数据集
     * 状态
     * 文件
     * 错误
     */
    private int resultType;


    private ResponseData lastError;

    private List<LeftRightPair<String, ResponseData>> errorStepResult;
    private Object resultObject;

    public DataOptResult(){
        resultType = RETURN_OPT_DATA;
        errorStepResult = new ArrayList<>(16);
    }

    public void addLastStepResult(String sKey, ResponseData objValue) {
        lastError = objValue;
        if(ResponseData.RESULT_OK != objValue.getCode()) {
            errorStepResult.add(new LeftRightPair<>(sKey, objValue));
        }
    }

    public ResponseData getLastError() {
        return lastError;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        if(RETURN_FILE_STREAM==resultType){
            throw new ObjectException("不能直接将结果设置为文件类型，请调用setResultFile方法");
        }
        this.resultType = resultType;
    }

    public Object getResultObject() {
        return resultObject;
    }

    public void setResultObject(Object resultObject) {
        this.resultObject = resultObject;
    }

    public void setResultFile(String fileName, int fileSize, Object fileData) {
        this.resultType = RETURN_FILE_STREAM;
        this.resultObject =  CollectionsOpt.createHashMap(
            ConstantValue.FILE_NAME, fileName,
            ConstantValue.FILE_SIZE, fileSize,
            ConstantValue.FILE_CONTENT, fileData);
    }

    public int getReturnFileSize() {
        if(this.resultType != RETURN_FILE_STREAM){
            return -1;
        }
        Map<String, Object> fileInfo = (Map<String, Object> )this.resultObject;
        return NumberBaseOpt.castObjectToInteger(fileInfo.get(ConstantValue.FILE_SIZE));
    }

    public String getReturnFilename() {
        if(this.resultType != RETURN_FILE_STREAM){
            return null;
        }
        Map<String, Object> fileInfo = (Map<String, Object> )this.resultObject;
        return StringBaseOpt.castObjectToString(fileInfo.get(ConstantValue.FILE_NAME));
    }
    public InputStream getReturnFileStream() {
        if(this.resultType != RETURN_FILE_STREAM){
            return null;
        }
        Map<String, Object> fileInfo = (Map<String, Object> )this.resultObject;
        Object data = fileInfo.get(ConstantValue.FILE_CONTENT);
        if(data == null) {
            return null;
        }
        if (data instanceof InputStream) {
            return (InputStream)data;
        }
        if (data instanceof OutputStream) {
            ByteArrayOutputStream outputStream = (ByteArrayOutputStream) data;
            return new ByteArrayInputStream(outputStream.toByteArray());
        }

        if(data instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) data);
        }

        if(data instanceof String){
            return new ByteArrayInputStream(((String) data).getBytes(StandardCharsets.UTF_8));
        }
        String dataStr = JSON.toJSONString(data);
        return new ByteArrayInputStream(dataStr.getBytes(StandardCharsets.UTF_8));
    }

    public boolean hasErrors(){
        return errorStepResult.size()>0;
    }

    public String getErrorMessage(){
        StringBuilder errorMsg = new StringBuilder(200);
        for(LeftRightPair<String, ResponseData> pair : errorStepResult){
            //if(ResponseData.RESULT_OK != pair.getRight().getCode()) {
                errorMsg.append(pair.getLeft()).append(":").append(pair.getRight().getMessage());
            //}
        }
        return errorMsg.toString();
    }

    @Override
    public ResponseData toResponseData() {
        if(resultType == RETURN_CODE_AND_MESSAGE){
            int errorCode = lastError.getCode();
            if(errorCode == 0 && hasErrors()){
                errorCode = ResponseData.ERROR_OPERATION;
            }
            return ResponseData.makeErrorMessageWithData( lastError.getData(),
                errorCode,
                getErrorMessage()
            );
        } else {
            return ResponseData.makeResponseData(this.resultObject);
        }
    }

    public static DataOptResult createExceptionResult(ResponseData objValue){
        DataOptResult result = new DataOptResult();
        result.resultType = DataOptResult.RETURN_CODE_AND_MESSAGE;
        result.addLastStepResult("exception", objValue);
        return result;
    }
}
