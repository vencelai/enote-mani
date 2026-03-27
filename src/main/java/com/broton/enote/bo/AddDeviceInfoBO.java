package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "新增/更新設備物件")
public class AddDeviceInfoBO {
	
	@ApiModelProperty(value = "據點 ID")
	private BigInteger branchId;

	@ApiModelProperty(value = "設備 ID")
	private String deviceId;
}
