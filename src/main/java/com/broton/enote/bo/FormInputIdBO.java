package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "表單輸入項目流水號 物件")
public class FormInputIdBO {

	@ApiModelProperty(value = "表單輸入項目自動流水號 ID")
	private BigInteger id;	
}
