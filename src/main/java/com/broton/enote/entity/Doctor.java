package com.broton.enote.entity;

import java.math.BigInteger;
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
@Table(name = "doctor")
@ApiModel(value = "醫師資料", description = "醫師資料")
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(value = "自動流水號", required = true)
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	@ApiModelProperty(value = "醫生 ID 編號", required = false)
	@Column(name = "doctor_id_no", columnDefinition = "varchar")
	private String doctorIdNo;
	
	@ApiModelProperty(value = "醫生姓名", required = false)
	@Column(name = "doctor_name", columnDefinition = "varchar")
	private String doctorName;
	
	@ApiModelProperty(value = "醫字號", required = false)
	@Column(name = "medical_number", columnDefinition = "varchar")
	private String mediaclNumber;
	
	@ApiModelProperty(value = "印章", required = false)
	@Lob
    @Basic(fetch = FetchType.LAZY)
	@Column(name = "stamp", columnDefinition = "longblob")
	private byte[] stamp;
}
