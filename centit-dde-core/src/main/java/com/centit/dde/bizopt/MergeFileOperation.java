package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.json.JSONTransformer;
import com.centit.support.office.DocOptUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 合并 图片文件、pdf文档、 文本文件
 */
public class MergeFileOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        // 文件类型 pdf、 txt、 jpeg
        String fileType = BuiltInOperation.getJsonFieldString(bizOptJson, "fileType", "pdf");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if("pdf".equals(fileType)){
            List<FileDataSet> files = FileDataSetOptUtil.fetchFiles(dataSet, bizOptJson);
            if(files.isEmpty()){
                throw new ObjectException(ObjectException.EMPTY_RESULT_EXCEPTION, "pdf文件数据获取失败");
            }
            String fileNameDesc = BuiltInOperation.getJsonFieldString(bizOptJson, ConstantValue.FILE_NAME, "");
            BizModelJSONTransform transformer = new BizModelJSONTransform(bizModel, dataSet.getData());
            String fileName = null;
            if(StringUtils.isNotBlank(fileNameDesc)){
                fileName = StringBaseOpt.objectToString(JSONTransformer.transformer(fileNameDesc, transformer));
            }
            if(StringUtils.isBlank(fileName)) {
                fileName = DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(),"yyyyMMDD_HHmm") + ".pdf";
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            List<InputStream> osPdfs = new ArrayList<>();
            for(FileDataSet file : files){
                osPdfs.add(file.getFileInputStream());
            }
            DocOptUtil.mergePdfFiles(baos , osPdfs);
            bizModel.putDataSet(targetDsName, new FileDataSet(fileName, baos.size(), baos));
            return BuiltInOperation.createResponseSuccessData(1);
        }

        return BuiltInOperation.createResponseSuccessData(0);
    }
}
