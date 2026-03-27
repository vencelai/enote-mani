package com.broton.enote.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/** 寫入參數值至圖片pdf 裡
 * @author vencelai
 * @date 2022年12月29日 上午10:25:30

 */
public class WriteParameterToImagePdf {

	public static void main(String[] args) {
		
		try {
			BufferedImage image = ImageIO.read(new File("D:\\Temp\\ImageToPDF_Demo\\001.png"));
			
			Graphics2D g2d = image.createGraphics();
			Font nameFont = new Font("微軟正黑體", Font.TYPE1_FONT, 22);
			g2d.setFont(nameFont);
			g2d.setColor(new Color(0, 0, 0));
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			//MyDrawString("顏百駿", 480, 550, 1, g2d);
			drawStringMultipleLine(g2d, "顏百駿 0988123456\n103台北市大同區鄭州路87號", 480, 490);
			
			File outputfile = new File("D:\\Temp\\ImageToPDF_Demo\\0011.png");
			ImageIO.write(image, "png", outputfile);
			System.out.println("圖片押入參數值完成");
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
	
	public static void drawStringMultipleLine(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}

}
