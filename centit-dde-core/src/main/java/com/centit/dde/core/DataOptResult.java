package com.centit.dde.core;

import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ToResponseData;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.LeftRightPair;
import com.centit.support.common.ObjectException;
import com.centit.support.file.FileIOOpt;

import java.io.InputStream;
import java.io.Serializable;
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

    private ResponseData lastResponse;

    private List<LeftRightPair<String, ResponseData>> errorList;
    private Object resultObject;

    public DataOptResult(){
        resultType = RETURN_OPT_DATA;
        errorList = new ArrayList<>(16);
    }

    public void setStepResponse(String sKey, ResponseData objValue) {
        lastResponse = objValue;
        if(ResponseData.RESULT_OK != objValue.getCode()) {
            errorList.add(new LeftRightPair<>(sKey, objValue));
        }
    }

    public ResponseData getLastError() {
        int errorCount = errorList.size();
        if(errorCount>0){
            return errorList.get(errorCount-1).getRight();
        }
        return null;
    }

    public ResponseData getLastReturn() {
        return lastResponse;
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

    public void setResultFile(Map<String, Object> fileInfo) {
        this.resultType = RETURN_FILE_STREAM;
        this.resultObject =  fileInfo;
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
        return FileIOOpt.castObjectToInputStream(data);
    }

    public boolean hasErrors(){
        return !errorList.isEmpty();
    }

    public ResponseData makeErrorResponse(){
        int errorCount = errorList.size();
        if(errorCount==1){
            return errorList.get(0).getRight();
        }
        int errorCode = errorList.get(errorCount-1).getRight().getCode();
        if(errorCode == 0){
            errorCode = ResponseData.ERROR_OPERATION;
        }

        StringBuilder errorMsg = new StringBuilder(200);
        for(LeftRightPair<String, ResponseData> pair : errorList){
            //if(ResponseData.RESULT_OK != pair.getRight().getCode()) {
                errorMsg.append(pair.getLeft()).append(":").append(pair.getRight().getMessage());
            //}
        }
        return ResponseData.makeErrorMessageWithData(errorList,
            errorCode,
            errorMsg.toString()
        );
    }

    public Object getResultData() {
        if(resultObject == null && hasErrors()){
            return makeErrorResponse();
        }
        if(resultType == RETURN_CODE_AND_MESSAGE) {
            if(resultObject instanceof ResponseData) {
                return resultObject;
            }
            return ResponseData.makeResponseData(resultObject);
        }
        return resultObject;
    }

    @Override
    public ResponseData toResponseData() {
        Object obj = getResultData();
        if(obj instanceof ResponseData) {
            return (ResponseData) obj;
        } else {
            return ResponseData.makeResponseData(obj);
        }
    }

    public static DataOptResult createExceptionResult(ResponseData objValue){
        DataOptResult result = new DataOptResult();
        result.resultType = DataOptResult.RETURN_CODE_AND_MESSAGE;
        result.setStepResponse("exception", objValue);
        return result;
    }
}
