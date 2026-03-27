package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "ERP客戶ID物件")
public class PdfListBO {

	@ApiModelProperty(value = "ERP客戶ID")
	private String csid;	
	
	@ApiModelProperty(value = "令牌")
	private String token;
}
