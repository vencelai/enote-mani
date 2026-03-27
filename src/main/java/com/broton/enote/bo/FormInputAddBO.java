package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormInputAddBO {

	@ApiModelProperty(value = "文件 ID", required = true)
	private BigInteger formId;
	
	@ApiModelProperty(value = "pdf 頁碼", required = true)
	private int pageNo;
	
	@ApiModelProperty(value = "輸入項目類型", required = true)
	private int inputType;
	
	@ApiModelProperty(value = "座標值 json 字串", required = true)
	private String position;	
	
	@ApiModelProperty(value = "目的座標說明", required = true)
	private String memo;
}
