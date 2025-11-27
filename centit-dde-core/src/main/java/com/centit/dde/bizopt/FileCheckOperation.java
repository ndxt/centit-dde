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
import com.centit.support.xml.XMLSchemaValidationUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Iterator;

public class FileCheckOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

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
            case "checkImageSize": {
                String imageSizeExpr = bizOptJson.getString("ruleExpr");
                ImageInfo imageInfo = getImageInfo(fileInfo.getFileInputStream());
                Object res = VariableFormula.calculate(imageSizeExpr, CollectionsOpt.createHashMap(
                    "width", imageInfo.width, "height", imageInfo.height, "dpi", imageInfo.dpi));

                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        "图像大小为：" + imageInfo.width + "*" + imageInfo.height + "分辨率为：" + imageInfo.dpi + "，大小不符合：" + imageSizeExpr);
                }
                break;
            }
        }
        if (responseData == null)
            return BuiltInOperation.createResponseData(0, 1, 500, "未知文件校验操作！");
        bizModel.putDataSet(targetDsName, new DataSet(responseData));
        return BuiltInOperation.createResponseSuccessData(1);
    }

    private ImageInfo getImageInfo(InputStream inputStream) {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image != null) {
                ImageInfo info = new ImageInfo();
                info.width = image.getWidth();
                info.height = image.getHeight();
                info.dpi = 72; // 默认DPI

                // 如果需要从元数据获取实际DPI
                ImageInputStream iis = ImageIO.createImageInputStream(inputStream);
                Iterator<ImageReader> readers = ImageIO.getImageReaders(image);

                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(iis);
                    IIOMetadata metadata = reader.getImageMetadata(0);

                    if (metadata != null) {
                        info.dpi = parseStandardDpi(metadata);
                    }
                }

                return info;
            }
        } catch (Exception e) {
            // 异常处理
            e.printStackTrace();
        }
        return new ImageInfo(0, 0, 72); // 默认值
    }

    // 内部类用于封装图片信息
    private static class ImageInfo {
        int width;
        int height;
        int dpi;

        ImageInfo() {
        }

        ImageInfo(int width, int height, int dpi) {
            this.width = width;
            this.height = height;
            this.dpi = dpi;
        }
    }

    private int parseStandardDpi(IIOMetadata metadata) {
        try {
            Node root = metadata.getAsTree("javax_imageio_1.0");
            NodeList childNodes = root.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if ("Dimension".equals(node.getNodeName())) {
                    return parseDimensionNode(node);
                }
            }
        } catch (Exception e) {
            // 异常处理
        }
        return 72;
    }

    private int parseDimensionNode(Node dimensionNode) {
        try {
            NodeList dimensionNodes = dimensionNode.getChildNodes();
            double pixelSizeX = 0.28; // 默认值 1/25.4 inch
            double pixelSizeY = 0.28;

            for (int j = 0; j < dimensionNodes.getLength(); j++) {
                Node dimNode = dimensionNodes.item(j);
                if ("HorizontalPixelSize".equals(dimNode.getNodeName())) {
                    NamedNodeMap attrs = dimNode.getAttributes();
                    Node value = attrs.getNamedItem("value");
                    if (value != null) {
                        pixelSizeX = Double.parseDouble(value.getNodeValue());
                    }
                } else if ("VerticalPixelSize".equals(dimNode.getNodeName())) {
                    NamedNodeMap attrs = dimNode.getAttributes();
                    Node value = attrs.getNamedItem("value");
                    if (value != null) {
                        pixelSizeY = Double.parseDouble(value.getNodeValue());
                    }
                }
            }

            // 计算DPI并返回代表性值
            int dpiX = (int) Math.round(25.4 / pixelSizeX);
            int dpiY = (int) Math.round(25.4 / pixelSizeY);

            // 返回平均DPI值作为代表性DPI
            return (dpiX + dpiY) / 2;
        } catch (Exception e) {
            return 72;
        }
    }
}
