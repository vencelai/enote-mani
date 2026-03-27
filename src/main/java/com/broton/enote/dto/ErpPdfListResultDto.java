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
@ApiOperation(value = "取得客戶已簽署PDF清單返回DTO")
public class ErpPdfListResultDto {
	
	@ApiModelProperty(value = "同意書分類代碼")
    private String fromType;
	
	@ApiModelProperty(value = "同意書分類名稱")
    private String fromTypeName;
	
	@ApiModelProperty(value = "PDF檔名(不含路徑,需含.pdf)")
    private String fileName;
	
	@ApiModelProperty(value = "醫師 id")
    private String stfnoDrSrv;
	
	@ApiModelProperty(value = "建檔時間yyyy-mm-dd HH:mm:ss")
    private String createTime;	
	
	@ApiModelProperty(value = "紀錄UNID(取檔要求用)")
    private String unid;

}
