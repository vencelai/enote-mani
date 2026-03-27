package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
@ApiOperation(value = "上傳表單 ID 物件")
public class UploadLogIdBO {

	@ApiModelProperty(value = "上傳日誌自動流水號 ID")
	private BigInteger id;	
}
