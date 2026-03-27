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
@ApiOperation(value = "醫師回傳DTO")
public class DoctorResultListDto {
	
	@ApiModelProperty(value = "流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "醫師ID編號")
    private String doctorIdNo;
	
	@ApiModelProperty(value = "醫師名稱")
    private String doctorName;
	
	@ApiModelProperty(value = "醫字號")
    private String medicalNumber;

}
