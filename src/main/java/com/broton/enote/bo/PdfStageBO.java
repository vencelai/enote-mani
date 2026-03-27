package com.broton.enote.bo;

import java.math.BigInteger;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "表單暫存物件")
public class PdfStageBO {
	
	@ApiModelProperty(value = "表單暫存id(無表示為新增, 有則為更新)", required = false)
	private BigInteger id;
	
	@ApiModelProperty(value = "原始表單id", required = true)
	private BigInteger formId;
	
	@ApiModelProperty(value = "據點id", required = true)
	private BigInteger branchId;

	@ApiModelProperty(value = "PDF base64 字串", required = true)
	private String pdfBase64;
	
	@ApiModelProperty(value = "簽名軌跡 base64 字串", required = true)
	private String signTrackBase64;
	
	@ApiModelProperty(value = "PDF名稱(需含 *.pdf)", required = true)
	private String pdfName;
	
	@ApiModelProperty(value = "客戶ID", required = true)
	private String CSID;
	
	@ApiModelProperty(value = "醫師ID", required = true)
	private String doctorId;
	
	@ApiModelProperty(value = "員工ID", required = true)
	private String employeeId;
	
	@ApiModelProperty(value = "同意書分類代碼", required = true)
	private String fromType;
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
}
