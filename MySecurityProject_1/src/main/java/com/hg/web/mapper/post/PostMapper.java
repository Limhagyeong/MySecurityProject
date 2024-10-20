package com.hg.web.mapper.post;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.post.ImgDTO;
import com.hg.web.dto.post.PostInsertDTO;
import com.hg.web.dto.post.PostSelectDTO;

import java.util.List;

@Mapper
public interface PostMapper {
	// 게시물 존재 여부 확인
	int countPost(int p_num);
	// 게시물 내용 insert
	void insertContent(PostInsertDTO postDTO);
	// 게시물 이미지 insert
	void insertImgUrl(PostInsertDTO postDTO);
	// 게시물 내용 update
	void updatePost(PostInsertDTO postDTO);
	// 게시물 이미지 update
	void updateImg(ImgDTO imgDTO);
	// 게시물 출력
	List<PostSelectDTO> selectPost(String username);
	// 이미지 삭제
	void deleteImgUrl(PostInsertDTO postDTO);
	// 게시물 삭제
	void deleteContent(int pNum);
	// 게시물 이미지 삭제
	void deleteImg(int pNum);
	// 버킷 이미지 URL
	List<String> S3imgUrl(int pNum);
}
