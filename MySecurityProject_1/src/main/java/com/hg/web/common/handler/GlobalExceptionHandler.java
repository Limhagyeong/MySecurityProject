package com.hg.web.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.hg.web.common.exception.AuthenticationException;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.common.exception.InternalErrorException;
import com.hg.web.dto.api.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice // 전역적 예외 처리 컨트롤을 위한 어노테이션, json으로 예외 정보 클라이언트에 전달해줌
public class GlobalExceptionHandler  {
	
	// 400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException: {}", e.getMessage(), e);
        
        ResponseDTO<Void> responseApi = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),e.getMessage());

        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }
    
    // 401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleNotFoundException(AuthenticationException e) {
    	log.error("UnAuthorizedException: {}",e.getMessage(), e);
    	ResponseDTO<Void> responseApi=new ResponseDTO<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    	return new ResponseEntity<>(responseApi, HttpStatus.UNAUTHORIZED);
    }
    
    //Validation
    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInternalErrorException(InternalErrorException e) {
    	log.error("ValidationException: {}", e.getServerLogginMessage(), e);   
        
        ResponseDTO<Void> responseApi = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());

        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }
    
    // Validation 실패 오류 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.error("ValidationException: {}", e.getMessage(), e);

        ResponseDTO<Void> responseApi = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),"잘못된 입력값입니다.");

        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }
    

}

