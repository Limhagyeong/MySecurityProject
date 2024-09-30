package com.hg.web.dto;

import lombok.Data;

@Data
public class ResponseDTO<T> {

	private boolean success;
	private int code;
	private String message;
	private T data;
	private String loggingMessage;
	
	
	// 요청 성공 시 ResponseApi (데이터 없는 단순 요청에 대한)
	public ResponseDTO() {
		this.success=true;
		this.code=200;
		this.message="요청성공";
		this.data=null;
	}
	
	// 요청 성공 시 ResponseApi
	public ResponseDTO(T data) {
		this.success=true;
		this.code=200;
		this.message="요청성공";
		this.data=data; // => 제네릭으로 받음
	}
	
	// 요청 실패 시 ResponseApi
	public ResponseDTO(int code,String message) {
	    
        this.success=false; // 실패 
        this.code = code;
        this.message = message; // 전달할 메시지
        this.data = null; // 보낼 데이터는 없음 
    }
}
