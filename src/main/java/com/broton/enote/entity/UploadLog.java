package com.broton.enote.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "upload_log")
@ApiModel(value = "上傳記錄", description = "上傳記錄")
public class UploadLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "客戶 ID", required = false)
	@Column(name = "customer_id", columnDefinition = "varchar")
	private String customerId;
	
	@ApiModelProperty(value = "客戶姓名", required = false)
	@Column(name = "customer_name", columnDefinition = "varchar")
	private String customerName;
	
	@ApiModelProperty(value = "客戶手機", required = false)
	@Column(name = "customer_phone", columnDefinition = "varchar")
	private String customerPhone;
	
	@ApiModelProperty(value = "文件名稱 ID", required = false)
	@Column(name = "file_name", columnDefinition = "varchar")
	private String fileName;
	
	@ApiModelProperty(value = "文件類別 ID", required = false)
	@Column(name = "file_type_id", columnDefinition = "varchar")
	private String fileTypeId;
	
	@ApiModelProperty(value = "醫師 ID", required = false)
	@Column(name = "doctor_id", columnDefinition = "varchar")
	private String doctorId;
	
	@ApiModelProperty(value = "醫師姓名", required = false)
	@Column(name = "doctor_name", columnDefinition = "varchar")
	private String doctorName;
	
	@ApiModelProperty(value = "PDF 內容", required = false)
	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "file_content", columnDefinition = "longblob")
	private byte[] fileContent;
	
	@ApiModelProperty(value = "上傳狀態  0:失敗  1:成功", required = false)
	@Column(name = "status", columnDefinition = "int")
	private Integer status;
	
	@ApiModelProperty(value = "據點 id", required = false)
	@Column(name = "branch_id", columnDefinition = "bigint")
	private BigInteger branchId;
	
	@ApiModelProperty(value = "上傳失敗次數", required = false)
	@Column(name = "fail_count", columnDefinition = "int")
	private Integer failCount;
	
	@ApiModelProperty(value = "新增人員", required = false)
	@Column(name = "create_user", columnDefinition = "varchar(50)")
	private String createUser;
	
	@ApiModelProperty(value = "新增日期", required = false)
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;

}
