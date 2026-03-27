package com.broton.enote.bo;

import java.math.BigInteger;

import lombok.Data;

@Data
public class UpdateManagerBO {
	
	private BigInteger id;
	
	private String userName;
	
	private String userPassword;
	
	private Integer active;
}
