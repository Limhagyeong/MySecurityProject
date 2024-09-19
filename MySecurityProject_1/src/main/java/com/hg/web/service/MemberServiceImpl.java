package com.hg.web.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hg.web.common.InputValidator;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.common.exception.InternalErrorException;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;
import com.hg.web.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

private final UserMapper membermapper;
private final BCryptPasswordEncoder bpe;
private final MailServiceImpl mailService;

@Override
public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto) {
	// 비밀번호 유효성 검사
	if(!InputValidator.pwdValCheck(dto.getPwd())) {
		throw new InternalErrorException("유효하지 않은 입력입니다.", "pwdValidation 실패");
	}
	
	String encodedPwd=bpe.encode(dto.getPwd()); // 	비밀번호 암호화
	dto.setPwd(encodedPwd); 
	
	dto.setRole("ROLE_USER");
		
	membermapper.Joinprocess(dto);
		
	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<Void>(),HttpStatus.OK); //성공 
}

// 아이디 중복 검증
@Override
public ResponseEntity<ResponseDTO<Void>> CountID(String username){
	// TODO Auto-generated method stub
	
    int count=membermapper.countID(username); // 아이디 중복 검증
    
	if(count>0) {
		throw new BadRequestException("사용 중인 아이디입니다.");
	}

	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
}

@Override
public ResponseEntity<ResponseDTO<Void>> findID(UserDTO dto) {
	// TODO Auto-generated method stub
	if(membermapper.findID(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	UserDTO findDto=membermapper.findID(dto);
	mailService.sendMail(findDto.getEmail(), "아이디 찾기 테스트", findDto.getUsername());
	
	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
	}

@Override
public ResponseEntity<ResponseDTO<Void>> findPwd(UserDTO dto) {
	// TODO Auto-generated method stub
	if(membermapper.findID(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	return null;
}
}

