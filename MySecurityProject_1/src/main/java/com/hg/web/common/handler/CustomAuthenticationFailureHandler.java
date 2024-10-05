package com.hg.web.common.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// 로그인 정보가 다를 시 AuthenticationFailureHandler가 호출되어 onAuthenticationFailure 메소드 실행시킴
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub

		String errorMessage="잘못된 정보를 입력하였습니다.";

		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // status애 401 에러 코드 저장
		response.getWriter().write("{\"error\": \"" + errorMessage + "\", \"code\": \"401\"}"); // 프론트로 전달할 정보
		response.getWriter().flush();
	}

}
