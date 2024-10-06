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
		http 
				.authorizeHttpRequests((auth)->auth
						.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
						.requestMatchers("/api","/api/**").permitAll() 
						.requestMatchers("/api/admin/**").hasRole("ADMIN") 
						.anyRequest().denyAll()	
						);
		
		 http
         		 .formLogin((auth) -> auth
                 .loginProcessingUrl("/api/loginProcess") 
                 .failureHandler(customerauthenticationfailirehandler) // 로그인 실패 시 예외처리
                 .defaultSuccessUrl("/api")
                 .permitAll() 
         );
		 
		 http
		 		.logout((auth) -> auth
		 	    .logoutUrl("/api/logout")
		 	    .logoutSuccessUrl("/api/loginProcess")
		 	    .invalidateHttpSession(true) // 세션 무효화
		 	    .deleteCookies("JSESSIONID") // 쿠키 삭제
		 );
		 
		 http
         		 .csrf((auth) -> auth.disable());
		 
		//cors 관련 설정 
	       http
	        .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					// TODO Auto-generated method stub
					CorsConfiguration configuration = new CorsConfiguration();

	                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));//3000번 포트 허용 
	                configuration.setAllowedMethods(Collections.singletonList("*"));//모든메소드 허용 
	                configuration.setAllowCredentials(true);
	                configuration.setAllowedHeaders(Collections.singletonList("*"));
	                configuration.setMaxAge(3600L);
	                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

	                return configuration;
				}
	        })));
	       
	       
		
		return http.build();
	}

}

