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
@ApiOperation(value = "據點回傳DTO")
public class BranchListResultDto {
	
	@ApiModelProperty(value = "據點自動流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "據點名稱")
    private String branchName;
	
	@ApiModelProperty(value = "ERP對映的id")
    private String erpId;
	
	@ApiModelProperty(value = "網段")
    private String network;

}
