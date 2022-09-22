package com.centit.dde;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataRowVariableTranslate;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.search.utils.TikaTextExtractor;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {

    private final ESServerConfig esServerConfig;

    public EsWriteBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //操作类型  增  删  改  合并
        String operationType = bizOptJson.getString("operationType");
        if (StringUtils.isBlank(operationType)){
            return ResponseData.makeErrorMessage("请选择操作类型！");
        }

        boolean indexFile = "indexFile".equals(bizOptJson.get("indexType"));
        ESIndexer esIndexer = indexFile ?
            IndexerSearcherFactory.obtainIndexer(esServerConfig, FileDocument.class)
            :IndexerSearcherFactory.obtainIndexer(esServerConfig, ObjectDocument.class);

        return indexFile?
            fileDocumentOperation(bizModel,bizOptJson,dataOptContext,esIndexer,operationType)
            : objectDocumentOperation(bizModel,bizOptJson,dataOptContext,esIndexer,operationType);
    }
    //es 文件文档操作
    private ResponseData fileDocumentOperation(BizModel bizModel, JSONObject bizOptJson,DataOptContext dataOptContext,
                                               ESIndexer esIndexer,String operationType) throws Exception{
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        Object documentId = transform.attainExpressionValue(bizOptJson.getString("documentId"));
        if (documentId == null) return ResponseData.makeErrorMessage("文档主键不能为空！");

        if (!"delete".equals(operationType)){
            String dataSetId = bizOptJson.getString("source");
            if (StringUtils.isBlank(dataSetId)) return ResponseData.makeErrorMessage("文档内容不能为空！");

            DataSet dataSet = bizModel.getDataSet(dataSetId);
            if (dataSet == null ) return ResponseData.makeErrorMessage("选择的文档内容不存在！");

            Object optTag = transform.attainExpressionValue(bizOptJson.getString("optTag"));
            if (optTag == null) return ResponseData.makeErrorMessage("业务主键不能为空！");
        }
        Object result;
        switch (operationType){
            case "add":
                result = esIndexer.saveNewDocument(fileDocumentBuild(bizModel, bizOptJson, dataOptContext,transform));
                break;
            case "delete":
                result= esIndexer.deleteDocument(StringBaseOpt.castObjectToString(documentId));
                break;
            case "update":
                result = esIndexer.updateDocument(fileDocumentBuild(bizModel, bizOptJson, dataOptContext,transform));
                break;
            default: //合并
                result = esIndexer.mergeDocument(fileDocumentBuild(bizModel, bizOptJson, dataOptContext,transform));
                break;
        }
        return ResponseData.makeResponseData(result);
    }
    //构建文件文档信息
    private FileDocument fileDocumentBuild(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext,BizModelJSONTransform modelTrasform) throws Exception{
        DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
        FileDocument fileInfo = new FileDocument();
        fileInfo.setOptId(dataOptContext.getOptId());
        fileInfo.setOsId(dataOptContext.getOsId());
        fileInfo.setOptMethod("index");
        fileInfo.setUserCode(dataOptContext.getCurrentUserCode());
        fileInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
        fileInfo.setContent(TikaTextExtractor.extractInputStreamText(DataSetOptUtil.getInputStreamFormFile(dataSet.getFirstRow())));
        fileInfo.setFileId(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("documentId"))));
        fileInfo.setFileName(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("fileName"))));
        fileInfo.setFileSummary(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("fileSummary"))));
        fileInfo.setFileMD5(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("fileMD5"))));
        fileInfo.setOptTag(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("optTag"))));
        fileInfo.setCreateTime(new Date());
        Object keywords = modelTrasform.attainExpressionValue(bizOptJson.getString("keywords"));
        if(keywords != null) {
            fileInfo.setKeywords(StringBaseOpt.castObjectToString(keywords).split(" "));
        }
        return  fileInfo;
    }

    //es 对象文档操作
    private ResponseData objectDocumentOperation(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext,
                                                 ESIndexer esIndexer,String operationType){
        String dataSetId = bizOptJson.getString("source");
        if (StringUtils.isBlank(dataSetId)) return ResponseData.makeErrorMessage("参数来源不能为空！");

        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null ) return ResponseData.makeErrorMessage("选择的参数来源不存在！");

        String optTag = bizOptJson.getString("optTag");
        if (StringUtils.isBlank(optTag)) return ResponseData.makeErrorMessage("业务主键不能为空！");

        if("delete".equals(operationType)){
            Object documentId = JSONTransformer.transformer(
                BuiltInOperation.getJsonFieldString(bizOptJson, "documentId", null), new BizModelJSONTransform(bizModel));
            if (documentId == null ) return ResponseData.makeErrorMessage("文档主键不能为空！");
            boolean b = esIndexer.deleteDocument(StringBaseOpt.castObjectToString(documentId));
            return ResponseData.makeResponseData(b);
        }else {
            VariableFormula formula = new VariableFormula();
            List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
            int rowInd = 0;
            int rowCount = dataAsList.size();
            List<Object> addResult = new ArrayList<>();
            for(Map<String, Object> documentInfo : dataAsList) {
                formula.setTrans(new DataRowVariableTranslate(documentInfo, rowInd++, rowCount));
                switch (operationType) {
                    case "add":
                        addResult.add(esIndexer.saveNewDocument(objectDocumentBuild(formula, documentInfo, bizOptJson, dataOptContext)));
                        break;
                    case "update":
                        addResult.add(esIndexer.updateDocument(objectDocumentBuild(formula, documentInfo, bizOptJson, dataOptContext)));
                        break;
                    default: //合并
                        addResult.add(esIndexer.mergeDocument(objectDocumentBuild(formula, documentInfo, bizOptJson, dataOptContext)));
                }
            }
            return  ResponseData.makeResponseData(addResult);
        }
    }
    // 构建对象文档信息
    private ObjectDocument objectDocumentBuild(VariableFormula formula,Map<String, Object> documentInfo,JSONObject bizOptJson,DataOptContext dataOptContext){
        ObjectDocument objInfo = new ObjectDocument();
        objInfo.setOptId(dataOptContext.getOptId());
        objInfo.setOsId(dataOptContext.getOsId());
        objInfo.setOptMethod("index");
        objInfo.setUserCode(dataOptContext.getCurrentUserCode());
        objInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
        objInfo.setOptTag(StringBaseOpt.castObjectToString(formula.calcFormula(bizOptJson.getString("optTag"))));
        objInfo.contentObject(documentInfo);
        Object title = formula.calcFormula(bizOptJson.getString("title"));
        objInfo.setTitle(StringBaseOpt.castObjectToString(title));
        objInfo.setCreateTime(new Date());
        return objInfo;
    }
}
