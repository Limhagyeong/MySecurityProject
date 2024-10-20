package com.hg.web.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.post.PostSelectDTO;
import com.hg.web.service.post.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api")
public class MainController {

	@GetMapping
	public ResponseEntity<ResponseDTO<Void>> mainHome(){
		// 세션이 비었다면 예외던지기
		return new ResponseEntity<>(new ResponseDTO<>(),HttpStatus.OK);
	}
}
