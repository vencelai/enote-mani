package com.broton.enote.bo;

import lombok.Data;

/**表單資料列表查詢物件
 * @author vencelai
 */
@Data
public class QueryFormListBO {

	// 表單類別 ID
	//private BigInteger formTypeId;
	private String formTypeId;
	
	private Integer start;
	
	private Integer length;
	
}
