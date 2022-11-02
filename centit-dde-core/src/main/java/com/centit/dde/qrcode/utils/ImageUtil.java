package com.centit.dde.qrcode.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageUtil {

    /**
     * 在图片中间,插入圆角的logo
     *
     * @param qrCode 原图
     * @param logo   logo地址
     * @throws IOException
     */
    public static void insertLogo(BufferedImage qrCode, String logo) throws IOException {
        int QRCODE_WIDTH = qrCode.getWidth();
        int QRCODE_HEIGHT = qrCode.getHeight();

        // 获取logo图片
        BufferedImage bf = getImageByPath(logo);
        int size = bf.getWidth() > QRCODE_WIDTH * 2 / 10 ? QRCODE_WIDTH * 2 / 50 : bf.getWidth() / 5;
        bf = ImageUtil.makeRoundBorder(bf, 60, size, Color.WHITE); // 边距为二维码图片的1/10

        // logo的宽高
        int w = bf.getWidth() > QRCODE_WIDTH * 2 / 10 ? QRCODE_WIDTH * 2 / 10 : bf.getWidth();
        int h = bf.getHeight() > QRCODE_HEIGHT * 2 / 10 ? QRCODE_HEIGHT * 2 / 10 : bf.getHeight();

        // 插入LOGO
        Graphics2D graph = qrCode.createGraphics();

        int x = (QRCODE_WIDTH - w) / 2;
        int y = (QRCODE_HEIGHT - h) / 2;

        graph.drawImage(bf, x, y, w, h, null);
        graph.dispose();
        bf.flush();
    }


    /**
     * 根据路径图片图片
     *
     * @param path 本地路径 or 网络地址
     * @return 图片
     * @throws IOException
     */
    public static BufferedImage getImageByPath(String path) throws IOException {
        if (path.startsWith("http")) {
            // 从网络获取logo
            return ImageIO.read(new URL(path));
        } else if (path.startsWith("/")) {
            // 绝对地址获取logo
            return ImageIO.read(new File(path));
        } else {
            // 从资源目录下获取logo
            return ImageIO.read(new File(path));
        }
    }


    /**
     * 生成圆角图片 & 圆角边框
     *
     * @param image        原图
     * @param cornerRadius 圆角的角度
     * @param size         边框的边距
     * @param color        边框的颜色
     * @return 返回带边框的圆角图
     */
    public static BufferedImage makeRoundBorder(BufferedImage image, int cornerRadius, int size, Color color) {
        // 将图片变成圆角
        image = makeRoundedCorner(image, cornerRadius);

        int borderSize = size << 1;
        int w = image.getWidth() + borderSize;
        int h = image.getHeight() + borderSize;
        BufferedImage output = new BufferedImage(w, h,
            BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color == null ? Color.WHITE : color);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,
            cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, size, size, null);
        g2.dispose();

        return output;
    }


    /**
     * 生成圆角图片
     *
     * @param image        原始图片
     * @param cornerRadius 圆角的弧度
     * @return 返回圆角图
     */
    public static BufferedImage makeRoundedCorner(BufferedImage image,int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)
        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius,cornerRadius));
        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

}
