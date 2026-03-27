package com.broton.enote.dto;

import java.math.BigInteger;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@ApiOperation(value = "表單暫存資料回傳DTO")
public class FormStageResultListDto {
	
	@ApiModelProperty(value = "表單暫存流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "客戶 ID")
    private String customerId;
	
	@ApiModelProperty(value = "客戶手機")
    private String customerPhone;
	
	@ApiModelProperty(value = "客戶姓名")
    private String customerName;
	
	@ApiModelProperty(value = "原始表單id")
    private BigInteger formId;
	
	@ApiModelProperty(value = "文件名稱")
    private String fileName;
	
	@ApiModelProperty(value = "文件類別ID")
    private String fileTypeId;
	
	@ApiModelProperty(value = "醫師id")
    private String doctorId;
	
	@ApiModelProperty(value = "醫師名稱")
    private String doctorName;
	
	@JsonIgnore
	@ApiModelProperty(value = "pdf 檔案內容")
    private byte[] fileContent;
	
	@JsonIgnore
	@ApiModelProperty(value = "簽名軌跡")
    private byte[] signTrack;
	
	@ApiModelProperty(value = "暫存的日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
	
	@ApiModelProperty(value = "上傳人員")
    private String createUser;
	
	@ApiModelProperty(value = "據點 id")
    private BigInteger branchId;
	
	@ApiModelProperty(value = "據點名稱")
    private String branchName;
	
	@ApiModelProperty(value = "鎖定的員工id")
    private String lockAd;
	
	@ApiModelProperty(value = "鎖定的員工姓名")
    private String lockAdName;
	
	@ApiModelProperty(value = "鎖定的日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lockDate;
}
