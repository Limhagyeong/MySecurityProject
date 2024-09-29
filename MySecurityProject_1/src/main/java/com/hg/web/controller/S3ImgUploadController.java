package com.hg.web.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.ResponseDTO;

import com.hg.web.service.impl.S3ImgUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3ImgUploadController {
	
	private final S3ImgUploadService s3imgiuploadservice;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseDTO<Void>> s3Upload(@RequestBody MultipartFile image) throws IOException{
		
		return s3imgiuploadservice.upload(image);
		
		
	}
	
}
