package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.file.FileIOOpt;
import com.centit.support.json.JSONTransformer;
import com.centit.support.report.JsonDocxContext;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
public class DocReportOperation implements BizOperation {
    private FileInfoOpt fileInfoOpt;

    public DocReportOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String fileId = bizOptJson.getString("fileId");
        boolean transToPdf = BooleanBaseOpt.castObjectToBoolean(bizOptJson.get("transToPdf"), true);

        String fileName = Pretreatment.mapTemplateString(
            BuiltInOperation.getJsonFieldString(bizOptJson, "documentName", bizModel.getModelName()),
            new BizModelJSONTransform(bizModel));
        if(transToPdf){
            if(! StringUtils.endsWithIgnoreCase(fileName, ".pdf")) {
                fileName = fileName +".pdf";
            }
        } else {
            if(! StringUtils.endsWithIgnoreCase(fileName, ".docx")) {
                fileName = fileName +".docx";
            }
        }
        JSONArray fieldInfos = bizOptJson.getJSONArray("config");
        Map<String, String> params =new HashMap<>();
        if (fieldInfos != null) {
            for (Object fieldInfo : fieldInfos) {
                if (fieldInfo instanceof Map) {
                    Map<String, Object> map = CollectionsOpt.objectToMap(fieldInfo);
                    String columnName = StringBaseOpt.objectToString(map.get("columnName"));
                    String expression = StringBaseOpt.objectToString(map.get("expression"));
                    if (StringUtils.isBlank(expression)) {
                        continue;
                    }
                    String value = StringBaseOpt.objectToString(JSONTransformer.transformer(expression, bizModel));
                    params.put(columnName, value);
                }
            }
        }
        ByteArrayInputStream in = generateWord(bizModel, fileId, params);

        FileDataSet dataSet = transToPdf? new FileDataSet(fileName,
            -1, word2Pdf(in)) : new FileDataSet(fileName, -1, in);

        bizModel.putDataSet(targetDsName, dataSet);
        return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
    }

    private FieldsMetadata getFieldsMetadata(Map<String, String> params, JSONObject docData) throws Exception {
        FieldsMetadata metadata = new FieldsMetadata();
        if (params != null) {
            int i = 0;
            for (Map.Entry<String, String> map : params.entrySet()) {
                i++;
                String imageName = map.getKey();
                String fileFieldPath = map.getValue();
                addImageMeta(metadata, docData, fileFieldPath, imageName, "image_" + i);
                }
            }
        return metadata;
    }

    private void addImageMeta(FieldsMetadata metadata, JSONObject docData, String fieldValue,
                              String imageName, String placeholder) throws Exception {
        metadata.addFieldAsImage(imageName, placeholder);
        //书签，数据集+img_+图片字段
        docData.put(placeholder, new ByteArrayImageProvider(FileIOOpt.readBytesFromFile(fileInfoOpt.getFile(fieldValue))));
    }

    private ByteArrayInputStream generateWord(BizModel dataModel, String fileId, Map<String, String> params) throws Exception {
        JSONObject docData = dataModel.toJsonObject(false);
        // 准备图片元数据
        FieldsMetadata metadata = getFieldsMetadata(params, docData);
        try (InputStream in = new FileInputStream(fileInfoOpt.getFile(fileId))) {
            // 1) Load ODT file and set Velocity template engine and cache it to the registry
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Freemarker, false);
            // 2) Create Java model context
            IContext context = new JsonDocxContext(docData);
            report.setFieldsMetadata(metadata);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            report.process(context, out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException | XDocReportException e) {
            throw new ObjectException(ResponseData.ERROR_PROCESS_FAILED, e.getMessage());
        }
    }

    private OutputStream word2Pdf(InputStream in) {
        OutputStream outPdf = new ByteArrayOutputStream();
        com.centit.support.office.OfficeToPdf.word2Pdf(in, outPdf, "docx");
        return outPdf;
    }
}
