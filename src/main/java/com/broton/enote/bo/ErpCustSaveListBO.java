package com.broton.enote.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "依客戶查詢剩餘課程物件")
public class ErpCustSaveListBO {
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;

	@ApiModelProperty(value = "客戶編號", required = true)
	private String csid;
}
