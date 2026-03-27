/**
 * 
 */
package com.broton.enote.dto.common;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.broton.enote.utils.JwtTokenUtil;

public class JwtRequestToken extends UsernamePasswordAuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private String token;
	
	public JwtRequestToken(String token) {
		super(JwtTokenUtil.getUsernameFromToken(token), null);
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
}
