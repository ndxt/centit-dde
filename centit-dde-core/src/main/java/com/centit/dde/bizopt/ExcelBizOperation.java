package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.report.ExcelImportUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author codefan
 * 读取excel中的内容
 */
public class ExcelBizOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String id =bizOptJson.getString("id");
        String source = bizOptJson.getString("source");
        Map<String, Object> params = new HashMap<>();
        BizModelJSONTransform modelTrasform = new BizModelJSONTransform(bizModel);
        String sheetName = bizOptJson.getString("sheetName");
        if( StringUtils.isNotBlank(sheetName)) {
            sheetName = Pretreatment.mapTemplateStringAsFormula(sheetName, modelTrasform);
        }

        DataSet dataSet = bizModel.fetchDataSetByName(source);
        Map<String, Object> fileInfo = DataSetOptUtil.getFileFormDataset(dataSet, bizOptJson);
        InputStream inputStream = DataSetOptUtil.getInputStreamFormFile(fileInfo);

        int beginRow = NumberBaseOpt.castObjectToInteger(
            modelTrasform.attainExpressionValue(bizOptJson.getString("beginRow")), 0);
        int endRow = NumberBaseOpt.castObjectToInteger(
            modelTrasform.attainExpressionValue(bizOptJson.getString("endRow")), 0);


        boolean headerInRow = BooleanBaseOpt.castObjectToBoolean(params.get("headerInRow"), true);
        if(headerInRow){
            String startColumnNumber =
                StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("startColumnNumber")));
            int beginColumn =  StringUtils.isBlank(startColumnNumber)? 0 : ExcelImportUtil.mapColumnIndex(startColumnNumber);

            String endColumnNumber =
                StringBaseOpt.castObjectToString(modelTrasform.attainExpressionValue(bizOptJson.getString("endColumnNumber")));
            int endColumn =  StringUtils.isBlank(endColumnNumber)? 0 : ExcelImportUtil.mapColumnIndex(endColumnNumber);
            Integer headerRow =  NumberBaseOpt.castObjectToInteger(bizOptJson.getString("headerRow"), 0);
            List<Map<String, Object>> datas = StringUtils.isNotBlank(sheetName)?
                ExcelImportUtil.loadMapFromExcelSheet(inputStream,sheetName,headerRow,beginRow,endRow,beginColumn,endColumn):
                ExcelImportUtil.loadMapFromExcelSheet(inputStream,0,headerRow,beginRow,endRow,beginColumn,endColumn);

            DataSet simpleDataSet= new DataSet(datas);
            bizModel.putDataSet(id, simpleDataSet);
            return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());

        } else {
            Map<String, String> headers = CollectionsOpt.mapCollectionToMap(bizOptJson.getJSONArray("headers"),
                (a) -> ((JSONObject) a).getString("columnName"),  (a) -> ((JSONObject) a).getString("fieldName"));
            Map<Integer, String> headDesc = ExcelImportUtil.mapColumnIndex(headers);
            List<Map<String, Object>> datas = StringUtils.isNotBlank(sheetName)?
                ExcelImportUtil.loadMapFromExcelSheet(inputStream,sheetName,headDesc,beginRow,endRow):
                ExcelImportUtil.loadMapFromExcelSheet(inputStream,0,headDesc,beginRow,endRow);

            DataSet simpleDataSet= new DataSet(datas);
            bizModel.putDataSet(id, simpleDataSet);
            return BuiltInOperation.createResponseSuccessData(simpleDataSet.getSize());
        }
    }
}
