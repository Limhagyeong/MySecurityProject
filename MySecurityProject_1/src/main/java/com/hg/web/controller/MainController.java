package com.hg.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hg.web.service.SecSessionServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	private final SecSessionServiceImpl sss;
	
	@GetMapping("/api")
	public String main(Model model) {
		
		Map<String, String> sessionData=sss.secSession();
		
		String id=sessionData.get("id");
		String role=sessionData.get("role");
		
		model.addAttribute("id",id);
		model.addAttribute("role",role);
		
		return "main";
	}
}
