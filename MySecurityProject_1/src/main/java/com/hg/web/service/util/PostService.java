package com.hg.web.service.util;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.util.PostDTO;

@Service
public interface PostService {
	ResponseEntity<ResponseDTO<Void>> uploadPosting(PostDTO postDTO); 
	// 포스트 업로드
	ResponseEntity<ResponseDTO<Void>> uploadImg(PostDTO postDTO); 
	// 이미지 파일 S3에 업로드	
}
