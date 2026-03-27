package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.broton.enote.bo.QueryUploadLogListBO;
import com.broton.enote.bo.UploadLogIdBO;
import com.broton.enote.dto.UploadLogResultListDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.UploadLogService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "上傳日誌相關 API", description = "上傳日誌相關 API")
@RestController
@RequestMapping("/api/v1/uploadLog")
public class UploadLogController {
	
	@Autowired
	private UploadLogService uploadLogService;

	@ApiOperation(value = "取得上傳日誌列表", httpMethod = "POST")
	@PostMapping("/getUploadLogList")
	public ResponseEntity<ResponseDto<Object>> getUploadLogList(@RequestBody(required = true) @Valid @ApiParam(value = "上傳日誌列表查詢物件") QueryUploadLogListBO qryBo) {
		log.info("開始取得上傳日誌資料列表");
		Page<UploadLogResultListDto> output = null;
		output = uploadLogService.getUploadLogList(qryBo);
		log.info("結束取得上傳日誌資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "取得上傳日誌表單的PDF base64", httpMethod = "POST")
	@PostMapping("/getUploadFormPdf")
	public ResponseEntity<ResponseDto<Object>> getUploadFormPdf(@RequestBody(required = true) @Valid @ApiParam(value = "取得上傳日誌表單的PDF查詢物件") UploadLogIdBO idBo) {
		log.info("開始取得上傳日誌表單的PDF base64");
		String output = uploadLogService.getUploadFormPdf(idBo);
		log.info("結束取得上傳日誌表單的PDF base64");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "重新上傳失敗的表單", httpMethod = "POST")
	@PostMapping("/reUploadPdf")
	public ResponseEntity<ResponseDto<Object>> reUploadPdf(@RequestBody(required = true) @Valid @ApiParam(value = "重新上傳失敗的表單物件") UploadLogIdBO idBo) {
		log.info("開始重新上傳失敗的表單");
		uploadLogService.reUploadPdf(idBo);
		log.info("結束重新上傳失敗的表單");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
}
