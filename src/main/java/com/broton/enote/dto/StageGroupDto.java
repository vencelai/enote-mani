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
@ApiOperation(value = "暫存表單-客戶群組回傳物件")
public class StageGroupDto {

    @ApiModelProperty(value = "客戶id")
    private String customerId;
    
    @ApiModelProperty(value = "客戶姓名")
    private String customerName;
    
}
