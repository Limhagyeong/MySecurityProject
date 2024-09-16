package com.hg.web.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice // 전역적 예외 처리 컨트롤을 위한 어노테이션, json으로 예외 정보 클라이언트에 전달해줌
@Slf4j
public class GlobalExceptionHandler {
	
	// 400 관련 예외 처리
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBadRequestException(BadRequestException e) {
        log.error("BadRequestException: {}", e.getMessage(), e);
        
        ResponseDTO<Void> responseApi = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(),e.getMessage());

        return new ResponseEntity<>(responseApi, HttpStatus.BAD_REQUEST);
    }
}

