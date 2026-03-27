package com.broton.enote.entity;

import java.math.BigInteger;
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
@Table(name = "form_type")
@ApiModel(value = "文件所屬類別", description = "文件所屬類別")
public class FormType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "文件類別代號", required = false)
	@Column(name = "code", columnDefinition = "varchar(255)")
	private String code;
	
	@ApiModelProperty(value = "文件類別名稱", required = false)
	@Column(name = "name", columnDefinition = "varchar(255)")
	private String name;
	
}
