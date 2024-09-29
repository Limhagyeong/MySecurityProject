package com.hg.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hg.web.MySecurityProject1Application;
import com.hg.web.service.impl.SecSessionServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@RequiredArgsConstructor
public class MainController {
	
	private final SecSessionServiceImpl sss;
	
	@GetMapping("/api")
	public Map<String, String> main() {
		
		Map<String, String> sessionData=sss.secSession();
		
		String id=sessionData.get("id");
		String role=sessionData.get("role");
		
		Map<String, String> res=new HashMap<>();
		
		res.put("id", id);
		res.put("role", role);
		
		return res;
	}
}
