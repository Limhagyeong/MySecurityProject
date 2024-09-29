package com.hg.web.service.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
// 이미지 URL로 변환하는 코드 작성
@Service
@RequiredArgsConstructor
public class S3ImgUploadService {
	
	private final S3Client s3Client;
	
	@Value("${s3.bucket}")
	private String bucket;
}
