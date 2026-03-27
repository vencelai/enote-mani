package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "醫師ID物件")
public class DoctorIdBO {

	@ApiModelProperty(value = "醫師流水號")
	private BigInteger id;	
}
