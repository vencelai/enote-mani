package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormAddBO {

	@ApiModelProperty(value = "文件類型 ID", required = true)
	private String typeId;
	
	@ApiModelProperty(value = "文件名稱", required = true)
	private String formName;
	
	@ApiModelProperty(value = "是否需要醫師簽章 0:否  1:是", required = true)
	private Integer doctorSign;
	
	@ApiModelProperty(value = "PDF文件(base64)", required = true)
	private String pdfBase64;
}
