package com.hg.web.service;


import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {
	public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto); // 회원가입
	public ResponseEntity<ResponseDTO<Void>> CountID(String username); // ID 중복 검증
	public ResponseEntity<ResponseDTO<Void>> findID(UserDTO dto); // ID 찾기
	public ResponseEntity<ResponseDTO<Void>> findPwd(UserDTO dto); // 임시비밀번호 메일 전송
	public ResponseEntity<ResponseDTO<Void>> mailAuth(String email); // 이메일 인증
	public ResponseEntity<ResponseDTO<Void>> mailAuthOK(MailAuthDTO dto); // 이메일 인증 완료
 	
}

