package com.broton.enote.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.broton.enote.dto.common.ResultCode;
import com.broton.enote.exception.ErrorCodeException;
import com.broton.enote.repository.FormTypeRepository;
import com.broton.enote.service.FormTypeService;
import com.broton.enote.dto.FormTypeResultListDto;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FormTypeServiceImpl implements FormTypeService {

	@Autowired
	private FormTypeRepository formTypeRepository;
	
	@Override
	public List<FormTypeResultListDto> getFormTypeList() {
		List<FormTypeResultListDto> output = new ArrayList<FormTypeResultListDto>();
		try {
			output = formTypeRepository.getFormTypeList();
		} catch (Exception e) {
			log.error("取得文件名稱列表錯誤:{}", e.toString());
			throw new ErrorCodeException(ResultCode.ERR_5901.getCode(), ResultCode.ERR_5901.getDesc());
		}
		return output;
	}

}
