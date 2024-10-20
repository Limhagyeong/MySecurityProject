package com.hg.web.dto.post;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostInsertDTO {
	private int p_num; // 게시물 고유 번호
	private int p_img_num; // 게시물 고유 번호
	private List<String> pImgNum;
	private int id; // 작성자 아이디
	private String username;
	private String content; 
	private String p_img_url; // S3 URL
	private List<MultipartFile> img; // 원본 이미지
	private String updated; // 이미지 업데이트 여부 확인 => S3와 DB 사이 데이터 일관성 위함
}
