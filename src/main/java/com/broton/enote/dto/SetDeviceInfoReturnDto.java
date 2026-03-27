package com.broton.enote.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiOperation(value = "接收板子發送的設備狀態回傳物件")
public class SetDeviceInfoReturnDto {

    @ApiModelProperty(value = "所屬據點id")
    private BigInteger branchId;
}
