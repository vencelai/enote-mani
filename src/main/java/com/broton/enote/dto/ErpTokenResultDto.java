package com.broton.enote.dto;

import java.math.BigInteger;
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
@ApiOperation(value = "ERP 登入 回傳物件")
public class ErpTokenResultDto {
	
	@ApiModelProperty(value = "登入者id")
    private String userId;
	
	@ApiModelProperty(value = "登入者名稱")
    private String userName;
    
    @ApiModelProperty(value = "由設備id取得對映所在的據點id")
    private BigInteger branchId;

    @ApiModelProperty(value = "ERP-返回的 token")
    private String token;
    
    @ApiModelProperty(value = "給平板校時用")
    private Long currentDateTime;
    
    @ApiModelProperty(value = "新增客戶用的表單 id")
    private BigInteger newCustomerFormId;
}
