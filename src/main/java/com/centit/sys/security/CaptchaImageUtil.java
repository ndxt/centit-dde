package com.centit.sys.security;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaImageUtil {
    private static String range = "0123456789abcdefghjkmnpqrstuvwxyz";
    public static String SESSIONCHECKCODE = "session_checkcode";
    public static String REQUESTCHECKCODE = "j_checkcode";


    public static String getRandomString() {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            result.append(range.charAt(random.nextInt(range.length())));
        }
        return result.toString();
    }

/*	public static BufferedImage generateCaptchaImage(String captchaKey){
		DefaultKaptcha producer = new DefaultKaptcha();
		producer.setConfig(new Config(new Properties()));
		BufferedImage image = producer.createImage(captchaKey);
		return image;

	}*/

    public static BufferedImage generateCaptchaImage(String captchaKey) {
        // 设置图片的长宽
        int width = 62, height = 22;
        // ////// 创建内存图像
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.createGraphics();
        // 设定图像背景色(因为是做背景，所以偏淡)
        g.setColor(getRandColor(180, 250));
        g.fillRect(0, 0, width, height);
        // 设置字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, 21));
        g.setColor(new Color(0, 0, 0));
        Random rand = new Random();
        for (int i = 0; i < captchaKey.length(); i++) {
            g.drawString(captchaKey.substring(i, i + 1), 13 * i + 6 + rand.nextInt(5), 14 + rand.nextInt(6));
        }
        // 图象生效
        g.dispose();
        return image;
    }


    public static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        int fci = (fc > 255) ? 255 : fc;
        int bci = (bc > 255) ? 255 : bc;
        int r = fci + random.nextInt(bci - fci);
        int g = fci + random.nextInt(bci - fci);
        int b = fci + random.nextInt(bci - fci);
        return new Color(r, g, b);
    }

}
