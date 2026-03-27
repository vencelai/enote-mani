package com.broton.enote.service;

import org.springframework.web.multipart.MultipartFile;

import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.LoginBO;
import com.broton.enote.bo.NewCustomerBO;
import com.broton.enote.dto.FormPdfAndPosition;
import com.broton.enote.dto.LoginReturnDto;

/**
 * 平板 Service
 * 
 */
public interface PadService {
	
	/**
	 * 員工登入(向 ERP 檢核)
	 * 
	 * @param loginBO
	 */
	public LoginReturnDto employeeLogin(LoginBO loginBO);
	
	/** 取得表單 PDF and 表單控制項
	 * @param formIdBo
	 * @return
	 */
	public FormPdfAndPosition getFormPdfAndPosition(FormIdBO formIdBo);
	
	/** 平板上傳Log檔至AP Server
	 * @param file
	 */
	public void uploadPadLog(MultipartFile file);	

}
