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
@Table(name = "branch")
@ApiModel(value = "據點", description = "據點")
public class Branch {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "據點自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "據點名稱", required = false)
	@Column(name = "branch_name", columnDefinition = "varchar(255)")
	private String branchName;
	
	@ApiModelProperty(value = "ERP 對映的id", required = false)
	@Column(name = "erp_id", columnDefinition = "varchar(255)")
	private String erp_id;
	
	@ApiModelProperty(value = "網段", required = false)
	@Column(name = "network", columnDefinition = "varchar(255)")
	private String network;
}
