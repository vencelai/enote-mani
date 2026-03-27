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
@ApiOperation(value = "上傳日誌資料回傳DTO")
public class UploadLogResultListDto {
	
	@ApiModelProperty(value = "上傳日誌流水號")
    private BigInteger id;
	
	@ApiModelProperty(value = "客戶 ID")
    private String customerId;
	
	@ApiModelProperty(value = "客戶手機")
    private String customerPhone;
	
	@ApiModelProperty(value = "客戶姓名")
    private String customerName;
	
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
	
	@ApiModelProperty(value = "上傳狀態 0:失敗  1:成功")
    private Integer status;
	
	@ApiModelProperty(value = "上傳失敗次數")
    private Integer failCount;
	
	@ApiModelProperty(value = "失敗/成功")
    private String statusName;
	
	@ApiModelProperty(value = "上傳的日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
	
	@ApiModelProperty(value = "上傳人員")
    private String createUser;
	
	@ApiModelProperty(value = "據點 id")
    private BigInteger branchId;
	
	@ApiModelProperty(value = "據點名稱")
    private String branchName;
}
