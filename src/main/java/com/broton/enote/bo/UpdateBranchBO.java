package com.broton.enote.bo;

import java.math.BigInteger;

import lombok.Data;

@Data
public class UpdateBranchBO {
	
	private BigInteger branchId;

	private String branchName;
	
	private String erpId;
	
	private String network;
}
