package com.broton.enote.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.service.FormService;
import com.broton.enote.service.PadService;
import com.broton.enote.utils.JwtTokenUtil;
import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.LoginBO;
import com.broton.enote.bo.NewCustomerBO;
import com.broton.enote.dto.FormInputListResultDto;
import com.broton.enote.dto.FormPdfAndPosition;
import com.broton.enote.dto.FormPdfDto;
import com.broton.enote.dto.LoginReturnDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PadServiceImpl implements PadService {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private FormService formService;
	
	@Override
	public LoginReturnDto employeeLogin(LoginBO loginBO) {
		LoginReturnDto output = new LoginReturnDto();
		if (loginBO.getId().equals("emp001") && loginBO.getPassword().equals("123456")) {
			UserDetails userDetails = null;
			userDetails = User.builder()
					.username(loginBO.getId())
					.password("N/A")
					.authorities("employee")
					.build();
			
			String genToken = jwtTokenUtil.generateToken(userDetails);
			output.setName("員工001");
			output.setUserType("employee");
			output.setToken("Bearer " + genToken);
			log.info("員工登入檢核成功 {}", output);
		} else {
			log.error("員工登入檢核失敗:{}", loginBO);
			throw new ErrorCodeException(ResultCode.ERR_5302.getCode(), ResultCode.ERR_5302.getDesc());
		}	
		return output;
	}
	
	@Override
	public FormPdfAndPosition getFormPdfAndPosition(FormIdBO formIdBo) {
		FormPdfAndPosition output = new FormPdfAndPosition();
		try {
			FormPdfDto formPdfDto = formService.getFormPdf(formIdBo);
			List<FormInputListResultDto> listPosition = formService.getFormInputList(formIdBo);
			output.setPdf(formPdfDto.getPdf());
			output.setPositions(listPosition);
			log.info("取得表單&座標資訊成功");
		} catch (Exception e) {
			log.error("取得表單&座標資訊錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5708.getCode(), ResultCode.ERR_5708.getDesc());
		}
		return output;
	}
	
	@Override
	public void uploadPadLog(MultipartFile file) {		
		try {
			String fileName = file.getOriginalFilename();
			String userDirectory = Paths.get("").toAbsolutePath().toString();
			// 上傳的log檔存放於 broton-enote-mani.jar 同層路徑下(檔名為原始上傳檔名)
			File logFile = new File(userDirectory + "\\" + fileName);
			file.transferTo(logFile);
			log.info("平板 log 上傳完成 {}", userDirectory + "\\" + fileName);
		} catch (IllegalStateException | IOException e) {
			log.error("平板 log 上傳失敗 {}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5717.getCode(), ResultCode.ERR_5717.getDesc());
		}
	}
}
