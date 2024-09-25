package com.hg.web.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.MailAuthDTO;
import com.hg.web.dto.UserDTO;

@Mapper
public interface MailAuthMapper {
		// 이메일 인증이력 저장
		void mailAuthCode(MailAuthDTO mailDTO);
		// 코드 인증 검증
		MailAuthDTO mailAuthValidation(MailAuthDTO dto);
		// 인증 완료
		void mailAuthOK(MailAuthDTO mailDTO);
		// 미인증 이력 삭제
		int deleteMailAuthCode();
}
