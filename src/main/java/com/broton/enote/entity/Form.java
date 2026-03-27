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
@Table(name = "form")
@ApiModel(value = "表單資料", description = "表單資料")
public class Form {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	//@ApiModelProperty(value = "文件名稱ID", required = false)
	//@Column(name = "form_name_id", columnDefinition = "bigint")
	//private BigInteger formNameId;
	
	@ApiModelProperty(value = "文件名稱", required = false)
	@Column(name = "form_name", columnDefinition = "varchar")
	private String formName;
	
	@ApiModelProperty(value = "文件所屬類別ID", required = false)
	@Column(name = "form_type_id", columnDefinition = "varchar")
	private String formTypeId;
	
	@ApiModelProperty(value = "PDF 內容", required = false)
	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "file_content", columnDefinition = "longblob")
	private byte[] fileContent;
	
	@ApiModelProperty(value = "版本號", required = false)
	@Column(name = "version", columnDefinition = "int")
	private Integer version;
	
	@ApiModelProperty(value = "是否顯示  0:否  1:是", required = false)
	@Column(name = "show_flag", columnDefinition = "int")
	private Integer showFlag;
	
	@ApiModelProperty(value = "是否需要醫師簽章  0:否  1:是", required = false)
	@Column(name = "doctor_sign", columnDefinition = "int")
	private Integer doctorSign;
	
	@ApiModelProperty(value = "新增人員", required = false)
	@Column(name = "create_user", columnDefinition = "varchar(50)")
	private String createUser;
	
	@ApiModelProperty(value = "新增日期", required = false)
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;
	
	@ApiModelProperty(value = "修改人員", required = false)
	@Column(name = "edit_user", columnDefinition = "varchar(50)")
	private String editUser;
	
	@ApiModelProperty(value = "修改日期", required = false)
	@Column(name = "edit_date", columnDefinition = "datetime")
	private Date editDate;
	
}
