package com.broton.enote.bo;

import java.math.BigInteger;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "上傳新增客戶PDF物件")
public class NewCustomerPdfUploadBO {
	
	@ApiModelProperty(value = "據點id", required = true)
	private BigInteger branchId;
	
	@ApiModelProperty(value = "PDF base64 字串", required = true)
	private String pdfBase64;
	
	@ApiModelProperty(value = "PDF名稱(需含 *.pdf)", required = true)
	private String pdfName;
	
	@ApiModelProperty(value = "客戶ID", required = true)
	private String CSID;
	
	@ApiModelProperty(value = "同意書分類代碼", required = true)
	private String fromType;
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	
	@ApiModelProperty(value = "員工ID", required = true)
	private String employeeId;
}
