package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "項目代碼傳輸物件")
public class CustomerItemListBO {

	@ApiModelProperty(value = "項目代碼", required = false)
	private String cIcode;
	
	@ApiModelProperty(value = "項目值(true/false)", required = false)
	private boolean cIvalue;
	
	@ApiModelProperty(value = "項目輸入文字", required = false)
	private String cImemo;
}
