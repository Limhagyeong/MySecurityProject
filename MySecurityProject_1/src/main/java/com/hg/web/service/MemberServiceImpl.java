package com.hg.web.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

private final UserMapper membermapper;
private final BCryptPasswordEncoder bpe;
private final MailServiceImpl mailService;

@Override
public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto) {
	// 이메일 인증 진행 여부
	if(membermapper.mailVerified(dto)==0) {
		throw new BadRequestException("이메일 인증을 진행하세요.");
	}
	// 비밀번호 유효성 검사
	if(!InputValidator.pwdValCheck(dto.getPassword())) {
		throw new InternalErrorException("유효하지 않은 입력입니다.", "pwdValidation 실패");
	}
	String encodedPwd=bpe.encode(dto.getPassword()); // 	비밀번호 암호화
	dto.setPassword(encodedPwd); 
	dto.setRole("ROLE_USER");
	dto.setRequestType(dto.getRequestType());
		
	membermapper.joinProcess(dto);
		
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

// 아이디 찾기
@Override
public ResponseEntity<ResponseDTO<Void>> findID(UserDTO dto) {
	// TODO Auto-generated method stub
	
	// 가입여부 확인
	if(membermapper.findUser(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	UserDTO findDto=membermapper.findUser(dto);
	mailService.sendMail(findDto.getEmail(), "아이디 찾기 테스트", findDto.getUsername(), dto.getRequestType());
	
	return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
	}

// 비밀번호 찾기
@Override
public ResponseEntity<ResponseDTO<Void>> findPwd(UserDTO dto) {
	// TODO Auto-generated method stub
	
	// 가입여부 확인
	if(membermapper.findUser(dto)==null) {
		throw new BadRequestException("가입되지 않은 정보입니다.");
	}
	
	UserDTO findDto=membermapper.findUser(dto); // 클라이언트가 보낸 정보로 회원 찾기
	
	findDto.setPassword(TempRandomChar.getRandomPassword()); // 임시 비밀번호 발급 => 이메일 전송
	String encodedPwd=bpe.encode(findDto.getPassword()); // 암호화된 임시 비밀번호 => DB 업데이트

	findDto.setEmail(dto.getEmail());
	findDto.setUsername(dto.getUsername());
	
	membermapper.updateTempPwd(encodedPwd, findDto.getEmail()); // 암호화된 임시 비번으로 업데이트
	
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


}

