package com.hg.web.dto.post;

import lombok.Data;

@Data
public class ImgDTO {
	private int pImgNum; // 게시물 고유 번호
    private String pImgUrl; // S3 URL (필드 이름을 변경)
}
