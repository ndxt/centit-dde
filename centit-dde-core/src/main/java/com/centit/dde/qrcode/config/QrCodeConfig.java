package com.centit.dde.qrcode.config;

import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class QrCodeConfig {
    /**
     * 塞入二维码的信息
     */
    private String msg;

    /**
     * 二维码顶部文字
     */
    private String topText;

    /**
     * 二维码顶部文字大小
     */
    private Integer topTextFontSize;

    /**
     * 二维码底部文字
     */
    private String downText;

    /**
     * 二维码底部文字大小
     */
    private Integer downTextFontSize;

    /**
     * 顶部部字体类型
     */
    private String topTextFontType;

    /**
     * 底部部字体类型
     */
    private String downTextFontType;

    /**
     * 二维码中间的logo
     */
    private String logo;
    /**
     * 生成二维码的宽
     */
    private Integer qrWidth;
    /**
     * 生成二维码的高
     */
    private Integer qrHeight;
    /**
     * 生成二维码的颜色
     */
    private MatrixToImageConfig matrixToImageConfig;

    private Map<EncodeHintType, Object> hints;

    /**
     * 生成二维码图片的格式 png, jpg
     */
    private String picType;

    @ToString
    public static class QrCodeConfigBuilder {
        private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

        /**
         * The message to put into QrCode
         */
        private String msg;

        /**
         * 二维码顶部文字
         */
        private String topText;

        /**
         * 二维码顶部文字大小
         */
        private Integer topTextFontSize = 8;

        /**
         * 二维码底部文字
         */
        private String downText;

        /**
         * 二维码底部文字大小
         */
        private Integer downTextFontSize = 7;

        /**
         * 顶部部字体类型
         */
        private String topTextFontType = "雅黑";

        /**
         * 底部部字体类型
         */
        private String downTextFontType = "雅黑";

        /**
         * qrcode center logo
         */
        private String logo;


        /**
         * qrcode image width
         */
        private Integer qrWidth = 200;


        /**
         * qrcode image height
         */
        private Integer qrHeight = 200;


        /**
         * qrcode bgcolor, default white
         */
        private Integer offColor;


        /**
         * qrcode msg color, default black
         */
        private Integer onColor;


        /**
         * qrcode message's code, default UTF-8
         */
        private String code;

        /**
         * 二维码白边预留参数
         * 0 - 4
         */
        private Integer padding = 0;


        /**
         * error level, default H
         */
        private ErrorCorrectionLevel errorCorrection;


        /**
         * output qrcode image type, default png
         */
        private String picType;

        public String getTopTextFontType() {
            return topTextFontType;
        }

        public QrCodeConfigBuilder setTopTextFontType(String topTextFontType) {
            if(StringUtils.isNotBlank(topTextFontType)){
                this.topTextFontType = topTextFontType;
            }
            return this;
        }

        public String getDownTextFontType() {
            return downTextFontType;
        }

        public QrCodeConfigBuilder setDownTextFontType(String downTextFontType) {
            if(StringUtils.isNotBlank(topTextFontType)) {
                this.downTextFontType = downTextFontType;
            }
            return this;
        }

        public Integer getTopTextFontSize() {
            return topTextFontSize;
        }

        public QrCodeConfigBuilder setTopTextFontSize(Integer topTextFontSize) {
            if( topTextFontSize != null && topTextFontSize > 0){
                this.topTextFontSize = topTextFontSize;
            }
            return this;
        }

        public Integer getDownTextFontSize() {
            return downTextFontSize;
        }

        public QrCodeConfigBuilder setDownTextFontSize(Integer downTextFontSize) {
           if(downTextFontSize != null && downTextFontSize > 0){
               this.downTextFontSize = downTextFontSize;
           }
            return this;
        }

        public String getTopText() {
            return topText;
        }

        public QrCodeConfigBuilder setTopText(String topText) {
            this.topText = topText;
            return this;
        }

        public String getDownText() {
            return downText;
        }

        public QrCodeConfigBuilder setDownText(String downText) {
            this.downText = downText;
            return this;
        }

        public String getMsg() {
            return msg;
        }

        public QrCodeConfigBuilder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public String getLogo() {
            return logo;
        }

        public QrCodeConfigBuilder setLogo(String logo) {
            this.logo = logo;
            return this;
        }

        public Integer getQrWidth() {
            return qrWidth == null ? (qrHeight == null ? 200 : qrHeight) : qrWidth;
        }

        public QrCodeConfigBuilder setQrWidth(Integer qrWidth) {
            if (qrWidth != null && qrWidth < 0) {
                throw new IllegalArgumentException("logoImage宽度不能小于0！");
            }
            this.qrWidth = qrWidth;
            return this;
        }

        public Integer getQrHeight() {
            if (qrWidth != null && qrWidth < 0) {
                throw new IllegalArgumentException("logoImage宽度不能小于0！");
            }
            return qrHeight == null ? (qrWidth == null ? 200 : qrWidth) : qrHeight;
        }

        public QrCodeConfigBuilder setQrHeight(Integer qrHeight) {
            this.qrHeight = qrHeight;
            return this;
        }

        public Integer getOffColor() {
            return offColor == null ? MatrixToImageConfig.WHITE : offColor;
        }

        public QrCodeConfigBuilder setOffColor(Integer offColor) {
            this.offColor = offColor;
            return this;
        }

        public Integer getOnColor() {
            return onColor == null ? MatrixToImageConfig.BLACK : onColor;
        }

        public QrCodeConfigBuilder setOnColor(Integer onColor) {
            this.onColor = onColor;
            return this;
        }

        public String getCode() {
            return code == null ? "UTF-8" : code;
        }

        public QrCodeConfigBuilder setCode(String code) {
            this.code = code;
            return this;
        }

        public Integer getPadding() {
            if (padding == null) {
                return 1;
            }

            if (padding < 0) {
                return 0;
            }

            if (padding > 4) {
                return 4;
            }

            return padding;
        }

        public QrCodeConfigBuilder setPadding(Integer padding) {
            this.padding = padding;
            return this;
        }


        public String getPicType() {
            return picType == null ? "png" : picType;
        }

        public QrCodeConfigBuilder setPicType(String picType) {
            this.picType = picType;
            return this;
        }

        public ErrorCorrectionLevel getErrorCorrection() {
            return errorCorrection == null ? ErrorCorrectionLevel.H : errorCorrection;
        }

        public void setErrorCorrection(ErrorCorrectionLevel errorCorrection) {
            this.errorCorrection = errorCorrection;
        }

        private void validate() {
            if (msg == null || msg.length() == 0) {
                throw new IllegalArgumentException("二维码内容不能为空!");
            }
        }

        private QrCodeConfig create() {
           // this.validate();
            QrCodeConfig qrCodeConfig = new QrCodeConfig();
            qrCodeConfig.setMsg(getMsg());
            qrCodeConfig.setQrHeight(getQrHeight());
            qrCodeConfig.setQrWidth(getQrWidth());
            qrCodeConfig.setLogo(getLogo());
            qrCodeConfig.setPicType(getPicType());
            qrCodeConfig.setTopText(getTopText());
            qrCodeConfig.setDownText(getDownText());
            qrCodeConfig.setTopTextFontSize(getTopTextFontSize());
            qrCodeConfig.setDownTextFontSize(getDownTextFontSize());
            qrCodeConfig.setTopTextFontType(getTopTextFontType());
            qrCodeConfig.setDownTextFontType(getDownTextFontType());

            Map<EncodeHintType, Object> hints = new HashMap<>(3);
            hints.put(EncodeHintType.ERROR_CORRECTION, this.getErrorCorrection());
            hints.put(EncodeHintType.CHARACTER_SET, this.getCode());
            hints.put(EncodeHintType.MARGIN, this.getPadding());
            qrCodeConfig.setHints(hints);

            MatrixToImageConfig config;
            if (getOnColor() == MatrixToImageConfig.BLACK
                && getOffColor() == MatrixToImageConfig.WHITE) {
                config = DEFAULT_CONFIG;
            } else {
                config = new MatrixToImageConfig(getOnColor(), getOffColor());
            }
            qrCodeConfig.setMatrixToImageConfig(config);
            return qrCodeConfig;
        }

        public QrCodeConfig build() {
            return create();
        }

    }
}

