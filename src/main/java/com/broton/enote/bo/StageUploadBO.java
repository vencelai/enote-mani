package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "員工上傳暫存表單物件")
public class StageUploadBO {
	
	@ApiModelProperty(value = "表單流水號(無表示為新增,有表示為更新)", required = false)
	private BigInteger id;
	
	@ApiModelProperty(value = "表單id", required = true)
	private BigInteger formId;
	
	@ApiModelProperty(value = "簽名軌跡內容(base64)", required = false)
	private String signTrack;
	
	@ApiModelProperty(value = "客戶id", required = true)
	private String customerId;
	
	@ApiModelProperty(value = "醫師id", required = false)
	private String doctorId;
	
	@ApiModelProperty(value = "員工id", required = true)
	private String employeeId;
}
