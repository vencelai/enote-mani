package com.broton.enote.dto;

import java.util.List;
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
@ApiOperation(value = "表單PDF&座標資訊回傳DTO")
public class FormPdfAndPosition {
	
	@ApiModelProperty(value = "表單 pdf base64")
    private byte[] pdf;
	
	@ApiModelProperty(value = "表單控制項列表")
    private List<FormInputListResultDto> positions;

}
