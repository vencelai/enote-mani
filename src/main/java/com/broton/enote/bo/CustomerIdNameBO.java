package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "客戶暫存表單查詢物件")
public class CustomerIdNameBO {

	@ApiModelProperty(value = "客戶 ID")
	private String customerId;	
	
	@ApiModelProperty(value = "客戶名稱")
	private String customerName;
}
