package com.broton.enote.bo;

import java.math.BigInteger;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "ERP上傳新的客戶同意書物件")
public class PdfUploadBO {
	
	@ApiModelProperty(value = "據點id", required = true)
	private BigInteger branchId;

	@ApiModelProperty(value = "PDF base64 字串", required = true)
	private String pdfBase64;
	
	@ApiModelProperty(value = "PDF名稱(需含 *.pdf)", required = true)
	private String pdfName;
	
	@ApiModelProperty(value = "客戶ID", required = true)
	private String CSID;
	
	@ApiModelProperty(value = "醫師ID", required = true)
	private String doctorId;
	
	@ApiModelProperty(value = "員工ID", required = true)
	private String EmployeeId;
	
	@ApiModelProperty(value = "同意書分類代碼", required = true)
	private String fromType;
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
}
