package com.hg.web.mapper.util;

import org.apache.ibatis.annotations.Mapper;

import com.hg.web.dto.util.PostDTO;

@Mapper
public interface PostMapper {
	void insertContent(PostDTO postDTO);
	void insertImgUrl(PostDTO postDTO);
	void deleteImgUrl(PostDTO postDTO);
}
