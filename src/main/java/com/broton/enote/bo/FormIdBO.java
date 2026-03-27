package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "取得表單 pdf 物件")
public class FormIdBO {

	@ApiModelProperty(value = "表單自動流水號 ID")
	private BigInteger id;	
}
