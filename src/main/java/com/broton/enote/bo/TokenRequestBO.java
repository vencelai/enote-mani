package com.broton.enote.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("專案請求令牌 DTO")
public class TokenRequestBO {

	@ApiModelProperty("使用者名稱")
	private String userName;
	
	@ApiModelProperty("專案代碼")
	private String clientId;
	
	@ApiModelProperty("專案密鑰")
	private String clientSecret;
}
