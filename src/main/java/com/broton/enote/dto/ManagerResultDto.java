package com.broton.enote.dto;

import java.math.BigInteger;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiOperation(value = "管理人員資料回傳DTO")
public class ManagerResultDto {

    @ApiModelProperty(value = "資料流水號")
    private BigInteger id;
        
    @ApiModelProperty(value = "管理人員帳號")
    private String userId;

    @ApiModelProperty(value = "會員名稱")
    private String userName;
    
    @ApiModelProperty(value = "1:啟用 / 2:停用")
    private int active;
    
    @ApiModelProperty(value = "啟用/停用")
    private String activeName;
    
    @ApiModelProperty(value = "建立日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    
    @ApiModelProperty(value = "建立人員")
    private String createUser;
    
    @ApiModelProperty(value = "修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editDate;
    
    @ApiModelProperty(value = "修改人員")
    private String editUser;

}
