package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "板子發送設備狀態物件")
public class SendDeviceInfoBO {
	
	//@ApiModelProperty(value = "據點 ID")
	//private BigInteger branchId;

	@ApiModelProperty(value = "設備 ID")
	private String deviceId;
	
	@ApiModelProperty(value = "軟體版本")
	private String version;
	
	@ApiModelProperty(value = "電量")
	private String battery;
	
	@ApiModelProperty(value = "IP")
	private String ip;
	
	@ApiModelProperty(value = "登入人員帳號")
	private String lastUpdateUser;
	
	@ApiModelProperty(value = "登入人員姓名")
	private String lastUpdateUserName;
}
