package com.broton.enote.service;

import java.util.List;
import com.broton.enote.dto.FormTypeResultListDto;

/**
 * 文件類別 Service
 * 
 */
public interface FormTypeService {
	
	/** 取得文件名稱列表
	 * @return
	 */
	public List<FormTypeResultListDto> getFormTypeList();

}
