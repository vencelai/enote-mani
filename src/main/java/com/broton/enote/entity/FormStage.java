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
@Table(name = "form_stage")
@ApiModel(value = "表單暫存", description = "表單暫存")
public class FormStage {
	
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
	
	@ApiModelProperty(value = "所屬表單id", required = true)
	@Column(name = "form_id", columnDefinition = "bigint")
	private BigInteger formId;
	
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
	
	@ApiModelProperty(value = "簽名軌跡", required = false)
	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "sign_track", columnDefinition = "longblob")
	private byte[] signTrack;
	
	@ApiModelProperty(value = "據點 id", required = false)
	@Column(name = "branch_id", columnDefinition = "bigint")
	private BigInteger branchId;
	
	@ApiModelProperty(value = "是否棄用 y/n", required = false)
	@Column(name = "termination", columnDefinition = "varchar(2)")
	private String termination;
	
	@ApiModelProperty(value = "檔案鎖定的員工帳號", required = false)
	@Column(name = "lock_ad", columnDefinition = "varchar(50)")
	private String lockAd;
	
	@ApiModelProperty(value = "檔案鎖定的員工姓名", required = false)
	@Column(name = "lock_ad_name", columnDefinition = "varchar(50)")
	private String lockAdName;
	
	@ApiModelProperty(value = "檔案鎖定的日期", required = false)
	@Column(name = "lock_date", columnDefinition = "datetime")
	private Date lockDate;
	
	@ApiModelProperty(value = "新增人員", required = false)
	@Column(name = "create_user", columnDefinition = "varchar(50)")
	private String createUser;
	
	@ApiModelProperty(value = "新增日期", required = false)
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;

}
