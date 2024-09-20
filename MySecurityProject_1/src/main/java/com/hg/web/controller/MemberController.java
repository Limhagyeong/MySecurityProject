package com.hg.web.controller;

import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.web.common.InputValidator;
import com.hg.web.common.TempPwd;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.common.exception.InternalErrorException;
import com.hg.web.dto.ResponseDTO;
import com.hg.web.dto.UserDTO;
import com.hg.web.service.MemberService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController<T>{
	
	private final MemberService memberService;
	private final TempPwd r;
	
	// 회원가입
	@PostMapping
	public ResponseEntity<ResponseDTO<Void>> joinProcess(UserDTO dto) {
		// 이메일 유효성 검사
		if(!InputValidator.emailValCheck(dto.getEmail())) {
			throw new InternalErrorException("유효하지 않은 입력입니다.", "emailValidation 실패");
		}
		
		// 전화번호 유효성 검사
		if(!InputValidator.phoneValCheck(dto.getPhone())) {
			throw new InternalErrorException("유효하지 않은 입력입니다.", "phoneValidation 실패");
		}
		
		// 날짜 형식 검사
		if(!InputValidator.isDatePattern(dto.getBday())) {
			throw new BadRequestException("유효하지 않은 입력입니다.");
		}
		
		return memberService.Joinprocess(dto);
	}
	
	// 아이디 중복 검증
	@PostMapping("/idValidate")
	public ResponseEntity<ResponseDTO<Void>> idValidate(@RequestBody UserDTO dto){
		
		if(!InputValidator.IDValCheck(dto.getUsername())) {
			throw new InternalErrorException("유효하지 않은 입력입니다.", "IDValidation 실패");
		}
		
		return memberService.CountID(dto.getUsername());
	}
//	
//	// 아이디 찾기
//		@PostMapping("/findID")
//		public ResponseEntity<ResponseDTO<Void>> findID(@RequestBody UserDTO dto){
//			// 이메일 유효성 검사
//			if (!InputValidator.emailValCheck(dto.getEmail())) {
//				throw new InternalErrorException("유효하지 않은 입력입니다.", "emailValidation 실패");
//			}
//			
//			return memberService.findID(dto);
//		}
//		
//		// 비밀번호 찾기
//		@PostMapping("/findPwd")
//		public ResponseEntity<ResponseDTO<Void>> findPwd(@RequestBody UserDTO dto){
//			// 이메일 유효성 검사
//			if (!InputValidator.emailValCheck(dto.getEmail())) {
//				throw new InternalErrorException("유효하지 않은 입력입니다.", "emailValidation 실패");
//			}
//			
//			return memberService.findPwd(dto);
//		}
	
	@PostMapping("/findUserInfo")
	public ResponseEntity<ResponseDTO<Void>> findUserInfo(@RequestBody UserDTO dto) {
	    
		// 이메일 유효성 검사
	    if (!InputValidator.emailValCheck(dto.getEmail())) {
	        throw new InternalErrorException("유효하지 않은 입력입니다.", "emailValidation 실패");
	    }
	    
	    // 요청에 따른 로직 수행
	    if (dto.getRequestType().equals("findID")) {
	        return memberService.findID(dto); // 아이디 찾기 로직
	    } 
	    else if (dto.getRequestType().equals("findPwd")) {
	        return memberService.findPwd(dto); // 비밀번호 찾기 로직
	    } 
	    else {
	        throw new BadRequestException("잘못된 요청입니다.");
	    }
	}
};
