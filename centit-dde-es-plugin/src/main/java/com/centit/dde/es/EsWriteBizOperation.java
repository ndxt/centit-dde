package com.centit.dde.es;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.search.document.ESDocument;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESIndexer;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.support.algorithm.StringBaseOpt;

import java.util.List;
import java.util.Map;

public class EsWriteBizOperation implements BizOperation {

    private ESServerConfig esServerConfig;

    public EsWriteBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String indexType = bizOptJson.getString("indexType");
        if (StringBaseOpt.isNvl(indexType)) return ResponseData.makeErrorMessage("请选择写入的索引类型！");

        String dataSetId = bizOptJson.getString("source");
        if (StringBaseOpt.isNvl(dataSetId)) return ResponseData.makeErrorMessage("请选择需要写入的数据！");

        DataSet dataSet = bizModel.getDataSet(dataSetId);
        if (dataSet == null ) return ResponseData.makeErrorMessage("选择的数据集不存在！");

        ESIndexer esIndexer;
        ESDocument esDocument;
        switch (indexType){
            case "indexObject":
                esIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, ObjectDocument.class);
                esDocument = new ObjectDocument();
                break;
            case "indexFile":
                esIndexer = IndexerSearcherFactory.obtainIndexer(esServerConfig, FileDocument.class);
                esDocument = new FileDocument();
                break;
            default:
                return ResponseData.makeErrorMessage("未知索引类型！");
        }
        List<Map<String, Object>> dataAsList = dataSet.getDataAsList();
        Map<String, String> mapInfo = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "expression");
        dataAsList.stream().forEach(documentInfo -> {
            DataSet destDs = DataSetOptUtil.mapDateSetByFormula(new DataSet(documentInfo), mapInfo.entrySet());
            if (destDs != null){
                String eSDocumentInfo = StringBaseOpt.castObjectToString(destDs.getFirstRow());
                ESDocument esDocumentInfo = JSONObject.parseObject(eSDocumentInfo, esDocument.getClass());
                esIndexer.mergeDocument(esDocumentInfo);
            }
        });
        return BuiltInOperation.createResponseSuccessData(dataAsList.size());
    }
}
