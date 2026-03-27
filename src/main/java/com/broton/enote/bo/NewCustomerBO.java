package com.broton.enote.bo;

import java.math.BigInteger;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@ApiModel(description = "平板新增客戶回傳物件")
public class NewCustomerBO {
	
	@ApiModelProperty(value = "據點id", required = true)
	private BigInteger branchId;
	
	// 前端不用給值-後端填入
	@ApiModelProperty(value = "分店碼", required = false)
	private String brNo;
	// 前端不用給值-ERP 端填入
	@ApiModelProperty(value = "系統客代(病歷碼)", required = false)
	private String csid;
	// 前端不用給值-後端填入
	@ApiModelProperty(value = "諮詢日期", required = false)
	private String consultDate;
	
	// ------------- 基本資料 -------------
	@ApiModelProperty(value = "平板登入的員工帳號", required = true)
	private String userID;
	
	@ApiModelProperty(value = "客戶姓名", required = true)
	private String custCname;
	
	@ApiModelProperty(value = "身份證字號", required = true)
	private String custTWID;
	
	@ApiModelProperty(value = "性別 1:男 2:女", required = true)
	private String sexType;
	
	@ApiModelProperty(value = "出生年月日(西元年)", required = true)
	private String birthday;
	
	@ApiModelProperty(value = "婚姻狀態 1:已婚 2:未婚 3:其它", required = true)
	private String marriage;
	
	@ApiModelProperty(value = "職業", required = false)
	private String jobTitle;
	
	@ApiModelProperty(value = "身高(cm)", required = true)
	private String custHeight;
	
	@ApiModelProperty(value = "體重(kg)", required = true)
	private String custWeight;
	
	@ApiModelProperty(value = "血型", required = true)
	private String custBlood;
	
	@ApiModelProperty(value = "電子郵件", required = false)
	private String custEmail;
	
	@ApiModelProperty(value = "偏好描述/注意事項", required = false)
	private String custLikeMemo;
	
	@ApiModelProperty(value = "客戶資料備註", required = false)
	private String custDataMemo;
	
	@ApiModelProperty(value = "行動電話", required = true)
	private String gsmtel;
	
	@ApiModelProperty(value = "行動電話備註", required = false)
	private String custGSMMemo;
	
	@ApiModelProperty(value = "住家電話", required = false)
	private String custHomeTel;
	
	@ApiModelProperty(value = "住家(公司)電話備註", required = false)
	private String custHomeMemo;
	
	@ApiModelProperty(value = "公司電話", required = false)
	private String custOfficeTel;
	
	@ApiModelProperty(value = "公司電話備註", required = false)
	private String custOfficeMemo;
	
	@ApiModelProperty(value = "緊急連絡人電話", required = true)
	private String custMergTel;
	
	@ApiModelProperty(value = "緊急連絡人姓名", required = true)
	private String custMergName;
	
	@ApiModelProperty(value = "緊急連絡人關係", required = true)
	private String custMergRelation;
	
	@ApiModelProperty(value = "連絡地址", required = true)
	private String custAddress;
	
	@ApiModelProperty(value = "LINE ID", required = false)
	private String custLineID;
		
	// 諮詢問券選項明細值陣列		
	@ApiModelProperty(value = "CustomerItemListBO", required = false)
	private List<CustomerItemListBO> itemList;
	
	//------------------------------------------------------------------------------------------
	
	@ApiModelProperty(value = "要上傳的PDF base64 字串", required = true)
	private String pdfBase64;
	
	@ApiModelProperty(value = "前台不需給-後端填入", required = true)
	private String pdfName;
	
	@ApiModelProperty(value = "令牌", required = true)
	private String token;
}
