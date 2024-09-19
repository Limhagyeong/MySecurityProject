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
	// 아이디 찾기
	UserDTO findID(UserDTO dto);
	// 발급한 임시 비밀번호로 업데이트
	public void updateTempPwd(UserDTO dto);
}
