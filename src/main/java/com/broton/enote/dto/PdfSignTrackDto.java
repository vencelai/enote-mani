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
@ApiOperation(value = "表單 base64, 簽名軌跡 base64")
public class PdfSignTrackDto {
	
	@ApiModelProperty(value = "客戶 ID")
    private String customerId;
	
	@ApiModelProperty(value = "客戶手機")
    private String customerPhone;
	
	@ApiModelProperty(value = "客戶姓名")
    private String customerName;
	
	@ApiModelProperty(value = "客戶生日")
    private String birthday;

	@ApiModelProperty(value = "pdfBase64")
	private String pdfBase64;
	
	@ApiModelProperty(value = "signTrackBase64")
	private String signTrackBase64;
}
