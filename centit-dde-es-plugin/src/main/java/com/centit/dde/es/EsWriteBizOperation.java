package com.centit.dde.es;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataRowVariableTranslate;
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
import com.centit.support.file.FileIOOpt;

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

        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        VariableFormula formula = new VariableFormula();
        int rowInd = 0;
        int rowCount = dataAsList.size();

        for(Map<String, Object> documentInfo : dataAsList){
            formula.setTrans(new DataRowVariableTranslate(documentInfo, rowInd ++, rowCount));
            if(indexFile){
                Object fileData = documentInfo.get(mapInfo.get("content"));
                if(fileData==null){
                    continue;
                }
                FileDocument fileInfo = new FileDocument();
                fileInfo.setOptId(dataOptContext.getOptId());
                fileInfo.setOsId(dataOptContext.getOsId());

                fileInfo.setOptMethod("index");
                fileInfo.setUserCode(dataOptContext.getCurrentUserCode());
                fileInfo.setUnitCode(dataOptContext.getCurrentUnitCode());
                fileInfo.setContent(TikaTextExtractor.extractInputStreamText(
                    FileIOOpt.castObjectToInputStream(fileData)));

                fileInfo.setOptTag(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("optTag"))));
                fileInfo.setFileId(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("fileId"))));
                fileInfo.setFileName(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("fileName"))));
                fileInfo.setFileSummary(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("fileSummary"))));
                fileInfo.setFileMD5(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("fileMD5"))));
                fileInfo.setKeywords(StringBaseOpt.castObjectToString(formula.calcFormula(mapInfo.get("keywords"))).split(" "));

                esIndexer.mergeDocument(fileInfo);
            } else {
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

        };
        return BuiltInOperation.createResponseSuccessData(dataAsList.size());
    }
}
