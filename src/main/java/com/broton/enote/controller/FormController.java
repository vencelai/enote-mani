package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broton.enote.bo.FormAddBO;
import com.broton.enote.bo.FormIdBO;
import com.broton.enote.bo.FormInputAddBO;
import com.broton.enote.bo.FormInputIdBO;
import com.broton.enote.bo.FormInputUpdateBO;
import com.broton.enote.bo.FormUpdateBO;
import com.broton.enote.bo.QueryFormListBO;
import com.broton.enote.bo.SetFormShowBO;
import com.broton.enote.dto.FormInputListResultDto;
import com.broton.enote.dto.FormListResultDto;
import com.broton.enote.dto.FormPdfDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.FormService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "表單相關 API", description = "表單相關 API")
@RestController
@RequestMapping("/api/v1/form")
public class FormController {
	
	@Autowired
	private FormService formService;

	@ApiOperation(value = "取得表單資料列表", httpMethod = "POST")
	@PostMapping("/getFormList")
	public ResponseEntity<ResponseDto<Object>> getFormList(@RequestBody(required = true) @Valid @ApiParam(value = "表單資料列表查詢物件") QueryFormListBO qryBo) {
		log.info("開始取得表單資料列表");
		Page<FormListResultDto> pageFormResultDto = null;
		pageFormResultDto = formService.getFormList(qryBo);
		log.info("結束取得表單資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(pageFormResultDto);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單PDF base64", httpMethod = "POST")
	@PostMapping("/getFormPdf")
	public ResponseEntity<ResponseDto<Object>> getFormPdf(@RequestBody(required = true) @Valid @ApiParam(value = "取得表單PDF base64查詢物件") FormIdBO qryBo) {
		log.info("開始取得表單PDF base64");
		FormPdfDto formPdfDto = formService.getFormPdf(qryBo);
		log.info("結束取得表單PDF base64");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(formPdfDto.getPdf());
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得表單控制項列表", httpMethod = "POST")
	@PostMapping("/getFormPositionList")
	public ResponseEntity<ResponseDto<Object>> getFormPositionList(@RequestBody(required = true) @Valid @ApiParam(value = "取得表單控制項查詢物件") FormIdBO qryBo) {
		log.info("開始取得表單控制項列表");
		List<FormInputListResultDto> output = formService.getFormInputList(qryBo);
		log.info("結束取得表單控制項列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "新增表單", httpMethod = "POST")
	@PostMapping("/formAdd")
	public ResponseEntity<ResponseDto<Object>> formAdd(@RequestBody(required = true) @Valid @ApiParam(value = "新增表單物件") FormAddBO addBo,
			@ApiIgnore Principal principal) {
		log.info("開始新增表單");
		formService.formAdd(addBo, principal.getName());
		log.info("結束新增表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "更新表單", httpMethod = "POST")
	@PostMapping("/formUpdate")
	public ResponseEntity<ResponseDto<Object>> formUpdate(@RequestBody(required = true) @Valid @ApiParam(value = "更新表單物件") FormUpdateBO updBo,
			@ApiIgnore Principal principal) {
		log.info("開始更新表單");
		formService.formUpdate(updBo, principal.getName());
		log.info("結束更新表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "刪除表單", httpMethod = "POST")
	@PostMapping("/formDelete")
	public ResponseEntity<ResponseDto<Object>> formDelete(@RequestBody(required = true) @Valid @ApiParam(value = "刪除表單物件") FormIdBO idBo,
			@ApiIgnore Principal principal) {
		log.info("開始刪除表單");
		formService.formDelete(idBo);
		log.info("結束刪除表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "表單上/下架", httpMethod = "POST")
	@PostMapping("/setFormShow")
	public ResponseEntity<ResponseDto<Object>> setFormShow(@RequestBody(required = true) @Valid @ApiParam(value = "表單上/下架物件") SetFormShowBO setBo,
			@ApiIgnore Principal principal) {
		log.info("開始表單上/下架");
		formService.setFormShow(setBo, principal.getName());
		log.info("結束表單上/下架");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "新增表單輸入項目", httpMethod = "POST")
	@PostMapping("/formInputAdd")
	public ResponseEntity<ResponseDto<Object>> formInputAdd(@RequestBody(required = true) @Valid @ApiParam(value = "新增表單輸入項目物件") FormInputAddBO addBo,
			@ApiIgnore Principal principal) {
		log.info("開始新增表單輸入項目");
		formService.formInputAdd(addBo, principal.getName());
		log.info("結束新增表單輸入項目");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "修改表單輸入項目", httpMethod = "POST")
	@PostMapping("/formInputUpdate")
	public ResponseEntity<ResponseDto<Object>> formInputUpdate(@RequestBody(required = true) @Valid @ApiParam(value = "修改表單輸入項目物件") FormInputUpdateBO updBo,
			@ApiIgnore Principal principal) {
		log.info("開始修改表單輸入項目");
		formService.formInputUpdate(updBo, principal.getName());
		log.info("結束修改表單輸入項目");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "刪除表單輸入項目", httpMethod = "POST")
	@PostMapping("/formInputDelete")
	public ResponseEntity<ResponseDto<Object>> formInputDelete(@RequestBody(required = true) @Valid @ApiParam(value = "刪除表單輸入項目物件") FormInputIdBO idBo,
			@ApiIgnore Principal principal) {
		log.info("開始刪除表單輸入項目");
		formService.formInputDelete(idBo);
		log.info("結束刪除表單輸入項目");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
}
