package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.broton.enote.dto.ErpPhraseResultListDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.ErpService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "文件類別相關 API", description = "文件類別相關 API")
@RestController
@RequestMapping("/api/v1/formType")
public class FormTypeController {
	
	//@Autowired
	//private FormTypeService formTypeService;
	@Autowired
	private ErpService erpService;
	
	@ApiOperation(value = "取得文件類別列表", httpMethod = "POST")
	@PostMapping("/getFormTypeList")
	public ResponseEntity<ResponseDto<Object>> getFormTypeList() {
		log.info("開始取得文件類別列表");
		//List<FormTypeResultListDto> output = formTypeService.getFormTypeList();
		List<ErpPhraseResultListDto> output = erpService.apiGetPhraseByPublic();
		log.info("結束取得文件類別列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
}
