package com.broton.enote.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * 醫美-產出印章(雙字號)測試
 * 
 * @author vencelai
 * @date 2022年12月20日 上午10:33:57
 * 
 */
public class GenStampGraphic3 {

	public static void main(String[] args) {
		// 製作使用者的印章圖像(base64)

		int width = 165;
		int height = 40;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();		
		g2d.setBackground(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(new Color(255, 0, 0));
		Stroke stroke1 = new BasicStroke(3f);
		g2d.setStroke(stroke1);
		g2d.drawRect(0, 0, width - 1, height - 1);
		// g2d.drawRoundRect(0, 0, width - 1, height - 1, 6, 6);

		Font titleFont = new Font("微軟正黑體", Font.BOLD, 10);
		Font numberFont = new Font("arial", Font.BOLD, 9);
		Font nameFont = new Font("微軟正黑體", Font.BOLD, 16);

		// 開啟抗鋸齒
		// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
		// RenderingHints.VALUE_STROKE_DEFAULT);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setFont(titleFont);
		MyDrawString("醫字第", 4, 33, 1, g2d);
		g2d.setFont(numberFont);
		MyDrawString("036875", 36, 33, 1, g2d);
		g2d.setFont(titleFont);
		MyDrawString("號", 66, 33, 1, g2d);
		
		g2d.setFont(titleFont);
		MyDrawString("醫字第", 90, 33, 1, g2d);
		g2d.setFont(numberFont);
		MyDrawString("123456", 120, 33, 1, g2d);
		g2d.setFont(titleFont);
		MyDrawString("號", 150, 33, 1, g2d);

		g2d.setFont(nameFont);

		// g2d.setStroke(new BasicStroke(1));
		// g2d.drawLine(0, 25, 173, 25);

		// MyDrawString("李天", 4, 19, 3.4, g2d);
		MyDrawString("顏百駿", 4, 19, 1.8, g2d);
		// MyDrawString("東邪西毒", 4, 19, 1.2, g2d);
		g2d.dispose();

		RenderedImage rendImage = image;

		String output = imageToBase64String(rendImage, "png");

		System.out.println(output);

		File outputfile = new File("D:/stamp.png");
		try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void MyDrawString(String str, int x, int y, double rate, Graphics g) {
		String tempStr = new String();
		int orgStringWight = g.getFontMetrics().stringWidth(str);
		int orgStringLength = str.length();
		int tempx = x;
		int tempy = y;
		while (str.length() > 0) {
			tempStr = str.substring(0, 1);
			str = str.substring(1, str.length());
			g.drawString(tempStr, tempx, tempy);
			tempx = (int) (tempx + (double) orgStringWight / (double) orgStringLength * rate);
		}
	}

	@SuppressWarnings("restriction")
	public static String imageToBase64String(RenderedImage image, String type) {
		String ret = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, type, bos);
			byte[] bytes = bos.toByteArray();
			//BASE64Encoder encoder = new BASE64Encoder();
			//ret = encoder.encode(bytes);
			ret = Arrays.toString(java.util.Base64.getEncoder().encode(bytes));
			ret = ret.replace(System.lineSeparator(), "");
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return ret;
	}

}
