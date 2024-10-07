package com.hg.web.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.member.UserDTO;
import com.hg.web.dto.util.MailAuthDTO;

@Mapper
public interface UserMapper {
	
	// 회원가입 중복 방지
	int countID(String username);
	// 회원가입
	void joinProcess(UserDTO dto);
	// 로그인 검증
	UserDTO idCheck(String username);
	// 아이디 찾기
	UserDTO findUser(UserDTO dto);
	// 암호화된 임시 비밀번호로 업데이트
	void updateTempPwd(String password, String email);
	// 이메일 중복 여부 확인
	UserDTO findEmail(String email);
	// 이메일 인증 진행 여부 확인
	int mailVerified(UserDTO dto);
	
	
}
