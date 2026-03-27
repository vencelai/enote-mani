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
@ApiOperation(value = "表單控制項關聯座標物件")
public class RelationPositionDto {

	@ApiModelProperty(value = "同群組的名稱")
	private String groupId;
	
	@ApiModelProperty(value = "選取的返回值")
	private String selectValue;
	
	@ApiModelProperty(value = "輸入框的座標值")
	private Position position;
}
