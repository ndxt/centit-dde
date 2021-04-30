package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class ExcelBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());

        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);

        String excelexpression=BuiltInOperation.getJsonFieldString(bizOptJson,"excelexpression",null);

        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet==null){
            return BuiltInOperation.getResponseData(0, 500,
                bizOptJson.getString("SetsName")+"：读取EXCEL文件异常，请选择数据集！");
        }
        List<InputStream> inputStreams;
        List<Object> objectList = new ArrayList<>();
        if (StringUtils.isNotBlank(excelexpression)){
            inputStreams  = DataSetOptUtil.getInputStreamByFieldName(excelexpression,dataSet);
            if (inputStreams.size()==0){
                return BuiltInOperation.getResponseData(0, 500,
                    bizOptJson.getString("SetsName")+"：读取EXCEL文件异常，不支持的流类型转换！");
            }
        }else {
            inputStreams = DataSetOptUtil.getInputStreamByFieldName(dataSet);
        }
        for (InputStream inputStream : inputStreams) {
            ExcelDataSet excelDataSet = new ExcelDataSet();
            excelDataSet.setInputStream(inputStream);
            SimpleDataSet  simpleDataSet= excelDataSet.load(null);
            objectList.add(simpleDataSet.getData());
        }
        bizModel.putDataSet(targetDsName,new SimpleDataSet(objectList));
        return BuiltInOperation.getResponseSuccessData(1);
    }
}
