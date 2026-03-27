package com.broton.enote.bo;

import java.math.BigInteger;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "暫存表單上傳物件")
public class UploadStageBO {
	
	@ApiModelProperty(value = "暫存表單流水號", required = false)
	private BigInteger stageId;

	@ApiModelProperty(value = "表單 id", required = true)
	private BigInteger formId;
	
	@ApiModelProperty(value = "醫師id", required = true)
	private String doctorId;
	
	@ApiModelProperty(value = "簽名軌跡(base64字串)", required = true)
	private String signTrack;
	
	@ApiModelProperty(value = "客戶id", required = true)
	private String customerId;
	
	@ApiModelProperty(value = "員工id", required = true)
	private String employeeId;
	
	@ApiModelProperty(value = "員工姓名", required = true)
	private String employeeName;
	
	@ApiModelProperty(value = "表單建立日期", required = true)
	private Date createDate;
	
}
