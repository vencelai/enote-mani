package com.broton.enote.bo;

import lombok.Data;

/** (醫師)員工資料列表查詢物件
 * @author vencelai
 * @date 2022年12月29日 下午4:17:05

 */
@Data
public class ErpGetStaffsBO {

	private String brNo;
	
	private String jobTitle;
	
	private String stfNo;
	
	private String loginID;
	
	private String beginModifyTime;
	
	private String token;
}
