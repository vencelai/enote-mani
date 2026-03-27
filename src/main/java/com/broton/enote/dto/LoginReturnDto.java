package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "登入回傳物件")
public class LoginReturnDto {

    @ApiModelProperty(value = "名稱")
    private String name;
    
    @ApiModelProperty(value = "id")
    private BigInteger id;
    
    @ApiModelProperty(value = "類別")
    private String userType;
    
    @ApiModelProperty(value = "令牌")
    private String token;
}
