package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormUpdateBO {
	
	@ApiModelProperty(value = "表單自動流水號 ID")
	private BigInteger id;
	
	@ApiModelProperty(value = "PDF文件(base64)")
	private String pdfBase64;
	
	@ApiModelProperty(value = "表單名稱")
	private String formName;
	
	@ApiModelProperty(value = "表單類型代號")
	private String typeId;
	
	@ApiModelProperty(value = "是否需要醫師簽章 0:否  1:是")
	private Integer doctorSign;
}
