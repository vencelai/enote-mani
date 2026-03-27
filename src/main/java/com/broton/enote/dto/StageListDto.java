package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "暫存表單-列表回傳物件")
public class StageListDto {

    @ApiModelProperty(value = "暫存表單流水號")
    private BigInteger stageId;
    
    @ApiModelProperty(value = "表單名稱")
    private String formName;
    
    @ApiModelProperty(value = "客戶姓名")
    private String customerName;
    
    @ApiModelProperty(value = "醫師id")
    private String doctorId;
    
    @ApiModelProperty(value = "醫師姓名")
    private String doctorName;
    
    @ApiModelProperty(value = "建立員工姓名")
    private String createEmployeeName;
    
    @ApiModelProperty(value = "表單建立日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    
    @ApiModelProperty(value = "表單暫存日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stageDate;
    
}
