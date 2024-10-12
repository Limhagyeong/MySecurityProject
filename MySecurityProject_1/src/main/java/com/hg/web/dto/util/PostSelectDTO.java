package com.hg.web.dto.util;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostSelectDTO {
	private int pNum; // 게시물 고유 번호
	private int id; // 작성자 아이디
	private String content; 
	private String imgUrl; // S3 URL
	private LocalDateTime date;
}
