package com.centit.dde.es;

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
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.VariableFormula;

import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {

    private ESServerConfig esServerConfig;

    public EsWriteBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        Object indexType = bizOptJson.get("indexType");
        //if (StringBaseOpt.isNvl(indexType)) return ResponseData.makeErrorMessage("请选择写入的索引类型！");
        boolean indexFile = BooleanBaseOpt.castObjectToBoolean(indexType, false);
        String dataSetId = bizOptJson.getString("source");
        if (StringBaseOpt.isNvl(dataSetId)) return ResponseData.makeErrorMessage("请选择需要写入的数据！");

        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null ) return ResponseData.makeErrorMessage("选择的数据集不存在！");
        ESIndexer esIndexer = indexFile ? IndexerSearcherFactory.obtainIndexer(esServerConfig, FileDocument.class)
                 :IndexerSearcherFactory.obtainIndexer(esServerConfig, ObjectDocument.class);

        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
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
            fileInfo.setOptTag(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("optTag"))));
            fileInfo.setFileId(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("fileId"))));
            fileInfo.setFileName(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("fileName"))));
            fileInfo.setFileSummary(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("fileSummary"))));
            fileInfo.setFileMD5(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("fileMD5"))));
            fileInfo.setKeywords(StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(mapInfo.get("keywords"))).split(" "));

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
                objInfo.setOptTag(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("optTag"))));
                objInfo.contentObject(documentInfo);
                objInfo.setTitle(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("title"))));
            }
        }
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
