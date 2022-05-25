package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

/**
 * 生成CSV文件节点信息
 */
public class GenerateCsvFileBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);

        String fileName=StringUtils.isNotBlank(bizOptJson.getString("fileName"))?
            StringBaseOpt.castObjectToString(Pretreatment.mapTemplateStringAsFormula(
                bizOptJson.getString("fileName"), new BizModelJSONTransform(bizModel))):
            DatetimeOpt.currentTimeWithSecond();

        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);

        if (dataSet==null){
            return BuiltInOperation.createResponseData(0, 1,ResponseData.ERROR_OPERATION, bizOptJson.getString("SetsName")+"：生成CSV文件异常，请指定数据集！");
        }
        InputStream inputStream = CsvDataSet.createCsvStream(dataSet, bizOptJson);

        FileDataSet objectToDataSet =new FileDataSet(fileName.endsWith(".csv")?fileName:fileName+".csv",
            inputStream.available(), inputStream);
        bizModel.putDataSet(targetDsName,objectToDataSet);

        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }
}
