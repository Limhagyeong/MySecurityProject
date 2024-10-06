package com.hg.web.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hg.web.common.exception.BadRequestException;
import com.hg.web.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;
// 클라이언트로부터 받은 이미지를 버킷에 올림
@Service
@RequiredArgsConstructor
public class S3ImgUploadService {
	
	private final AmazonS3 amazonS3;
	
	@Value("${s3.bucket}")
	private String bucket;
	
	public ResponseEntity<ResponseDTO<Void>> upload(MultipartFile image) throws IOException {
	    if(image==null || image.isEmpty() || Objects.isNull(image.getOriginalFilename())){
	      throw new BadRequestException("업로드할 파일이 없습니다.");
	    }
	    return this.uploadImageToS3(image);
	  }
	
	// S3에 이미지 업로드
    private ResponseEntity<ResponseDTO<Void>> uploadImageToS3(MultipartFile image) throws IOException{
    	
    	String originalFilename=image.getOriginalFilename();
		String extension=originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 확장자 추출

		System.out.println(extension);
		String s3FileName=UUID.randomUUID().toString().substring(0,10) + originalFilename; //  원본 파일명 유지 + 파일명 중복 방지
		
		InputStream is=image.getInputStream(); // InputStream => 파일의 데이터를 한줄씩 바이트 단위로 읽어들임 (데이터에 접근하는 방식)
		byte[] bytes=IOUtils.toByteArray(is); // 바이트 배열을 통해 데이터를 메모리에 저장 (바이트 길이는 파일 크기와 동일)
		
		
		ObjectMetadata metaData=new ObjectMetadata();
		metaData.setContentType("image/"+extension); // 파일 확장자
		metaData.setContentLength(bytes.length); // 파일 크기
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
		
		try {
			bucket=bucket.trim();
			PutObjectRequest putObjectRequest=
					new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, metaData);
			amazonS3.putObject(putObjectRequest); // 이미지를 S3에 저장
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			is.close();
		}

		System.out.println(amazonS3.getUrl(bucket, s3FileName).toString());
		return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
				
	}
	

}