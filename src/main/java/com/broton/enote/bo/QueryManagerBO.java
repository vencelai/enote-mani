package com.broton.enote.bo;

import lombok.Data;

/**管理人員資料列表查詢物件
 * @author vencelai
 */
@Data
public class QueryManagerBO {

	// 帳號
	private String userId;
	
	// 名稱關鍵字
	private String userName;
	
	private Integer start;
	
	private Integer length;
}
