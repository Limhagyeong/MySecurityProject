package com.hg.web.service.member.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hg.web.common.InputValidator;
import com.hg.web.common.TempRandomChar;
import com.hg.web.common.exception.AuthenticationException;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.common.exception.InternalErrorException;
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.member.UserDTO;
import com.hg.web.dto.util.MailAuthDTO;
import com.hg.web.mapper.member.UserMapper;
import com.hg.web.mapper.util.MailAuthMapper;
import com.hg.web.service.member.MemberService;
import com.hg.web.service.util.impl.MailServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

private final UserMapper usermapper;
private final BCryptPasswordEncoder bpe;
private final MailServiceImpl mailService;

@Override
public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto) {
	// 이메일 인증 진행 여부
	if(usermapper.mailVerified(dto)==0) {
		throw new BadRequestException("이메일 인증을 진행하세요.");
	}
	// 비밀번호 유효성 검사
	if(!InputValidator.pwdValCheck(dto.getPassword())) {
		throw new InternalErrorException("유효하지 않은 입력입니다.", "pwdValidation 실패");
	}
	// 이름 유효성 검사
	if(!InputValidator.nameValidation(dto.getName())) {
		throw new InternalErrorException("유효하지 않은 입력입니다.", "nameValidation 실패");
	}
	String encodedPwd=bpe.encode(dto.getPassword()); // 	비밀번호 암호화
	dto.setPassword(encodedPwd); 
	dto.setRole("ROLE_USER");
	dto.setRequestType(dto.getRequestType());
		
	usermapper.joinProcess(dto);
		
	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<Void>(),HttpStatus.OK); //성공 
}

// 아이디 중복 검증
@Override
public ResponseEntity<ResponseDTO<Void>> CountID(String username){
	// TODO Auto-generated method stub
	
    int count=usermapper.countID(username); // 아이디 중복 검증
    
	if(count>0) {
		throw new BadRequestException("사용 중인 아이디입니다.");
	}

	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
}

// 아이디 찾기
@Override
public ResponseEntity<ResponseDTO<Void>> findID(UserDTO dto) {
	// TODO Auto-generated method stub
	
	// 가입여부 확인
	if(usermapper.findUser(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	UserDTO findDto=usermapper.findUser(dto);
	mailService.sendMail(findDto.getEmail(), "아이디 찾기 테스트", findDto.getUsername(), dto.getRequestType());
	
	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
	}

// 비밀번호 찾기
@Override
public ResponseEntity<ResponseDTO<Void>> findPwd(UserDTO dto) {
	// TODO Auto-generated method stub
	
	// 가입여부 확인
	if(usermapper.findUser(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	
	UserDTO findDto=usermapper.findUser(dto); // 클라이언트가 보낸 정보로 회원 찾기
	
	findDto.setPassword(TempRandomChar.getRandomPassword()); // 임시 비밀번호 발급 => 이메일 전송
	String encodedPwd=bpe.encode(findDto.getPassword()); // 암호화된 임시 비밀번호 => DB 업데이트

	findDto.setEmail(dto.getEmail());
	findDto.setUsername(dto.getUsername());
	
	usermapper.updateTempPwd(encodedPwd, findDto.getEmail()); // 암호화된 임시 비번으로 업데이트
	
	mailService.sendMail(findDto.getEmail(), "비밀번호 찾기 테스트", findDto.getPassword(), dto.getRequestType()); // 임시 비번 메일 발송

	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
}

// 이메일 인증
@Override
public ResponseEntity<ResponseDTO<Void>> mailAuth(String email) {
	// TODO Auto-generated method stub
	// 이메일 인증 번호 전송
	return mailService.sendAuthMail(email);
}

// 메일 인증 코드 검증
@Override
public ResponseEntity<ResponseDTO<Void>> mailAuthOK(MailAuthDTO dto) {
	// TODO Auto-generated method stub
	return mailService.mailAuthValidation(dto);
}

// session Id, Role
@Override
public ResponseEntity<ResponseDTO<Map<String, String>>> secSession() {
	// TODO Auto-generated method stub
	String id=SecurityContextHolder.getContext().getAuthentication().getName(); // 스프링 세션 아이디
	
	// 인증 정보
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	// 권한 정보
	Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	Iterator<? extends GrantedAuthority> iter = authorities.iterator();
	
	GrantedAuthority auth = iter.next();
	String role = auth.getAuthority(); // 스프링 세션 롤
    
    if(!role.equals("ROLE_ADMIN") && !role.equals("ROLE_USER")) {
    	throw new AuthenticationException("로그인 전");
    }
	
	Map<String, String> responseData = new HashMap<>();
    responseData.put("id", id);
    responseData.put("role", role);

    
    return new ResponseEntity<ResponseDTO<Map<String, String>>> (new ResponseDTO<>(responseData), HttpStatus.OK);
}
}

