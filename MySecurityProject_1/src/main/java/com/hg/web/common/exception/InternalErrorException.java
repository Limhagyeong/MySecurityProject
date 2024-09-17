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
		this.serverLogginMessage=serverLogginMessage;
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
