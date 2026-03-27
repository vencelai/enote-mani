package com.broton.enote.bo;

import java.math.BigInteger;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**設備狀態資料列表查詢物件
 * @author vencelai
 */
@Data
public class QueryDeviceStatusListBO {

	@ApiModelProperty(value = "查詢的日期")
	private String date;
	
	@ApiModelProperty(value = "員工 ID")
	private String employeeId;
	
	@ApiModelProperty(value = "據點 ID")
	private BigInteger branchId;
	
	@ApiModelProperty(value = "設備 ID")
	private String deviceId;
	
	@ApiModelProperty(value = "排序欄位 deviceId, lastUpdateTime, version")
	private String orderBy;
	
	@ApiModelProperty(value = "遞增或遞減  asc, desc")
	private String sort;
	
	private Integer start;
	
	private Integer length;
	
}
