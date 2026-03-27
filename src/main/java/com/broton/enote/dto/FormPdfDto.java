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
@ApiOperation(value = "表單PDF回傳DTO")
public class FormPdfDto {
	
	@ApiModelProperty(value = "表單 pdf base64")
    private byte[] pdf;

}
