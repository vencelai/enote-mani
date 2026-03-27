package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.math.BigInteger;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "暫存表單回傳物件")
public class StageDto {

    @ApiModelProperty(value = "暫存表單流水號")
    private BigInteger stageId;
    
    @ApiModelProperty(value = "表單id")
    private BigInteger formId;
    
    @ApiModelProperty(value = "簽名軌跡(base64)")
    private byte[] signTrack;
    
    @ApiModelProperty(value = "客戶id")
    private String customerId;
    
    @ApiModelProperty(value = "客戶姓名")
    private String customerName;
    
    @ApiModelProperty(value = "客戶手機")
    private String customerTel;
    
    @ApiModelProperty(value = "客戶生日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String customerBirthday;
    
    private String rcYY;
    
    private String rcMM;
    
    private String rcDD;
    
    @ApiModelProperty(value = "醫師id")
    private String doctorId;
    
    @ApiModelProperty(value = "醫師姓名")
    private String doctorName;
    
    @ApiModelProperty(value = "建立員工id")
    private String createEmployeeId;
    
    @ApiModelProperty(value = "建立員工姓名")
    private String createEmployeeName;
    
}
