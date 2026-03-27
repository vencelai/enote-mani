package com.broton.enote;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

public class PicGrayToColor {

    public static void main(String[] args) {
        File file = new File("E:\\黑白印章.png");
        //File file = new File("E:\\黑白印章.tif");
        File file1 = new File("E:\\new");
        System.out.println(picGrayToColour(file.getPath(),file1.getPath(),"33-new.png"));
    }

    /**
     * 灰度图转真彩图
     * @param path-灰度图路径
     * @param outPath-真彩图输出路径
     */
    public static boolean picGrayToColour(String path, String outPath, String picName){
        try {
            File file = new File(path);
            BufferedImage bufferedImage = ImageIO.read(file);
            BufferedImage bImage=new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
            Raster data = bufferedImage.getData();
            for(int i=0;i<bufferedImage.getHeight();i++){
                for(int j=0;j<bufferedImage.getWidth();j++){
                    double[] pixel = data.getPixel(j, i, new double[4]);
                    int rgba;
                    if(pixel[0]<20){
                        rgba = new Color(255,255,255).getRGB();
                    }else {
                        int[] colorMsg = getColorMsg(pixel[0]);
                        //rgba = new Color(colorMsg[0],colorMsg[1],colorMsg[2],colorMsg[3]).getRGB();
                        rgba = new Color(255, 0, 0).getRGB();
                    }
                    bImage.setRGB(j,i,rgba);
                }
            }
            //创建文件与文件夹
            File outFile = new File(outPath);
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            File pic = new File(outPath + File.separator + picName);
            if (!pic.exists()) {
                pic.createNewFile();
            }
            ImageIO.write(bImage,"png", pic);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private static int[] getColorMsg(double data){
        //int[] gradient = new int[]{10,15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70};
        //int[][] gradientColor = new int[][]{{1, 160, 246, 255}, {0, 236, 236, 255}, {0, 216, 0, 255}, {1, 144, 0, 255}, {255, 255, 0, 255}, {231, 192, 0, 255}, {255, 144, 0, 255}, {255, 0, 0, 255}, {214, 0, 0, 255}, {192, 0, 0, 255}, {255, 0, 240, 255}, {150, 0, 180, 255}, {173, 144, 240, 255}};

        int[] gradient = new int[]{10,15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70};
        int[][] gradientColor = new int[][]{{255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {255, 0, 0}, {214, 0, 0, 255}, {192, 0, 0, 255}, {255, 0, 240, 255}, {150, 0, 180, 255}, {173, 144, 240, 255}};
        int[] colorMsg = new int[4];
        for(int i=0;i<gradient.length-1;i++){
            if(data<=gradient[i]){
                colorMsg = gradientColor[i];
                break;
            }
            if(data==gradient[i+1]){
                colorMsg = gradientColor[i+1];
                break;
            }
            if(data>gradient[i]&&data<gradient[i+1]){
                //                colorMsg[0] = new Double((gradientColor[i+1][0]-gradientColor[i][0])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][0]).intValue();
                //                colorMsg[1] = new Double((gradientColor[i+1][1]-gradientColor[i][1])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][1]).intValue();
                //                colorMsg[2] = new Double((gradientColor[i+1][2]-gradientColor[i][2])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][2]).intValue();
                colorMsg[0] = Double.valueOf((gradientColor[i+1][0]-gradientColor[i][0])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][0]).intValue();
                colorMsg[1] = Double.valueOf((gradientColor[i+1][1]-gradientColor[i][1])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][1]).intValue();
                colorMsg[2] = Double.valueOf((gradientColor[i+1][2]-gradientColor[i][2])/(gradient[i+1]-gradient[i])*(data-gradient[i])+gradientColor[i][2]).intValue();

                colorMsg[3] = 255;
                break;
            }
            if(i+1==gradient.length-1){
                colorMsg = gradientColor[i+1];
            }
        }
        return colorMsg;
    }
}
