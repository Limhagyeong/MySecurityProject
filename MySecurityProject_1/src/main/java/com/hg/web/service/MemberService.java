package com.hg.web.service;


import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
	public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto); // 회원가입
	public ResponseEntity<ResponseDTO<Void>> CountID(String username); // ID 중복 검증
	
}

