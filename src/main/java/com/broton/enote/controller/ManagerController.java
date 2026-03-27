package com.broton.enote.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.security.Principal;
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
import com.broton.enote.bo.ManagerBO;
import com.broton.enote.bo.ManagerIdBO;
import com.broton.enote.bo.QueryManagerBO;
import com.broton.enote.bo.UpdateManagerBO;
import com.broton.enote.dto.ManagerResultDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.ManagerService;
import com.broton.enote.utils.DtoUtils;
import com.broton.enote.bo.LoginBO;
import com.broton.enote.dto.LoginReturnDto;

@Log4j2
@Api(value = "管理人員相關 API", description = "管理人員相關 API")
@RestController
@RequestMapping("/api/v1/manager")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@ApiOperation(value = "管理人員登入", httpMethod = "POST", notes = "管理人員登入")
	@PostMapping("/managerLogin")
	public ResponseEntity<ResponseDto<Object>> managerLogin(@RequestBody(required = true) @Valid @ApiParam(value = "管理人員登入物件") LoginBO loginBO) {
		log.info("開始管理人員登入");
		LoginReturnDto loginReturnDto = new LoginReturnDto();
		loginReturnDto = managerService.managerLogin(loginBO);
		log.info("結束管理人員登入");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(loginReturnDto);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "管理人員資料列表", httpMethod = "POST")
	@PostMapping("/getManagerList")
	public ResponseEntity<ResponseDto<Object>> getManagerList(@RequestBody(required = true) @Valid @ApiParam(value = "管理人員資料列表查詢物件") QueryManagerBO queryManagerBO) {
		log.info("開始取得管理人員資料列表");
		Page<ManagerResultDto> pageManagerResultDto = null;
		pageManagerResultDto = managerService.getManagerList(queryManagerBO);
		log.info("結束取得管理人員資料列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(pageManagerResultDto);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "新增管理人員", httpMethod = "POST")
	@PostMapping("/addManager")
	public ResponseEntity<ResponseDto<Object>> addManager(@RequestBody(required = true) @Valid @ApiParam(value = "新增管理人員資料物件") ManagerBO managerBO, 
			@ApiIgnore Principal principal) {
		log.info("開始新增管理人員資料");
		log.debug(managerBO.toString());
		managerService.addManager(managerBO, principal.getName());
		log.info("結束新增管理人員資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "刪除管理人員", httpMethod = "POST")
	@PostMapping("/delManager")
	public ResponseEntity<ResponseDto<Object>> delManager(@RequestBody(required = true) @Valid @ApiParam(value = "刪除管理人員資料物件") ManagerIdBO managerIdBO) {
		log.info("開始刪除管理人員資料");
		log.debug(managerIdBO.toString());
		managerService.delManager(managerIdBO);
		log.info("結束刪除管理人員資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
	@ApiOperation(value = "修改管理人員", httpMethod = "POST")
	@PostMapping("/updateManager")
	public ResponseEntity<ResponseDto<Object>> updateManager(@RequestBody(required = true) @Valid @ApiParam(value = "修改管理人員資料物件") UpdateManagerBO updateManagerBO, 
			@ApiIgnore Principal principal) {
		log.info("開始修改管理人員資料");
		log.debug(updateManagerBO.toString());
		managerService.updateManager(updateManagerBO, principal.getName());
		log.info("結束修改管理人員資料");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}
	
}
