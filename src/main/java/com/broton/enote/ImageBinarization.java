package com.broton.enote;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageBinarization {

    public static void main(String[] args) throws IOException {
        // 加載圖片
        File file = new File("E:\\黑白印章.png");
        BufferedImage image = ImageIO.read(file);

        // 處理圖片
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int pixel = image.getRGB(i, j);

                // 獲取圖片的紅綠藍分量
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                // 計算灰度值
                int gray = (red + green + blue) / 3;

                // 根據灰度值進行二質化處理
                if (gray < 128) {
                    //image.setRGB(i, j, 0xFF0016); // 設為黑色
                    image.setRGB(255, 0, 0);
                } else {
                    image.setRGB(i, j, 0xFFFFFF); // 設為白色
                }
            }
        }

        // 保存處理後的圖片
        File output = new File("E:\\new\\黑白印章-new.png");
        ImageIO.write(image, "jpg", output);
    }
}
