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
@Table(name = "stage")
@ApiModel(value = "暫存表單", description = "暫存表單")
public class Stage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "據點自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "表單id", required = true)
	@Column(name = "form_id", columnDefinition = "bigint")
	private BigInteger formId;
	
	@ApiModelProperty(value = "簽名軌跡內容", required = false)
	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "sign_track", columnDefinition = "longblob")
	private byte[] signTrack;
	
	@ApiModelProperty(value = "客戶id", required = true)
	@Column(name = "customer_id", columnDefinition = "varchar(255)")
	private String customerId;
	
	@ApiModelProperty(value = "客戶姓名", required = false)
	@Column(name = "customer_name", columnDefinition = "varchar(255)")
	private String customerName;
	
	@ApiModelProperty(value = "醫師id", required = false)
	@Column(name = "doctor_id", columnDefinition = "varchar(255)")
	private String doctorId;
	
	@ApiModelProperty(value = "員工id", required = true)
	@Column(name = "employee_id", columnDefinition = "varchar(255)")
	private String employeeId;
	
	@ApiModelProperty(value = "員工姓名", required = true)
	@Column(name = "employee_name", columnDefinition = "varchar(255)")
	private String employeeName;
	
	@ApiModelProperty(value = "新增日期", required = false)
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;
	
	@ApiModelProperty(value = "暫存日期", required = false)
	@Column(name = "stage_date", columnDefinition = "datetime")
	private Date stageDate;
	
}
