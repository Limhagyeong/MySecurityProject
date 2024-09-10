package com.hg.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.web.dto.UserDTO;
import com.hg.web.service.MemberServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
	
	private final MemberServiceImpl memberserviceimpl;

	// 로그인 페이지
	@GetMapping("/login")
		public String loginPage() {
			return "members/login";
		}
	
	// 회원가입 페이지
	@GetMapping("/join")
		public String joinpage() {
		return "members/join";
	}
	
	// 회원가입 진행
	@PostMapping("/joinProcess")
	public String joinProcess(UserDTO dto) {
	
	boolean res=memberserviceimpl.Joinprocess(dto);
	System.out.println(res);
	
	if(!res) {
		System.out.println("회원가입 실패");
		return "redirect:/api/join";
	}
	
	return "redirect:/api/login";
}
}
