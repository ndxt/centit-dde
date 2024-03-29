package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 生成json文件节点信息
 */
public class WriteJsonFileOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws IOException {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String fileName = StringUtils.isNotBlank(bizOptJson.getString("fileName"))?
            StringBaseOpt.castObjectToString(Pretreatment.mapTemplateStringAsFormula(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel))):
            DatetimeOpt.currentTimeWithSecond();
        Object data = bizModel.getDataSet(sourDsName).getData();
        String object = JSON.toJSONString(data);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(object.getBytes());

        FileDataSet objectToDataSet =new FileDataSet(fileName.endsWith(".json")?fileName:fileName+".json",
            inputStream.available(), inputStream);

        bizModel.putDataSet(targetDsName,objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
