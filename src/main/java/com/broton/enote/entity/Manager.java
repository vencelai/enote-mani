package com.broton.enote.entity;

import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理人員資料表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manager")
@Entity
@DynamicUpdate
public class Manager {
	
	/**
	 * 自動流水號
	 */	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@Column(name = "id", columnDefinition = "bigint")
	private BigInteger id;
	
	/**
	 * 使用者id
	 */
	@Column(name = "user_id", columnDefinition = "varchar(100)")
	/*
	@ColumnTransformer(
            read = "CAST(AES_DECRYPT(UNHEX(user_id),'btencdec')as char(1024))",
            write = "HEX(AES_ENCRYPT(?, 'btencdec'))"
    )
    */
	private String userId;
	
	/**
	 * 名稱
	 */
	@Column(name = "user_name", columnDefinition = "varchar(100)")
	/*
	@ColumnTransformer(
            read = "CAST(AES_DECRYPT(UNHEX(user_name),'btencdec')as char(1024))",
            write = "HEX(AES_ENCRYPT(?, 'btencdec'))"
    )
    */
	private String userName;
	
	/**
	 * 登入密碼
	 */
	@Column(name = "user_password", columnDefinition = "varchar(100)")
	@ColumnTransformer(
            read = "CAST(AES_DECRYPT(UNHEX(user_password),'btencdec')as char(1024))",
            write = "HEX(AES_ENCRYPT(?, 'btencdec'))"
    )
	private String userPassword;
		
	/**
	 * 1 啟用 / 2 停用
	 */
	@Column(name = "active", columnDefinition = "tinyint")
	private int active;
	
	/**
	 * 建立人員
	 */
	@Column(name = "create_user", columnDefinition = "varchar(100)")
	private String createUser;
	
	/**
	 * 建立日期
	 */
	@Column(name = "create_date", columnDefinition = "datetime")
	private Date createDate;
	
	/**
	 * 修改人員
	 */
	@Column(name = "edit_user", columnDefinition = "varchar(100)")
	private String editUser;
	
	/**
	 * 修改日期
	 */
	@Column(name = "edit_date", columnDefinition = "datetime")
	private Date editDate;	
}
