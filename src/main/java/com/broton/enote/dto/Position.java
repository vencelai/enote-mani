package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "表單控制項座標物件")
public class Position {

	@ApiModelProperty(value = "left")
	private Integer left;
	
	@ApiModelProperty(value = "top")
	private Integer top;
	
	@ApiModelProperty(value = "right")
	private Integer right;
	
	@ApiModelProperty(value = "bottom")
	private Integer bottom;
}
