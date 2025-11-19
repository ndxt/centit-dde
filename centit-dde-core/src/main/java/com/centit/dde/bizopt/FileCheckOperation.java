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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class FileCheckOperation implements BizOperation {

    private FileInfoOpt fileInfoOpt;

    public FileCheckOperation(FileInfoOpt fileInfoOpt) {
        this.fileInfoOpt = fileInfoOpt;
    }
    /**
     * 文件合法性检查 四性检测 组件一、组件三
     * @param bizModel 业务模型
     * @param bizOptJson 业务操作参数
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
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source","");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        FileDataSet fileInfo = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
        ResponseData responseData = null;
        switch (checkType){
            case "checkFileSize": {
                String fileSizeExpr = bizOptJson.getString("ruleExpr");
                Object res = VariableFormula.calculate(fileSizeExpr, CollectionsOpt.createHashMap("fileSize", fileInfo.getSize()));
                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        "文件大小为："+fileInfo.getSize()+", 不符合：" + fileSizeExpr);
                }
                break;
            }
            case "checkXMLSchema": {
                String schemaFileId = bizOptJson.getString("schemaFileId");
                InputStream inputStreamXsd = fileInfoOpt.loadFileStream(schemaFileId);
                JSONObject jsonObj = XMLSchemaValidationUtil.validate(inputStreamXsd,fileInfo.getFileInputStream());
                responseData = HttpReceiveJSON.valueOf(jsonObj).toResponseData();
                break;
            }
            case "checkImageSize": {
                String imageSizeExpr = bizOptJson.getString("ruleExpr");
                BufferedImage image = ImageIO.read(fileInfo.getFileInputStream());
                Object res = VariableFormula.calculate(imageSizeExpr, CollectionsOpt.createHashMap(
                    "width", image.getWidth(), "height", image.getHeight()));

                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        "图像大小为："+image.getWidth()+"*"+image.getHeight()+"，大小不符合：" + imageSizeExpr);
                }
                break;
            }
            //  针对图片文件 检测图片文件格式 checkImageDpi,  分辨率大小表达式 ruleExpr
            /*case "checkImageDpi": {
                String imageDpiExpr = bizOptJson.getString("ruleExpr");
                BufferedImage image = ImageIO.read(fileInfo.getFileInputStream());
                Object res = VariableFormula.calculate(imageDpiExpr, CollectionsOpt.createHashMap(
                    "dpi", image.get);

                if (BooleanBaseOpt.castObjectToBoolean(res, false)) {
                    responseData = ResponseData.makeSuccessResponse();
                } else {
                    responseData = ResponseData.makeErrorMessage(ObjectException.DATA_VALIDATE_ERROR,
                        "图像的分辨率为："+image.getWidth()+"*"+image.getHeight()+"，大小不符合：" + imageDpiExpr);
                }
                break;
            }*/
        }
        if(responseData == null)
            return BuiltInOperation.createResponseData(0, 1, 500, "未知文件校验操作！");
        bizModel.putDataSet(targetDsName, new DataSet(responseData));
        return BuiltInOperation.createResponseSuccessData(1);
    }
}
