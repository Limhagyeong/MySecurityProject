package com.hg.web.controller.post;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.post.PostInsertDTO;
import com.hg.web.dto.post.PostSelectDTO;
import com.hg.web.service.post.beforeService;
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
	public ResponseEntity<ResponseDTO<Void>> post(@RequestParam(value="img", required = false) List<MultipartFile> img, 
												  @RequestParam(value="content", required = false) String content,
												  @RequestParam(value = "username", required = false) String username
												  )
	{
		if (img==null||img.isEmpty()) 
		{
	        throw new BadRequestException("사진을 선택해주세요.");
	    }
		
		PostInsertDTO postDTO=new PostInsertDTO();
		postDTO.setImg(img);
		postDTO.setContent(content);
		postDTO.setUsername(username);
		
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
	public ResponseEntity<ResponseDTO<Void>> updatePost(@RequestParam(value="img", required = false) List<MultipartFile> img, 
			  										    @RequestParam(value="content", required = false) String content,
			  										    @RequestParam(value="pNum", required = false) int pNum,
			  										    @RequestParam(value = "updated", required = false) String updated,
			  										    @RequestParam(value = "imgNum", required = false) List<String> imgNum)
	
	{
		System.out.println("se"+imgNum);
		
		PostInsertDTO postDTO=new PostInsertDTO();
		if(img!=null&&!img.isEmpty()) { 
			postDTO.setImg(img);
		}
		postDTO.setContent(content);
		postDTO.setP_num(pNum);
		postDTO.setUpdated(updated);
		postDTO.setPImgNum(imgNum);
		
		
		
		return postingService.Posting(postDTO);
	}

	
}
