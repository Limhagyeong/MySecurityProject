package com.hg.web.controller.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.util.PostDTO;
import com.hg.web.service.util.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class S3ImgUploadController {
	
	private final PostService postingService;
	
	@PostMapping()
	public ResponseEntity<ResponseDTO<Void>> posting(@RequestParam(value="p_img", required = false) MultipartFile p_img, 
												     @RequestParam(value="p_content", required = false) String content,
												     @RequestParam(value = "username", required = false) String username)
	{
		if (p_img==null||p_img.isEmpty()) 
		{
	        throw new BadRequestException("업로드할 파일이 없습니다.");
	    }

		PostDTO postDTO=new PostDTO();
		postDTO.setP_img(p_img);
		postDTO.setP_content(content);
		postDTO.setUsername(username);
		
		return postingService.uploadPosting(postDTO);
	}

	
}
