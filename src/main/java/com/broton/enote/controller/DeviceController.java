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

import com.broton.enote.bo.AddDeviceInfoBO;
import com.broton.enote.bo.QueryDeviceStatusListBO;
import com.broton.enote.bo.SendDeviceInfoBO;
import com.broton.enote.dto.DeviceResultListDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.DeviceService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "設備相關 API", description = "設備相關 API")
@RestController
@RequestMapping("/api/v1/device")
public class DeviceController {
	
	@Autowired
	private DeviceService deviceService;

	@ApiOperation(value = "取得設備狀態資料列表", httpMethod = "POST")
	@PostMapping("/getDeviceStatusList")
	public ResponseEntity<ResponseDto<Object>> getDeviceStatusList(@RequestBody(required = true) 
		@Valid @ApiParam(value = "設備狀態資料列表查詢物件") QueryDeviceStatusListBO qryBo) {
		log.info("開始取得設備狀態資料列表");
		Page<DeviceResultListDto> output = null;
		output = deviceService.getDeviceStatusList(qryBo);
		log.info("結束取得設備狀態資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "接收板子發送的設備狀態", httpMethod = "POST")
	@PostMapping("/setDeviceInfo")
	public ResponseEntity<ResponseDto<Object>> setDeviceInfo(@RequestBody(required = true) 
		@Valid @ApiParam(value = "接收板子發送的設備狀態物件") SendDeviceInfoBO sendBo) {
		log.info("開始接收板子發送的設備狀態");
		deviceService.setDeviceInfo(sendBo);
		log.info("結束接收板子發送的設備狀態");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "新增/修改設備", httpMethod = "POST")
	@PostMapping("/addDevice")
	public ResponseEntity<ResponseDto<Object>> addDevice(@RequestBody(required = true) 
		@Valid @ApiParam(value = "新增/修改設備物件") AddDeviceInfoBO addBo) {
		log.info("開始新增/修改設備");
		deviceService.addDevice(addBo);
		log.info("結束新增/修改設備");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
}
