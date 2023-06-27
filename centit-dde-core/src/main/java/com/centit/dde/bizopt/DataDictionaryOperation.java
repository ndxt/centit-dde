package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.components.CodeRepositoryUtil;
import com.centit.framework.model.basedata.IDataDictionary;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author zhf
 */
public class DataDictionaryOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext){

        String catalog = bizOptJson.getString("catalog");
        boolean asTree = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("asTree"), false);
        String startCode = bizOptJson.getString("startCode");
        int levels = NumberBaseOpt.castObjectToInteger(bizOptJson.get("levels"), -1);

        List<? extends IDataDictionary> dicts = CodeRepositoryUtil.getDictionary(catalog);
        if(asTree) {
            if(StringUtils.isNotBlank(startCode)){
                TreeNode<? extends IDataDictionary> treeNode =
                    CollectionsOpt.fetchTreeBranch( dicts, (b) -> StringUtils.equals(startCode , b.getDataCode()) ,
                    (p, c) -> StringUtils.equals(p.getDataCode(), c.getExtraCode()), levels);
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(treeNode.toJSONObject()));
            } else {
                List<? extends TreeNode<? extends IDataDictionary>> dataAsTree = CollectionsOpt.storedAsTree(dicts,
                    (p, c) -> StringUtils.equals(p.getDataCode(), c.getExtraCode()));
                bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(TreeNode.toJSONArray(dataAsTree)));
            }
        } else {
            bizModel.putDataSet(bizOptJson.getString("id"), new DataSet(dicts));
        }
        return BuiltInOperation.createResponseSuccessData(1);
    }

}
