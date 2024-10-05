package com.hg.web.common.exception;
// 401 에러 제어 (로그인 실패)
public class AuthenticationException extends RuntimeException {
	
	private final String errorCode;
	private final String errorMessage;

	public AuthenticationException(String errorMessage) {
		super(errorMessage);
		this.errorCode="401";
		this.errorMessage = errorMessage;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}
	
	
}
