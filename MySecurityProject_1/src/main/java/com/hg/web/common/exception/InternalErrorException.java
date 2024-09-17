package com.hg.web.common.exception;

public class InternalErrorException extends RuntimeException{
	
	private final String errorCode;
	private final String errorMessage;
	private final String serverLogginMessage;

	//서버 내부오류는 클라이언트에게는 단순 ' 서버내부 오류입니다. ' 를 전송하고 , 서버측에서는 상세 로그를 확인해야하니
	//두개의 매개변수를 받아 첫번째 인자는 클라이언트에게 전송할 에러메시지 , 두번째 인자는 서버가 인지할 메시지를 받는다 .
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
