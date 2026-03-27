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
@ApiOperation(value = "ERP客戶查詢剩餘課程資料回傳DTO")
public class ErpCustSaveResultListDto {
	
	@ApiModelProperty(value = "客戶代碼")
    private String csid;
	
	@ApiModelProperty(value = "商品代碼")
	private String prodItemCode;
	
	@ApiModelProperty(value = "商品名稱")
	private String prodItemName;
	
	@ApiModelProperty(value = "購買日期")
	private String saleDate;
	
	@ApiModelProperty(value = "使用期限")
	private String limitDate;
	
	@ApiModelProperty(value = "點數/堂數類型 儲值點=0/堂數型=1")
	private String pointClassType;
	
	@ApiModelProperty(value = "原購點數")
	private Integer salePoint;
	
	@ApiModelProperty(value = "含贈送點數")
	private Integer giftPoint;
	
	@ApiModelProperty(value = "原購堂數")
	private Integer saleClassQty;
	
	@ApiModelProperty(value = "已用堂數")
	private Integer useClassQty;
	
	@ApiModelProperty(value = "剩餘點數")
	private Integer lastPoint;
	
	@ApiModelProperty(value = "剩餘堂數")
	private Integer lastClassQty;
	
	@ApiModelProperty(value = "尚欠金額")
	private Integer overdraftMoney;
}
