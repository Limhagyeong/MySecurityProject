package com.hg.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.UserDTO;

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
	// 이메일 인증이력 저장
	void mailAuthCode(MailAuthDTO mailDTO);
	// 코드 인증 검증
	MailAuthDTO mailAuthValidation(MailAuthDTO dto);
	// 인증 완료
	void mailAuthOK(MailAuthDTO mailDTO);
	
}
