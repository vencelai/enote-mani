package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "暫存表單id物件")
public class StageIdBO {

	@ApiModelProperty(value = "表單自動流水號 ID")
	private BigInteger id;	
}
