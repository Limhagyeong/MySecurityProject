package com.hg.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;
import com.hg.web.service.MemberService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController<T>{
	
	private final MemberService memberService;
	
	// 회원가입
	@PostMapping
	public ResponseEntity<ResponseDTO<Void>> joinProcess(UserDTO dto) {
		
	System.out.println("회원가입 controller 실행");

	return memberService.Joinprocess(dto);
	}
	
	// 아이디 중복 검증
	@PostMapping("/idValidate")
	public ResponseEntity<ResponseDTO<Void>> idValidate(@RequestBody UserDTO dto){
		
		return memberService.CountID(dto.getUsername());
	}

};
