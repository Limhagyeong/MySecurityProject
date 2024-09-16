package com.hg.web.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;
import com.hg.web.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

private final UserMapper membermapper;
private final BCryptPasswordEncoder bpe;

@Override
public ResponseEntity<ResponseDTO<Void>> Joinprocess(UserDTO dto) {
	
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
}
