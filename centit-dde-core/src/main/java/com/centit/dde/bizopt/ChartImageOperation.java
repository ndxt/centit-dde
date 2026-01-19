package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.image.ImageOpt;
import com.centit.support.report.ChartImageUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class ChartImageOperation implements BizOperation {

    /**
     * 生成统计图图片文件
     * @param bizModel 业务模型
     * @param bizOptJson 业务操作参数
     * @param dataOptContext 数据操作上下文
     * @return 响应数据
     * @throws Exception 异常
     */
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //参数 图表类型 chartType ：柱状图 bar, 饼状图 pie, 折线图 line
        //参数 图片文件大小 imageWidth、 imageHeight
        String targetDsName = bizOptJson.getString("id");
        String title = bizOptJson.getString("title");
        String chartType = bizOptJson.getString("chartType");
        int imageWidth = NumberBaseOpt.castObjectToInteger(bizOptJson.get("width"), 400);
        int imageHeight = NumberBaseOpt.castObjectToInteger(bizOptJson.get("height"), 300);
        String source = bizOptJson.getString("source");
        DataSet dataSet = bizModel.getDataSet(source);
        if (dataSet == null || !(dataSet.getData() instanceof JSONObject)) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found2", source));
        }
        JSONObject chartData = (JSONObject)dataSet.getData();
        JSONObject chartStyle = bizOptJson.getJSONObject("style");

        BufferedImage image = ChartImageUtils.createChartImage(chartType, title, imageWidth, imageHeight, chartData, chartStyle);
        try (ByteArrayInputStream imageIS = ImageOpt.imageToInputStream(image)) {
            bizModel.putDataSet(targetDsName, new FileDataSet(title+".jpg", imageIS.available(), imageIS));
            // 直接用 BufferedImage 创建数据集应该也可以，不知道report中怎么用
            //bizModel.putDataSet(targetDsName, new FileDataSet(title+".jpg", (long) imageWidth * imageHeight, image));
            return BuiltInOperation.createResponseSuccessData(1);
        }
    }
}
