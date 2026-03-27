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
import com.broton.enote.bo.BranchBO;
import com.broton.enote.bo.UpdateBranchBO;
import com.broton.enote.dto.BranchListResultDto;
import com.broton.enote.dto.common.ResponseDto;
import com.broton.enote.service.BranchService;
import com.broton.enote.utils.DtoUtils;

@Log4j2
@Api(value = "據點相關 API", description = "據點相關 API")
@RestController
@RequestMapping("/api/v1/branch")
public class BranchController {

	@Autowired
	private BranchService branchService;

	@ApiOperation(value = "取得據點列表", httpMethod = "POST")
	@PostMapping("/getBranchList")
	public ResponseEntity<ResponseDto<Object>> getBranchList() {
		log.info("開始取得據點列表");
		List<BranchListResultDto> output = branchService.getBranchList();
		log.info("結束取得據點列表");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(output);
		return ResponseEntity.ok(responseDto);
	}

	@ApiOperation(value = "新增據點", httpMethod = "POST")
	@PostMapping("/addBranch")
	public ResponseEntity<ResponseDto<Object>> addBranch(@RequestBody(required = true) @Valid @ApiParam(value = "新增據點物件") BranchBO addBo) {
		log.info("開始新增據點");
		branchService.addBranch(addBo);
		log.info("結束新增據點");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}

	@ApiOperation(value = "修改據點", httpMethod = "POST")
	@PostMapping("/updateBranch")
	public ResponseEntity<ResponseDto<Object>> updateBranch(@RequestBody(required = true) @Valid @ApiParam(value = "修改據點物件") UpdateBranchBO updBo) {
		log.info("開始修改據點");
		branchService.updateBranch(updBo);
		log.info("結束修改據點");
		ResponseDto<Object> responseDto = DtoUtils.setDefaultResponseDto(null);
		return ResponseEntity.ok(responseDto);
	}

}
