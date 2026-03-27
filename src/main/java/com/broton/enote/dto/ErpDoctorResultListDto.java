package com.broton.enote.dto;

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
@ApiOperation(value = "ERP醫師回傳DTO")
public class ErpDoctorResultListDto {
	
	@ApiModelProperty(value = "")
    private String brNo;
	
	@ApiModelProperty(value = "")
    private String stfno;
	
	@ApiModelProperty(value = "姓名")
    private String stfName;
	
	@ApiModelProperty(value = "職稱")
    private String jobTitle;
	
	@ApiModelProperty(value = "")
    private String loginID;
	
	@ApiModelProperty(value = "醫字號")
    private String drLicenseNo;
	
	@ApiModelProperty(value = "")
    private String manageBranchs;
	
	@ApiModelProperty(value = "")
    private String userLevel;
	
	@ApiModelProperty(value = "")
    private String modifyTime;
}
