package com.broton.enote.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.DoctorRepository;
import com.broton.enote.service.DoctorService;
import com.broton.enote.service.ErpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.broton.enote.bo.DoctorStfNoBO;
import com.broton.enote.bo.ErpGetStaffsBO;
import com.broton.enote.dto.DoctorResultListDto;
import com.broton.enote.dto.ErpDoctorResultListDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DoctorServiceImpl implements DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private ErpService erpService;

	@Override
	public List<DoctorResultListDto> getDoctorList() {
		List<DoctorResultListDto> output = new ArrayList<DoctorResultListDto>();
		try {
			output = doctorRepository.getDoctorList();
			log.info("取得醫師列表成功, {} 筆", output.size());
		} catch (Exception e) {
			log.error("取得醫師列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5971.getCode(), ResultCode.ERR_5971.getDesc());
		}
		return output;
	}

	@Override
	public String getDoctorStamp(DoctorStfNoBO idBo) {
		String output = "";
		try {
			List<ErpDoctorResultListDto> listDto = new ArrayList<ErpDoctorResultListDto>();
			ErpGetStaffsBO qryBo = new ErpGetStaffsBO();
			qryBo.setStfNo(idBo.getStfNo());
			qryBo.setToken(idBo.getToken());
			listDto = erpService.apiGetStaffs(qryBo);
			if (listDto.size() > 0) {
				//log.info(listDto);
				// listDto 需作轉換的動作,不然直接取會報錯
				ObjectMapper mapper = new ObjectMapper();
				List<ErpDoctorResultListDto> dto = new ArrayList<ErpDoctorResultListDto>();		
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(listDto);				
				dto = Arrays.asList(mapper.readValue(json, ErpDoctorResultListDto[].class));
				String doctorName = dto.get(0).getStfName(); // 醫師姓名
				String drLicenseNo = dto.get(0).getDrLicenseNo(); // 醫師證號(含醫字第xxx號)
				if (StringUtils.hasText(doctorName) && StringUtils.hasText(drLicenseNo)) {
					output = generateDoctorStamp(doctorName, drLicenseNo);
				}	
			} else {
				log.error("取得醫師印章錯誤,醫師資料不存在 {}", idBo.getStfNo());
				throw new ErrorCodeException(ResultCode.ERR_5973.getCode(), ResultCode.ERR_5973.getDesc());
			}
		} catch (Exception e) {
			log.error("取得醫師印章錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5972.getCode(), ResultCode.ERR_5972.getDesc());
		}		
		return output;
	}

	public String generateDoctorStamp(String doctorName, String medicalNumber) {
		String output = "";
		try {
			int width = 170;
			int height = 80;
			// 依據字數動態調整印章寬度
			width = (int) (medicalNumber.length() * 12.5);

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = image.createGraphics();
			g2d.setBackground(Color.WHITE);
			g2d.fillRect(0, 0, width, height);
			g2d.setColor(new Color(255, 0, 0));
			Stroke stroke1 = new BasicStroke(3f);
			g2d.setStroke(stroke1);
			g2d.drawRect(0, 0, width - 1, height - 1);

			Font titleFont = new Font("微軟正黑體", Font.BOLD, 15);
			Font numberFont = new Font("arial", Font.BOLD, 9);
			Font nameFont = new Font("微軟正黑體", Font.BOLD, 25);

			// 開啟抗鋸齒
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			g2d.setFont(titleFont);
			//MyDrawString(medicalNumber, 5, 60, 1.4, g2d);
			Rectangle r = new Rectangle(0, 60, width, -10);
			drawCenteredString(g2d, medicalNumber, r, titleFont);
			/*
			g2d.setFont(numberFont);
			MyDrawString(medicalNumber, 36, 33, 1, g2d);
			g2d.setFont(titleFont);
			MyDrawString("號", 66, 33, 1, g2d);
			*/			

			// g2d.setStroke(new BasicStroke(1));
			// g2d.drawLine(0, 25, 173, 25);

			g2d.setFont(nameFont);
			// MyDrawString("李天", 5, 30, 4.8, g2d);
			// MyDrawString("顏百駿", 5, 30, 2.5, g2d);
			// MyDrawString("東邪西毒", 5, 30, 1.7, g2d);
			
			switch (doctorName.length()) {
			case 2:
				//MyDrawString(doctorName, 5, 35, 4.8, g2d);
				Rectangle r1 = new Rectangle(0, 35, width, -20);
				drawCenteredStringAddSpace(g2d, doctorName, r1, nameFont);
				break;
			case 3:
				//MyDrawString(doctorName, 5, 35, 2.5, g2d);
				Rectangle r2 = new Rectangle(0, 35, width, -20);
				drawCenteredStringAddSpace(g2d, doctorName, r2, nameFont);
				break;
			case 4:
				//MyDrawString(doctorName, 5, 35, 1.7, g2d);
				Rectangle r3 = new Rectangle(0, 35, width, -20);
				drawCenteredStringAddSpace(g2d, doctorName, r3, nameFont);
				break;
			}

			g2d.dispose();
			RenderedImage rendImage = image;
			output = imageToBase64String(rendImage, "png");
		} catch (Exception e) {
			log.error("產出醫師印章錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5974.getCode(), ResultCode.ERR_5974.getDesc());
		}
		return output;
	}

	public void MyDrawString(String str, int x, int y, double rate, Graphics g) {
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
	
	public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}
	
	public void drawCenteredStringAddSpace(Graphics g, String text, Rectangle rect, Font font) {
		text = appendSpace(text);
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}
	
	public String appendSpace(String  para){  
        int length = para.length();  
        char[] value = new char[length << 1];  
        for (int i=0, j=0; i<length; ++i, j = i << 1) {  
            value[j] = para.charAt(i);  
            value[1 + j] = ' ';  
        }  
        return new String(value);  
    }

	public String imageToBase64String(RenderedImage image, String type) {
		String ret = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, type, bos);
			byte[] bytes = bos.toByteArray();
			Encoder encoder = Base64.getEncoder();
			ret = encoder.encodeToString(bytes);
			ret = ret.replace(System.lineSeparator(), "");
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return ret;
	}
	
}
