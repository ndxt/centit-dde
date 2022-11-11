package com.centit.dde.qrcode;

import com.centit.dde.qrcode.config.QrCodeConfig;
import com.centit.dde.qrcode.utils.FileUtil;
import com.centit.dde.qrcode.utils.MatrixToImageUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class QrCodeGenWrapper {
    private static final int QUIET_ZONE_SIZE = 4;

    public static QrCodeConfig.QrCodeConfigBuilder createQrCodeConfig() {
        return new QrCodeConfig.QrCodeConfigBuilder();
    }

    /**
     * 生成二维码流
     * @param qrCodeConfig 二维码配置信息
     * @return BufferedImage 二维码流
     * @throws WriterException
     * @throws IOException
     */
    public static BufferedImage asBufferedImage(QrCodeConfig qrCodeConfig) throws Exception {
        BitMatrix bitMatrix = encode(qrCodeConfig);
        return MatrixToImageUtil.toBufferedImage(qrCodeConfig, bitMatrix);
    }

    /**
     * 将二维码写到指定目录下
     * @param qrCodeConfig 二维码配置信息
     * @param absFileName  保存路径
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public static boolean asFile(QrCodeConfig qrCodeConfig, String absFileName) throws Exception {
        File file = FileUtil.createFile(absFileName);
        if (file == null) {
            throw new IllegalArgumentException("file not exists! absFile: " + absFileName);
        }
        BufferedImage bufferedImage = asBufferedImage(qrCodeConfig);
        if (!ImageIO.write(bufferedImage, qrCodeConfig.getPicType(), file)) {
            throw new IOException("save qrcode image error!");
        }
        return true;
    }

    public static void imagesToPdf( List<Image> imageList,ByteArrayOutputStream byteArrayOutputStream){
        Document document = new Document(PageSize.A4, 0, 0, 0, 0);
        try{
            PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();

            float a4Width = document.getPageSize().getWidth();
            float a4Height = document.getPageSize().getHeight();
            float imageWidth = imageList.get(0).getWidth();
            float imageHeight = imageList.get(0).getHeight();

            //每行能放几个二维码
            int rowTotalCount = (int)(a4Width/imageWidth);
            //一页a4一共能放几行
            int totalRowCount = (int)(a4Height/imageHeight);

            //每个二维码在x轴位置偏移倍数，
            float qrCodeX = 0;

            //记录这是当前行的第几个二维码
            int columnCount = 0;

            //记录当前页写到第几行了 默认第一行开始
            int rowCount = 1;

            for (int i = 0; i < imageList.size(); i++) {
                Image image = imageList.get(i);
                image.setAlignment(Image.MIDDLE);
                float x  , y;
                //当行的个数等于每行只能写入的总个数时进行换行
                if( ++columnCount   > rowTotalCount){
                    qrCodeX = 0;
                    rowCount ++;
                    columnCount = 1;
                }
                //超过一页时重新创建新的一页
                if (rowCount > totalRowCount){
                    document.setPageSize(new Rectangle(a4Width,a4Height));
                    document.newPage();
                    rowCount = 1;
                    qrCodeX = 0;
                    columnCount = 1;
                }
                //左边偏移量 始终保证二维码处于居中的位置
                float offsetX = (a4Width - (imageWidth * rowTotalCount)) / rowTotalCount;
                float offsetY = (a4Height - (imageHeight * totalRowCount)) / ( totalRowCount * 2 ) * rowCount;
                if (columnCount > 1 ){
                    offsetX += 6 * qrCodeX;
                }
                x = offsetX + imageWidth  * qrCodeX ++;
                y = a4Height - (imageHeight * rowCount) - offsetY;

                image.setAbsolutePosition( x , y );
                document.add(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            document.close();
        }
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter#encode(String, BarcodeFormat, int, int, Map)}
     */
    private static BitMatrix encode(QrCodeConfig qrCodeConfig) throws WriterException {
        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.Q;
        int quietZone = 1;
        if (qrCodeConfig.getHints() != null) {
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(qrCodeConfig.getHints().get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (qrCodeConfig.getHints().containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(qrCodeConfig.getHints().get(EncodeHintType.MARGIN).toString());
            }
            if (quietZone > QUIET_ZONE_SIZE) {
                quietZone = QUIET_ZONE_SIZE;
            } else if (quietZone < 0) {
                quietZone = 0;
            }
        }
        QRCode code = Encoder.encode(qrCodeConfig.getMsg(), errorCorrectionLevel, qrCodeConfig.getHints());
        return renderResult(code, qrCodeConfig.getQrWidth(), qrCodeConfig.getQrHeight(), quietZone);
    }


    /**
     * 对 zxing 的 QRCodeWriter 进行扩展, 解决白边过多的问题
     * <p/>
     * 源码参考 {@link com.google.zxing.qrcode.QRCodeWriter #renderResult(QRCode, int, int, int)}
     *
     * @param code
     * @param width
     * @param height
     * @param quietZone 取值 [0, 4]
     * @return
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        // xxx 二维码宽高相等, 即 qrWidth == qrHeight
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int qrWidth = inputWidth + (quietZone * 2);
        int qrHeight = inputHeight + (quietZone * 2);
        // 白边过多时, 缩放
        int minSize = Math.min(width, height);
        int scale = calculateScale(qrWidth, minSize);
        if (scale > 0) {
            if (log.isDebugEnabled()) {
                log.debug("qrCode scale enable! scale: {}, qrSize:{}, expectSize:{}x{}", scale, qrWidth, width, height);
            }
            int padding, tmpValue;
            // 计算边框留白
            padding = (minSize - qrWidth * scale) / QUIET_ZONE_SIZE * quietZone;
            tmpValue = qrWidth * scale + padding;
            if (width == height) {
                width = tmpValue;
                height = tmpValue;
            } else if (width > height) {
                width = width * tmpValue / height;
                height = tmpValue;
            } else {
                height = height * tmpValue / width;
                width = tmpValue;
            }
        }
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }

        return output;
    }


    /**
     * 如果留白超过15% , 则需要缩放
     * (15% 可以根据实际需要进行修改)
     *
     * @param qrCodeSize 二维码大小
     * @param expectSize 期望输出大小
     * @return 返回缩放比例, <= 0 则表示不缩放, 否则指定缩放参数
     */
    private static int calculateScale(int qrCodeSize, int expectSize) {
        if (qrCodeSize >= expectSize) {
            return 0;
        }
        int scale = expectSize / qrCodeSize;
        int abs = expectSize - scale * qrCodeSize;
        if (abs < expectSize * 0.15) {
            return 0;
        }
        return scale;
    }
}
