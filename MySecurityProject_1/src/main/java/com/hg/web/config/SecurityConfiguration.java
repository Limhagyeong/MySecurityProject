package com.hg.web.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hg.web.common.handler.CustomAuthenticationFailureHandler;
import com.hg.web.common.handler.GlobalExceptionHandler;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
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
		http
			.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				// TODO Auto-generated method stub
				CorsConfiguration configuration = new CorsConfiguration();

				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));// 3000번 포트 허용
				configuration.setAllowedMethods(Collections.singletonList("*"));// 모든메소드 허용
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));

				return configuration;
			}
		})));
		
		 http
 		 	.csrf((auth) -> auth.disable());
		
		// 접근 권한 설정
		http 
				.authorizeHttpRequests((auth)->auth
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/api/login","/api/loginProcess","/api/members/**","/api/mail/**").permitAll() // 회원가입, 로그인, 메일
//						.requestMatchers("/api/s3/**").hasAnyRole("USER","ADMIN")
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
                 .loginProcessingUrl("/api/loginProcess") 
                 .failureHandler(customerauthenticationfailirehandler) // 로그인 실패 시 예외처리
                 .defaultSuccessUrl("http://localhost:3000/", true) 
                 .permitAll() 
         );
		 
	       http
	 		.logout((auth) -> auth
	 	    .logoutUrl("/api/logout")
	 	    .logoutSuccessUrl("http://localhost:3000/")
	 	    .invalidateHttpSession(true) // 세션 무효화
	 	    .deleteCookies("JSESSIONID") // 쿠키 삭제
	    );
	       
	       
		
		return http.build();
	}

}

