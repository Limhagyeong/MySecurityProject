package com.hg.web.common.exception;

public class BadRequestException extends RuntimeException{
	
		private final String errorCode;
		private final String errorMessage;
		
	    //400 에러 사용자 정의 예외
		public BadRequestException(String errorMessage) {
			// TODO Auto-generated constructor stub
			super(errorMessage);
			this.errorCode="400";
			this.errorMessage=errorMessage;
		}
	
		public String getErrorCode() {
	        return errorCode;
	    }
		
	    @Override
	    public String getMessage() {
	        return errorMessage;
	    }
}