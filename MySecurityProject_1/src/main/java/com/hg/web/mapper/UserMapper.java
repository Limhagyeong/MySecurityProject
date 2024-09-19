package com.hg.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.UserDTO;

@Mapper
public interface UserMapper {
	
	// 회원가입 중복 방지
	int countID(String username);
	// 회원가입
	public void Joinprocess(UserDTO dto);
	// 로그인 검증
	UserDTO IDCheck(String username);
}
