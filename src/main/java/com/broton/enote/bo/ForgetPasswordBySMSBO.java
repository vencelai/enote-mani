package com.broton.enote.bo;

import javax.validation.constraints.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "忘記密碼-使用簡訊傳輸物件")
public class ForgetPasswordBySMSBO {
	
	@NotEmpty(message = "請輸入身份證號/居留證號")
	@ApiModelProperty(value = "身份證號/居留證號")
	private String idNum;

	@NotEmpty(message = "請輸入行動電話")
	@ApiModelProperty(value = "行動電話")
	private String mobileNum;
	
}
