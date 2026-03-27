package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "以紀錄UNID下載特定PDF檔案物件")
public class PdfDownloadBO {
	
	@ApiModelProperty(value = "檔案UNID")
	private String unid;
	
	@ApiModelProperty(value = "令牌")
	private String token;
}
