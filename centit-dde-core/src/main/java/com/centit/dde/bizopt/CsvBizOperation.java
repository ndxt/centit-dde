package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataSet;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.CsvDataSet;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zhf
 */
public class CsvBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String csvexpressions=BuiltInOperation.getJsonFieldString(bizOptJson,"csvexpressions",null);
        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (StringUtils.isNotBlank(csvexpressions)){
            List<InputStream> inputStreams = DataSetOptUtil.getInputStreamByFieldName(csvexpressions,dataSet);
            if (inputStreams.size()==0){
                return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：读取CSV文件异常，不支持的流类型转换！");
            }
            List<Object> objectList = new ArrayList<>();
            for (InputStream inputStream : inputStreams) {
                CsvDataSet csvDataSet = new CsvDataSet();
                csvDataSet.setInputStream(inputStream);
                SimpleDataSet  simpleDataSet= csvDataSet.load(null);
                objectList.add(simpleDataSet.getData());
            }
            bizModel.putDataSet(targetDsName,new SimpleDataSet(objectList));
        }else {
            return BuiltInOperation.getResponseData(0, 500, bizOptJson.getString("SetsName")+"：读取CSV文件异常，未指定参数");
        }
        return BuiltInOperation.getResponseSuccessData(1);
    }
}
