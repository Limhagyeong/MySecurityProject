package com.hg.web.service.member.impl;
// DB로부터 username에 대한 검증을 마치고 데이터를 저장하여 dto로 넘긴다
// dto에서는 전달받은 데이터를 가지고 시큐리티컨피그에서 넘기면 컨피그에서 검증하고 완료 후 스프링 세션에 데이터가 저장되게된다
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.hg.web.common.exception.AuthenticationException;
import com.hg.web.dto.member.UserDTO;
import com.hg.web.dto.security.CustomUserDetails;
import com.hg.web.mapper.member.UserMapper;

import lombok.RequiredArgsConstructor;
// CustomUserDetails는 UserDetails 인터페이스를 구현한 클래스여야 함 
// UserDetailService는 매개변수로 받은 username이 DB에 저장된 사용자인지 확인하는 역할
@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService{
	private final UserMapper membermapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) {

		// DB로부터 username을 검증하여 데이터를 담음
		UserDTO data=membermapper.idCheck(username);
			
			if (data==null) {
	            // 예외를 던져서 사용자 정보를 찾지 못했음을 알림
	            throw new AuthenticationException("사용자를 찾을 수 없습니다.");
	}
			 return new CustomUserDetails(data);

}
	
}
