package com.hg.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hg.web.service.CustomUserDetailsServiceImpl;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class MySecurityProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(MySecurityProject1Application.class, args);
	}

}
