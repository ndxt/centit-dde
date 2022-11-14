package com.centit.dde.qrcode.utils;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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

    /**
     * 二维码写入pdf
     * @param imageList
     * @param byteArrayOutputStream
     */

    public static void imagesToPdf(List<com.lowagie.text.Image> imageList, ByteArrayOutputStream byteArrayOutputStream){
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

            //当前行的第几个二维码
            int columnCount = 0;

            //记录当前页写到第几行 默认第一行开始
            int rowCount = 1;

            for (int i = 0; i < imageList.size(); i++) {
                com.lowagie.text.Image image = imageList.get(i);
                image.setAlignment(com.lowagie.text.Image.MIDDLE);
                float x  , y;
                //当行的个数等于每行只能写入的总个数时进行换行
                if( columnCount   >= rowTotalCount){
                    rowCount ++;
                    columnCount = 0;
                }
                //超过一页时重新创建新的一页
                if (totalRowCount == 1 || rowCount > totalRowCount){
                    document.setPageSize(new Rectangle(a4Width,a4Height));
                    document.newPage();
                    rowCount = 1;
                    columnCount = 0;
                }

                //每行x轴方向空白总宽度
                float xBlankTotalWidth = a4Width - imageWidth * rowTotalCount;
                //根据每行二维码个数平分空白
                float  xBlankWidth =  xBlankTotalWidth / (rowTotalCount + 1);
                //左边偏移量 始终保证二维码处于居中的位置
                x = columnCount > 0 ? (xBlankWidth * (columnCount + 1)) + (imageWidth  * columnCount++)
                    : xBlankWidth  + (imageWidth  * columnCount++);

                //y轴空白总高度
                float yBlankTotalHeight = a4Height - imageHeight * totalRowCount;
                //每行的间隔距离
                float  yBlankHeight =  yBlankTotalHeight / (totalRowCount + 1);

                y = a4Height - (imageHeight * rowCount) - (yBlankHeight * rowCount);

                image.setAbsolutePosition( x , y );
                document.add(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            document.close();
        }
    }

}
