package com.broton.enote.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "員工登入物件")
public class ErpLoginBO {

	@ApiModelProperty(value = "員工帳號", required = true)
	private String id;
	
	@ApiModelProperty(value = "員工密碼", required = true)
	private String pass;
	
	@ApiModelProperty(value = "設備ID", required = true)
	private String deviceId;
}
