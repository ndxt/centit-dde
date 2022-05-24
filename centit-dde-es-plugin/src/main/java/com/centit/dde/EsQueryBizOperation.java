package com.centit.dde;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
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
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;
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
        String queryParameter = bizOptJson.getString("queryParameter");
        if(StringBaseOpt.isNvl(queryParameter)) return ResponseData.makeErrorMessage("查询关键字不能为空！");

        String osId = dataOptContext.getOsId();
        if (StringUtils.isBlank(osId)) return ResponseData.makeErrorMessage("osId不能为空！");

        BizModelJSONTransform bizModelJSONTransform = new BizModelJSONTransform(bizModel);
        Map<String,Object> queryParam = new HashMap<>(6);
        queryParam.put("osId",osId);

        if (!bizOptJson.getBoolean("queryAll")){
            String optTag = bizOptJson.getString("optTag");
            String unitCode = bizOptJson.getString("unitCode");
            String userCode = bizOptJson.getString("userCode");
            if (StringUtils.isNotBlank(optTag)){
                queryParam.put("optTag",
                    StringBaseOpt.castObjectToString(JSONTransformer.transformer(optTag, bizModelJSONTransform)));
            }
            if (StringUtils.isNotBlank(unitCode)){
                queryParam.put("unitCode",
                    StringBaseOpt.castObjectToString(JSONTransformer.transformer(unitCode, bizModelJSONTransform)));
            }
            if (StringUtils.isNotBlank(userCode)){
                queryParam.put("userCode",
                    StringBaseOpt.castObjectToString(JSONTransformer.transformer(userCode, bizModelJSONTransform)));
            }
        }

        String pageSize = bizOptJson.getString("pageSize");
        String pageNo = bizOptJson.getString("pageNo");
        Integer pageSizeTrans = NumberBaseOpt.castObjectToInteger(JSONTransformer.transformer(pageSize, bizModelJSONTransform),1);
        Integer pageNoTrans = NumberBaseOpt.castObjectToInteger(JSONTransformer.transformer(pageNo, bizModelJSONTransform),20);

        boolean indexFile = "indexFile".equals(bizOptJson.get("indexType"));
        ESSearcher esSearcher = indexFile ? IndexerSearcherFactory.obtainSearcher(esServerConfig, FileDocument.class)
            :IndexerSearcherFactory.obtainSearcher(esServerConfig, ObjectDocument.class);

        String keyword = StringBaseOpt.castObjectToString(JSONTransformer.transformer(queryParameter, bizModelJSONTransform));

        Pair<Long, List<Map<String, Object>>> search = esSearcher.search(queryParam, keyword, pageNoTrans, pageSizeTrans);
        DataSet dataSet = new DataSet(search.getValue());
        bizModel.putDataSet(bizOptJson.getString("id"),dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
