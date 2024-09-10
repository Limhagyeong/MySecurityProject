package com.hg.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
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
						.requestMatchers("/my/**").hasAnyRole("ADMIN","USER")
						.anyRequest().denyAll()	
						);
		
		 http
         .formLogin((auth) -> auth.loginPage("/api/login") 
                 .loginProcessingUrl("/loginProcess") 
                 .permitAll() 
         ); 
		 
		 http
         .csrf((auth) -> auth.disable());
		
		return http.build();
		
	}

}
