package com.centit.dde.qrcode.utils;

import com.centit.dde.qrcode.config.QrCodeConfig;
import com.google.zxing.common.BitMatrix;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MatrixToImageUtil {

    /**
     * 根据二维码配置 & 二维码矩阵生成二维码图片
     *
     * @param qrCodeConfig
     * @param bitMatrix
     * @return
     * @throws IOException
     */
    public static BufferedImage toBufferedImage(QrCodeConfig qrCodeConfig, BitMatrix bitMatrix) throws IOException {
        int qrCodeWidth = bitMatrix.getWidth();
        int qrCodeHeight = bitMatrix.getHeight();
        BufferedImage qrCode = new BufferedImage(qrCodeWidth, qrCodeHeight, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < qrCodeWidth; x++) {
            for (int y = 0; y < qrCodeHeight; y++) {
                qrCode.setRGB(x, y,
                    bitMatrix.get(x, y) ?
                        qrCodeConfig.getMatrixToImageConfig().getPixelOnColor() :
                        qrCodeConfig.getMatrixToImageConfig().getPixelOffColor());
            }
        }
        // 插入logo
        if (!(qrCodeConfig.getLogo() == null || "".equals(qrCodeConfig.getLogo()))) {
            ImageUtil.insertLogo(qrCode, qrCodeConfig.getLogo());
        }
        //插入二维码头文字信息
        if(StringUtils.isNotBlank(qrCodeConfig.getTopText()) || StringUtils.isNotBlank(qrCodeConfig.getDownText())){
            qrCode = addTextInfo(qrCode,qrCodeConfig);
        }
     /*   // 若二维码的实际宽高和预期的宽高不一致, 则缩放
        int realQrCodeWidth = qrCodeConfig.getQrWidth();
        int realQrCodeHeight = qrCodeConfig.getQrHeight();
        if (qrCodeWidth != realQrCodeWidth || qrCodeHeight != realQrCodeHeight) {
            BufferedImage tmp = new BufferedImage(realQrCodeWidth, realQrCodeHeight, BufferedImage.TYPE_INT_RGB);
            tmp.getGraphics().drawImage(
                qrCode.getScaledInstance(realQrCodeWidth, realQrCodeHeight,Image.SCALE_SMOOTH),0, 0, null);
            qrCode = tmp;
        }*/
        return qrCode;
    }
    /**
     * 增加底部的说明文字
     * @param source 二维码
     */
    public static BufferedImage addTextInfo(BufferedImage source,QrCodeConfig qrCodeConfig) {
        Graphics2D graphics = (Graphics2D)source.getGraphics();
        String topText = qrCodeConfig.getTopText();
        String downText = qrCodeConfig.getDownText();

        double topTextHeight = 0.0;
        double topTextWidth = 0.0;

        double downTextWidth = 0.0;
        double downTextHeight = 0.0;

        Font topTextFont = null;
        Font downTextFont = null;

        if(StringUtils.isNotBlank(topText)) {
            topTextFont = new Font(qrCodeConfig.getTopTextFontType(), Font.BOLD, qrCodeConfig.getTopTextFontSize());
            graphics.setFont(topTextFont);
            Rectangle2D stringBounds = topTextFont.getStringBounds(topText, graphics.getFontRenderContext());
            topTextHeight = stringBounds.getHeight();
            topTextWidth = stringBounds.getWidth();
        }

        if(StringUtils.isNotBlank(downText)) {
            downTextFont = new Font(qrCodeConfig.getDownTextFontType(), Font.BOLD, qrCodeConfig.getDownTextFontSize());
            graphics.setFont(downTextFont);
            Rectangle2D downTextStringBounds = downTextFont.getStringBounds(downText, graphics.getFontRenderContext());
            downTextWidth = downTextStringBounds.getWidth();
            downTextHeight = downTextStringBounds.getHeight();
        }
        double offSetHeight  = topTextHeight > 0 && downTextHeight > 0  ? (topTextHeight + downTextHeight) * 2 : (topTextHeight + downTextHeight) * 4;
        int newHeight = (int) (source.getHeight() + offSetHeight);
        int newQrWidth = newHeight;

        //在内存创建图片缓冲区  这里设置画板的宽高和类型
        BufferedImage outImage = new BufferedImage(newHeight,newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = outImage.createGraphics();
        //开启文字抗锯齿
        graph.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        // 画文字到新的面板
        graph.setBackground(Color.BLUE);
        graph.setColor(Color.white);
        graph.clearRect(0, 0, newQrWidth,newHeight);

        if(StringUtils.isNotBlank(topText)){
            graph.setFont(topTextFont);
            //drawString(文字信息、x轴、y轴)方法根据参数设置文字的坐标轴 ，根据需要来进行调整
            float x = (float) (newQrWidth - topTextWidth) / 2;
            float y = (float) topTextHeight;
            graph.drawString(topText,x,y);
        }

        if(StringUtils.isNotBlank(downText)){
            graph.setFont(downTextFont);
            int x = (int) (newQrWidth - downTextWidth) / 2;
            int y = (int) (newHeight -  downTextHeight);
            graph.drawString(downText,x,y);
        }
        // 在画布上画上二维码  X轴Y轴，宽度高度
        graph.drawImage(source, (newQrWidth - source.getWidth()) / 2, (newHeight - source.getHeight()) / 2, source.getWidth(), source.getHeight(), null);
        graph.dispose();
        outImage.flush();
        return outImage;
    }
}
