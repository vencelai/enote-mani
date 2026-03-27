package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "新增客戶初診諮詢返回物件")
public class RetConsult {

	@ApiModelProperty(value = "傳回處理的客戶編號(CSID)")
	private String csid;
	
	@ApiModelProperty(value = "作業分店代碼")
	private String brno;
	
	@ApiModelProperty(value = "回傳狀態碼'1'=新增存檔完成,'0'=已存在相同諮詢資料,已更新完成,<'0' =存檔錯誤")
	private String RetCode;
	
	@ApiModelProperty(value = "回傳錯誤訊息,空白=正確完成,其他內容表示錯誤提示")
	private String RetMessage;
	
}
