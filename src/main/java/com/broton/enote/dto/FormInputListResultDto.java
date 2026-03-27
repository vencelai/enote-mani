package com.broton.enote.dto;

import java.math.BigInteger;
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
@ApiOperation(value = "表單控制項回傳DTO")
public class FormInputListResultDto {

    @ApiModelProperty(value = "控制項流水號")
    private BigInteger id;
    
    @ApiModelProperty(value = "所屬頁碼")
    private Integer pageNo;
        
    @ApiModelProperty(value = "控制項類別")
    private Integer inputType;
    
    @ApiModelProperty(value = "控制項類別名稱")
    private String inputTypeName;
    
    @JsonIgnore
    @ApiModelProperty(value = "座標字串")
    private String positionStr;

    @ApiModelProperty(value = "座標")
    private Position position;
    
    @JsonIgnore
    @ApiModelProperty(value = "關聯座標字串")
    private String relationPositionStr;
    
    @ApiModelProperty(value = "關聯座標")
    private RelationPositionDto relationPosition;
    
    @ApiModelProperty(value = "目的座標說明")
    private String memo;
    
    @ApiModelProperty(value = "是否為必填  y/n")
    private String required;
    
    @ApiModelProperty(value = "返回值的欄位名稱")
    private String returnField;
    
}
