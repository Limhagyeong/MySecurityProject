package com.hg.web.service;

import org.springframework.http.ResponseEntity;

import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;

public interface MailService {
	// 이메일 보내기
	public void sendMail(String to, String subject, String body);
}
