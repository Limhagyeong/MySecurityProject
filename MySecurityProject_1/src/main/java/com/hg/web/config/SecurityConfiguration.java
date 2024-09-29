package com.hg.web.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{
	
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
         .formLogin((auth) -> auth.loginPage("/api/login") 
                 .loginProcessingUrl("/api/loginProcess") 
                 .defaultSuccessUrl("/api", true)
                 .permitAll() 
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
