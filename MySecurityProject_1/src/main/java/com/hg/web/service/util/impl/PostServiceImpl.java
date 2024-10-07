package com.hg.web.service.util.impl;

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
import com.hg.web.dto.api.ResponseDTO;
import com.hg.web.dto.util.PostDTO;
import com.hg.web.mapper.util.PostMapper;
import com.hg.web.service.util.PostService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
// 클라이언트로부터 받은 이미지를 버킷에 올림
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
	
	private final AmazonS3 amazonS3;
	private final PostMapper postingmapper;
	
	@Value("${s3.bucket}")
	private String bucket;
	

	@Override
	@Transactional
	public ResponseEntity<ResponseDTO<Void>> uploadPosting(PostDTO postDTO) {
		try{
			postingmapper.insertContent(postDTO); // content DB 저장
			System.out.println(postDTO.getP_num());
		}catch(Exception e) {
			e.printStackTrace();
		}

		return this.uploadImg(postDTO); // S3 이미지 업로드
	}

	@Override
	public ResponseEntity<ResponseDTO<Void>> uploadImg(PostDTO postDTO){
	    
	    return this.uploadImageToS3(postDTO);
	    
	  }
	
	// S3에 이미지 업로드
	private ResponseEntity<ResponseDTO<Void>> uploadImageToS3(PostDTO postDTO){
    	
    	String originalFilename=postDTO.getP_img().getOriginalFilename();
		String extension=originalFilename.substring(originalFilename.lastIndexOf(".")+1); // 확장자 추출

		System.out.println(extension);
		String s3FileName=UUID.randomUUID().toString().substring(0,10) + originalFilename; //  원본 파일명 유지 + 파일명 중복 방지
		
		 InputStream is = null; 
		 
		try {
			is = postDTO.getP_img().getInputStream(); // InputStream => 파일의 데이터를 한줄씩 바이트 단위로 읽어들임 (데이터에 접근하는
																	// 방식)
			byte[] bytes = IOUtils.toByteArray(is); // 바이트 배열을 통해 데이터를 메모리에 저장 (바이트 길이는 파일 크기와 동일)
			
			ObjectMetadata metaData=new ObjectMetadata();
			metaData.setContentType("image/"+extension); // 파일 확장자
			metaData.setContentLength(bytes.length); // 파일 크기
			ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
			
			bucket=bucket.trim();
			PutObjectRequest putObjectRequest=
					new PutObjectRequest(bucket, s3FileName, byteArrayInputStream, metaData);
			amazonS3.putObject(putObjectRequest); // 이미지를 S3에 저장
			
			// 포스트 번호, 이미지 URL DB 저장
			postDTO.setP_num(postDTO.getP_num());
			postDTO.setP_img_url(amazonS3.getUrl(bucket, s3FileName).toString());
			postingmapper.insertImgUrl(postDTO);
		} catch (Exception e) {
			// S3 업로드 중 문제가 발생한 경우 예외 발생시켜 롤백 기능 수행
						postingmapper.deleteImgUrl(postDTO);
						throw new RuntimeException("S3 업로드 실패: " + e.getMessage());
		}finally {
			if (is != null) {
	            try {
	                is.close();
	            } catch (IOException e) {
	                throw new RuntimeException("InputStream 닫기 실패: " + e.getMessage());
	            }
	        }
		}
		
		return new ResponseEntity<ResponseDTO<Void>> (new ResponseDTO<>(),HttpStatus.OK); //성공 
				
	}

}