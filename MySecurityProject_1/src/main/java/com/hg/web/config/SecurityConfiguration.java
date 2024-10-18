package com.hg.web.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;


import com.hg.web.common.handler.CustomAuthenticationFailureHandler;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration{
	
	private final CustomAuthenticationFailureHandler customerauthenticationfailirehandler;
	
	@Bean
	public BCryptPasswordEncoder bcPwd() {
		return new BCryptPasswordEncoder(); 
	}
	
	@Bean
	public SecurityFilterChain filter(HttpSecurity http) throws Exception{
		
		// cors 관련 설정
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(request -> {
		    CorsConfiguration configuration = new CorsConfiguration();
		    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 클라이언트 주소
		    configuration.setAllowedMethods(Collections.singletonList("*")); // 필요한 메서드만 허용
		    configuration.setAllowCredentials(true); // 쿠키, 인증 헤더 등의 자격 증명 허용
		    
		    return configuration;
		}));
		
		 http
 		 	.csrf((auth) -> auth.disable());
		
		
		// 접근 권한 설정
		http 
				.authorizeHttpRequests((auth)->auth
						.requestMatchers("/api","/api/members/**","/api/mail/**").permitAll() // 회원가입, 로그인, 메일
						.anyRequest().authenticated() // 나머지는 권한이 필요함
						)
						
						.exceptionHandling(exceptions -> 
				        exceptions.authenticationEntryPoint((request, response, authException) -> {
				            // 인증되지 않은 사용자의 접근 시 401 반환
				            response.setContentType("application/json; charset=UTF-8");
				            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				            String json = "{\"message\": \"로그인이 필요한 서비스입니다.\", \"code\": \"401\"}";
				            response.getWriter().write(json);
				            response.getWriter().flush();
				        })
				);

				
		 http
         		 .formLogin((auth) -> auth
                 .loginProcessingUrl("/api/login")                  
                 .defaultSuccessUrl("/api", true) 
                 .failureHandler(customerauthenticationfailirehandler) // 로그인 실패 시 예외처리
                 .permitAll() 

         );
		 
	       http
	 		.logout((auth) -> auth
	 	    .logoutUrl("/api/logout")
	 	    .logoutSuccessUrl("/api")
	 	    .invalidateHttpSession(true) // 세션 무효화
	 	    .deleteCookies("JSESSIONID") // 쿠키 삭제
	    );
	       
	       
	      
		
		return http.build();
	}

}

