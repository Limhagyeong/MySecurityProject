package com.hg.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hg.web.common.Random;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class RandomTest {
	
	@Autowired
	private Random r;
	
	@Test
	void randomTest() {
		r.getRandomPassword();
	}

}
