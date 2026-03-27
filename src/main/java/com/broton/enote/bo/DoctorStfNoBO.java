package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "醫師代號物件")
public class DoctorStfNoBO {

	@ApiModelProperty(value = "醫師代號", required = true)
	private String stfNo;	
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
}
