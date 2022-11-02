import com.centit.dde.qrcode.QrCodeGenWrapper;
import com.centit.dde.qrcode.config.QrCodeConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestQrCode {
    public static void main(String[] args) {
        String context = "{\"deviceId\":\"#0001\",\"deviceCode\":\"898687\",\"deviceLabel\":\"23433\",\"deviceName\":\"笔记本电脑\",\"deviceType\":\"电脑\"}";
        // 生成的二维码的路径及名称
        String destPath = "C:/Users/user/Desktop/1.jpg";
        // 嵌入二维码的图片路径
        String logImgPath = "https://cloud.centit.com/locode/api/fileserver/fileserver/download/preview/0a8ac34c2349402b91fab132e39ac3fb?accessToken=0b2a1e59-e0a2-49a7-b085-69d9c34ca3b8";
        // 简单的生成
        QrCodeConfig qrCodeConfig = QrCodeGenWrapper.createQrCodeConfig()
            .setMsg(context)
            .setQrHeight(300)
            .setQrWidth(300)
            //白边预留值  取值 0-4  0最小
            //.setPadding(0)
            //.setTopText("测试二维码")
            //.setTopTextFontSize(12)
            //.setTopTextFontType("雅黑")
            //.setDownText("江苏省-南京市-雨花台区")
            //.setDownTextFontSize(10)
            //.setDownTextFontType("宋体")
            //二维码中心logo图片
            //.setLogo(logImgPath)
            .build();
        try {
            BufferedImage bufferedImage = QrCodeGenWrapper.asBufferedImage(qrCodeConfig);
            QrCodeGenWrapper.asFile(qrCodeConfig,destPath);
            ImageIO.write(bufferedImage, "JPG", new File(destPath));
            //System.out.println(QrCodeReaderWrapper.decode(logImgPath));
        } catch (Exception e) {
            System.out.println("create qrcode error! e: " + e);
        }
    }
}
