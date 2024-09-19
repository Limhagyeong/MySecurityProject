package com.hg.web.common.exception;

public class InternalErrorException extends RuntimeException{
	
	private final String errorCode;
	private final String errorMessage;
	private final String serverLogginMessage;

	public InternalErrorException(String errorMessage, String serverLogginMessage) {
		// TODO Auto-generated constructor stub
		super(errorMessage);
		this.errorCode="500";
		this.errorMessage=errorMessage;
		this.serverLogginMessage=serverLogginMessage; // 서버에서도 간단히 오류 확인할 수 있또록 로그 메시지 지정
	}

	public String getErrorCode() {
        return errorCode;
    }
	public String getServerLogginMessage() {
		return serverLogginMessage;
	}

    @Override
    public String getMessage() {
        return errorMessage;
    }

}
