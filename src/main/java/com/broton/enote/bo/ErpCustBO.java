package com.broton.enote.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "客戶資料列表查詢物件")
public class ErpCustBO {
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;

	@ApiModelProperty(value = "手機,姓名,客戶代碼關鍵字", required = true)
	private String custFindStr;
}
