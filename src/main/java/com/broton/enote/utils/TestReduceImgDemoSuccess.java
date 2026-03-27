package com.broton.enote.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;

public class TestReduceImgDemoSuccess {

    /**
     * 图像的乘法因子
     */
    public static float FACTOR = 0.3f;

    /**
     *
     *
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException
     * @throws DocumentException
     */
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
    	System.out.println(new Date());
        PdfName key = new PdfName("ITXT_SpecialId");
        PdfName value = new PdfName("123456789");
        // 读取pdf文件
        PdfReader reader = new PdfReader(src);
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {

            object = reader.getPdfObject(i);
            PdfObject pdfObject = reader.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PRStream) object;
            PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
            //System.out.println(stream.type());
            if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
                PdfImageObject image = new PdfImageObject(stream);
                BufferedImage bi = image.getBufferedImage();
                if (bi == null) continue;
                int width = (int) (bi.getWidth() * FACTOR);
                int height = (int) (bi.getHeight() * FACTOR);
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(FACTOR, FACTOR);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                //标记此处，后面会修改
                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false, PRStream.BEST_COMPRESSION);
                stream.put(PdfName.TYPE, PdfName.XOBJECT);
                stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                stream.put(key, value);
                stream.put(PdfName.FILTER, PdfName.DCTDECODE);
                stream.put(PdfName.WIDTH, new PdfNumber(width));
                stream.put(PdfName.HEIGHT, new PdfNumber(height));
                stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
            }
        }
        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
        reader.close();
        System.out.println(new Date());
        System.out.println("壓縮完成");
    }

    /**
     * Main method.
     *
     * @param args no arguments needed
     * @throws DocumentException
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
        new TestReduceImgDemoSuccess().manipulatePdf("D:/abc.pdf", "D:/abc-已壓縮.pdf");
    }

}