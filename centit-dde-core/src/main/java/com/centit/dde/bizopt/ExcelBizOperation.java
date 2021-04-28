package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.dataset.ExcelDataSet;
import com.centit.dde.utils.GetJsonFieldValueUtils;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.centit.support.report.ExcelExportUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * @author zhf
 */
public class ExcelBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        String id=bizOptJson.getString("id");
        String source=bizOptJson.getString("source");
        String excelexpression = bizOptJson.getString("excelexpression");
        if (StringUtils.isNotBlank(excelexpression)){
            JSONArray jsonFieldValue = GetJsonFieldValueUtils.getJsonFieldValue(excelexpression, bizModel.getDataSet(source));
            bizModel.putDataSet(id,new SimpleDataSet(jsonFieldValue));
        }else {
            bizModel.putDataSet(id,new SimpleDataSet(bizModel.getDataSet(source).getData()));
        }
        return BuiltInOperation.getResponseSuccessData(bizModel.getDataSet(source).getSize());
    }
}
