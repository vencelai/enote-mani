package com.broton.enote.dto;

import java.math.BigInteger;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiOperation(value = "表單資料回傳DTO")
public class FormListResultDto {

    @ApiModelProperty(value = "資料流水號")
    private BigInteger id;
        
    //@ApiModelProperty(value = "文件名稱 ID")
    //private BigInteger formId;

    @ApiModelProperty(value = "文件名稱")
    private String formName;        
    
    @ApiModelProperty(value = "文件類別 ID")
    private String formTypeId;
    
    //@ApiModelProperty(value = "文件類別代號")
    //private String formTypeCode;
    
    @ApiModelProperty(value = "文件類別名稱")
    private String formTypeName;
    
    @ApiModelProperty(value = "版本號")
    private String version;
    
    @ApiModelProperty(value = "建立日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
    
    @ApiModelProperty(value = "建立人員")
    private String createUser;
    
    @ApiModelProperty(value = "修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date editDate;
    
    @ApiModelProperty(value = "修改人員")
    private String editUser;
    
    @ApiModelProperty(value = "是否顯示 0:否 1:是")
    private Integer showFlag;
    
    @ApiModelProperty(value = "是否需要醫師簽章 0:否  1:是")
    private Integer doctorSign;

}
