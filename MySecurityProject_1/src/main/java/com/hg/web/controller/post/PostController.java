package com.hg.web.controller.post;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.post.PostInsertDTO;
import com.hg.web.dto.post.PostSelectDTO;
import com.hg.web.service.post.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
	
	private final PostService postingService;
	
	@GetMapping("/allpost")
	public ResponseEntity<ResponseDTO<List<PostSelectDTO>>> allpost(){
		return postingService.allPost();
	}
	
	@PostMapping
	public ResponseEntity<ResponseDTO<Void>> post(@ModelAttribute PostInsertDTO postDTO)
	{
		if (postDTO.getImg()==null||postDTO.getImg().isEmpty()) 
		{
	        throw new BadRequestException("사진을 선택해주세요.");
	    }

		return postingService.Posting(postDTO);
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<ResponseDTO<List<PostSelectDTO>>> getpost(@PathVariable String username){
		
		return postingService.selectPost(username);
	
	}
	
	@DeleteMapping("/{pNum}")
	public ResponseEntity<ResponseDTO<Void>> deletePost(@PathVariable int pNum){
		
		return postingService.deletePost(pNum);
	}
	
	@PatchMapping("/{pNum}")
	public ResponseEntity<ResponseDTO<Void>> updatePost(@ModelAttribute PostInsertDTO postDTO, @PathVariable int pNum)
	
	{

		if(postDTO.getImg()!=null&&!postDTO.getImg().isEmpty()) { 
			postDTO.setImg(postDTO.getImg());
		}
	
		return postingService.Posting(postDTO);
	}

	
}
