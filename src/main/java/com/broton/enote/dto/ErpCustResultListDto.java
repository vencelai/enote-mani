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
@ApiOperation(value = "ERP客戶資料回傳DTO")
public class ErpCustResultListDto {
	
	@ApiModelProperty(value = "客戶代碼")
    private String csid;
	
	@ApiModelProperty(value = "客戶名稱")
    private String custCname;
	
	@ApiModelProperty(value = "行動電話")
    private String gsmtel;
	
	@ApiModelProperty(value = "出生年月日 (yyyy/mm/dd)")
    private String birthday;
	
}
