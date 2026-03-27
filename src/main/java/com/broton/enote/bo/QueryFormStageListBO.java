package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**表單暫存查詢物件
 * @author vencelai
 */
@Data
public class QueryFormStageListBO {
	
	@ApiModelProperty(value = "查詢的起始日期")
	private String startDate;
	
	@ApiModelProperty(value = "查詢的終止日期")
	private String endDate;
	
	@ApiModelProperty(value = "查詢的客戶ID / 姓名 / 手機 關鍵字")
	private String customerIdOrPhone;
	
	@ApiModelProperty(value = "醫師流水號 ID") 
	private String DoctorId;

	@ApiModelProperty(value = "表單類型 ID")
	private String formTypeId;
	
	@ApiModelProperty(value = "據點代碼")
	private Integer branchId;
	
	private Integer start;
	
	private Integer length;
	
}
