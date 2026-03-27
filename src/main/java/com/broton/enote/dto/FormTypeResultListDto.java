package com.broton.enote.dto;

import java.math.BigInteger;

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
@ApiOperation(value = "文件名稱回傳DTO")
public class FormTypeResultListDto {
	
	@ApiModelProperty(value = "文件類別流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "文件類別代號")
    private String code;
	
	@ApiModelProperty(value = "文件類別名稱")
    private String name;

}
