package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "表單暫存 ID 物件")
public class FormStageIdBO {

	@ApiModelProperty(value = "表單暫存自動流水號 ID")
	private BigInteger id;	
	
	@ApiModelProperty(value = "員工 ID")
	private String employeeId;
	
	@ApiModelProperty(value = "員工 ID")
	private String token;
}
