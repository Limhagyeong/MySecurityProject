package com.hg.web.service;

import java.time.LocalDateTime;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hg.web.common.InputValidator;
import com.hg.web.common.TempRandomChar;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.common.exception.InternalErrorException;
import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;
import com.hg.web.mapper.UserMapper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final UserMapper memberMapper;
	private SimpleMailMessage simpleMessage=new SimpleMailMessage();
	private final BCryptPasswordEncoder bpe;

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
	// 이메일 인증 코드 발송
	@Override
	@Transactional
	public ResponseEntity<ResponseDTO<Void>> sendAuthMail(String email) {
		// TODO Auto-generated method stub
		System.out.println(email);
		// 이메일 유효성 검사
	    if (!InputValidator.emailValCheck(email)) {
	        throw new InternalErrorException("유효하지 않은 입력입니다.", "emailValidation 실패");
	    }
		
		// 이메일 중복 여부 확인
		UserDTO dto=memberMapper.findEmail(email);
		if(dto!=null) {
			throw new BadRequestException("이미 가입된 이메일입니다.");
		}
		
		try {
			MimeMessage message=mailSender.createMimeMessage();
			MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"UTF-8");
			
			String authCode=TempRandomChar.emailAuthCode(); // 인증코드 생성
			
			System.out.println(authCode);
			System.out.println(bpe.encode(authCode));
			
			MailAuthDTO mailDTO=new MailAuthDTO();
			mailDTO.setEmail(email);
			mailDTO.setCode(bpe.encode(authCode));
			mailDTO.setExpiration(LocalDateTime.now().plusMinutes(1)); // 유효시간 1분
			
			messageHelper.setSubject("이메일 인증 코드 발송 테스트"); //  제목
			messageHelper.setTo(mailDTO.getEmail()); // 수신자
			messageHelper.setFrom(from); // 발송자
			String sendBody=String.format("메일 인증번호는 [&nbsp;%s&nbsp;] 입니다.<br>해당 정보로 인증을 진행해주세요.", authCode); 
			messageHelper.setText(sendBody,true); // 본문
			
			memberMapper.mailAuthCode(mailDTO); // 인증 코드 관련 정보 DB 저장
			
			mailSender.send(message); 
			
			
		}catch(MessagingException e) {
			e.printStackTrace();
			throw new BadRequestException("메일 발송 실패");
		}
		
			return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
	}
	
	// 인증코드 검증
	@Transactional
	public ResponseEntity<ResponseDTO<Void>> mailAuthValidation(MailAuthDTO dto){

		// 정보에 맞는 DB데이터가 있는지 검증
		MailAuthDTO MailDto=memberMapper.mailAuthValidation(dto);	
		// 인증 코드가 일치하지 않는 경우
		if (!bpe.matches(dto.getCode(), MailDto.getCode())) { // 입력 코드를 암호화한 후 DB에 저장된 암호화된 코드와 비교 (솔트는 자동으로 처리됨)
			throw new BadRequestException("인증코드가 일치하지 않습니다.");
		} 		
		// 인증완료 이력 업데이트
		memberMapper.mailAuthOK(MailDto);
		return new ResponseEntity<ResponseDTO<Void>>(new ResponseDTO<>(), HttpStatus.OK); // 성공
		
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
