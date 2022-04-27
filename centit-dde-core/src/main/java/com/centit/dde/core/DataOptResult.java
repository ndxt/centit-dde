package com.centit.dde.core;

import com.centit.framework.common.ResponseData;
import com.centit.framework.common.ToResponseData;
import com.centit.support.common.LeftRightPair;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DataOptResult implements ToResponseData {

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
