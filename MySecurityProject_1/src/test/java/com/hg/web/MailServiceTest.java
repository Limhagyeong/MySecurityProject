package com.hg.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hg.web.service.util.impl.MailServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MailServiceTest {
	@Autowired
	private MailServiceImpl ms;
	
	@Test
	@DisplayName("메일전송")
	void sendMail() {
		System.out.println("메일 테스트");
		ms.sendMail("mumu878@naver.com", "시큐리티 프로젝트 메일 테스트", "시큐리티 프로젝트 메일 테스트", "findID");
	}
}
