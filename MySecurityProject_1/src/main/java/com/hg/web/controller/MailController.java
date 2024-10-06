package com.hg.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mail")
public class MailController {

	private final MemberService memberService;
	
	// 이메일 인증 코드 발송
	@PostMapping("/sendMailAuth")
	public ResponseEntity<ResponseDTO<Void>> sendAuthMail(@RequestBody MailAuthDTO dto) {
		return memberService.mailAuth(dto.getEmail());

	}

	// 이메일 인증 여부
	@PostMapping("/mailAuthVal")
	public ResponseEntity<ResponseDTO<Void>> mailAuthVal(@RequestBody MailAuthDTO dto) {
		return memberService.mailAuthOK(dto);
	}
}
