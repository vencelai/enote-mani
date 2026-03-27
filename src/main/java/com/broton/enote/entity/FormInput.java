package com.broton.enote.entity;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "form_input")
@ApiModel(value = "表單輸入資料", description = "表單輸入資料")
public class FormInput {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "所屬表單資料ID", required = true)
	@Column(name = "form_id", columnDefinition = "bigint")
	private BigInteger formId;
	
	@ApiModelProperty(value = "所屬頁碼", required = true)
	@Column(name = "page_no", columnDefinition = "int")
	private Integer pageNo;
	
	@ApiModelProperty(value = "1:手寫 2:文字輸入...", required = false)
	@Column(name = "input_type", columnDefinition = "int")
	private Integer inputType;
	
	@ApiModelProperty(value = "座標 json", required = false)
	@Column(name = "position", columnDefinition = "varchar")
	private String position;
	
	@ApiModelProperty(value = "目的座標說明", required = false)
	@Column(name = "memo", columnDefinition = "varchar")
	private String memo;
	
	@ApiModelProperty(value = "建立人員", required = false)
	@Column(name = "create_user", columnDefinition = "varchar(50)")
	private String createUser;
	
	@ApiModelProperty(value = "建立日期", required = false)
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;
	
	@ApiModelProperty(value = "修改人員", required = false)
	@Column(name = "edit_user", columnDefinition = "varchar(50)")
	private String editUser;
	
	@ApiModelProperty(value = "修改日期", required = false)
	@Column(name = "edit_date", columnDefinition = "datetime")
	private Date editDate;
}
