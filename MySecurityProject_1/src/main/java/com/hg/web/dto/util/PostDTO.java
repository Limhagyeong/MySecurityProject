package com.hg.web.dto.util;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PostDTO {
	private int p_num; // 게시물 고유 번호
	private int id; // 작성자 아이디
	private String username;
	private String p_content; 
	private String p_img_url; // S3 URL
	private MultipartFile p_img; // 원본 이미지
}
