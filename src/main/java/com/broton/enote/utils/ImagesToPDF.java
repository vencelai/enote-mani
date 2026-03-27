package com.broton.enote.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 圖片轉 PDF 使用 iTextPdf 範例
 * 
 */
public class ImagesToPDF {

	public static void main(String[] args) {
		ArrayList<String> imageUrllist = new ArrayList<String>();
		imageUrllist = getAllFileByPath("D:\\Temp\\ImageToPDF_Demo\\");

		imgOfPdf("D:\\Temp\\ImageToPDF_Demo\\ImageToPDF.pdf", imageUrllist);
	}

	/**
	 * 取得路徑下的所有圖片檔案
	 * 
	 * @param sourcePath
	 * @return
	 */
	public static ArrayList<String> getAllFileByPath(String sourcePath) {
		ArrayList<String> imageUrllist = new ArrayList<String>();
		try {
			File dir = new File(sourcePath);
			List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
			for (File file : files) {
				// System.out.println("file: " + file.getCanonicalPath());
				String fe = FilenameUtils.getExtension(file.getCanonicalPath()).toLowerCase();
				if (fe.equals("jpg") || fe.equals("jpeg") || fe.equals("png")) {
					imageUrllist.add(file.getCanonicalPath());
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageUrllist;
	}

	public static File Pdf(ArrayList<String> imageUrllist, String mOutputPdfFileName) {
		Document doc = new Document(PageSize.A4, 0, 0, 0, 0); // new一个pdf文档
		try {
			PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName)); // pdf写入
			doc.open();// 打开文档
			for (int i = 0; i < imageUrllist.size(); i++) { // 循环图片List，将图片加入到pdf中
				doc.newPage(); // 在pdf创建一页
				Image png1 = Image.getInstance(imageUrllist.get(i)); // 通过文件路径获取image
				float heigth = png1.getHeight();
				float width = png1.getWidth();
				int percent = getPercent2(heigth, width);
				png1.setAlignment(Image.MIDDLE);
				png1.scalePercent(percent + 3);// 表示是原来图像的比例;
				doc.add(png1);
			}
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		File mOutputPdfFile = new File(mOutputPdfFileName); // 输出流
		if (!mOutputPdfFile.exists()) {
			mOutputPdfFile.deleteOnExit();
			return null;
		}
		return mOutputPdfFile; // 反回文件输出流
	}

	public static int getPercent(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		if (h > w) {
			p2 = 297 / h * 100;
		} else {
			p2 = 210 / w * 100;
		}
		p = Math.round(p2);
		return p;
	}

	public static int getPercent2(float h, float w) {
		int p = 0;
		float p2 = 0.0f;
		p2 = 530 / w * 100;
		p = Math.round(p2);
		return p;
	}

	/**
	 * @Description: 通过图片路径及生成pdf路径，将图片转成pdf
	 */
	public static void imgOfPdf(String filepath, ArrayList<String> imageList) {
		try {
			String pdfUrl = filepath; // 输出pdf文件路径
			File file = Pdf(imageList, pdfUrl);// 生成pdf
			file.createNewFile();
			System.out.println("圖片轉PDF完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
