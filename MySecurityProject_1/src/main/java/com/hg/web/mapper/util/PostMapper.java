package com.hg.web.mapper.util;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import com.hg.web.dto.util.PostInsertDTO;
import com.hg.web.dto.util.PostSelectDTO;

@Mapper
public interface PostMapper {
	void insertContent(PostInsertDTO postDTO);
	// 게시물 내용 insert
	void insertImgUrl(PostInsertDTO postDTO);
	// 게시물 이미지 insert
	void updateContent(PostInsertDTO postDTO);
	// 게시물 내용 update
	void updateImgUrl(PostInsertDTO postDTO);
	// 게시물 이미지 update
	List<PostSelectDTO> selectPost(String username);
	// 게시물 출력
	void deleteImgUrl(PostInsertDTO postDTO);
	// 이미지 삭제
}
