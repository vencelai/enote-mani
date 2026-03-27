/**
 * 
 */
package com.broton.enote.dto.common;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Data;

/**
 * @title 取用token專用
 * @description 取用token專用
 */
@Data
public class TokenDto {
	@JsonAlias("code")
	private String code;
	@JsonAlias("msg")
	private String msg;
	@JsonAlias("data")
	private String bearerToken;
}
