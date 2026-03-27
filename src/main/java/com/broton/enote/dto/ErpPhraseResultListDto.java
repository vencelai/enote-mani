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
@ApiOperation(value = "ERP片語資料回傳DTO")
public class ErpPhraseResultListDto {
	
	@ApiModelProperty(value = "片語類型")
    private String phraseType;
	
	@ApiModelProperty(value = "片語代碼")
    private String phCode;
	
	@ApiModelProperty(value = "片語名稱")
    private String phName;
	
}
