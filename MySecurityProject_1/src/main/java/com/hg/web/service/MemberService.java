package com.hg.web.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;


public interface MemberService {
	ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto); // 회원가입
	ResponseEntity<ResponseDTO<Void>> CountID(String username); // ID 중복 검증
	ResponseEntity<ResponseDTO<Void>> findID(UserDTO dto); // ID 찾기
	ResponseEntity<ResponseDTO<Void>> findPwd(UserDTO dto); // 임시비밀번호 메일 전송
	ResponseEntity<ResponseDTO<Void>> mailAuth(String email); // 이메일 인증
	ResponseEntity<ResponseDTO<Void>> mailAuthOK(MailAuthDTO dto); // 이메일 인증 완료
	ResponseEntity<ResponseDTO<Map<String, String>>> secSession(); // 세션 정보
}

