package com.hg.web.service;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hg.web.mapper.UserMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final UserMapper memberMapper;
	private SimpleMailMessage simpleMessage=new SimpleMailMessage();

	@Value("${spring.mail.username}")
	private String from;
	/*
	 * 메일 전송
	 * @param to : 수신자
	 * @param subject : 제목
	 * @param body :  본문
	 */
	
	@Override
	public void sendMail(String to, String subject, String body, String request) {
		MimeMessage message=mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"UTF-8");
			messageHelper.setSubject(subject); //  제목
			messageHelper.setTo(to); // 수신자
			messageHelper.setFrom(from); // 발송자
			
			if(request.equals("findID")) {
				String sendBody=String.format("회원님의 아이디는 [&nbsp;%s&nbsp;] 입니다.<br>해당 정보로 로그인을 시도해주세요.", body); 
				messageHelper.setText(sendBody,true); // 본문
			}
			
			if(request.equals("findPwd")) {
				String escapedPassword = StringEscapeUtils.escapeHtml4(body);  // 비밀번호 이스케이프 처리
				String sendBody=String.format("임시 비밀번호는 [&nbsp;%s&nbsp;] 입니다.<br>해당 정보로 로그인 후 비밀번호를 변경하여주세요.", escapedPassword); 
				messageHelper.setText(sendBody,true); // 본문
			}
			
			mailSender.send(message); // 발송
		}catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 고정 메세지 (다수에게 일괄적으로 보내야할 경우)
	 * @param message : 본문
	 */
	
//	public void sendSimpleMail(String message) {
//		simpleMessage.setText(message);
//		mailSender.send(simpleMessage);
//	}

	

}
