package com.hg.web.mapper.util;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.util.PostDTO;

@Mapper
public interface PostMapper {
	void insertContent(PostDTO postDTO);
	// 게시물 내용 insert
	void insertImgUrl(PostDTO postDTO);
	// 게시물 이미지 insert
	void updateContent(PostDTO postDTO);
	// 게시물 내용 update
	void updateImgUrl(PostDTO postDTO);
	// 게시물 이미지 update

	void deleteImgUrl(PostDTO postDTO);
}
