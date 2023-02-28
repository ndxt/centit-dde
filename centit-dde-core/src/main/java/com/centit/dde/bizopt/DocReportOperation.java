package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.dataset.FileDataSet;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.ReflectionOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.Pretreatment;
import com.centit.support.file.FileIOOpt;
import com.centit.support.report.JsonDocxContext;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

import java.io.*;
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
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", bizModel.getModelName());
        String filePath = bizOptJson.getString("fileId");
        String fileName = Pretreatment.mapTemplateString(BuiltInOperation.getJsonFieldString(bizOptJson, "documentName", bizModel.getModelName()), bizModel)
            + ".pdf";
        Map<String, String> params = BuiltInOperation.jsonArrayToMap(bizOptJson.getJSONArray("config"), "columnName", "cName");
        ByteArrayInputStream in = generateWord(bizModel, filePath, params);

        FileDataSet dataSet =new FileDataSet(fileName,
            -1, word2Pdf(in));

        bizModel.putDataSet(sourDsName, dataSet);
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
                int namePos = imageName.indexOf("[*]");
                int pathPos = fileFieldPath.indexOf("[*]");
                if (namePos > 0 && pathPos > 0) {
                    //数组通配符; 目前只能做一维数组，也就是只有一个通配符
                    String nameH = imageName.substring(0, namePos);
                    String nameT = imageName.substring(namePos + 3);
                    String pathH = fileFieldPath.substring(0, pathPos);
                    String pathT = fileFieldPath.substring(pathPos + 3);
                    int j = 0;
                    while (true) {
                        Object fieldValue = ReflectionOpt.attainExpressionValue(docData, pathH + "[" + j + "]" + pathT);
                        if (fieldValue == null) {
                            break;
                        }
                        addImageMeta(metadata, docData, fieldValue, nameH + "_" + j + "_" + nameT, "image_" + i + "_" + j);
                        j++;
                    }
                } else {
                    Object fieldValue = ReflectionOpt.attainExpressionValue(docData, fileFieldPath);
                    if (fieldValue != null) {
                        addImageMeta(metadata, docData, fieldValue, imageName, "image_" + i);
                    }
                }
            }
        }
        return metadata;
    }

    private void addImageMeta(FieldsMetadata metadata, JSONObject docData, Object fieldValue,
                              String imageName, String placeholder) throws Exception {
        metadata.addFieldAsImage(imageName, placeholder);
        //书签，数据集+img_+图片字段
        if (fieldValue instanceof byte[]) {
            docData.put(placeholder, new ByteArrayImageProvider((byte[]) fieldValue));
        } else if (fieldValue instanceof String) {
            String fileId = StringBaseOpt.castObjectToString(fieldValue);
            docData.put(placeholder, new ByteArrayImageProvider(FileIOOpt.readBytesFromFile(fileInfoOpt.getFile(fileId))));
        }
    }

    private ByteArrayInputStream generateWord(BizModel dataModel, String filePath, Map<String, String> params) throws Exception {
        JSONObject docData = dataModel.toJsonObject(false);
        // 准备图片元数据
        FieldsMetadata metadata = getFieldsMetadata(params, docData);
        try (InputStream in = new FileInputStream(fileInfoOpt.getFile(filePath))) {
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
