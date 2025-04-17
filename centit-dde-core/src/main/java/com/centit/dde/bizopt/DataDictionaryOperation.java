package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.DataDictionary;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.TreeNode;
import com.centit.support.json.JSONTransformer;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author zhf
 */
public class DataDictionaryOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){
        BizModelJSONTransform bizModelJSONTransform = new BizModelJSONTransform(bizModel);
        String catalog = bizOptJson.getString("catalog");
        catalog=StringBaseOpt.castObjectToString(JSONTransformer.transformer(catalog, bizModelJSONTransform));
        boolean asTree = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("asTree"), false);
        String startCode = bizOptJson.getString("startCode");
        startCode=StringBaseOpt.castObjectToString(JSONTransformer.transformer(startCode, bizModelJSONTransform));
        int levels = NumberBaseOpt.castObjectToInteger(bizOptJson.get("levels"), -1);

        List<DataDictionary> dicts = CodeRepositoryUtil.getDictionary(catalog);
        if(asTree) {
            if(StringUtils.isNotBlank(startCode)){
                String finalStartCode = startCode;
                TreeNode<DataDictionary> treeNode =
                    CollectionsOpt.fetchTreeBranch( dicts, (b) -> StringUtils.equals(finalStartCode, b.getDataCode()) ,
                    (p, c) -> StringUtils.equals(p.getDataCode(), c.getExtraCode()), levels);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(treeNode.toJSONObject()));
            } else {
                List<? extends TreeNode<DataDictionary>> dataAsTree = CollectionsOpt.storedAsTree(dicts,
                    (p, c) -> StringUtils.equals(p.getDataCode(), c.getExtraCode()));
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(TreeNode.toJSONArray(dataAsTree)));
            }
        } else {
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(dicts));
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
