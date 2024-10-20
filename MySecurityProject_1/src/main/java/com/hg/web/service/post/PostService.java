package com.hg.web.service.post;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.post.PostInsertDTO;
import com.hg.web.dto.post.PostSelectDTO;

@Service
public interface PostService {
	// 게시물 삽입 및 수정
	ResponseEntity<ResponseDTO<Void>> Posting(PostInsertDTO postDTO); 
	// 게시물 출력
	ResponseEntity<ResponseDTO<List<PostSelectDTO>>> selectPost(String username);
	// 게시물 삭제
	ResponseEntity<ResponseDTO<Void>> deletePost(int pNum);
	// 전체 게시물 출력
	ResponseEntity<ResponseDTO<List<PostSelectDTO>>>  allPost();
}
