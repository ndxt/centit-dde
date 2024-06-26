package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataRowVariableTranslate;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.product.metadata.po.SourceInfo;
import com.centit.product.metadata.service.SourceInfoMetadata;
import com.centit.product.metadata.transaction.AbstractSourceConnectThreadHolder;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.search.utils.ImagePdfTextExtractor;
import com.centit.search.utils.TikaTextExtractor;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {

    private final ESServerConfig esServerConfig;
    private final ImagePdfTextExtractor.OcrServerHost ocrServerHost;
    private  SourceInfoMetadata sourceInfoMetadata;
    public EsWriteBizOperation(ESServerConfig esServerConfig, SourceInfoMetadata sourceInfoMetadata, ImagePdfTextExtractor.OcrServerHost ocrServerHost) {
        this.esServerConfig = esServerConfig;
        this.sourceInfoMetadata = sourceInfoMetadata;
        this.ocrServerHost = ocrServerHost;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //操作类型  增  删  改  合并
        String operationType = bizOptJson.getString("operationType");
        if (StringUtils.isBlank(operationType)) return ResponseData.makeErrorMessage(
            ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
            dataOptContext.getI18nMessage("error.701.field_is_blank", "operationType"));
        String  indexType  = bizOptJson.getString("indexType");
        if("custom".equals(indexType)){
            return customDocOperation(bizModel, bizOptJson, operationType, dataOptContext);
        } else {
            boolean indexFile = "indexFile".equals(bizOptJson.get("indexType"));

            ESIndexer esIndexer = indexFile ?
                IndexerSearcherFactory.obtainIndexer(esServerConfig, FileDocument.class) :
                IndexerSearcherFactory.obtainIndexer(esServerConfig, ObjectDocument.class);

            return indexFile ?
                fileDocumentOperation(bizModel, bizOptJson, dataOptContext, esIndexer, operationType) :
                objectDocumentOperation(bizModel, bizOptJson, dataOptContext, esIndexer, operationType);
        }
    }

    /**
     *自定义文档操作
     */
    private ResponseData customDocOperation(BizModel bizModel, JSONObject bizOptJson,String operationType, DataOptContext dataOptContext) throws Exception{
        String databaseCode = BuiltInOperation.getJsonFieldString(bizOptJson, "databaseName", null);
        if (StringUtils.isBlank(databaseCode))
            return ResponseData.makeErrorMessage(
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "databaseName"));

        SourceInfo esInfo = sourceInfoMetadata.fetchSourceInfo(databaseCode);
        String indexName = BuiltInOperation.getJsonFieldString(bizOptJson, "indexName", null);
        if (StringUtils.isBlank(indexName)) return ResponseData.makeErrorMessage(
            ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
            dataOptContext.getI18nMessage("error.701.field_is_blank", "indexName"));

        RestHighLevelClient esClient = AbstractSourceConnectThreadHolder.fetchESClient(esInfo);
        if("delete".equals(operationType)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
            String documentId = StringBaseOpt.castObjectToString(transform.attainExpressionValue(bizOptJson.getString("documentId")));
            if (StringUtils.isBlank(documentId))
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "documentId"));
            return deleteCustomDocument(esClient, indexName, documentId);
        }
        DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
        if (dataSet == null )
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        String primaryKeyName = bizOptJson.getString("primaryKey");
        if (StringUtils.isBlank(primaryKeyName))
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "primaryKeyName"));
        return batchSaveDocuments(esClient, dataSet.getDataAsList(), indexName, primaryKeyName, operationType, dataOptContext);
    }

    //es 文件文档操作
    private ResponseData fileDocumentOperation(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext,
                                               ESIndexer esIndexer, String operationType) throws Exception{
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);

        String documentId=null;
        if ("delete".equals(operationType)) {
            documentId = StringBaseOpt.castObjectToString(transform.attainExpressionValue(bizOptJson.getString("documentId")));
            if (StringUtils.isBlank(documentId))
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "documentId"));
        } else {
            DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
            if (dataSet == null )
                return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                    dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
            Object optTag = transform.attainExpressionValue(bizOptJson.getString("optTag"));
            if (optTag == null)
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "optTag"));
        }
        Object result;
        switch (operationType){
            case "add":
                result = esIndexer.saveNewDocument(fileDocumentBuild(bizModel, bizOptJson, dataOptContext, transform));
                break;
            case "delete":
                result= esIndexer.deleteDocument(documentId);
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
    private FileDocument fileDocumentBuild(BizModel bizModel, JSONObject bizOptJson,
                                           DataOptContext dataOptContext, BizModelJSONTransform modelTrasform) throws Exception{
        DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
        FileDocument fileInfo = new FileDocument();
        fileInfo.setOptId(dataOptContext.getOptId());
        fileInfo.setOsId(dataOptContext.getOsId());
        fileInfo.setOptMethod("index");
        fileInfo.setUserCode(dataOptContext.getCurrentUserCode());
        fileInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
        // 获取文件文本，这边需要添加 图片pdf文件的获取方式
        // ocrServerHost
        InputStream fileInputStream = DataSetOptUtil.getInputStreamFormDataSet(dataSet);
        if(fileInputStream!=null) {
            String fileType = bizOptJson.getString("fileType");
            if ("jpg".equals(fileType)) {
                fileInfo.setContent(ImagePdfTextExtractor.imageToText(fileInputStream, ocrServerHost));
            } else if ("imagePdf".equals(fileType)) {
                fileInfo.setContent(ImagePdfTextExtractor.imagePdfToText(fileInputStream, ocrServerHost));
            } else {
                fileInfo.setContent(TikaTextExtractor.extractInputStreamText(fileInputStream));
            }
        }

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
        return fileInfo;
    }


    //es 对象文档操作
    private ResponseData objectDocumentOperation(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext,
                                                 ESIndexer esIndexer, String operationType){

        DataSet dataSet = bizModel.getDataSet(bizOptJson.getString("source"));
        if (dataSet == null )
            return ResponseData.makeErrorMessage(ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));

        String optTag = bizOptJson.getString("optTag");
        if (StringUtils.isBlank(optTag))
            return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "optTag"));

        if("delete".equals(operationType)){
            Object documentId = JSONTransformer.transformer(
                BuiltInOperation.getJsonFieldString(bizOptJson, "documentId", null),
                new BizModelJSONTransform(bizModel));
            if (documentId == null )
                return ResponseData.makeErrorMessage(ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "documentId"));

            boolean b = esIndexer.deleteDocument(StringBaseOpt.castObjectToString(documentId));
            return ResponseData.makeResponseData(b);
        }else {
            VariableFormula formula = new VariableFormula();
            List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
            int rowInd = 0;
            int rowCount = dataAsList.size();
            List<Object> addResult = new ArrayList<>();
            for(Map<String, Object> documentInfo : dataAsList) {
                formula.setTrans(new DataRowVariableTranslate(bizModel, documentInfo, rowInd++, rowCount));
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
    private ObjectDocument objectDocumentBuild(VariableFormula formula,Map<String, Object> documentInfo,
                                               JSONObject bizOptJson, DataOptContext dataOptContext){
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

    private ResponseData deleteCustomDocument(RestHighLevelClient restHighLevelClient,
                                              String indexName, String documentId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(documentId);
        /*DeleteResponse deleteResponse = */restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        return BuiltInOperation.createResponseSuccessData(1);
    }

    /**
     * 批量插入或者更新   es存在就更新，不存在就插入
     */
    private  ResponseData batchSaveDocuments(RestHighLevelClient restHighLevelClient, List<Map<String, Object>> jsonDatas,
                                             String indexName,String primaryKeyName, String operationType,
                                             DataOptContext dataOptContext) throws IOException {
        BulkRequest requestBulk = new BulkRequest(indexName);

        for (Map<String, Object> jsonData : jsonDatas) {
            String documentId = StringBaseOpt.castObjectToString(jsonData.get(primaryKeyName));
            if (StringUtils.isBlank(documentId))
                return BuiltInOperation.createResponseData(0, jsonDatas.size(),
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "documentId"));

            switch (operationType) {
                case "add":
                    IndexRequest addRequest = new IndexRequest().source(jsonData, XContentType.JSON);
                    addRequest.id(documentId);
                    requestBulk.add(addRequest);
                    break;
                case "update":
                    UpdateRequest updateRequest= new UpdateRequest(indexName,documentId);
                    updateRequest.doc(jsonData, XContentType.JSON);
                    requestBulk.add(updateRequest);
                    break;
                default: //合并
                    if (documentIdExist(restHighLevelClient, indexName,documentId)){
                        UpdateRequest mergeUpdateRequest= new UpdateRequest(indexName,documentId);
                        mergeUpdateRequest.doc(jsonData, XContentType.JSON);
                        requestBulk.add(mergeUpdateRequest);
                    }else {
                        IndexRequest mergeIndexReq = new IndexRequest().source(jsonData, XContentType.JSON);
                        mergeIndexReq.id(documentId);
                        requestBulk.add(mergeIndexReq);
                    }
            }
        }
        int updateCount=0;
        int addCount=0;
        int faildCount=0;
        BulkResponse bulkResponse = restHighLevelClient.bulk(requestBulk, RequestOptions.DEFAULT);
        //成功返回
        JSONObject result = new JSONObject();
        JSONObject faildData = new JSONObject();
        for(BulkItemResponse bulkItemResponse : bulkResponse){
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            if (itemResponse instanceof IndexResponse){
                addCount++;
            }
            if (itemResponse instanceof UpdateResponse){
                updateCount++;
            }
            if(bulkItemResponse.isFailed()){
                faildCount++;
                faildData.put(bulkItemResponse.getId(),bulkItemResponse.getFailureMessage());
            }
        }
        result.put("success", addCount + updateCount);
        result.put("error", faildCount);
        result.put("addCount",addCount);
        result.put("updateCount",updateCount);
        result.put("faildCount",faildCount);
        if (faildCount>0){
            result.put("errorData",faildData);
        }
        return ResponseData.makeResponseData(result);
    }

    //判断文档id是否已经存在
    public static boolean documentIdExist(RestHighLevelClient restHighLevelClient,String indexName,String documentId) throws IOException {
        GetRequest request = new GetRequest(indexName).id(documentId);
        return restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    }
}
