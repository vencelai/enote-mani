package com.broton.enote.exception;

public class ErrorCodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String errorCode;

	/** msg拿來做為回傳內容或錯誤訊息 **/
	public ErrorCodeException(String errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
