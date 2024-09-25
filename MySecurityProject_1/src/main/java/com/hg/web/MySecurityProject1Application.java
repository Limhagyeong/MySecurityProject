package com.hg.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MySecurityProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(MySecurityProject1Application.class, args);
	}

}
