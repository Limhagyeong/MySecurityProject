package com.hg.web.service;

import org.springframework.http.ResponseEntity;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;

public interface MailService {
	// 이메일 보내기
	void sendMail(String to, String subject, String body, String request);
	// 이메일 인증
	ResponseEntity<ResponseDTO<Void>> sendAuthMail(String email);
	// 이메일 인증 완료
	ResponseEntity<ResponseDTO<Void>> mailAuthValidation(MailAuthDTO dto);
}
