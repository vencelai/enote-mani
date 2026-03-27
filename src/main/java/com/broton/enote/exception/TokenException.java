/**
 * 
 */
package com.broton.enote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 * @title 令牌例外
 * @desction 令牌例外
 */
public class TokenException extends AuthenticationCredentialsNotFoundException {

	private static final long serialVersionUID = 1L;
	private final HttpStatus status;

	public TokenException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public HttpStatus getHttpStatus() {
		return status;
	}

}
