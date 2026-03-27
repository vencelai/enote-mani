package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "表單上/下架物件")
public class SetFormShowBO {

	@ApiModelProperty(value = "表單自動流水號 ID")
	private BigInteger id;
	
	@ApiModelProperty(value = "是否顯示  0:否  1:是")
	private Integer show;
}
