package com.broton.enote.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**上傳日誌列表查詢物件
 * @author vencelai
 */
@Data
public class QueryUploadLogListBO {
	
	@ApiModelProperty(value = "查詢的日期")
	private String date;
	
	@ApiModelProperty(value = "查詢的客戶ID / 姓名 / 手機 關鍵字")
	private String customerIdOrPhone;
	
	@ApiModelProperty(value = "醫師流水號 ID") 
	private String DoctorId;

	@ApiModelProperty(value = "上傳狀態 0:失敗  1:成功")
	private Integer uploadStatus;
	
	@ApiModelProperty(value = "表單類型 ID")
	private String formTypeId;
	
	@ApiModelProperty(value = "據點代碼")
	private Integer branchId;
	
	private Integer start;
	
	private Integer length;
	
}
