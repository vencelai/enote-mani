package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.broton.enote.bo.ErpGetStaffsByPublicBO;
import com.broton.enote.dto.ErpDoctorResultListDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.ErpService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "醫師相關 API", description = "醫師相關 API")
@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
	
	//@Autowired
	//private DoctorService doctorService;
	@Autowired
	private ErpService erpService;
	
	@ApiOperation(value = "取得醫師列表", httpMethod = "POST")
	@PostMapping("/getDoctorList")
	public ResponseEntity<ResponseDto<Object>> getDoctorList(@RequestBody(required = true) @Valid @ApiParam(value = "取得醫師列表查詢物件") ErpGetStaffsByPublicBO bo) {
		log.info("開始取得醫師列表");
		//List<DoctorResultListDto> output = doctorService.getDoctorList();
		List<ErpDoctorResultListDto> output = erpService.apiGetStaffsByPublic(bo);
		log.info("結束取得醫師列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	/*
	@ApiOperation(value = "取得醫師印章", httpMethod = "POST")
	@PostMapping("/getDoctorStamp")
	public ResponseEntity<ResponseDto<Object>> getDoctorStamp(@RequestBody(required = true) 
	@Valid @ApiParam(value = "醫師流水號物件") DoctorIdBO idBo) {
		log.info("開始取得醫師印章");
		String output = doctorService.getDoctorStamp(idBo);
		log.info("結束取得醫師印章");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	*/
}
