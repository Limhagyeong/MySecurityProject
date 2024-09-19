package com.hg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.web.service.MailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailSendController {
	private final MailService mailservice;
	
	@RequestMapping("/api/mail/send")
	public String sendMail() {
		mailservice.sendMail("mumu878@naver.com", "시큐리티 프로젝트 메일 테스트", "시큐리티 프로젝트 메일 테스트");
		return "send OK";
	}
}
