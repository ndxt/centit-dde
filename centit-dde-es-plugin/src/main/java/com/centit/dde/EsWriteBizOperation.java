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
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {

    private ESServerConfig esServerConfig;

    public EsWriteBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {

        String dataSetId = bizOptJson.getString("source");
        if (StringBaseOpt.isNvl(dataSetId)) return ResponseData.makeErrorMessage("请选择需要写入的数据！");

        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null ) return ResponseData.makeErrorMessage("选择的数据集不存在！");

        boolean indexFile = "indexFile".equals(bizOptJson.get("indexType"));
        ESIndexer esIndexer = indexFile ? IndexerSearcherFactory.obtainIndexer(esServerConfig, FileDocument.class)
                 :IndexerSearcherFactory.obtainIndexer(esServerConfig, ObjectDocument.class);

        String optTag = bizOptJson.getString("optTag");
        if(indexFile){
            FileDocument fileInfo = new FileDocument();
            fileInfo.setOptId(dataOptContext.getOptId());
            fileInfo.setOsId(dataOptContext.getOsId());

            fileInfo.setOptMethod("index");
            fileInfo.setUserCode(dataOptContext.getCurrentUserCode());
            fileInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
            fileInfo.setContent(TikaTextExtractor.extractInputStreamText(
                DataSetOptUtil.getInputStreamFormFile(dataSet.getFirstRow())));

            BizModelJSONTransform modelTrasform = new BizModelJSONTransform(bizModel);
            String fileId = bizOptJson.getString("fileId");
            if (StringUtils.isBlank(fileId)){
                return ResponseData.makeErrorMessage("文档主键不能为空！");
            }

            fileInfo.setFileId(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(fileId)));

            String fileName = bizOptJson.getString("fileName");
            if(StringUtils.isNotBlank(fileName)) {
                fileInfo.setFileName(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(fileName)));
            }

            String fileSummary = bizOptJson.getString("fileSummary");
            if(StringUtils.isNotBlank(fileSummary)) {
                fileInfo.setFileSummary(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(fileSummary)));
            }

            String fileMD5 = bizOptJson.getString("fileMD5");
            if(StringUtils.isNotBlank(fileMD5)) {
                fileInfo.setFileMD5(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(fileMD5)));
            }

            String keywords = bizOptJson.getString("keywords");
            if(StringUtils.isNotBlank(keywords)) {
                fileInfo.setKeywords(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(keywords)).split(" "));
            }

            if(StringUtils.isNotBlank(optTag)) {
                fileInfo.setOptTag(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(optTag)));
            }
            fileInfo.setCreateTime(new Date());
            esIndexer.mergeDocument(fileInfo);
        } else {
            List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
            VariableFormula formula = new VariableFormula();
            int rowInd = 0;
            int rowCount = dataAsList.size();
            for(Map<String, Object> documentInfo : dataAsList){
                formula.setTrans(new DataRowVariableTranslate(documentInfo, rowInd ++, rowCount));
                ObjectDocument objInfo = new ObjectDocument();
                objInfo.setOptId(dataOptContext.getOptId());
                objInfo.setOsId(dataOptContext.getOsId());

                objInfo.setOptMethod("index");
                objInfo.setUserCode(dataOptContext.getCurrentUserCode());
                objInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
                objInfo.setOptTag(StringBaseOpt.castObjectToString(formula.calcFormula(optTag)));
                objInfo.contentObject(documentInfo);
                String title = bizOptJson.getString("title");
                objInfo.setTitle(StringBaseOpt.castObjectToString(formula.calcFormula(title)));
                objInfo.setCreateTime(new Date());
                esIndexer.mergeDocument(objInfo);
            }
        }
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
