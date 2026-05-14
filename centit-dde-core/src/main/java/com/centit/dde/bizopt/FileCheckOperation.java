package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.FileDataSetOptUtil;
import com.centit.fileserver.common.FileInfoOpt;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.file.FileIOOpt;
import com.centit.support.file.FileType;
import com.centit.support.file.TxtTemplate;
import com.centit.support.xml.XMLSchemaValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileCheckOperation implements BizOperation {
    protected static final Logger logger = LoggerFactory.getLogger(FileCheckOperation.class);
    private final FileInfoOpt fileInfoOpt;

    public FileCheckOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }

    /**
     * 文件合法性检查 四性检测 组件一、组件三
     *
     * @param bizModel       业务模型
     * @param bizOptJson     业务操作参数
     * @param dataOptContext 数据操作上下文
     * @return 响应数据
     * @throws Exception 异常
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //参数 文件类型 fileType 通用 *, xml, jpg/jepg
        //参数 检测类型 checkType： 针对通用 检测文件大小 checkFileSize,  文件大小表达式 ruleExpr
        //   针对xml 检测xml是否符合XMLSchema checkXMLSchema ,  schema文件Id
        //  针对图片文件 检测图片文件格式 checkImageSize,  分辨率大小表达式 ruleExpr
        String targetDsName = bizOptJson.getString("id");
        //String fileType = bizOptJson.getString("fileType");
        String checkType = bizOptJson.getString("checkType");
        //获取文件数据集
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", "");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        FileDataSet fileInfo = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
        ResponseData responseData = null;
        switch (checkType) {
            case "checkFileSize": {
                String fileSizeExpr = bizOptJson.getString("ruleExpr");
                Object res = VariableFormula.calculate(fileSizeExpr, CollectionsOpt.createHashMap("fileSize", fileInfo.getSize()));
                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        "文件大小为：" + fileInfo.getSize() + ", 不符合：" + fileSizeExpr);
                }
                break;
            }
            case "checkXMLSchema": {
                String schemaFileId = bizOptJson.getString("schemaFileId");
                InputStream inputStreamXsd = fileInfoOpt.loadFileStream(schemaFileId);
                JSONObject jsonObj = XMLSchemaValidationUtil.validate(inputStreamXsd, fileInfo.getFileInputStream());
                responseData = HttpReceiveJSON.valueOf(jsonObj).toResponseData();
                break;
            }
            case "checkTxtTemplate": {
                String templateFileId = bizOptJson.getString("templateFileId");
                InputStream templateFile = fileInfoOpt.loadFileStream(templateFileId);
                TxtTemplate.CompareResult result = TxtTemplate.compareWithTemplate(
                    FileIOOpt.readStringFromInputStream(templateFile),
                    FileIOOpt.readStringFromInputStream(fileInfo.getFileInputStream())
                );
                responseData = HttpReceiveJSON.valueOf(result).toResponseData();
                break;
            }
            case "checkImageSize": {
                String imageSizeExpr = bizOptJson.getString("ruleExpr");
                ImageInfo imageInfo = getImageInfo(fileInfo.getFileInputStream());

                // 计算图片打印A4纸的等效DPI
                // A4纸尺寸: 210mm × 297mm = 8.27英寸 × 11.69英寸
                final double A4_WIDTH_INCH = 8.27;
                final double A4_HEIGHT_INCH = 11.69;
                double a4DpiByWidth = imageInfo.width / A4_WIDTH_INCH;
                double a4DpiByHeight = imageInfo.height / A4_HEIGHT_INCH;
                double a4EquivalentDpi = Math.min(a4DpiByWidth, a4DpiByHeight);

                Object res = VariableFormula.calculate(imageSizeExpr, CollectionsOpt.createHashMap(
                    "width", imageInfo.width,
                    "height", imageInfo.height,
                    "a4Dpi", a4EquivalentDpi));

                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        String.format("图像信息 - A4等效DPI：%.1f，图片尺寸：%dx%d，不满足条件：%s",
                            a4EquivalentDpi, imageInfo.width, imageInfo.height, imageSizeExpr));
                }
                break;
            }
            case "checkReadability": {
                String extName = FileType.getFileExtName(fileInfo.getFileName());
                responseData = checkFileReadability(fileInfo.getFileInputStream(), extName);
                break;
            }
            default:
                break;
        }
        if (responseData == null)
            return BuiltInOperation.createResponseData(0, 1, 500, "未知文件校验操作！");
        bizModel.putDataSet(targetDsName, new DataSet(responseData));
        return responseData;
    }

    /**
     * 获取图片信息(宽度、高度)
     *
     * @param inputStream 图片输入流
     * @return 图片信息对象
     */
    private ImageInfo getImageInfo(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image != null) {
                return new ImageInfo(image.getWidth(), image.getHeight());
            }
        } catch (Exception e) {
            logger.error("获取图片信息失败", e);
        }
        return new ImageInfo(0, 0);
    }

    // 内部类用于封装图片信息
    private static class ImageInfo {
        int width;
        int height;

        ImageInfo(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private static byte[] readAllBytes(InputStream is) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = is.read(buf)) != -1) {
            bos.write(buf, 0, n);
        }
        return bos.toByteArray();
    }

    private ResponseData checkFileReadability(InputStream inputStream, String extName) {
        try {
            byte[] data = readAllBytes(inputStream);
            if (data.length == 0) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "文件为空");
            }
            if (extName == null) {
                extName = "";
            }
            switch (extName.toLowerCase()) {
                case "pdf":
                    return checkPdfReadability(data);
                case "jpg":
                case "jpeg":
                    return checkJpegReadability(data);
                case "png":
                    return checkPngReadability(data);
                case "gif":
                    return checkGifReadability(data);
                case "bmp":
                    return checkBmpReadability(data);
                case "tiff":
                case "tif":
                    return checkTiffReadability(data);
                case "xml":
                    return checkXmlReadability(data);
                case "ofd":
                    return checkOfdReadability(data);
                case "doc":
                case "xls":
                    return checkOleReadability(data);
                case "docx":
                case "xlsx":
                    return checkZipOfficeReadability(data);
                default:
                    return ResponseData.makeSuccessResponse("未知文件类型，基础可读");
            }
        } catch (Exception e) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                "文件读取异常: " + e.getMessage());
        }
    }

    private static ResponseData checkPdfReadability(byte[] data) {
        if (data.length < 5) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PDF文件过小");
        }
        String header = new String(data, 0, 5);
        if (!"%PDF-".equals(header)) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PDF文件头损坏");
        }
        int tailStart = Math.max(0, data.length - 1024);
        String tail = new String(data, tailStart, data.length - tailStart);
        if (!tail.contains("%%EOF")) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PDF文件结尾标记缺失");
        }
        if (!tail.contains("startxref")) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PDF交叉引用表缺失");
        }
        return ResponseData.makeSuccessResponse("PDF文件可正常读取");
    }

    private static ResponseData checkJpegReadability(byte[] data) {
        if (data.length < 4) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "JPEG文件过小");
        }
        if ((data[0] & 0xFF) != 0xFF || (data[1] & 0xFF) != 0xD8 || (data[2] & 0xFF) != 0xFF) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "JPEG文件头标识损坏");
        }
        if ((data[data.length - 2] & 0xFF) != 0xFF || (data[data.length - 1] & 0xFF) != 0xD9) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "JPEG文件结尾标识损坏");
        }
        return ResponseData.makeSuccessResponse("JPEG文件可正常读取");
    }

    private static ResponseData checkPngReadability(byte[] data) {
        if (data.length < 8) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PNG文件过小");
        }
        int[] pngMagic = {0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
        for (int i = 0; i < 8; i++) {
            if ((data[i] & 0xFF) != pngMagic[i]) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PNG文件头标识损坏");
            }
        }
        int offset = data.length - 8;
        if (data[offset] != 0x49 || data[offset + 1] != 0x45 || data[offset + 2] != 0x4E || data[offset + 3] != 0x44) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "PNG文件结尾标识损坏");
        }
        return ResponseData.makeSuccessResponse("PNG文件可正常读取");
    }

    private static ResponseData checkGifReadability(byte[] data) {
        if (data.length < 6) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "GIF文件过小");
        }
        String header = new String(data, 0, 6);
        if (!"GIF87a".equals(header) && !"GIF89a".equals(header)) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "GIF文件头标识损坏");
        }
        if (data[data.length - 1] != 0x3B) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "GIF文件结尾标识损坏");
        }
        return ResponseData.makeSuccessResponse("GIF文件可正常读取");
    }

    private static ResponseData checkBmpReadability(byte[] data) {
        if (data.length < 6) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "BMP文件过小");
        }
        if (data[0] != 0x42 || data[1] != 0x4D) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "BMP文件头标识损坏");
        }
        int declaredSize = (data[5] & 0xFF) << 24 | (data[4] & 0xFF) << 16
            | (data[3] & 0xFF) << 8 | (data[2] & 0xFF);
        if (declaredSize > 0 && declaredSize != data.length) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "BMP声明大小与实际不符");
        }
        return ResponseData.makeSuccessResponse("BMP文件可正常读取");
    }

    private static ResponseData checkTiffReadability(byte[] data) {
        if (data.length < 4) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "TIFF文件过小");
        }
        boolean le = data[0] == 0x49 && data[1] == 0x49;
        boolean be = data[0] == 0x4D && data[1] == 0x4D;
        if (!le && !be) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "TIFF文件头标识损坏");
        }
        int magic = le
            ? (data[3] & 0xFF) << 8 | (data[2] & 0xFF)
            : (data[2] & 0xFF) << 8 | (data[3] & 0xFF);
        if (magic != 42) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "TIFF魔数校验失败");
        }
        return ResponseData.makeSuccessResponse("TIFF文件可正常读取");
    }

    private static ResponseData checkXmlReadability(byte[] data) {
        try {
            String content = new String(data, StandardCharsets.UTF_8).trim();
            if (!content.startsWith("<")) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "XML缺少起始标签");
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.newDocumentBuilder().parse(new ByteArrayInputStream(data));
            return ResponseData.makeSuccessResponse("XML文件可正常解析");
        } catch (org.xml.sax.SAXParseException e) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                "XML格式错误(行" + e.getLineNumber() + "): " + e.getMessage());
        } catch (Exception e) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                "XML解析异常: " + e.getMessage());
        }
    }

    private static ResponseData checkOfdReadability(byte[] data) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data))) {
            boolean hasEntry = false;
            boolean hasDocRoot = false;
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                hasEntry = true;
                String name = entry.getName();
                if ("OFD.xml".equals(name) || name.endsWith("/OFD.xml")) {
                    hasDocRoot = true;
                }
                zis.closeEntry();
            }
            if (!hasEntry) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "OFD文件为空ZIP包");
            }
            if (!hasDocRoot) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "OFD缺少OFD.xml入口文件");
            }
            return ResponseData.makeSuccessResponse("OFD文件可正常读取");
        } catch (Exception e) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                "OFD解析异常: " + e.getMessage());
        }
    }

    private static ResponseData checkOleReadability(byte[] data) {
        if (data.length < 8) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "Office文件过小");
        }
        long oleMagic = 0xE11AB1A1E011CFD0L;
        long fileMagic = ((long)(data[0] & 0xFF))
            | ((long)(data[1] & 0xFF) << 8)
            | ((long)(data[2] & 0xFF) << 16)
            | ((long)(data[3] & 0xFF) << 24)
            | ((long)(data[4] & 0xFF) << 32)
            | ((long)(data[5] & 0xFF) << 40)
            | ((long)(data[6] & 0xFF) << 48)
            | ((long)(data[7] & 0xFF) << 56);
        if (fileMagic != oleMagic) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR, "OLE文件头标识损坏");
        }
        return ResponseData.makeSuccessResponse("Office文件可正常读取");
    }

    private static ResponseData checkZipOfficeReadability(byte[] data) {
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data))) {
            boolean hasContentTypes = false;
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if ("[Content_Types].xml".equals(entry.getName())) {
                    hasContentTypes = true;
                }
                zis.closeEntry();
            }
            if (!hasContentTypes) {
                return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                    "Office文件缺少[Content_Types].xml");
            }
            return ResponseData.makeSuccessResponse("Office文件可正常读取");
        } catch (Exception e) {
            return ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                "Office解析异常: " + e.getMessage());
        }
    }
}
