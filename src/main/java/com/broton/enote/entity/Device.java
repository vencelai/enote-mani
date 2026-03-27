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
@Table(name = "device")
@ApiModel(value = "設備狀態", description = "設備狀態")
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "所在據點", required = false)
	@Column(name = "branch_id", columnDefinition = "bigint")
	private BigInteger branchId;
	
	@ApiModelProperty(value = "裝置id", required = false)
	@Column(name = "device_id", columnDefinition = "varchar")
	private String deviceId;
	
	@ApiModelProperty(value = "最後回傳時間", required = false)
	@Column(name = "last_update_time", columnDefinition = "datetime")
	private Date lastUpdateTime;
	
	@ApiModelProperty(value = "軟體版本號", required = false)
	@Column(name = "version", columnDefinition = "varchar")
	private String version;
	
	@ApiModelProperty(value = "電量", required = false)
	@Column(name = "battery", columnDefinition = "varchar")
	private String battery;
	
	@ApiModelProperty(value = "IP", required = false)
	@Column(name = "ip", columnDefinition = "varchar")
	private String ip;
	
	@ApiModelProperty(value = "最後登入人員 ID", required = false)
	@Column(name = "last_login_user", columnDefinition = "varchar")
	private String lastLoginUser;
	
	@ApiModelProperty(value = "最後登入人員名稱", required = false)
	@Column(name = "last_login_user_name", columnDefinition = "varchar")
	private String lastLoginUserName;
}
