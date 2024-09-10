package com.hg.web.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hg.web.dto.UserDTO;
import com.hg.web.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

private final UserMapper membermapper;
private final BCryptPasswordEncoder bpe;

@Override
public boolean Joinprocess(UserDTO dto) {
	
	UserDTO data=new UserDTO();
	System.out.println(dto.getUsername());
	int count=membermapper.countID(dto.getUsername()); // 아이디 중복 검증
	
	try {
		
		if(count>0) {
			System.out.println("이미 존재하는 아이디 입니다");
			return false;
		}
		
		data.setUsername(dto.getUsername());
		data.setPwd(bpe.encode(dto.getPwd()));
		data.setRole("ROLE_USER");
		
		System.out.println(data.getRole());
		
		membermapper.Joinprocess(data);
		
		
	}catch(Exception e) {
		System.out.println("회원가입 처리 중 오류 발생"+e);
		return false;
	}
	
	return true;
}
}
