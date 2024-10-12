package com.hg.web.service.util;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.util.PostInsertDTO;
import com.hg.web.dto.util.PostSelectDTO;

@Service
public interface PostService {
	ResponseEntity<ResponseDTO<Void>> insertPosting(PostInsertDTO postDTO); 
	// 게시물 업로드
	ResponseEntity<ResponseDTO<Void>> insertImgUrl(PostInsertDTO postDTO); 
	// 이미지 파일 S3에 업로드	
	ResponseEntity<ResponseDTO<List<PostSelectDTO>>> selectPost(String username);
	// 게시물 출력
	ResponseEntity<ResponseDTO<Void>> updatePosting(PostInsertDTO postDTO);
	// 게시물 업데이트
}
