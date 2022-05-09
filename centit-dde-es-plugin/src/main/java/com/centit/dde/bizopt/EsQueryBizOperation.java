package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.search.document.FileDocument;
import com.centit.search.document.ObjectDocument;
import com.centit.search.service.ESServerConfig;
import com.centit.search.service.Impl.ESSearcher;
import com.centit.search.service.IndexerSearcherFactory;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsQueryBizOperation implements BizOperation {
    private ESServerConfig esServerConfig;

    public EsQueryBizOperation(ESServerConfig esServerConfig) {
        this.esServerConfig = esServerConfig;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        String indexType = bizOptJson.getString("indexType");
        if (StringBaseOpt.isNvl(indexType)) return ResponseData.makeErrorMessage("请选择查询的索引类型！");

        String queryParameter = bizOptJson.getString("queryParameter");
        if(StringBaseOpt.isNvl(queryParameter)) return ResponseData.makeErrorMessage("查询内容不能为空！");

        JSONArray fieldAttributeInfos = bizOptJson.getJSONArray("fieldAttributeInfos");
        Map<String,Object> queryParam = new HashMap<>();
        fieldAttributeInfos.stream().forEach(fieldAttributeInfo->{
            Map<String, Object> fieldInfo = CollectionsOpt.objectToMap(fieldAttributeInfo);
            String fieldName = StringBaseOpt.castObjectToString(fieldInfo.get("fieldName"));
            Object fieldValue = JSONTransformer.transformer(fieldInfo.get("value"), new BizModelJSONTransform(bizModel));
            if (!StringBaseOpt.isNvl(fieldName) && fieldValue != null){
                queryParam.put(fieldName,fieldValue);
            }
        });

        ESSearcher esSearcher;
        switch (indexType){
            case "indexObject":
                esSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, ObjectDocument.class);
                break;
            case "indexFile":
                esSearcher = IndexerSearcherFactory.obtainSearcher(esServerConfig, FileDocument.class);
                break;
            default:
                return ResponseData.makeErrorMessage("未知索引类型！");
        }

        int pageNo = NumberBaseOpt.castObjectToInteger(queryParam.get("pageNo"),1);
        int pageSize = NumberBaseOpt.castObjectToInteger(queryParam.get("pageSize"),20);

        Pair<Long, List<Map<String, Object>>> search = esSearcher.search(queryParam, queryParameter, pageNo, pageSize);
        DataSet dataSet = new DataSet(search.getValue());
        bizModel.putDataSet(bizOptJson.getString("id"),dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
