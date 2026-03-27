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
@ApiOperation(value = "設備回傳DTO")
public class DeviceResultListDto {
	
	@ApiModelProperty(value = "設備流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "設備所在據點")
    private BigInteger branchId;
	
	@ApiModelProperty(value = "設備所在據點名稱")
    private String branchName;
	
	@ApiModelProperty(value = "設備 ID")
    private String deviceId;
	
	@ApiModelProperty(value = "最後回傳日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;
	
	@ApiModelProperty(value = "設備軟體版本")
    private String version;
	
	@ApiModelProperty(value = "設備電量")
    private String battery;

	@ApiModelProperty(value = "設備IP")
    private String ip;
	
	@ApiModelProperty(value = "最後登入人員 ID")
    private String lastLoginUser;
	
	@ApiModelProperty(value = "最後登入人員名稱")
    private String lastLoginUserName;
}
