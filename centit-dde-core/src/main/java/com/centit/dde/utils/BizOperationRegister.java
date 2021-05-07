package com.centit.dde.utils;

import com.centit.dde.bizopt.*;
import com.centit.dde.core.impl.BizOptFlowImpl;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 统一注册节点BizOperation对象
 */
@Configuration
public class BizOperationRegister {

    @Resource
    BizOptFlowImpl bizOptFlow;

 /*   @Resource
    AssignmentBizOperation assignmentBizOperation;*/

    @Resource
    GenerateCsvBizOperation generateCsvBizOperation;

    @Resource
    GenerateJsonBizOperation generateJsonBizOperation;

    @Resource
    FileUploadBizOperation fileUploadBizOperation;

    @Resource
    GenerateExcelBizeOperation generateExcelBizeOperation;

    @Resource
    FileDownloadBizOperation fileDownloadBizOperation;

    //注册节点对象
    @PostConstruct
    private void init(){
       /* bizOptFlow.registerOperation(ConstantValue.ASSIGN,assignmentBizOperation);*/
        bizOptFlow.registerOperation(ConstantValue.GENERATECSV,generateCsvBizOperation);
        bizOptFlow.registerOperation(ConstantValue.GENERATEJSON,generateJsonBizOperation);
        bizOptFlow.registerOperation(ConstantValue.FILEUPLOADS,fileUploadBizOperation);
        bizOptFlow.registerOperation(ConstantValue.GENERATEXCEL,generateExcelBizeOperation);
        bizOptFlow.registerOperation(ConstantValue.FILEDOWNLOAD,fileDownloadBizOperation);
    }

}
