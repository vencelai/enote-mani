package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "員工id物件")
public class EmployeeIdBO {

	@ApiModelProperty(value = "員工id")
	private String employeeId;	
}
